<template>
  <div class="role-page">
    <!-- 头部容器：整合标题与工具栏，优化间距和对齐 -->
    <div class="header-container">
      <!-- 页面标题：增强视觉权重，添加辅助信息 -->
      <div class="header-title-group">
        <h2 class="page-title">角色列表</h2>
        <p class="page-subtitle">管理系统角色及对应的权限标签</p>
      </div>

      <!-- 工具栏：优化布局结构，增强响应式适配 -->
      <div class="toolbar">
        <!-- 搜索和筛选区域：用卡片包裹，增强视觉区分 -->
        <div class="search-filter-group">
          <el-input v-model="keyword" placeholder="按角色名称搜索..." clearable class="toolbar-input" size="default"
            :maxlength="30" prefix-icon="Search" />
          <el-select v-model="tagFilter" class="toolbar-select" placeholder="全部权限" clearable size="default">
            <el-option label="全部权限" value="全部权限" />
            <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
          </el-select>
        </div>

        <!-- 操作按钮区域：右对齐，与搜索区形成视觉平衡 -->
        <div class="action-buttons">
          <el-button type="success" class="add-btn" @click="openAdd" size="default" icon="Plus">
            新增角色
          </el-button>
        </div>
      </div>
    </div>

    <!-- 卡片网格：优化语义化，增强 hover 交互 -->
    <div v-loading="loading" class="role-grid-container">
      <div class="card-item" v-for="item in filteredRoles" :key="item.id">
        <el-card class="role-card" shadow="hover" :style="{
          backgroundColor: item.cardBgColor,
          borderColor: item.cardBorderColor,

          borderRadius: '16px'
        }">
          <div class="card-head">
            <!-- 徽章：用 Element Plus 图标容器替代自定义 div，优化样式一致性 -->
            <el-avatar :style="{
              backgroundColor: item.badgeColor,
              width: '28px',
              height: '28px'
            }" />
            <div class="head-text">
              <div class="title">{{ item.name }}</div>
              <div class="subtitle">{{ item.description || '无角色说明' }}</div>
            </div>
            <div class="card-actions">
              <!-- 替换原生按钮为 Element Plus 按钮，复用内置样式和交互 -->
              <el-button type="text" class="pill-btn edit" @click="openEdit(item)" size="small" icon="Edit">
                编辑
              </el-button>
              <el-button type="text" class="pill-btn delete" @click="confirmDelete(item)" size="small" icon="Delete">
                删除
              </el-button>
            </div>
          </div>
          <!-- 标签：用 Element Plus Tag 组件替代自定义 span，优化样式和兼容性 -->
          <div class="tag-list">
            <el-tag v-for="tag in getDisplayTags(item.tags).displayTags" :key="tag" size="small" round :style="{
              backgroundColor: 'rgba(0,0,0,0.03)',
              color: item.badgeColor,
              borderColor: 'rgba(0,0,0,0.08)'
            }">
              {{ tag || '暂无权限' }}
            </el-tag>

            <!-- 显示"等X个"标签 -->
            <el-tag v-if="getDisplayTags(item.tags).hasMore" size="small" round :style="{
              backgroundColor: 'rgba(0,0,0,0.03)',
              color: '#6b7280',
              borderColor: 'rgba(0,0,0,0.08)'
            }">
              等{{ getDisplayTags(item.tags).moreCount }}个权限
            </el-tag>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 对话框：优化交互体验，增加表单校验 -->
    <el-dialog v-model="dialogOpen" :title="dialogTitle" width="520px" :close-on-click-modal="false"
      :destroy-on-close="true" class="role-dialog">
      <el-form :model="form" label-width="110px" :rules="formRules" ref="formRef" label-suffix="：" class="role-form">
        <!-- 角色名称 -->
        <el-form-item label="角色名称" prop="name" class="form-item-required">
          <el-input v-model="form.name" placeholder="请输入角色名称" :maxlength="30" show-word-limit size="default"
            class="form-input" />
        </el-form-item>

        <!-- 角色说明 -->
        <el-form-item label="角色说明" prop="description">
          <el-input v-model="form.description" placeholder="请输入角色说明" :maxlength="100" show-word-limit size="default"
            class="form-input" />
        </el-form-item>

        <!-- 权限标签 -->
        <el-form-item label="权限标签" prop="tags">
          <el-select v-model="form.tags" multiple placeholder="选择权限标签" collapse-tags size="default" class="form-select">
            <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
          </el-select>
        </el-form-item>

        <!-- 颜色选择区域 -->
        <div class="color-group">
          <el-form-item label="徽章颜色" prop="badgeColor">
            <el-color-picker v-model="form.badgeColor" :predefine="predefineColors" placeholder="选择徽章颜色"
              class="color-picker" />
          </el-form-item>

          <el-form-item label="卡片背景色" prop="cardBgColor">
            <el-color-picker v-model="form.cardBgColor" :predefine="predefineBgColors" placeholder="选择卡片背景色"
              class="color-picker" />
          </el-form-item>

          <el-form-item label="卡片边框色" prop="cardBorderColor">
            <el-color-picker v-model="form.cardBorderColor" :predefine="predefineBorderColors" placeholder="选择卡片边框色"
              class="color-picker" />
          </el-form-item>
        </div>

        <el-form-item label="菜单权限">
          <el-tree ref="treeRef" :data="treeData" node-key="id" show-checkbox :default-checked-keys="checkedMenuIds"
            :props="{ label: 'label', children: 'children' }" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogOpen = false" size="default">
            取消
          </el-button>
          <el-button type="primary" @click="submitRole" size="default">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts" name="RoleManagement">
