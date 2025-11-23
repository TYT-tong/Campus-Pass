## 报错原因研判
- 代码中 `el-select` 使用了 `clearable`，清空选择时会把 `v-model` 设为 `null/undefined`。
- 当前 `tagFilter` 定义为 `ref<string>('全部权限')`，当被赋值为 `undefined/null` 时，TypeScript 会报错：`Type 'undefined' is not assignable to type 'string'`。
- 同时在 `filteredRoles` 里有 `r.tags.includes(tagFilter.value)`，当 `tagFilter` 为 `null/undefined` 时语义上应当表示“全部权限”，否则可能导致筛选结果异常或运行时报错。

## 修复方案
1. 调整 `tagFilter` 的类型与默认值
- 将 `const tagFilter = ref<string>('全部权限')` 改为 `const tagFilter = ref<string | null>('全部权限')`，兼容清空后的 `null`。
2. 增强筛选计算逻辑
- 在 `filteredRoles` 中把 `null/undefined/"全部权限"` 都视为“全部”：
```ts
const filteredRoles = computed(() => {
  const k = (keyword.value || '').trim()
  const tf = tagFilter.value
  return roles.value.filter(r => {
    const byName = k === '' || r.name.includes(k)
    const byTag = !tf || tf === '全部权限' || r.tags.includes(tf)
    return byName && byTag
  })
})
```
3. 下拉选择行为一致性（可选二选一）
- 方案A：保留“全部权限”选项与 `clearable`，并按上面的逻辑处理 `null`。
- 方案B：去掉“全部权限”选项，仅用 `clearable` 代表“全部”，`placeholder="全部权限"` 只做占位。
4. 代码健壮性小优化（非必须）
- 在 `submitRole` 前校验 `form.name` 非空已完成；保持即可。

## 预计改动位置
- `src/views/system/role/index.vue`
  - 变量：`tagFilter` 定义处
  - 计算属性：`filteredRoles`
  - （可选）模板：`el-select` 的“全部权限”选项保留或移除

## 验证步骤
- 启动页面后：
  - 清空下拉选择应不报错，并显示所有角色。
  - 选择任一标签筛选，列表只显示含该标签的角色。
  - 搜索框输入/清空与筛选联动正常，无控制台错误。

请确认以上方案，我将按此实施修复并完成验证。