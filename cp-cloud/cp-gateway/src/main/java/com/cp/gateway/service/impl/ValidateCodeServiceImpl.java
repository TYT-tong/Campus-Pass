package com.cp.gateway.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.Resource;
import javax.imageio.ImageIO;

import com.cp.gateway.config.properties.CaptchaProperties;
import com.cp.gateway.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import com.google.code.kaptcha.Producer;
import com.cp.common.core.constant.CacheConstants;
import com.cp.common.core.constant.Constants;
import com.cp.common.core.exception.CaptchaException;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.core.utils.sign.Base64;
import com.cp.common.core.utils.uuid.IdUtils;
import com.cp.common.core.web.domain.AjaxResult;
import com.cp.common.redis.service.RedisService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码实现处理
 *
 * @author cp
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 当 Redis 不可用时，本地内存存储验证码，键为 verifyKey，值为记录（code + 过期时间）
     */
    private static final Map<String, CaptchaRecord> LOCAL_CAPTCHA_STORE = new ConcurrentHashMap<>();

    private static class CaptchaRecord {
        final String code;
        final long expireAt;
        CaptchaRecord(String code, long expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
        boolean expired() { return System.currentTimeMillis() > expireAt; }
    }

    /**
     * 生成验证码
     */
    @Override
    public AjaxResult createCaptcha() throws IOException, CaptchaException
    {
        AjaxResult ajax = AjaxResult.success();
        Boolean enabledProp = captchaProperties.getEnabled();
        boolean captchaEnabled = (enabledProp == null) ? true : enabledProp.booleanValue();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        //UUID 生成和 Redis 键构建
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        String captchaType = captchaProperties.getType();
        // 缺省类型为 math
        if (!"math".equals(captchaType) && !"char".equals(captchaType)) {
            captchaType = "math";
        }
        // 生成验证码
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 优先写入 Redis，如不可用则写入本地内存（开发环境兜底）
        try {
            redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOCAL_CAPTCHA_STORE.put(verifyKey, new CaptchaRecord(
                    code,
                    System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Constants.CAPTCHA_EXPIRATION)
            ));
        }
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));

        return ajax;
    }

    /**
     * 校验验证码
     */
    @Override
    public void checkCaptcha(String code, String uuid) throws CaptchaException
    {
        if (StringUtils.isEmpty(code))
        {
            throw new CaptchaException("验证码不能为空");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = null;
        try {
            captcha = redisService.getCacheObject(verifyKey);
        } catch (Exception ignored) { }

        if (captcha == null)
        {
            CaptchaRecord local = LOCAL_CAPTCHA_STORE.remove(verifyKey);
            if (local == null || local.expired())
            {
                throw new CaptchaException("验证码已失效");
            }
            captcha = local.code;
        }
        try { redisService.deleteObject(verifyKey); } catch (Exception ignored) { }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException("验证码错误");
        }
    }
}
