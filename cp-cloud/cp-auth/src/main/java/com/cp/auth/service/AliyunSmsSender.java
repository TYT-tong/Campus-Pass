package com.cp.auth.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.cp.auth.domain.SmsLoginBody;
import com.cp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author tyt15
 */
@Component
@ConditionalOnProperty(prefix = "sms.mock", name = "enabled", havingValue = "false")
@ConditionalOnProperty(prefix = "aliyun.sms", name = "enabled", havingValue = "true")
public class AliyunSmsSender implements SmsSender {
    @Value("${aliyun.sms.region-id}")
    private String regionId;
    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.sign-name}")
    private String signName;
    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    @Override
    public R<SmsLoginBody> send(String key, Integer count, String phone, String code) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse response = client.getAcsResponse(request);
        if (response == null || !"OK".equalsIgnoreCase(response.getCode())) {
            throw new RuntimeException("短信发送失败");
        }
        return R.ok(new SmsLoginBody(phone, code));
    }
}