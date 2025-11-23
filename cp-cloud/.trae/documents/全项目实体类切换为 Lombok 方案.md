## 目标
- 将全项目的实体类统一改为使用 Lombok 生成 getter/setter（必要时 equals/hashCode/toString），减少样板代码，提升一致性。
- 保持与 MyBatis-Plus、校验注解、序列化（Jackson/Redis）等现有行为兼容。

## 范围
- 目录与包：
  - `cp-api/cp-api-system/src/main/java/com/cp/system/api/domain/*`、`.../model/*`
  - `cp-modules/cp-system/src/main/java/com/cp/system/domain/*`、`.../domain/vo/*`
  - 通用基类：`cp-common/cp-common-core/src/main/java/com/cp/common/core/web/domain/BaseEntity`
- 不涉及：Mapper/Service/Controller 类；表单/请求体若已用 Lombok可不变。

## Lombok 注解策略
- 继承 `BaseEntity` 的实体：`@Data + @EqualsAndHashCode(callSuper = true)`
- VO/简单对象：`@Data`（必要时 `@Accessors(chain = true)` 便于链式调用）
- 仅读/常量对象：按需使用 `@Getter` 或自定义组合
- 如需构建器：`@Builder`（仅在明确需要时增加）

## 迁移规则
- 移除显式的 getter/setter/toString/equals/hashCode 方法
- 将 `@NotBlank/@Size/@Pattern/...` 等 Bean Validation 注解从方法上迁移到字段上
- 保留所有 MyBatis-Plus 注解（`@TableId/@TableField/@TableName`）与 Excel 导出注解等
- Jackson 兼容：
  - 对历史缓存字段名差异，使用 `@JsonAlias`（例如 `isDefault` 兼容 `default`）
  - 若存在前缀 `is` 的布尔字段，确保序列化/反序列化一致（必要时 `@JsonProperty`）
- 序列化：保留 `serialVersionUID` 与 `implements Serializable`

## 实施步骤
1. 扫描并列出待改实体清单（domain、model、vo）
2. 执行批量改造：
  - 添加 Lombok 注解；删除冗余方法
  - 移动校验注解到字段
  - 增加必要的 `@JsonAlias`（如 `SysDictData.isDefault`）
3. 编译验证：逐模块 `mvn -DskipTests -pl <module> -am package`
4. 运行验证：启动 `cp-system` 与必要的消费者，访问典型接口：
  - 字典、用户、角色相关接口，确认序列化/反序列化与校验正常
5. 代码风格统一：确保所有实体注解排列、字段顺序与现有风格一致

## 验证点
- MyBatis 映射与分页查询正常（`selectList/selectPage`）
- 字典缓存读写正常（不再出现 `default` 字段报错）
- 登录与权限相关实体序列化/反序列化正常

## 风险与规避
- 方法级校验迁移到字段后需重新编译检查；若存在自定义校验逻辑，保留必要方法
- 旧缓存数据的字段名差异通过 `@JsonAlias` 或一次性清理缓存解决

## 交付
- 全项目实体已切换为 Lombok，编译与运行验证通过；提供改动列表与关键类路径说明。

确认后我将开始对全项目实体类执行上述批量改造与验证。