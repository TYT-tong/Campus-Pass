## 问题分析
- 启动报错：未找到 MyBatis Mapper（com.cp.personnel 包下无 mapper 被扫描），Spring 容器无 `PerStudentMapper` Bean。
- 直接原因：缺少 Mapper 扫描配置或 XML 位置未被 MyBatis-Plus加载。

## 解决方案
### 1) 启用 Mapper 扫描
- 在 cp-personnel 模块的启动类或配置类上添加 `@MapperScan("com.cp.personnel.mapper")`。
  - 若存在 `CampusPassPersonnelApplication`（或同等主类），在其类上添加。
  - 若没有主类或不便修改，新增配置类 `PersonnelMybatisConfig`：
    - `@Configuration`
    - `@MapperScan("com.cp.personnel.mapper")`

### 2) 明确 XML 加载路径
- 在 cp-personnel 的 `application.yml` 增加：
  - `mybatis-plus.mapper-locations: classpath*:mapper/**/*.xml`
  - （可选）`mybatis-plus.type-aliases-package: com.cp.personnel.domain.vo,com.cp.personnel.domain.po`
- 确保 `PerStudentMapper.xml` 位于 `src/main/resources/mapper/personnel/PerStudentMapper.xml`（已存在）。

### 3) 保险措施
- 在 `PerStudentMapper` 接口上添加 `@Mapper` 注解，以双重保证（与 `@MapperScan` 二选一或同时存在）。

### 4) 构建与验证
- 构建 cp-personnel：`mvn -pl cp-modules/cp-personnel -am -DskipTests package`
- 启动后验证：
  - 请求 `GET /student/list?pageNum=1&pageSize=10&keyword=张`；
  - 日志应出现 `SELECT COUNT(*)` 与 `LIMIT`，SQL 为联表 `per_student` 与 `sys_user`；
  - 响应 `rows` 包含 `userName`，`total` 为真实总数。

## 说明
- `@MapperScan` 是必要条件，`@Mapper` 为可选补充。
- `mapper-locations` 保证 XML 联表查询被加载，否则会回退到单表或找不到语句。
- 若 personnel 模块需要分页拦截器，与 system 模块一致，可复用已有全局配置或在 personnel 添加 `ApplicationConfig` 引入 `MybatisPlusInterceptor`。

如确认，我将：
1) 添加 `@MapperScan`（或新建配置类）；
2) 补充 personnel 的 `application.yml`；
3) 为 `PerStudentMapper` 加 `@Mapper`；
4) 构建并验证联表分页查询生效。