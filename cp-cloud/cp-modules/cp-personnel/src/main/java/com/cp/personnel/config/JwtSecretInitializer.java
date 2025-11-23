package com.cp.personnel.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cp.common.core.constant.TokenConstants;

/**
 * @author tyt15
 */
@Component
public class JwtSecretInitializer {
    @Value("${security.token.secret:abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()}")
    private String secret;

    @PostConstruct
    public void init() {
        TokenConstants.SECRET = secret;
    }
}

