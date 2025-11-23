## 目标

* 让接口返回的用户对象包含 `remark` 字段（来源 `sys_user.remark`）。

## 原因

* `SysUser` 继承 `BaseEntity`，`remark` 定义在父类；父类仅有 `@Setter`，无 getter，JSON 序列化未输出该字段。

## 改动

* 在 `BaseEntity` 增加 `@Getter`（并引入 `lombok.Getter`），为包含的字段（含 `remark`）生成 getter。

* 不改动 SQL/Mapper；现有映射已选出 `remark` 并写入实体。

## 验证

1. 重新构建并重启 `cp-system`。
2. 登录获取 `Bearer` 令牌（`/login`）。
3. 调用 `http://localhost:9202/user/list` 或经网关 `http://localhost:8081/system/user/list`。
4. 检查返回 `rows[*].remark` 存在（为空或有值）。

