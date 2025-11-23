## 问题
- 学生列表联表查询仅执行一次数据 SQL，未出现 `COUNT(*)` 与 `LIMIT`，导致 `rows` 为全量、`total=0`。

## 目标
- 让 MyBatis-Plus 分页拦截器对 `selectStudentPage(Page, dto)` 生效，自动生成 `COUNT(*)` 和分页 `LIMIT`。

## 变更方案
1) 恢复拦截器主路径（去除手动分页兜底）：
- 在 `PerStudentMapper.xml` 中移除 `<bind offset>` 与 `LIMIT/OFFSET`。
- 删除 `countStudent` 查询。
- 在 `PerStudentMapper.java` 删除 `countStudent(dto)` 方法声明。
- 在 `PerStudentServiceImpl.selectStudentPage` 恢复为：`Page<?> page = PageUtils.buildPage(); return baseMapper.selectStudentPage(page, dto);`

2) 确保拦截器 Bean 已注册：
- 启动类保留 `@EnableCustomConfig`（已添加），导入 `ApplicationConfig`。
- `ApplicationConfig` 中保留 `@Bean MybatisPlusInterceptor` 并添加 `PaginationInnerInterceptor(DbType.MYSQL)`。
- 如联表较复杂，启用 `optimizeJoin=true` 以提升 `COUNT` 解析成功率（若当前版本支持）。

3) 构建与验证
- 重新构建并启动 `cp-personnel` 模块。
- 调用：`/student/list?pageNum=1&pageSize=10`、携带筛选参数的请求。
- 观察日志：应出现 `SELECT COUNT(*)` 与带 `LIMIT` 的查询；响应 `rows.Count==10` 且 `total>0`。

## 说明
- 优先使用拦截器的分页路径，统一、可靠、可维护；手动分页逻辑全部移除，避免双重分页与维护成本。