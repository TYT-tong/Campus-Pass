## 目标
- 在你已清空 Redis 缓存的基础上，验证字典与用户缓存路径是否恢复正常。
- 同步解决 Nacos 客户端 UNHEALTHY/Timeout 的网络连通问题。

## 验证接口
- 访问 `GET /dict/data/type/sys_user_sex`，请求头携带 `Authorization: Bearer <jwt>`。
- 期望：首次走数据库并写入缓存；后续命中缓存直接返回。无 `ClassCastException`、无 `LinkedHashMap cannot be cast to LoginUser`。
- 如仍异常，我将增加一次性调试日志以打印从 Redis 读取到的实际类型，定位具体来源后再移除。

## Nacos 连通性修复
- 服务器侧：在 `192.168.253.131` 上确认并开放端口 `8848`、`9848`（Naming gRPC）、`9849`（Config gRPC），检查实际监听与防火墙策略。
- 客户端侧：通过环境变量注入超时参数以缓解抖动：在启动 `cp-system` 前设置 `JAVA_TOOL_OPTIONS=-Dnacos.remote.response.timeout=10000`。
- 验证：Nacos 客户端日志不再出现 UNHEALTHY/Timeout，配置监听正常。

## 说明
- `/` 与 `/favicon.ico` 的静态资源异常不影响业务接口；如需消除，我可加一个健康端点或配置静态资源目录。

请确认后，我按上述步骤协助验证接口并指导你完成 Nacos 端口与客户端参数调整。