import { ref, computed, reactive, onMounted } from 'vue'
import { ElForm, ElMessage, ElMessageBox } from 'element-plus'



// ===================== 常量配置（抽离常量，便于维护）=====================
// 预定义颜色（优化颜色选择体验，保持风格统一）
const PREDEFINE_COLORS = [
  '#4E9F3D', '#3498DB', '#F39C12', '#9B59B6', '#E74C3C', '#1ABC9C'
] as const
const PREDEFINE_BG_COLORS = [
  '#F0F7F2', '#F0F8FF', '#FFF7E6', '#F9F0FF', '#FDE7EB', '#E8F4F8'
] as const
const PREDEFINE_BORDER_COLORS = [
  '#D9EAD3', '#D9E8F6', '#FFE6C7', '#EBD3FF', '#FCD5D9', '#D1E7DD'
] as const

// 表单校验规则（抽离规则，逻辑清晰）
const formRules = reactive({
  name: [{ required: true, message: '请填写角色名称', trigger: 'blur' }],
  badgeColor: [{ required: true, message: '请选择徽章颜色', trigger: 'change' }],
  cardBgColor: [{ required: true, message: '请选择卡片背景色', trigger: 'change' }],
  cardBorderColor: [{ required: true, message: '请选择卡片边框色', trigger: 'change' }],
})

// ===================== 状态管理（规范命名，避免歧义）=====================

const formRef = ref<InstanceType<typeof ElForm>>() // 表单引用
const keyword = ref('') // 搜索关键词
const tagFilter = ref<string | null>('全部权限') // 标签筛选
const dialogOpen = ref(false) // 对话框显示状态
const dialogTitle = ref('') // 对话框标题
const editingId = ref<number | null>(null) // 编辑中的角色ID
const loading = ref(true)
import { getRolesData, updateRole, addRole } from '@/api/system/role'
import { roleMenuTreeSelect, getMenuTree } from '@/api/menu'
onMounted(() => {
  getRoleList()
})
const getRoleList = async () => {
  loading.value = true
  await getRolesData().then((res) => {
    roles.value = res.data
  })
    .catch(() => {
      ElMessage.error("查询角色列表失败")
    })
    .finally(() => {
      loading.value = false
    })
}

