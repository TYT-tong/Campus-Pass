## 设计说明
- 前端传参：`pageNum/pageSize/keyword/grade/class/status`
- 分页读取：沿用现有 `BaseController` + `PageUtils.buildPage()`，自动从 QueryString 读取 `pageNum/pageSize`。
- 查询对象：新增 `PerStudentQueryDTO` 用于接收筛选参数，避免直接在实体 `PerStudent` 上增加非表字段；`class` 字段建议用 `className` 以避免保留字冲突。
- 过滤规则：
  - `keyword`：模糊匹配 `student_no / nick_name`（或你定义的姓名字段）
  - `grade`：等值过滤
  - `className`：等值过滤
  - `status`：等值过滤

## 代码改动
1) 新增 DTO：`cp-modules/cp-personnel/src/main/java/com/cp/personnel/domain/dto/PerStudentQueryDTO.java`
- 字段：`String keyword, String grade, String className, String status`

2) Service 层：在 `IPerStudentService` 增加方法
- `IPage<PerStudent> selectStudentPage(PerStudentQueryDTO dto)`
- 实现于 `PerStudentServiceImpl`：
  - 使用 `LambdaQueryWrapper<PerStudent> qw = new LambdaQueryWrapper<>();`
  - `keyword` 非空：`qw.like(PerStudent::getStudentNo, keyword).or().like(PerStudent::getNickName, keyword)`（根据你的表字段调整）
  - `grade` 非空：`qw.eq(PerStudent::getGrade, grade)`
  - `className` 非空：`qw.eq(PerStudent::getClassName, className)`
  - `status` 非空：`qw.eq(PerStudent::getStatus, status)`
  - `qw.eq(PerStudent::getDelFlag, "0")`（如有软删除）
  - `Page<PerStudent> page = PageUtils.buildPage();`
  - `return baseMapper.selectPage(page, qw);`

3) Controller：修改 `PerStudentController` 列表方法（/student/list）
- 方法签名改为：`public TableDataInfo list(PerStudentQueryDTO dto)`
- 返回：`return getDataTable(perStudentService.selectStudentPage(dto));`

## 交互示例
- `GET /student/list?pageNum=1&pageSize=10&keyword=张&grade=2022&className=1班&status=0`
- 响应：`{ code, msg, rows: [当前页10条], total: 总记录数 }`

## 验证
- 构建并运行 `cp-personnel`，请求带不同筛选参数；检查 SQL 出现 `COUNT(*)` 与 `LIMIT`，`rows/total` 符合预期。

如确认，上述改动将直接为你实现分页与筛选查询，并与前端参数保持一致。