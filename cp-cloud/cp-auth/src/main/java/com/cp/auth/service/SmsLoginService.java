package com.cp.auth.service;

import com.aliyuncs.exceptions.ClientException;
import com.cp.auth.domain.SmsLoginBody;
import com.cp.common.core.constant.SecurityConstants;
import com.cp.common.core.domain.R;
import com.cp.common.core.exception.ServiceException;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.redis.service.RedisService;
import com.cp.common.security.service.TokenService;
import com.cp.system.api.rpc.UserRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import com.cp.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信登录服务类
 * 负责处理短信验证码的发送和验证，以及基于短信验证码的登录逻辑
 *
 * @author tyt15
 */
@Service
public class SmsLoginService {

    // 注入Redis服务，用于缓存验证码、发送次数限制等
    @Resource
    private RedisService redisService;

    // 注入阿里云短信发送器，用于实际发送短信验证码

    @Resource
    private SmsSender smsSender;
//    private AliyunSmsSender aliyunSmsSender;


    // 注入远程用户服务，用于获取或创建用户信息
    @DubboReference(version = "1.0.0", check = false)
    private UserRpcService remoteUserService;

    // 注入令牌服务，用于生成登录令牌
    @Resource
    private TokenService tokenService;

    // 从配置文件读取验证码过期时间（秒），默认300秒（5分钟）
    @Value("${sms.code.expire-seconds:300}")
    private long expireSeconds;

    // 从配置文件读取每日发送验证码上限，默认5次
    @Value("${sms.code.daily-limit:5}")
    private int dailyLimit;

    // 从配置文件读取重发间隔时间（秒），默认60秒
    @Value("${sms.code.resend-interval-seconds:60}")
    private long resendInterval;

    

    /**
     * 发送短信验证码
     *
     * @param phone 接收验证码的手机号
     * @throws ClientException 阿里云短信发送异常
     */
    public R<SmsLoginBody> sendCode(String phone) throws ClientException {
        // 构建发送频率限制的Redis键（防止频繁发送）
        String lockKey = "sms_lock:" + phone;
        // 检查是否存在发送锁
        String last = redisService.getCacheObject(lockKey);
        if (StringUtils.isNotEmpty(last)) {
            throw new ServiceException("发送太频繁");
        }

        // 构建每日发送次数限制的Redis键（按手机号和日期区分）
        String countKey = "sms_count:" + phone + ":" + LocalDate.now();

        // 获取今日已发送次数
        Integer cnt = StringUtils.toInteger(redisService.getCacheObject(countKey));
        if (cnt != null && cnt >= dailyLimit) {
            throw new ServiceException("今日次数已达上限");
        }

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 缓存验证码，设置过期时间
        redisService.setCacheObject("sms_code:" + phone, code, expireSeconds, TimeUnit.SECONDS);
        // 设置发送锁，防止短时间内重复发送
        redisService.setCacheObject(lockKey, "1", resendInterval, TimeUnit.SECONDS);



        // 调用阿里云/mock服务发送短信
        return smsSender.send(countKey,cnt,phone, code);
    }

    /**
     * 短信验证码登录
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 包含令牌的登录结果
     */
    public Map<String, Object> login(String phone, String code) {
        // 从Redis获取缓存的验证码
        String cached = redisService.getCacheObject("sms_code:" + phone);
        // 验证验证码是否正确（为空或不匹配则验证失败）
        if (!StringUtils.equals(code, cached)) {
            throw new ServiceException("验证码错误或已过期");
        }

        // 验证成功后删除缓存的验证码（防止重复使用）
        redisService.deleteObject("sms_code:" + phone);

        R<LoginUser> userResult = remoteUserService.getUserByPhone(phone, SecurityConstants.INNER);
        if (userResult.getCode() == R.FAIL) {
            throw new ServiceException(userResult.getMsg() != null ? userResult.getMsg() : "用户不存在");
        }
        LoginUser loginUser = userResult.getData();
        return tokenService.createToken(loginUser);
    }

    
}