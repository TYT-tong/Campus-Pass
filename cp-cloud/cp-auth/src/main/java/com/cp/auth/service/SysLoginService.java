package com.cp.auth.service;

import com.cp.auth.form.RegisterBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cp.common.core.constant.CacheConstants;
import com.cp.common.core.constant.Constants;
import com.cp.common.core.constant.SecurityConstants;
import com.cp.common.core.constant.UserConstants;
import com.cp.common.core.domain.R;
import com.cp.common.core.enums.UserStatus;
import com.cp.common.core.exception.ServiceException;
import com.cp.common.core.text.Convert;
import com.cp.common.core.utils.DateUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.core.utils.ip.IpUtils;
import com.cp.common.redis.service.RedisService;
import com.cp.common.security.utils.SecurityUtils;
import com.cp.system.api.rpc.UserRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import com.cp.system.api.domain.SysUser;
import com.cp.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Value;

/**
 * 登录校验方法
 * 
 * @author cp
 */
@Component
public class SysLoginService
{
    @DubboReference(version = "1.0.0", check = false)
    private UserRpcService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    

    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            //TODO 远程调用日志记录
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }

        R<LoginUser> userResult;
        LoginUser userInfo;
        SysUser user;
        try {
            userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
            if (userResult == null || R.FAIL == userResult.getCode() || userResult.getData() == null)
            {
                throw new ServiceException(userResult != null ? userResult.getMsg() : "用户信息获取失败");
            }
            userInfo = userResult.getData();
            user = userInfo.getSysUser();
        } catch (Exception ex) {
            throw new ServiceException( "用户登录获取失败");

        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        passwordService.validate(user, password);



        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        try {
            recordLoginInfo(user.getUserId());
        } catch (Exception ignore) {}
        return userInfo;
    }

    

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        // 更新用户登录IP
        sysUser.setLoginIp(IpUtils.getIpAddr());
        // 更新用户登录时间
        sysUser.setLoginDate(DateUtils.getNowDate());
        remoteUserService.recordUserLogin(sysUser, SecurityConstants.INNER);
    }

    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(RegisterBody registerBody)
    {
        String username  = registerBody.getUsername();
        String password = registerBody.getPassword();
        String phone = registerBody.getPhoneNumber();
        String affirmPassword = registerBody.getAffirmPassword();


        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password,phone))
        {
            throw new ServiceException("用户/密码/手机号必须填写");
        }
        if (!affirmPassword.equals(password)){
            throw new ServiceException("两次密码输入不正确");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }
        if (phone.length() != UserConstants.PHONE_LENGTH)
        {
            throw new ServiceException("手机号长度必须为11位数字");
        }



        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPhonenumber(phone);
        sysUser.setCreateTime(DateUtils.getNowDate());
        sysUser.setPwdUpdateDate(DateUtils.getNowDate());
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, "注册成功");
    }
}
