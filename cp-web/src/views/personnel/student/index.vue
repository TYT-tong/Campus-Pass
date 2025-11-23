<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'

import { ElMessageBox, ElMessage } from 'element-plus'

import { useDebounceFn } from '@vueuse/core'
import type { StudentType } from '#/personnel'
import { getStudentList } from '@/api/personnel/student'





const loading = ref(true)            // 列表加载状态
const statuses = [
  { label: '正常', value: '0' },
  { label: '禁用', value: '1' },
]
const pagedData = ref<StudentType[]>([])
const list = ref<StudentType[]>([])
const total = ref(0)

onMounted(() => {
  getStudentsData()
})
// 数据对象（包含表单数据、查询参数和验证规则）
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    keyword: undefined,
    grade: undefined,
    class: undefined,
    status: undefined,
  },

})
const { queryParams } = toRefs(data)

const getStudentsData = async () => {
  loading.value = true; // 开始加载

  try {

    const res: any = await getStudentList(queryParams.value)

    pagedData.value = res.rows
    total.value = res.total
  } catch (error) {
    console.error('获取学生数据失败:', error)
  } finally {
    loading.value = false; // 结束加载
  }

}


const columnsVisible = reactive({
  id: true,
  phone: true,
  grade: true,
  class: true,
  sex: true,
  card: true,
  idCard: true,
})

const savedCols = localStorage.getItem('student-columns')
if (savedCols) {
  try {
    Object.assign(columnsVisible, JSON.parse(savedCols))
  } catch { }
}

watch(columnsVisible, (nv) => {
  localStorage.setItem('student-columns', JSON.stringify(nv))
}, { deep: true })


watch(
  [() => queryParams, () => queryParams.value.grade, () => queryParams.value.class, () => queryParams.value.status],
  useDebounceFn(() => {
    queryParams.value.pageNum = 1
    getStudentsData()
  }, 300)
)

watch([() => queryParams.value.pageNum, () => queryParams.value.pageSize], () => {
  getStudentsData()
})

function handleSizeChange(val: number) {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = val
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
}
const searchKey = ref('')
watch(() => queryParams.value.keyword, useDebounceFn((v: any) => {
  searchKey.value = v.trim()
}, 300))



const classes = computed(() => {
  const base = queryParams.value.grade ? pagedData.value.filter((r) => r.grade === queryParams.value.grade) : pagedData.value
  return Array.from(new Set(base.map((r) => r.stuClass))).filter((v) => !!v)
})

const sortState = reactive<{ prop: string; order: 'ascending' | 'descending' | '' }>({ prop: '', order: '' })
const onSortChange = (p: { prop: string; order: 'ascending' | 'descending' | null }) => {
  sortState.prop = p.prop || ''
  sortState.order = (p.order || '') as any
}

const statusLabel = (v: string) => {
  const m = statuses.find((i) => i.value === v)
  return m ? m.label : v
}

const grades = computed(() => {
  return Array.from(new Set(pagedData.value.map((r) => r.grade))).filter((v) => !!v)
})

const edit = (_row: StudentType) => { }
const remove = (row: StudentType) => {
  ElMessageBox.confirm('确认删除该学生记录？', '提示', { type: 'warning' })
    .then(() => {
      list.value = list.value.filter((r) => r.studentId !== row.studentId)
      ElMessage.success('删除成功')
    })
    .catch(() => { })
}




