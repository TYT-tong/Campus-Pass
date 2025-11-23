## 变更内容
- 修改 `RedisConfig`，将 `RedisTemplate` 的 `valueSerializer` 与 `hashValueSerializer` 从 FastJson2 改为 `GenericJackson2JsonRedisSerializer`，保留 `StringRedisSerializer` 作为 key/hashKey。
- 不改动其余缓存方法与使用方式，保持向后兼容。

## 代码改动
- 文件：`cp-common/cp-common-redis/src/main/java/com/cp/common/redis/configure/RedisConfig.java`
- 关键替换：
```java
RedisTemplate<Object, Object> template = new RedisTemplate<>();
template.setConnectionFactory(connectionFactory);

// 旧：FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
// 新：
org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer jacksonSerializer = new org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer();

template.setKeySerializer(new StringRedisSerializer());
template.setValueSerializer(jacksonSerializer);

template.setHashKeySerializer(new StringRedisSerializer());
template.setHashValueSerializer(jacksonSerializer);
```

## 缓存清理
- 为避免旧的 FastJson2 数据与新序列化不兼容，建议删除历史登录缓存：`LOGIN_TOKEN_KEY*`。
- 可用 `RedisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*")` 遍历并删除，或直接清空相关命名空间。

## 验证步骤
- 启动 `cp-system` 与 `cp-auth`：通过 `/sms/send` → `/sms/login` 拿到 token。
- 访问任一需要登录的接口，观察 `TokenService.getLoginUser`：不再出现 `JSONObject cannot be cast to LoginUser`，能正常读取 `LoginUser`。
- 验证 `RedisService` 的 `getCacheMap/opsForHash` 等使用，读写正常。

## 影响评估
- 新序列化自动携带类型信息，可还原复杂对象树，降低类型转换异常风险。
- 需要一次性清理旧格式缓存；新写入数据将使用 Jackson 格式。

确认后我将完成代码修改、清理历史缓存，并重启验证。