// 角色数据（初始化数据规范，类型完整）
const roles = ref<RoleItem[]>([])

// 表单数据（默认值与初始化数据一致）
const form = reactive<RoleItem>({
  id: 0,
  name: '',
  description: '',
  tags: [],
  badgeColor: '#4E9F3D',
  cardBgColor: '#F0F7F2',
  cardBorderColor: '#D9EAD3'
})
const treeData = ref<any[]>([])
const checkedMenuIds = ref<number[]>([])
const treeRef = ref()

const loadRoleMenus = async (roleId: number) => {
  const resp = await roleMenuTreeSelect(roleId)
  const data = (resp as any).data || {}
  treeData.value = data.menus || data || []
  checkedMenuIds.value = data.checkedKeys || []
}
// ===================== 计算属性（逻辑抽离，复用性强）=====================
// 所有权限标签（去重）
const allTags = computed(() => {
  return Array.from(new Set(roles.value.flatMap(role => role.tags)))
})

// 筛选后的角色列表（逻辑清晰，便于扩展）
const filteredRoles = computed(() => {
  const trimKeyword = (keyword.value || '').trim()
  const tf = tagFilter.value
  return roles.value.filter(role => {
    const matchName = trimKeyword === '' || role.name.includes(trimKeyword)
    const matchTag = !tf || tf === '全部权限' || role.tags.includes(tf)
    return matchName && matchTag
  })
})

// 预定义颜色（响应式，便于表单使用）
const predefineColors = ref([...PREDEFINE_COLORS])
const predefineBgColors = ref([...PREDEFINE_BG_COLORS])
const predefineBorderColors = ref([...PREDEFINE_BORDER_COLORS])

// ===================== 方法封装（单一职责，便于测试）=====================
/** 打开新增角色对话框 */
const openAdd = () => {
  editingId.value = null
  // 重置表单
  form.id = Date.now()
  form.name = ''
  form.description = ''
  form.tags = []
  form.badgeColor = '#4E9F3D'
  form.cardBgColor = '#F0F7F2'
  form.cardBorderColor = '#D9EAD3'
  // 重置表单校验
  formRef.value?.resetFields()
  dialogTitle.value = '新增角色'
  checkedMenuIds.value = []
  getMenuTree().then((resp: any) => {
    treeData.value = resp.data || []
  })
  dialogOpen.value = true
}

