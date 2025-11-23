## 当前情况
- 接口位置：`/system/user/list` → `SysUserController#list(SysUser)`，返回 `TableDataInfo`（内部封装 MyBatis-Plus 的 `IPage<SysUser>`）。
- 域模型：`SysUser` 已包含 `remark` 字段（`cp-api/cp-api-system/src/main/java/com/cp/system/api/domain/SysUser.java:104-105`），并映射到 `sys_user.remark`。
- 持久层：
  - MyBatis-Plus 分页查询 `SysUserServiceImpl#selectUserPage` 默认 `select *`，`remark` 会被返回。
  - 传统 Mapper 的 `selectUserList` 和 `selectUserVo` 已显式选出 `u.remark`（`SysUserMapper.xml:38-40, 57`）。

## 可能缺失的原因
- 前端未展示 `remark` 字段（服务端已返回）。
- 序列化被覆盖或过滤（若有自定义 DTO/序列化策略）。

## 服务端改动方案（如需强制显式）
1. 保持 `SysUserController#list` 返回 `IPage<SysUser>`，无需改动（服务端已有 `remark`）。
2. 若需显式 VO：新增 `UserListVo`（包含 `remark`），在 `SysUserServiceImpl#selectUserPage` 中将 `records` 映射为 VO 后返回；分页元数据保持不变。
3. Mapper 与 MP 默认已返回 `remark`，无需调整 SQL。

## 验证
- 调用 `/system/user/list`，确认返回 JSON 的每条用户对象含有 `remark`。
- 如前端表格需展示，在前端列定义中添加 `remark` 列。