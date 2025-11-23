package com.cp.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cp.common.core.utils.ServletUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.gateway.config.properties.CaptchaProperties;
import com.cp.gateway.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 验证码过滤器
 *
 * @author cp
 */
@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
    private final static String[] VALIDATE_URL = new String[]{"/auth/sms/send","/auth/sms/login","/auth/login", "/auth/register"};
    private static final String CODE = "code";
    private static final String UUID = "uuid";
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private CaptchaProperties captchaProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 关键判断：只对登录和注册请求进行验证码验证
            // 非登录/注册请求或验证码关闭，不处理,
            boolean captchaEnabled = Boolean.TRUE.equals(captchaProperties.getEnabled());
            if (!StringUtils.equalsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL) || !captchaEnabled) {
                return chain.filter(exchange);
            }
            // 验证码验证逻辑
            try {
                String rspStr = resolveBodyFromRequest(request);
                JSONObject obj = JSON.parseObject(rspStr);
                validateCodeService.checkCaptcha(obj.getString(CODE), obj.getString(UUID));
            } catch (Exception e) {
                return ServletUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }
            // 🔑 验证成功，继续过滤器链，最终路由到Auth服务
            return chain.filter(exchange);
        };
    }

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }
}
