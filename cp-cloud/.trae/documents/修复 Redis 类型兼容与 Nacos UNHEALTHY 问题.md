## 问题回顾
- Token 读取异常：Redis 里历史值反序列化为 LinkedHashMap，`TokenService.getLoginUser` 强转为 `LoginUser` 失败。
- 字典缓存异常：`ArrayList cannot be cast to JSONArray`，说明仍命中了旧缓存/旧进程的类型不兼容路径。
- Nacos 客户端 UNHEALTHY/Timeout：HTTP 可通（8848），但 gRPC 通道（9848/9849）不通或延迟过大。

## 处理方案
### 1) 兼容旧缓存的用户对象
- 修改 `TokenService.getLoginUser(String token)`：
  - 如果从 Redis 获取到的是 `Map/LinkedHashMap`，使用 fastjson2 做安全转换：`JSON.parseObject(JSON.toJSONString(user), LoginUser.class)`。
  - 转换成功后直接返回；避免强转异常导致整个请求失败。
- 影响面：仅增强容错，既有逻辑与新写入（已带类型标注）不受影响。

### 2) 字典缓存类型路径彻底安全化
- 已实现 `DictUtils.getDictCache` 的 List 安全转换，但为避免旧类加载/行号不一致导致误判，微调顺序：
  - 先处理 `List<?>`，再处理 `JSONArray` 分支，彻底规避错误的类型判断。
  - 保持空列表返回 `null` 的语义。

### 3) Nacos UNHEALTHY 排查与缓解
- 网络连通：在 `192.168.253.131` 上确认并开放端口 `9848`（Naming gRPC）与 `9849`（Config gRPC），以及 `8848`；host 模式同样需要宿主机防火墙放行。
- 服务端配置：检查 Nacos `application.properties` 或启动参数是否显式设置 `server.grpc.port=9848`、`server.grpc.config.port=9849`，并确认实际监听。
- 客户端超时：为缓解网络抖动，增加响应超时 JVM 参数：`-Dnacos.remote.response.timeout=10000`（或在应用启动脚本中加入），观察 UNHEALTHY 是否消退。

### 4) 验证
- 重新启动 `cp-system` 后，调用：`GET /dict/data/type/sys_user_sex`，Header 携带合法 `Authorization: Bearer <jwt>`。
- 期望：不再出现 `LinkedHashMap cannot be cast to LoginUser` 与 `ArrayList cannot be cast to JSONArray`；首访可能走 DB，后续命中缓存直接返回。
- Nacos：客户端日志不再出现 UNHEALTHY/Timeout；控制台配置监听正常。

## 执行
- 若确认方案，我将：
  - 更新 `TokenService.getLoginUser` 的容错转换逻辑；
  - 微调 `DictUtils.getDictCache` 判断顺序；
  - 在 `cp-system` 启动参数中增加 Nacos 客户端超时设置，并指导你在服务器侧开放/校验 9848/9849 端口。