</script>
<template>
  <div class="student-page">
    <div class="panel" ref="panelRef">
      <div class="panel-header">
        <div class="panel-title">学生列表</div>

      </div>

      <div class="filters">
        <div class="filters-left">
          <el-input v-model="queryParams.keyword" placeholder="按姓名或手机号搜索..." clearable class="search-input"
            size="default">
            <template #prefix>
              <el-icon class="search-icon">
                <Search />
              </el-icon>
            </template>
          </el-input>
          <el-select v-model="queryParams.grade" placeholder="全部年级" clearable class="select" size="default">
            <el-option v-for="g in grades" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select v-model="queryParams.class" placeholder="全部班级" clearable class="select" size="default">
            <el-option v-for="c in classes" :key="c" :label="c" :value="c" />
          </el-select>
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable class="select" size="default">
            <el-option v-for="s in statuses" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </div>
        <div class="filters-right">
          <el-button type="primary" round size="default" class="add-btn">
            <el-icon>
              <Plus />
            </el-icon>
            新增
          </el-button>

          <el-dropdown>
            <el-button class="export-btn" round size="default">
              <el-icon>
                <Download />
              </el-icon>
              导出
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>导出 CSV</el-dropdown-item>
                <el-dropdown-item>导出 Excel</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <el-table v-loading="loading" :data="pagedData" class="data-table" @sort-change="onSortChange" :fit="true" stripe
        highlight-current-row>
        <el-table-column v-if="columnsVisible.id" prop="studentId" label="编号" width="80" align="center" fixed="left" />

        <el-table-column prop="studentName" label="姓名" width="100" align="center" show-overflow-tooltip />
        <el-table-column v-if="columnsVisible.grade" prop="grade" label="年级" width="100" sortable="custom"
          align="center" />
        <el-table-column v-if="columnsVisible.class" prop="stuClass" label="班级" width="100" sortable="custom"
          align="center" />
        <el-table-column v-if="columnsVisible.sex" prop="sex" label="性别" width="64" align="center">
          <template #default="{ row }">
            <span class="gender-tag" :class="row.sex === '男' ? 'male' : 'female'">
              {{ row.sex }}
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnsVisible.phone" prop="phone" label="手机号" width="180" align="center" />

        <el-table-column v-if="columnsVisible.card" prop="cardId" label="IC卡号" width="250" align="center"
          show-overflow-tooltip />
        <el-table-column prop="parentName" label="家长姓名" width="100" align="center" show-overflow-tooltip />

        <el-table-column prop="userName" label="用户名" width="160" align="center" show-overflow-tooltip />

        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }">
            <span class="status-cell">
              <span :class="['status-dot', row.status]" />
              <span :class="['status-text', row.status]">{{ statusLabel(row.status) }}</span>
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="196" align="center" fixed="right">
          <template #default="{ row }">
            <div class="actions">
              <el-link type="primary" @click="edit(row)" class="action-link">编辑</el-link>
              <el-divider direction="vertical" />
              <el-link type="danger" @click="remove(row)" class="action-link">删除</el-link>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 15, 25, 40]" :background="true" layout="total, sizes, prev, pager, next, jumper"
          :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" size="small" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
// 基础变量定义
$primary-color: #409EFF;
$success-color: #67C23A;
$danger-color: #F56C6C;
$warning-color: #E6A23C;
$text-primary: #303133;
$text-secondary: #606266;
$text-tertiary: #909399;
$border-color: #EBEEF5;
$bg-color: #F5F7FA;
$card-bg: #FFFFFF;
$hover-color: #F0F7FF;
$shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
$shadow-hover: 0 4px 16px 0 rgba(0, 0, 0, 0.08);
$radius: 8px;

.panel {
  background: $card-bg;
  border-radius: $radius;
  box-shadow: $shadow;
  overflow: hidden;
  position: relative;
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: $shadow-hover;
  }
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid $border-color;
}

.panel-title {
  font-size: 18px;
  font-weight: 600;
  color: $text-primary;
  display: flex;
  align-items: center;
  gap: 8px;

  &::before {
    content: '';
    width: 4px;
    height: 16px;
    background: $primary-color;
    border-radius: 2px;
  }
}

.column-setting {
  color: $text-tertiary;
  background: transparent;
  border: 1px solid $border-color;

  &:hover {
    color: $primary-color;
    border-color: $primary-color;
    background: rgba(64, 158, 255, 0.05);
  }
}

.filters {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  gap: 12px;
  flex-wrap: wrap;
  background-color: $bg-color;
  border-bottom: 1px solid $border-color;
}

.filters-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.filters-right {
  display: flex;
  gap: 12px;
}

.search-input {
  width: 280px;

  &:deep(.el-input__wrapper) {
    background: $card-bg;
    border-radius: $radius;
    transition: all 0.2s ease;

    &:focus-within {
      border-color: $primary-color;
      box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    }
  }
}

.search-icon {
  color: $text-tertiary;
}

.select {
  width: 120px;

  &:deep(.el-select__wrapper) {
    background: $card-bg;
    border-radius: $radius;
    transition: all 0.2s ease;

    &:focus-within {
      border-color: $primary-color;
      box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    }
  }
}

