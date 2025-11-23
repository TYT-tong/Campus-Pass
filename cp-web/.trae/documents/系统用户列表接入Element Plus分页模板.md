## 现状与目标
- 页面已使用自定义 `<pagination>` 组件，绑定 `queryParams.pageNum/pageSize` 并在 `@pagination` 时调用 `getList`（src/views/system/user/index.vue:99-101）。
- 目标：改用 Element Plus 原生 `<el-pagination>`，采用你提供的模板布局与交互（包含 sizes 下拉、总数、跳转等）。

## 接入方案
- 替换自定义 `<pagination>` 为 `<el-pagination>`，与现有数据流保持一致：
  - `v-model:current-page` 绑定 `queryParams.pageNum`
  - `v-model:page-size` 绑定 `queryParams.pageSize`
  - `:page-sizes` 使用你给的 `[100, 200, 300, 400]`
  - `layout="total, sizes, prev, pager, next, jumper"`
  - `:total="total"`
  - `:background="background"`、`:size="size"`、`:disabled="disabled"`
  - 事件：`@size-change="handleSizeChange"`、`@current-change="handleCurrentChange"`

## 代码改动
- 文件：`src/views/system/user/index.vue`
- 模板部分（bottom 容器内）：
```vue
<el-pagination
  v-model:current-page="queryParams.pageNum"
  v-model:page-size="queryParams.pageSize"
  :page-sizes="[100, 200, 300, 400]"
  :size="size"
  :disabled="disabled"
  :background="background"
  layout="total, sizes, prev, pager, next, jumper"
  :total="total"
  @size-change="handleSizeChange"
  @current-change="handleCurrentChange"
/>
```
- 脚本部分（新增本地配置与事件处理）：
```ts
const size = ref<'small' | 'default' | 'large'>('default')
const disabled = ref(false)
const background = ref(true)

function handleSizeChange(val: number) {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = val
  getList()
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  getList()
}
```
- 移除自定义 `<pagination>` 使用（保持 API 逻辑不变，`getList` 仍调用 `listUser` 并赋值 `userList/total`）。

## 保留与兼容
- 保留 `handleQuery` 将页码重置为 1 的逻辑（src/views/system/user/index.vue:370-373），与 sizes/jumper 行为一致。
- 不改动表格、查询条件、导入导出及增删改逻辑。

## 验证要点
- 切换页码与页大小后请求参数正确（`pageNum/pageSize`），接口返回的 `rows/total` 正常驱动表格和分页。
- `total > 0` 时显示分页；翻页、跳转、改变 sizes 均能触发 `getList` 并更新列表。

如确认，开始按上述方案替换并联调。