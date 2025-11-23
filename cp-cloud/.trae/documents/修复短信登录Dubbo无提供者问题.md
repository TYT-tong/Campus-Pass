## 问题分析
- 报错：No provider available for service `com.cp.system.api.rpc.UserRpcService:1.0.0`，说明消费者(cp-auth)在调用短信登录时找不到提供者(cp-system)。
- 主要原因：cp-system 当前未运行或未成功在 Nacos 注册 Dubbo 服务；也可能存在配置不一致（namespace/group/version/protocol）、网络不可达或被列入 blocklist。
- 代码一致性：
  - 提供者标注：`@DubboService(version = "1.0.0")`，见 `cp-modules/cp-system/src/main/java/com/cp/system/service/impl/UserRpcServiceImpl.java:19`。
  - 消费者引用：`@DubboReference(version = "1.0.0", check = false)`，见 `cp-auth/src/main/java/com/cp/auth/service/SmsLoginService.java:45-46`。
  - 版本匹配，关键在于提供者在线并完成注册。

## 核查与修复步骤
1. 启动并确认 cp-system 正常：
   - 观察启动日志是否包含：
     - “Export dubbo service com.cp.system.api.rpc.UserRpcService ...”
     - “Register dubbo service ... to registry 192.168.253.131:8848”
     - “Start NettyServer bind /0.0.0.0:20880”
   - 确保已加载 Nacos 配置（`cp-system-dev.yml`、数据源与 Redis）。
2. 在 Nacos UI 核查 Dubbo 注册：
   - 命名空间：`public`；分组：`DEFAULT_GROUP`。
   - 查看服务列表与订阅，确认 cp-system 的 Dubbo 服务已发布，接口 `UserRpcService:1.0.0` 可见。
3. 校验 cp-auth 消费者配置：
   - 注册中心地址与 namespace 与 cp-system 一致：`nacos://192.168.253.131:8848`、`public`。
   - 协议一致（日志显示 provider 使用 `dubbo` 协议）。
   - 版本一致（均为 `1.0.0`）。
4. 网络连通性检查：
   - 消费者主机到提供者 Dubbo 端口（默认 `20880`）连通，无防火墙阻断。
5. 若仍显示 blocklist 或 invokers=0：
   - 清理/重启注册缓存；检查 Dubbo 迁移规则（Nacos 中 `cp-system.migration`）是否影响路由。
   - 关闭/重置黑名单策略；确保提供者健康检查通过。

## 验证
- 重新访问 cp-auth 接口 `/sms/login`（确保短信验证码验证通过），应能正常调用 `UserRpcService.getUserByPhone` 并返回登录 token。
- 如需联调通过网关，确保网关路由只用于 HTTP 流量；Dubbo 走注册中心直连，不依赖网关。

## 可选增强（需你确认后实施）
- 为短信登录增加容错降级：在 cp-auth 中为 `@DubboReference` 配置 `mock` 或使用已有 `UserRpcConfig` 的本地降级 Bean，在提供者不可用时返回明确业务错误而非异常堆栈。
- 打开 Dubbo/消费者侧的更多日志（DEBUG）以便定位注册与订阅详情。

请确认是否按以上步骤执行。我将：
1) 启动 cp-system 并验证提供者成功注册；
2) 核查 Nacos 与消费者配置一致性；
3) 回归测试 `/sms/login` 并提供验证结果。