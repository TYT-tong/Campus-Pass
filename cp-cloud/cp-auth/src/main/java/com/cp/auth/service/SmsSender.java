package com.cp.auth.service;

import com.aliyuncs.exceptions.ClientException;
import com.cp.auth.domain.SmsLoginBody;
import com.cp.common.core.domain.R;
import org.springframework.stereotype.Component;

/**
 * @author tyt15
 */


public interface SmsSender {
    R<SmsLoginBody> send(String key,Integer count,String phone, String code) throws ClientException;
}