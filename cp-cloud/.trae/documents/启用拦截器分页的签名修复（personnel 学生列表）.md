## 目标
- 让 MyBatis-Plus 分页拦截器对联表查询生效，生成 `COUNT(*)` 与 `LIMIT`，恢复 `rows` 为当前页、`total` 为真实总数。

## 变更
1) Mapper 方法签名改为 Page 形式（更符合拦截器填充习惯）：
- `Page<PerStudentVO> selectStudentPage(Page<PerStudentVO> page, @Param("dto") PerStudentQueryDTO dto)`

2) Service 层传入并返回 Page：
- `Page<PerStudentVO> page = PageUtils.buildPage(); return baseMapper.selectStudentPage(page, dto);`

3) XML 保持无手动分页，交给拦截器处理（当前已移除 LIMIT/OFFSET 与 count）。

4) 验证
- 构建并重启 personnel；调用 `/student/list?pageNum=1&pageSize=10`
- 日志出现 `SELECT COUNT(*)` 与带 `LIMIT` 的查询；响应 `rows.Count==10`、`total>0`。

如确认，我将执行签名修复与构建验证。