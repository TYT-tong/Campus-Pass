package com.cp.auth.service;

import com.aliyuncs.exceptions.ClientException;
import com.cp.auth.domain.SmsLoginBody;
import com.cp.common.core.domain.R;
import com.cp.common.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author tyt15
 */
@Component
@ConditionalOnMissingBean(SmsSender.class)
public class DefaultSmsSender implements SmsSender {
  @Resource
  private RedisService redisService;

  @Override
  public R<SmsLoginBody> send(String key, Integer count, String phone, String code) throws ClientException {
    redisService.setCacheObject(key, String.valueOf((count == null ? 0 : count) + 1), 1L, TimeUnit.DAYS);
    return R.ok(new SmsLoginBody(phone, code),"mock模拟数据");
  }
}