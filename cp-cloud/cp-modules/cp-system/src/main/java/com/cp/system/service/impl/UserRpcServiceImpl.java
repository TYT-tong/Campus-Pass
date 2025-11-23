package com.cp.system.service.impl;

import com.cp.common.core.domain.R;
import com.cp.system.api.domain.SysUser;
import com.cp.system.api.model.LoginUser;
import com.cp.system.api.rpc.UserRpcService;
import com.cp.system.controller.SysUserController;
import com.cp.system.service.ISysPermissionService;
import com.cp.system.service.ISysUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
 

/**
 * @author tyt15
 */
@DubboService(version = "1.0.0")
public class UserRpcServiceImpl implements UserRpcService {
    private static final Logger log = LoggerFactory.getLogger(UserRpcServiceImpl.class);
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysPermissionService permissionService;
    

    @Override
    public R<LoginUser> getUserInfo(String username, String source) {
        long t0 = System.currentTimeMillis();
        SysUser user = userService.lambdaQuery()
                .eq(SysUser::getUserName, username)
                .orderByDesc(SysUser::getUserId)
                .last(" limit 1")
                .one();
        long t1 = System.currentTimeMillis();
        log.info("getUserInfo userName={} cost={}ms", username, (t1 - t0));
        if (user == null) {
            return R.fail("用户名或密码错误");
        }
        long t2 = System.currentTimeMillis();
        LoginUser vo = SysUserController.setVoUser(user, permissionService);
        log.info("build LoginUser cost={}ms", (t2 - t1));
        return R.ok(vo);
    }

    @Override
    public R<LoginUser> getUserByPhone(String phone, String source) {
        java.util.List<SysUser> users = userService.lambdaQuery()
                .eq(SysUser::getPhonenumber, phone)
                .orderByDesc(SysUser::getUserId)
                .list();
        if (users == null || users.isEmpty()) {
            return R.fail("用户不存在");
        }
        SysUser user = users.get(0);
        return R.ok(SysUserController.setVoUser(user, permissionService));
    }

    @Override
    public R<Boolean> recordUserLogin(SysUser sysUser, String source) {
        boolean ok = userService.updateById(sysUser);
        return ok ? R.ok(Boolean.TRUE) : R.fail("更新登录信息失败");
    }

    @Override
    public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
        boolean ok = userService.save(sysUser);
        return ok ? R.ok(Boolean.TRUE,"注册成功") : R.fail("注册用户失败");
    }

    
}