/** 打开编辑角色对话框 */
const openEdit = (item: RoleItem) => {
  editingId.value = item.id
  // 赋值表单
  Object.assign(form, { ...item, tags: [...item.tags] }) // 深拷贝，避免引用问题
  // 重置表单校验
  formRef.value?.resetFields()
  dialogTitle.value = '编辑角色'
  loadRoleMenus(item.id)
  dialogOpen.value = true
}
/** 确认删除角色 - 核心修复：使用全局 ElMessageBox */
const confirmDelete = (item: RoleItem) => {
  // 直接使用全局 ElMessageBox.confirm（无需手动导入路径）
  ElMessageBox.confirm(
    `是否确认删除角色“${item.name}”？`,
    '删除确认',
    {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      draggable: true,
      center: true
    }
  ).then(() => {
    roles.value = roles.value.filter(role => role.id !== item.id)
    // 全局 ElMessage 提示
    ElMessage.success('删除成功')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

/** 提交角色表单（新增/编辑） */
const submitRole = async () => {
  // 表单校验
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch (error) {
    return // 校验失败，终止提交
  }

  const roleName = form.name.trim()
  // 新增角色
  if (editingId.value === null) {
    const half = (treeRef.value && treeRef.value.getHalfCheckedKeys) ? treeRef.value.getHalfCheckedKeys() : []
    const full = (treeRef.value && treeRef.value.getCheckedKeys) ? treeRef.value.getCheckedKeys() : []
    const menuIds = Array.from(new Set([...(half as number[]), ...(full as number[])]))
    await addRole({ roleName: roleName, description: form.description.trim(), menuIds })
    ElMessage.success('新增成功')
    await getRoleList()
  } else {
    const half = (treeRef.value && treeRef.value.getHalfCheckedKeys) ? treeRef.value.getHalfCheckedKeys() : []
    const full = (treeRef.value && treeRef.value.getCheckedKeys) ? treeRef.value.getCheckedKeys() : []
    const menuIds = Array.from(new Set([...(half as number[]), ...(full as number[])]))
    await updateRole({ roleId: form.id, roleName: roleName, description: form.description.trim(), menuIds })
    ElMessage.success('修改成功')
    await getRoleList()
  }

  dialogOpen.value = false
}



// 处理标签显示（超过3个时显示前3个+等X个）
const getDisplayTags = (tags: string[]) => {
  const MAX_TAGS = 10 // 最多显示3个标签
  if (tags.length <= MAX_TAGS) {
    return { displayTags: tags, hasMore: false, moreCount: 0 }
  }
  return {
    displayTags: tags.slice(0, MAX_TAGS),
    hasMore: true,
    moreCount: tags.length - MAX_TAGS
  }
}
</script>

<style scoped lang="scss">
// 引入 Element Plus 全局变量（可选，保持样式一致性）
// @import "element-plus/theme-chalk/src/common/var.scss";

.role-page {
  width: 100%;
  padding: 16px 20px;
  box-sizing: border-box;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 16px;
  line-height: 1.5;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  background: #fafafc;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap; // 适配小屏幕换行
}

.toolbar-input {
  width: 280px;
  flex-shrink: 0;
}

.toolbar-select {
  width: 180px;
  flex-shrink: 0;
}

.flex-spacer {
  flex: 1;
}

.add-btn {
  border-radius: 999px;
  padding: 8px 16px;
}

// 卡片网格布局（优化响应式，间距合理）
.role-grid-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px; // 优化间距，避免过宽
  padding: 0 8px;
  max-width: 1400px; // 扩大最大宽度，适配宽屏
  margin: 0 auto;

  // 响应式：平板（768-1200px）一行1列
  @media (max-width: 1200px) and (min-width: 768px) {
    grid-template-columns: 1fr;
    gap: 30px;
  }

  // 响应式：手机（≤768px）一行1列，缩小间距
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 0;
  }
}

.card-item {
  width: 100%;
}

// 卡片样式（优化阴影层次，交互流畅）
.role-card {
  width: 100%;
  box-sizing: border-box;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  min-height: 240px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); // 更流畅的过渡动画

  &:hover {
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
    transform: translateY(-4px);
  }
}

.card-head {
  display: flex;
  align-items: flex-start; // 顶部对齐，避免内容错位
  gap: 12px;
  margin-bottom: 20px;
  flex-shrink: 0;
}

.head-text {
  flex: 1;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  margin-bottom: 6px;
}

.subtitle {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
  min-height: 44px; // 固定高度，避免卡片高度不一致
  display: -webkit-box;
  -line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

// 按钮样式（复用 Element Plus 文本按钮样式，优化 hover 效果）
.pill-btn {
  background-color: #f1f6f7;
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 999px;

  &.edit {
    color: #16a34a;

    &:hover {
      background: #f0f7f2;
    }
  }

  &.delete {
    color: #dc2626;

    &:hover {
      background: #fde7eb;
    }
  }
}

// 标签列表（优化布局，避免内容溢出）
.tag-list {
  margin-top: auto; // 自动下沉到底部，保持卡片底部对齐
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding-top: 8px;
}

// 头部整体容器
.header-container {
  width: 100%;
  padding: 10px 24px; // 加大内边距，提升呼吸感
  box-sizing: border-box;
  background-color: #fff; // 白色背景，与内容区卡片形成层次
  border-radius: 12px; // 统一圆角，增强精致感
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05); // 轻微阴影，区分页面层级
  margin-bottom: 24px; // 与下方卡片区保持距离
}

