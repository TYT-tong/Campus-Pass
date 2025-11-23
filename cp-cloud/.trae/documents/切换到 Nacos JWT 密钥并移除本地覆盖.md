## 目标
- 使用您在 Nacos 中配置的 `security.token.secret`（包含 `-JWT-NACOS-SECRET`）作为唯一 JWT 密钥来源，避免本地 `application.yml` 覆盖导致签名不一致。

## 当前情况
- 代码已通过 `JwtSecretInitializer` 读取 `security.token.secret`：
  - `cp-auth/src/main/java/com/cp/auth/config/JwtSecretInitializer.java:13-18`
  - `cp-gateway/src/main/java/com/cp/gateway/config/JwtSecretInitializer.java:13-18`
  - `cp-modules/cp-system/src/main/java/com/cp/system/config/JwtSecretInitializer.java:13-19`
- 各应用的 `application.yml` 已配置 `spring.config.import` 引入 Nacos：
  - `optional:nacos:cp-security-${spring.profiles.active}.yml`（三处均已存在）
- 我们此前为排查添加了本地 `security.token.secret`，会覆盖 Nacos 值。

## 实施步骤
1. 移除本地覆盖项
- 从以下文件删除 `security.token.secret` 配置段：
  - `cp-auth/src/main/resources/application.yml`
  - `cp-gateway/src/main/resources/application.yml`
  - `cp-modules/cp-personnel/src/main/resources/application.yml`
- 保留 `spring.config.import` 对 Nacos 的引入。

2. 确认 Nacos 配置
- 在 Nacos `DEFAULT_GROUP`、`public` 命名空间中的 `cp-security-${spring.profiles.active}.yml` 存在：
```
security:
  token:
    secret: abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-JWT-NACOS-SECRET
  captcha:
    enabled: false
    type: math
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v3/api-docs
      - /csrf
      - /auth/sms/send
      - /auth/sms/login
sms:
  code:
    expire-seconds: 300
    daily-limit: 5
    resend-interval-seconds: 60
```

3. 重启并验证
- 重启 `cp-auth`、`cp-gateway`、`cp-personnel`，使其从 Nacos 加载密钥。
- 登录获取 `access_token`，通过网关调用：`GET /parent/list?...`，Header：`Authorization: Bearer <access_token>`。
- 预期：不再出现 `JWT signature does not match...`，并且列表分页返回正常。

## 备注
- 代码层无需改动：`JwtSecretInitializer` 会自动将 `TokenConstants.SECRET` 设置为 Nacos 中的值。
- 若将来改密钥，旧令牌会失效，需要重新登录。
- MyBatis 参数绑定错误已修复于 `cp-modules/cp-personnel/src/main/resources/mapper/personnel/PerParentMapper.xml:25-35`，与本次改动互不影响。