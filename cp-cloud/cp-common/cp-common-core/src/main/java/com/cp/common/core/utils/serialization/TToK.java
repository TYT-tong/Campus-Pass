package com.cp.common.core.utils.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类型转换
 * @author tyt15
 */
public class TToK {
    private static final Logger log = LoggerFactory.getLogger(TToK.class);
    /**
     * 解析 Token 并反序列化为指定类型的对象
     * @param token Token 字符串（需是 JSON 格式的字符串）
     * @param clazz 目标类型的 Class 对象（如 LoginUser.class）
     * @param <T>   目标类型泛型
     * @return 反序列化后的对象，解析失败则抛出 RuntimeException
     */
    public static <T> T parseTokenToObject(String token, Class<T> clazz) {
        try {
            // 1. 校验 Token 非空
            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("Token 不能为空或空白字符串");
            }

            // 2. FastJSON2 解析 Token 为 JSONObject（兼容 JSON 格式 Token）
            JSONObject jsonObject = JSON.parseObject(token);
            if (jsonObject == null) {
                throw new RuntimeException("Token 格式无效，无法解析为 JSON 对象");
            }

            // 3. 转为目标类型，校验结果非空
            T result = jsonObject.toJavaObject(clazz);
            if (result == null) {
                throw new RuntimeException(String.format("Token 解析结果为空，无法转为 %s 类型", clazz.getSimpleName()));
            }

            return result;
        } catch (RuntimeException e) {
            // 直接抛出自定义运行时异常（保持原有抛出逻辑）
            log.error("解析 Token 为 {} 失败，Token: {}，异常信息：{}", clazz.getSimpleName(), token, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // 捕获其他未知异常（如字段不匹配、类型不兼容等）
            log.error("解析 Token 为 {} 发生未知异常，Token: {}，异常信息：{}", clazz.getSimpleName(), token, e.getMessage(), e);
            throw new RuntimeException(String.format("解析 %s 失败：%s", clazz.getSimpleName(), e.getMessage()));
        }
    }
}