// 标题组：主标题+副标题
.header-title-group {
  margin-bottom: 16px; // 与工具栏保持距离

  .page-title {
    font-size: 22px;
    font-weight: 700;
    color: #1d2129;
    margin: 0 0 6px 0; // 重置默认margin
    display: flex;
    align-items: center;
    gap: 8px;

    // 可选项：添加标题图标，增强识别度
    &::before {
      content: '';
      width: 4px;
      height: 20px;
      background-color: #4E9F3D; // 与新增按钮颜色呼应
      border-radius: 2px;
    }
  }

  .page-subtitle {
    font-size: 14px;
    color: #86909C; // 浅灰色，不抢主标题风头
    margin: 0; // 重置默认margin
    padding-left: 12px; // 与主标题图标对齐
  }
}

// 工具栏：搜索筛选+操作按钮
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between; // 两端对齐，布局更清晰
  width: 100%;
  gap: 16px; // 元素间最小间距
  flex-wrap: wrap; // 支持换行，适配小屏幕
}

// 搜索和筛选组合
.search-filter-group {
  display: flex;
  align-items: center;
  gap: 16px; // 搜索框与筛选框间距
  flex: 1; // 占满剩余空间，保持按钮右对齐
  min-width: 300px; // 最小宽度，避免压缩变形
}

// 搜索框样式优化
.toolbar-input {
  width: 320px; // 适度加宽，提升输入体验
  flex-shrink: 1; // 允许收缩，适配小屏幕
}

// 筛选框样式优化
.toolbar-select {
  width: 200px; // 适度加宽，标签显示更完整
  flex-shrink: 0; // 不收缩，保证选择框完整性
}

// 操作按钮区域
.action-buttons {
  display: flex;
  align-items: center;
  gap: 12px; // 预留多个按钮的间距
}

// 新增按钮优化
.add-btn {
  border-radius: 6px; // 适度圆角，与整体风格统一
  padding: 8px 20px; // 加大点击区域
  font-weight: 500; // 加粗文字，突出重要操作
  transition: all 0.2s;

  &:hover {
    transform: translateY(-1px); // 轻微上浮，增强交互反馈
    box-shadow: 0 2px 8px rgba(78, 159, 61, 0.2); // 绿色阴影，强化识别
  }
}


// 响应式适配
@media (max-width: 768px) {
  .header-container {
    padding: 16px 16px;
  }

  .search-filter-group {
    width: 100%; // 小屏幕占满宽度
  }

  .toolbar-input {
    width: 100%; // 搜索框占满剩余空间
  }

  .toolbar-select {
    width: 100%; // 筛选框占满宽度
  }

  .action-buttons {
    width: 100%; // 按钮区域占满宽度
    justify-content: flex-end; // 按钮右对齐
  }
}

.role-dialog {
  /* 优化对话框内边距 */
  border-radius: 12px;
  --el-dialog-padding-primary: 20px 24px;
}

.role-form {
  /* 增加表单整体间距 */
  margin-top: 10px;
}

.form-item-required ::v-deep .el-form-item__label::before {
  /* 自定义必填项星号样式 */
  content: '*';
  color: var(--el-color-danger);
  margin-right: 4px;
}

.form-input,
.form-select {
  /* 统一输入框宽度 */
  width: 100%;
}

.form-input,
.form-select {
  /* 统一输入框宽度 */
  width: 100%;
}


.color-group {
  /* 颜色选择区域增加内边距和背景 */
  padding: 12px;
  background-color: var(--el-fill-color-light);
  border-radius: var(--el-border-radius-base);
  margin-top: 8px;
}

.color-picker {
  /* 颜色选择器宽度调整 */
  width: 100%;
}

/* 优化表单项间距 */
::v-deep .el-form-item {
  margin-bottom: 16px;
}

/* 最后一个表单项去除底部间距 */
::v-deep .el-form-item:last-child {
  margin-bottom: 0;
}

.dialog-footer {
  /* 按钮区域布局优化 */
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}
</style>
