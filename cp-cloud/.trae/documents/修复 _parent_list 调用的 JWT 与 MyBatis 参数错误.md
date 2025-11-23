## 问题定位
- JWT 解析报错：`JWT signature does not match...`
  - 由 `TokenService.getLoginUser` 调用 `JwtUtils.parseToken` 触发，签名不匹配通常是各服务使用的 `secret` 不一致或客户端携带的令牌不是当前系统签发的。
  - 相关代码：`cp-common-core/src/main/java/com/cp/common/core/utils/JwtUtils.java:30-44` 使用 `TokenConstants.SECRET`；各服务在启动时通过 `JwtSecretInitializer` 注入配置：
    - `cp-auth/src/main/java/com/cp/auth/config/JwtSecretInitializer.java:13-18`
    - `cp-gateway/src/main/java/com/cp/gateway/config/JwtSecretInitializer.java:13-18`
    - `cp-modules/cp-system/src/main/java/com/cp/system/config/JwtSecretInitializer.java:13-19`
- MyBatis 绑定异常：`Parameter 'phone' not found. Available parameters are [page, param1, dto, param2]`
  - 映射方法签名已将 DTO 命名为 `dto`：`cp-modules/cp-personnel/src/main/java/com/cp/personnel/mapper/PerParentMapper.java:23`
  - XML 却直接引用 `phone`、`parentName`、`status`，未加 `dto.` 前缀：`cp-modules/cp-personnel/src/main/resources/mapper/personnel/PerParentMapper.xml:25-34`
  - 调用链：
    - 控制器：`cp-modules/cp-personnel/src/main/java/com/cp/personnel/controller/PerParentController.java:31-33`
    - 服务：`cp-modules/cp-personnel/src/main/java/com/cp/personnel/service/impl/PerParentServiceImpl.java:31-33`

## 修复方案
1. 修正 MyBatis XML 参数引用
- 在 `PerParentMapper.xml` 将所有条件改为通过 `dto` 引用：
  - `<if test="dto.phone != null and dto.phone != ''"> AND pp.phone = #{dto.phone} </if>`
  - `<if test="dto.parentName != null and dto.parentName != ''"> AND pp.parent_name LIKE CONCAT('%', #{dto.parentName}, '%') </if>`
  - `<if test="dto.status != null"> AND u.status = #{dto.status} </if>`
- 同时移除或改为 `parameterType="map"`（建议移除），以避免在多参数场景下产生误导：`PerParentMapper.xml:6`

2. 统一并显式配置 JWT 签名密钥
- 在所有会解析或签发 JWT 的应用中设置相同的配置：
  - 在各应用的 `application.yml` 或外部配置中心添加：
    ```yaml
    security:
      token:
        secret: "请设置同一密钥，例如：xZ3!N9pLq4F...（长度>=64）"
    ```
- 确认以下应用都加载到同一值：
  - `cp-auth`（签发令牌）
  - `cp-gateway`（网关校验令牌）
  - 所有下游业务服务（如 `cp-personnel`，通过 `TokenService` 解析令牌）

3. 客户端与网关调用规范
- 客户端请求必须携带 `Authorization: Bearer <access_token>`，`<access_token>` 为登录接口返回的 `access_token` 字段值。
- 建议通过网关访问 `GET /parent/list`，而非直接访问服务端口，确保统一鉴权链路。

## 验证步骤
- 启动 `cp-auth`、`cp-gateway`、`cp-personnel`，确保它们的 `security.token.secret` 值相同。
- 登录获取 `access_token`；使用该令牌通过网关调用：
  - `GET /parent/list?phone=...&parentName=...&status=...` 并在 Header 加入 `Authorization: Bearer <access_token>`。
- 预期：
  - 不再出现 `JWT signature does not match...`；`TokenService` 能正常从 Redis 取回 `LoginUser`。
  - 不再出现 `Parameter 'phone' not found`；分页查询返回数据。

## 影响范围与风险
- XML 参数前缀调整仅影响家长分页查询，不改变 SQL 逻辑。本次修改不涉及表结构与实体。
- JWT 密钥统一是跨服务配置项，需确保所有应用的配置来源一致（本地 yml 或配置中心）。密钥变更后旧令牌会失效，需要用户重新登录。