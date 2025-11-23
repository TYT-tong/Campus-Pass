## 问题总结
- 接口返回的 `rows` 有数据但 `total=0`，SQL 日志未出现 `COUNT(*)` 与 `LIMIT`，说明 MyBatis-Plus 分页拦截器未生效（或未被注册）。

## 修复方案
1) 明确注册分页拦截器：
   - 给 `cp-common-security/config/ApplicationConfig` 增加 `@Configuration` 注解，确保其作为配置类被 Spring 正确处理（当前通过 `@EnableCustomConfig @Import(ApplicationConfig.class)` 导入，添加注解更稳妥）。
   - 保持现有 `MybatisPlusInterceptor` + `PaginationInnerInterceptor(DbType.MYSQL)` 的 Bean 注册。
2) 服务层兜底保证 total：
   - 在 `SysUserServiceImpl.selectUserPage` 中调用 `selectPage` 后，若 `page.getTotal()==0`，执行一次 `baseMapper.selectCount(qw)` 并 `page.setTotal(count)`，保证接口 `total` 与实际数据一致；作为拦截器未生效时的临时保障。
3) 验证与回归：
   - 重编译并启动 system 模块；
   - 访问 `/user/list`，确认日志出现 `COUNT(*)` 与 `LIMIT`，`total` 与数据一致；
   - 再次检查在无 `pageNum/pageSize` 参数时默认分页行为是否正常。

## 影响范围
- 仅修改 `ApplicationConfig` 与 `SysUserServiceImpl`，不改动接口签名与返回结构。

如确认，我将按上述步骤修改并完成构建与验证，确保分页总数与数据库一致。