package com.cp.auth.controller;
import com.aliyuncs.exceptions.ClientException;
import com.cp.common.core.domain.R;
import com.cp.auth.domain.SmsSendBody;
import com.cp.auth.domain.SmsLoginBody;
import com.cp.auth.service.SmsLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author tyt15
 */
@RestController
@RequestMapping("/sms")
public class SmsTokenController {
  @Resource
  private SmsLoginService smsLoginService;

  @PostMapping("/send")
  public R<SmsLoginBody> send(@Validated @RequestBody SmsSendBody body) throws ClientException {

    return smsLoginService.sendCode(body.getPhone());
  }

  @PostMapping("/login")
  public R<Map<String, Object>> login(@Validated @RequestBody SmsLoginBody body) {
    Map<String, Object> token = smsLoginService.login(body.getPhone(), body.getCode());
    return R.ok(token);
  }
}