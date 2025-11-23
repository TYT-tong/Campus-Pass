# 角色权限编辑前端实现说明

## 页面目标
- 在 `src/views/system/role/index.vue` 中，为“编辑角色”提供权限编辑能力（菜单/路由权限树），提交到后端保存。
- 兼容现有“卡片+对话框”的交互：`openEdit` 打开对话框、表单校验通过后调用更新接口。

## 权限数据源
- 菜单树：`roleMenuTreeselect(roleId)` 获取角色对应的菜单树与选中状态（src/api/menu.ts:65-70）。
- 路由数据：`getMenuList()` 用于动态路由生成（src/stores/permission.ts:40-56、src/api/menu.ts:7-12），不直接用于编辑权限，但结构一致。

## 前置准备
- 组件与状态
  - 在角色编辑弹窗中新增一个 `el-tree`，展示菜单树；启用 `show-checkbox`，按 `checkedKeys` 初始化选中。
  - 增加 `treeData: any[]`、`checkedMenuIds: number[]`、`treeRef` 等响应式状态。
- 加载流程
  - `openEdit(item)` 触发后：
    1. 加载角色详情到表单（已实现，src/views/system/role/index.vue:248-256）。
    2. 并行请求 `roleMenuTreeselect(item.id)`，将返回的树数据与默认选中 keys 映射到 `el-tree`。

## UI实现
- 对话框内新增权限树区块：
  - `el-tree :data="treeData" node-key="id" show-checkbox default-checked-keys="checkedMenuIds" :props="{ label: 'label', children: 'children' }"`
  - 提供“全选/清空选中”辅助按钮（可选）。

## 接口调用
- 加载权限树
  - `roleMenuTreeselect(roleId)` 返回结构示例：`{ menus: TreeNode[], checkedKeys: number[] }`（以实际后端为准）。
  - 映射：`treeData = resp.menus`、`checkedMenuIds = resp.checkedKeys`。
- 提交更新
  - 将表单的角色基本信息与 `selectedMenuIds` 一并提交：
    - 若后端接口为统一更新：`PUT /system/role`，`{ roleId, roleName, description, menuIds }`
    - 若为单独权限更新：`PUT /system/role/menuIds`，`{ roleId, menuIds }`
  - 按项目网络层封装调用，利用拦截器统一错误提示（src/utils/request.ts:60-107）。

## 示例代码片段
- 加载树
```ts
import { roleMenuTreeselect } from '@/api/menu'
const treeData = ref<any[]>([])
const checkedMenuIds = ref<number[]>([])
const treeRef = ref()

async function loadRoleMenus(roleId: number) {
  const resp = await roleMenuTreeselect(roleId)
  treeData.value = (resp as any).data.menus || (resp as any).data || []
  checkedMenuIds.value = (resp as any).data.checkedKeys || []
}
```
- 打开编辑
```ts
async function openEdit(item: RoleItem) {
  editingId.value = item.id
  Object.assign(form, { ...item, tags: [...item.tags] })
  formRef.value?.resetFields()
  dialogTitle.value = '编辑角色'
  dialogOpen.value = true
  await loadRoleMenus(item.id)
}
```
- 读取选中并提交
```ts
function getSelectedMenuIds(): number[] {
  const half = treeRef.value.getHalfCheckedKeys?.() || []
  const full = treeRef.value.getCheckedKeys?.() || []
  return Array.from(new Set([ ...half, ...full ]))
}

async function submitRole() {
  await formRef.value?.validate()
  const menuIds = getSelectedMenuIds()
  // 统一更新
  await updateRole({ roleId: form.id, roleName: form.name.trim(), description: form.description?.trim(), menuIds })
  ElMessage.success('修改成功')
  dialogOpen.value = false
  await getRoleList()
}
```

## 数据结构约定
- TreeNode：`{ id: number; label: string; children?: TreeNode[] }`
- 提交 payload：`{ roleId: number; roleName?: string; description?: string; menuIds: number[] }`

## 校验与交互
- 表单校验沿用现有 `formRules`（src/views/system/role/index.vue:160-166）。
- 提交前确保 `form.name` 非空；`menuIds` 可为空（代表无权限）。
- 成功后刷新列表 `getRoleList()`（src/views/system/role/index.vue:180-191）。

## 异常与兜底
- 动态导入失败时路由层已兜底，不影响权限编辑（src/router/index.ts:47-55）。
- 接口错误统一由拦截器提示；在编辑弹窗中可额外显示细化错误（`ElMessage.error`）。

## 相关文件位置
- 角色页：`src/views/system/role/index.vue`（`openEdit`、`submitRole` 逻辑，248-256、279-311）
- 菜单 API：`src/api/menu.ts:65-70`（`roleMenuTreeselect`）
- 网络层：`src/utils/request.ts:60-107`（拦截与提示）
- 路由与权限：`src/stores/permission.ts:122-135`（组件加载）、`src/router/index.ts:47-55`（错误兜底）

## 扩展与优化
- 支持“数据权限”维度（部门树），与菜单树同构形式。
- 增加“搜索菜单”输入框与全选/反选/展开折叠操作。
- 将权限树与提交逻辑抽象为复用组件，以便在其他角色/资源编辑场景使用。