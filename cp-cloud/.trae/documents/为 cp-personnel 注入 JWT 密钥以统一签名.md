## 问题定位
- 仍报错原因：`cp-personnel` 没有 `JwtSecretInitializer`，导致 `TokenConstants.SECRET` 保持默认值，与 Nacos 中的密钥不同。
- 证据：
  - 其他服务均有：`cp-auth`、`cp-gateway`、`cp-system` 的 `JwtSecretInitializer` 存在并注入密钥。
  - `cp-personnel` 中未找到同名类（已检索确认）。

## 修复方案
1. 在 `cp-personnel` 新增配置类 `com.cp.personnel.config.JwtSecretInitializer`
- 内容与其他模块一致：`@Component` + `@Value("${security.token.secret:...}")`，在 `@PostConstruct` 中赋值 `TokenConstants.SECRET = secret`。
- 保持默认值与现有一致，仅作为兜底，不会覆盖 Nacos（优先级更高）。

2. 保持 Nacos 引入不变
- `application.yml` 已包含 `spring.config.import: optional:nacos:cp-security-${spring.profiles.active}.yml`，无需修改。

3. 验证
- 重启 `cp-personnel`，观察启动日志无签名异常。
- 使用通过 `cp-auth` 登录得到的 `access_token`，经 `cp-gateway` 调用 `/parent/list`，Header：`Authorization: Bearer <access_token>`。
- 预期不再出现 `JWT signature does not match...`，接口正常返回分页数据。

## 影响范围
- 新增一个配置类，作用仅为读取密钥并赋值，不改变业务逻辑；与已存在模块的方式保持一致。