.add-btn {
  transition: all 0.2s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
  }
}

.export-btn {
  background: $card-bg;
  border: 1px solid $border-color;
  color: $text-secondary;
  transition: all 0.2s ease;

  &:hover {
    color: $primary-color;
    border-color: $primary-color;
    background: rgba(64, 158, 255, 0.05);
  }
}

.data-table {
  table-layout: fixed;
  width: 100%;

  margin: 0;

  &:deep(.el-table__header-wrapper) {
    background-color: $bg-color;

    th {
      color: $text-tertiary;
      font-weight: 500;
      background: transparent;
      border-bottom: 1px solid $border-color;
      padding: 10px 0;
    }
  }

  &:deep(.el-table__body-wrapper) {
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }

    &::-webkit-scrollbar-thumb {
      background: #DCDFE6;
      border-radius: 3px;

      &:hover {
        background: #C0C4CC;
      }
    }
  }

  &:deep(.el-table__row) {
    height: 54px !important;

    transition: background-color 0.2s ease;

    &:hover>td {
      background-color: $hover-color !important;
    }

    &.current-row>td {
      background-color: rgba(64, 158, 255, 0.1) !important;
    }

    td {
      color: $text-secondary;
      border-bottom: 1px solid $border-color;
      padding: 12px 0;
      transition: background-color 0.2s ease;
    }
  }

  &:deep(.el-table__fixed-right, .el-table__fixed-left) {
    box-shadow: none;

    .el-table__fixed-header-wrapper {
      background-color: $bg-color;
    }
  }
}

.gender-tag {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;

  &.male {
    background-color: rgba(64, 158, 255, 0.1);
    color: $primary-color;
  }

  &.female {
    background-color: rgba(233, 30, 99, 0.1);
    color: #E91E63;
  }
}

.status-cell {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.status-dot {
  background: $success-color;
}

.status-dot.ban {
  background: $danger-color;
}

.status-dot.warning {
  background: $warning-color;
}

.status-text {
  color: $success-color;
  font-size: 13px;
}

.status-text.ban {
  color: $danger-color;
}

.status-text.warning {
  color: $warning-color;
}

.actions {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  white-space: nowrap;
}

.action-link {
  padding: 0 6px;
  height: 28px;
  line-height: 28px;
  border-radius: 4px;
  transition: all 0.2s ease;
  font-size: 13px;

  &:hover {
    opacity: 0.9;
    transform: translateY(-1px);
  }
}

:deep(.el-divider--vertical) {
  margin: 0 4px;
  height: 16px;
  background-color: $border-color;
}

.pagination {
  padding: 16px 24px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-top: 1px solid $border-color;

  &:deep(.el-pagination) {
    .el-pagination__total {
      color: $text-tertiary;
      margin-right: 8px;
    }

    .btn-prev,
    .btn-next {
      border-radius: 4px;
      transition: all 0.2s ease;

      &:hover {
        color: $primary-color;
        border-color: $primary-color;
        background-color: rgba(64, 158, 255, 0.05);
      }
    }

    .el-pager li {
      border-radius: 4px;
      margin: 0 2px;
      transition: all 0.2s ease;

      &:hover:not(.is-active) {
        color: $primary-color;
        background-color: rgba(64, 158, 255, 0.05);
      }

      &.is-active {
        background-color: $primary-color;
        color: #fff;
        font-weight: 500;
      }
    }

    .el-select .el-input .el-input__wrapper {
      width: 90px;
    }
  }
}

.columns-popover {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 12px;
  padding: 8px 0;

  &:deep(.el-checkbox) {
    margin: 0;
    padding: 4px 0;

    .el-checkbox__label {
      color: $text-secondary;
    }
  }
}

// 响应式调整
@media (max-width: 1200px) {
  .search-input {
    width: 220px;
  }
}

@media (max-width: 992px) {
  .filters {
    flex-direction: column;
    align-items: flex-start;
  }

  .filters-right {
    width: 100%;
    justify-content: flex-end;
    margin-top: 8px;
  }
}

@media (max-width: 768px) {
  .search-input {
    width: 100%;
  }

  .filters-left {
    width: 100%;
  }

  .select {
    flex: 1;
    min-width: 100px;
  }
}
</style>
