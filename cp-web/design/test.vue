<template>
  <div class="page">
    <div class="top-card">
      <el-input class="pill" v-model="keyword" placeholder="搜索姓名/手机号" clearable style="width:280px" />
      <el-select class="pill" v-model="roleFilter" placeholder="角色" clearable style="width:160px">
        <el-option v-for="r in roles" :key="r" :label="r" :value="r" />
      </el-select>
      <el-select class="pill" v-model="statusFilter" placeholder="状态" clearable style="width:160px">
        <el-option label="正常" value="ok" />
        <el-option label="冻结" value="frozen" />
      </el-select>
      <div class="spacer"></div>
      <el-button class="pill-btn" type="primary" @click="applyFilter">搜索</el-button>
      <el-button class="pill-btn" @click="add">新增</el-button>
      <div class="decor"></div>
    </div>

    <div class="list-card">
      <div class="list-header">
        <div>姓名</div>
        <div>手机号</div>
        <div>角色</div>
        <div>状态</div>
        <div>创建时间</div>
        <div>操作</div>
      </div>
      <div class="list-body">
        <div v-if="loading" class="loading-wrap">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else>
          <div v-for="row in viewRows" :key="row.id" class="row">
            <div>{{ row.name }}</div>
            <div>{{ row.phoneMasked }}</div>
            <div>{{ row.role }}</div>
            <div><span :class="['status-pill', row.status]">{{ statusText[row.status] }}</span></div>
            <div>{{ row.created }}</div>
            <div><span class="op-link" @click="edit(row)">编辑</span></div>
          </div>
          <div v-if="viewRows.length === 0" class="empty">
            <el-empty description="暂无数据" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

type Status = 'ok' | 'frozen'
type ParentRow = { id: number; name: string; phone: string; role: string; status: Status; created: string }

const props = withDefaults(defineProps<{ rows: ParentRow[]; loading?: boolean; roles?: string[] }>(), {
  rows: [],
  loading: false,
  roles: ['学生', '教师', '管理员']
})

const emit = defineEmits<{
  (e: 'search', payload: { keyword: string; role?: string; status?: Status }): void
  (e: 'add'): void
  (e: 'edit', row: ParentRow): void
}>()

const keyword = ref('')
const roleFilter = ref('')
const statusFilter = ref<Status | ''>('')

const roles = computed(() => props.roles)
const loading = computed(() => props.loading)

const statusText: Record<Status, string> = { ok: '正常', frozen: '冻结' }

const maskPhone = (p: string) => p.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2')

const viewRows = computed(() => {
  const k = keyword.value.trim()
  return props.rows
    .filter(r => (k === '' || r.name.includes(k) || r.phone.includes(k)) && (roleFilter.value === '' || r.role === roleFilter.value) && (statusFilter.value === '' || r.status === statusFilter.value))
    .map(r => ({ ...r, phoneMasked: maskPhone(r.phone) }))
})

const applyFilter = () => emit('search', { keyword: keyword.value.trim(), role: roleFilter.value || undefined, status: (statusFilter.value as Status) || undefined })
const add = () => emit('add')
const edit = (row: ParentRow) => emit('edit', row)
</script>

<style scoped>
.page {
  padding: 24px;
  background: radial-gradient(800px 300px at 95% 0%, #ffe4ea 0%, #f5f7fb 60%);
}

.top-card {
  position: relative;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.top-card .spacer {
  flex: 1;
}

.decor {
  position: absolute;
  right: 24px;
  top: 14px;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: radial-gradient(circle, #ffe9c9 0%, #f7f7f7 70%);
}

.pill .el-input__wrapper,
.pill .el-select__wrapper {
  background: #f0f2f5;
  box-shadow: none;
  border-radius: 16px;
}

.pill .el-input__inner {
  height: 36px;
}

.pill .el-input__wrapper {
  padding: 0 14px;
}

.pill .el-select__wrapper {
  padding: 0 8px;
}

.pill-btn {
  border-radius: 16px;
  height: 36px;
}

.list-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
}

.list-header {
  display: grid;
  grid-template-columns: 1.3fr 1.2fr 1fr 1fr 1.2fr 0.8fr;
  padding: 14px 16px;
  color: #7a8088;
  background: #f7f7f8;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
}

.list-body {
  padding: 16px;
  background: #f2f3f5;
  border-bottom-left-radius: 16px;
  border-bottom-right-radius: 16px;
  min-height: 280px;
}

.row {
  display: grid;
  grid-template-columns: 1.3fr 1.2fr 1fr 1fr 1.2fr 0.8fr;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
  padding: 12px 16px;
  margin-bottom: 12px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.status-pill.ok {
  background: #d5f3e7;
  color: #18a058;
}

.status-pill.frozen {
  background: #ffd9df;
  color: #d4380d;
}

.op-link {
  color: #409eff;
  cursor: pointer;
}

.loading-wrap {
  background: #fff;
  border-radius: 12px;
  padding: 12px;
}

.empty {
  padding: 24px 8px;
}
</style>
