## 问题说明
- 编译错误：`SysMenuController.java:51` 调用 `user.getSysUser().getUserType()` 报“找不到方法”。
- 现象：`rows` 有数据但 `total` 为 0（在用户列表分页接口）。

## 原因分析
- `SysUser` 位于 `cp-api-system`，理论上通过 Lombok `@Data` 会生成 `getUserType()`，但当前编译依赖的已编译类中未包含该 getter（可能是历史构建产物或 Lombok处理缺失导致）。
- `total=0` 常见原因是数据权限切面拼接的条件导致 Count 结果为 0；此前已修复切面逻辑，后续需要一次接口回归确认。

## 修复方案
1) 在 `cp-api-system` 的 `SysUser` 类中显式添加 `public String getUserType()`（即使使用 Lombok，也允许手动补齐，确保跨模块可见）。
2) 优化 `SysMenuController` 的判断更稳健：
   - 先判空：`LoginUser user = SecurityUtils.getLoginUser(); SysUser su = user != null ? user.getSysUser() : null;`
   - 若仍需屏蔽“99”类型：使用 `if (su != null && "99".equals(su.getUserType())) return success(Collections.emptyList());`
   - 同时可兼容管理员判断：`if (su != null && su.isAdmin())` 执行正常菜单逻辑。
3) 重新编译并运行 system 模块，验证：
   - 访问 `/user/list`：确认 `total` 正确，`rows` 与实际数据一致；
   - 访问 `/menu/getRouters`：确认不再编译报错，逻辑按用户类型/管理员表现正常。

## 验证与回归
- 执行模块打包，确保 `cp-api-system`、`cp-modules-system` 均成功。
- 用非管理员账号与管理员账号分别验证两个接口的行为；核对 `total` 与数据条目一致。

如你确认上述方案，我将按以上步骤提交代码修改并完成构建与回归测试。