## 问题根因定位
- 控制器 `SysUserController.list` 使用 `getDataTable(userService.selectUserPage(user))`，分页总数来自 `IPage.getTotal`（BaseController 中的分页分支）。
- 服务实现 `SysUserServiceImpl.selectUserPage` 使用 MyBatis-Plus `LambdaQueryWrapper` + `selectPage`，并在 `@DataScope(userAlias = "u")` 下，拼接数据权限：`qw.apply(ds)`（src/main/java/com/cp/system/service/impl/SysUserServiceImpl.java:84-106）。
- 数据权限切面 `DataScopeAspect` 存在两个关键问题（cp-common-datascope/src/main/java/com/cp/common/datascope/aspect/DataScopeAspect.java）：
  1) 形参传递错误：`handleDataScope` 调用 `dataScopeFilter(joinPoint, currentUser, controllerDataScope.userAlias(), permission)`（66-79），把 `permission` 传成了“用户别名”，导致拼接 `OR system:user:list.user_id = xxx`，非法别名影响 Count 与数据查询；
  2) MyBatis-Plus 场景不兼容：当未提供“用户别名”时，当前实现退化为 `OR 1 = 0`（90-141），直接使总数为 0；而 MP 的 `LambdaQueryWrapper` 生成 SQL 不含别名，适配应使用裸列名 `user_id`。
- 分页插件已注册（cp-common-security/src/main/java/com/cp/common/security/config/ApplicationConfig.java:18-28），总数计算会执行 Count SQL；目前总数为 0 多半由上述数据权限条件导致。

## 修复方案
1) 修正切面参数顺序：
   - 将 `handleDataScope` 中的调用改为 `dataScopeFilter(joinPoint, currentUser, "", controllerDataScope.userAlias())`，确保最后一个形参是真正的“用户别名”。
2) 兼容 MP 查询（无别名）：
   - 修改 `dataScopeFilter` 的别名为空时的处理逻辑：不再 `OR 1 = 0`，改为拼接裸列 `OR user_id = {currentUserId}`，以支持 MP 的 `LambdaQueryWrapper`。
   - 保持 XML 场景（有别名，如 `u`）继续使用 `<alias>.user_id`，兼容现有 Mapper XML。
3) 确认角色集加载：
   - 当前切面基于 `SecurityUtils.getLoginUser().getSysUser().getRoles()` 生成数据范围；若角色未加载会触发空条件，建议在登录/鉴权链路确保 `LoginUser.roles` 赋值（`TokenService` 缓存）或在切面遇到空角色时默认仅本人。

## 验证步骤
- 用 admin 账号访问 `GET /user/list`：应返回全量，`total > 0`。
- 用非 admin 账号访问同接口：至少返回“本人”一条，`total >= 1`。
- 同时验证 XML 查询的 `selectUserList` 与 MP 分页的 `selectUserPage` 两条路径。

## 变更范围
- 修改 `DataScopeAspect.handleDataScope` 调用参数顺序
- 修改 `DataScopeAspect.dataScopeFilter` 空别名逻辑
- 不改动控制器与服务签名；不改动分页与拦截器配置

如确认上述方案，我将提交最小化代码修复并回归测试，确保列表分页总数与数据符合预期。