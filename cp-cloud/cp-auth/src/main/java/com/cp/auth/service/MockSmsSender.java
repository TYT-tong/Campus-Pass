package com.cp.auth.service;

import com.cp.auth.domain.SmsLoginBody;
import com.cp.common.core.domain.R;
import com.cp.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tyt15
 */
@Component
@ConditionalOnProperty(prefix = "sms.mock", name = "enabled", havingValue = "true")
public class MockSmsSender implements SmsSender {
    private static final Logger log = LoggerFactory.getLogger(MockSmsSender.class);
    // 注入Redis服务，用于缓存验证码、发送次数限制等
    @Resource
    private RedisService redisService;
    @Value("${sms.mock.enabled:false}")
    private boolean mockEnabled;

    @Override
    public R<SmsLoginBody> send(String key, Integer count, String phone, String code) {

        if (mockEnabled) {
// 更新今日发送次数（原子递增）
            redisService.setCacheObject(key, String.valueOf((count == null ? 0 : count) + 1), 1L, java.util.concurrent.TimeUnit.DAYS);
            return R.ok(new SmsLoginBody(phone, code),"获取成功");
        }
        return R.fail("mockEnabled错误");
    }
}