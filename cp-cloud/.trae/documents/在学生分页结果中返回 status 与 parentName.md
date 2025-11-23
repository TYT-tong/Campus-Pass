## 目标
- 在 `PerStudentVO` 中新增的 `status` 与 `parentName` 字段返回：
  - `status`：来自关联用户（学生对应的 `sys_user.status`）
  - `parentName`：来自“学生-家长关系”找到家长，再取家长姓名

## 实现方案
### 1) Mapper XML 联表扩展
- 文件：`cp-modules/cp-personnel/src/main/resources/mapper/personnel/PerStudentMapper.xml`
- 在现有查询基础上，增加所需列别名：
  - 已有：`LEFT JOIN sys_user u ON u.phonenumber = ps.phone`
  - 选择列中加入：`u.status AS status`
- 家长姓名两种常见建模（选其一按你库结构）：
  1) 家长在 `sys_user`，关系表为 `per_student_parent_rel(student_id, parent_user_id, relation)`：
     - `LEFT JOIN per_student_parent_rel spr ON spr.student_id = ps.student_id AND spr.relation = 'primary'`
     - `LEFT JOIN sys_user pu ON pu.user_id = spr.parent_user_id`
     - 选择列加入：`pu.nick_name AS parentName`
  2) 家长独立表 `per_parent(parent_id, parent_name)`，关系表 `per_student_parent_rel(student_id, parent_id)`：
     - `LEFT JOIN per_student_parent_rel spr ON spr.student_id = ps.student_id`
     - `LEFT JOIN per_parent pp ON pp.parent_id = spr.parent_id`
     - 选择列加入：`pp.parent_name AS parentName`
- 若一个学生有多个家长：
  - 方案 A：关系表标记主监护人（`relation='primary'`），仅联主监护人
  - 方案 B：使用子查询挑选一个（如最早创建的家长），或 `GROUP_CONCAT(pp.parent_name)` 返回逗号拼接的多个姓名（需配合 `GROUP BY`）

### 2) Mapper 接口保持不变
- `Page<PerStudentVO> selectStudentPage(Page<PerStudentVO> page, @Param("dto") PerStudentQueryDTO dto)`
- `resultType` 继续使用 `PerStudentVO`，SQL 中的别名 `status` 与 `parentName` 自动映射到 VO 新增字段

### 3) Service 层与控制器保持现状
- Service：`Page<PerStudentVO> page = PageUtils.buildPage(); return baseMapper.selectStudentPage(page, dto);`
- 控制器：`getDataTable(perStudentService.selectStudentPage(queryDTO))`

### 4) 校验
- 构建并运行 personnel 模块
- 请求：`/student/list?pageNum=1&pageSize=10`
- 预期：
  - `rows[0].status` 为该学生关联用户的状态
  - `rows[0].parentName` 为主监护人或拼接的家长姓名
  - 分页日志出现 `COUNT(*)` 与 `LIMIT`

### 5) 注意事项
- 确认实际的家长关系与家长表结构名称（如 `per_student_parent_rel` / `per_parent` 或家长也在 `sys_user`）
- 若仅以手机号关联用户，`status` 可直接来自 `u.status`；如学生与用户用 `user_id` 关联，改为 `LEFT JOIN sys_user u ON u.user_id = ps.student_id`
- 多家长时优先使用“主监护人”标记避免聚合产生重复/分组问题；如需返回全部家长姓名，用 `GROUP_CONCAT` 并 `GROUP BY ps.student_id`。

确认后，我将根据你库的实际关系表与家长表命名，落地到 XML 并构建验证。