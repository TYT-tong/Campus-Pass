## 问题
- 返回 rows 为全部数据而非 10 条，total 为 0。原因：MyBatis-Plus 分页拦截器未对联表查询生效，未生成 COUNT 与 LIMIT，IPage.total 未填充。

## 解决方案（手动分页+总数兜底）
### 1) Mapper XML 增加总数查询与手动分页
- 文件：cp-modules/cp-personnel/src/main/resources/mapper/personnel/PerStudentMapper.xml
- 新增：
  - `<select id="countStudent" resultType="long">` 使用与 `selectStudentPage` 相同的联表与动态条件，返回 `COUNT(1)`
- 修改：
  - 在 `selectStudentPage` 增加 `<bind name="offset" value="(page.current-1)*page.size"/>`
  - SQL 末尾追加 `LIMIT #{page.size} OFFSET #{offset}`

### 2) Mapper 接口新增方法
- 文件：cp-modules/cp-personnel/src/main/java/com/cp/personnel/mapper/PerStudentMapper.java
- 新增：`long countStudent(@Param("dto") PerStudentQueryDTO dto)`

### 3) Service 层填充分页结果
- 文件：cp-modules/cp-personnel/src/main/java/com/cp/personnel/service/impl/PerStudentServiceImpl.java
- 在 `selectStudentPage(PerStudentQueryDTO dto)`：
  - 构造 `Page<?> page = PageUtils.buildPage()`
  - `long total = baseMapper.countStudent(dto)`，设置到 `Page<PerStudentVO>` 的 `total`
  - `IPage<PerStudentVO> rows = baseMapper.selectStudentPage(page, dto)`，取当前页记录设置到 `records`
  - 返回 `Page<PerStudentVO>` 给控制器

## 验证
- 构建并重启 personnel 模块
- 请求 `/student/list?pageNum=1&pageSize=10` 与携带筛选参数的请求，日志应出现 LIMIT/OFFSET；响应 `rows.Count==10`，`total>0`。

## 备注
- 保留拦截器方式；即使拦截器暂未生效也能保证分页与总数正确。