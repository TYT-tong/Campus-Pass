## 原因与现状
- 模型：`SysUser` 继承 `BaseEntity`，`BaseEntity` 已定义 `remark`；同时 `SysUser` 自身也再次定义了 `private String remark`（重复字段）。
- 映射：XML Mapper 和 MyBatis-Plus分页均可返回 `remark` 列；但序列化层可能因重复字段冲突导致 `remark` 未出现在 JSON。
- 你提供的响应中已包含 `BaseEntity` 字段（`createTime/updateTime`），但 `remark` 缺失，印证了该冲突问题。

## 方案
1) 统一字段来源
- 移除 `SysUser` 中重复的 `remark` 字段，仅保留 `BaseEntity.remark`。
- 保持 XML 映射 `<result property="remark" column="remark"/>` 不变（映射到父类属性）。

2) 序列化保障
- 如需总是输出（包括空值），在 `BaseEntity.remark` 上显式配置 `@JsonInclude(JsonInclude.Include.ALWAYS)`；但默认已输出空值，优先不加，以减少侵入。

3) 验证
- 重建并调用 `/system/user/list`，检查每条 `rows[i].remark` 是否存在（为空或有值）。
- 若前端未展示，补充前端列表列定义以显示 `remark`。

## 风险
- 变更仅限模型字段清理，不影响查询与入库；对 Mapper 映射透明。

如确认，我将按上述步骤移除重复字段并重建验证。