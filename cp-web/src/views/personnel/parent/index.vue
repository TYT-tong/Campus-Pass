<template>
  <div class="app-container">
    <div class="panel-header">
      <div class="panel-title">家长列表</div>

    </div>

    <div class="header">
      <el-form label-width="68px" :inline="true">
        <el-form-item label="用户名称">
          <el-input v-model="queryParams.parentName" placeholder="请输入用户名称" clearable style="width: 180px">
            <template #prefix>
              <el-icon class="search-icon">
                <Search />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号码" clearable style="width: 180px">
            <template #prefix>
              <el-icon class="search-icon">
                <Search />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable class="select" size="default">
            <el-option v-for="(label, value) in sys_normal_disable" :key="value" :label="label" :value="value" />
          </el-select>
        </el-form-item>

        <el-form-item class="btn_form">
          <el-button type="primary" plain icon="Plus" v-hasPermi="['system:user:add']">新增</el-button>
          <el-button type="warning" plain icon="Download" v-hasPermi="['system:user:export']">导出</el-button>
        </el-form-item>
      </el-form>
    </div>



    <div class="bottom">
      <el-table v-loading="loading" :data="pagedData" @sort-change="onSortChange">
        <el-table-column type="selection" width="30" align="center" />
        <el-table-column label="编号" align="center" key="parentId" width="100" prop="parentId"
          v-if="columnsVisible.id" />
        <el-table-column label="家长姓名" align="center" key="parentName" width="200" prop="parentName" />
        <el-table-column label="年龄" align="center" key="age" prop="age" width="100" :show-overflow-tooltip="true" />
        <el-table-column v-if="columnsVisible.sex" prop="sex" label="性别" width="100" align="center">
          <template #default="{ row }">
            <span class="gender-tag" :class="row.sex === '男' ? 'male' : 'female'">
              {{ row.sex }}
            </span>
          </template>
        </el-table-column> <el-table-column label="手机号码" align="center" key="phone" prop="phone" width="200" />
        <el-table-column label="学生姓名" align="center" key="studentName" width="150" prop="studentName"></el-table-column>

        <el-table-column label="用户名" align="center" key="userName" prop="userName"></el-table-column>

        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }">
            <span class="status-cell">
              <span :class="['status-dot', row.status]" />
              <span :class="['status-text', row.status]">{{ statusLabel(row.status) }}</span>
            </span>
          </template>
        </el-table-column>
        <el-table-column label=" 操作" align="center" width="150" class-name="small-padding fixed-width">
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
          :page-sizes="[10, 15, 25, 40]" :size="size" :disabled="disabled" :background="background"
          layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>


  </div>

</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted, toRefs } from 'vue'
import { Search } from '@element-plus/icons-vue'


import { useDebounceFn } from '@vueuse/core'
import type { ParentType } from '#/personnel'
import { getParentList } from '@/api/personnel/parent'
import { ElMessage } from 'element-plus'
import { isPhone } from '@/utils/validate'
import { scrollTo } from '@/utils/scroll-to'
import { useDictMap } from '@/utils/dict'





const loading = ref(true)            // 列表加载状态
const { sys_normal_disable } = useDictMap('sys_normal_disable')
const pagedData = ref<ParentType[]>([])
const total = ref(0)
const size = ref<'small' | 'default' | 'large'>('default')
const disabled = ref(false)
const background = ref(true)

onMounted(() => {
  getParentData()
})
// 数据对象（包含表单数据、查询参数和验证规则）
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    parentName: undefined,
    phone: undefined,
    status: undefined,
  },

})
const { queryParams } = toRefs(data)

const getParentData = async () => {
  loading.value = true; // 开始加载

  try {

    const res: any = await getParentList(queryParams.value)

    pagedData.value = res.rows
    total.value = res.total
  } catch (error) {
    console.error('获取家长数据失败:', error)
  } finally {
    loading.value = false; // 结束加载
  }

}


const columnsVisible = reactive({
  id: true,
  phone: true,
  studentName: true,
  userName: true,
  sex: true,
  age: true,
  cardId: true,
  status: true,
})

const savedCols = localStorage.getItem('parent-columns')
if (savedCols) {
  try {
    Object.assign(columnsVisible, JSON.parse(savedCols))
  } catch { }
}

watch(columnsVisible, (nv) => {
  localStorage.setItem('parent-columns', JSON.stringify(nv))
}, { deep: true })


watch(
  () => [
    queryParams.value.parentName,
    queryParams.value.phone,
    queryParams.value.status,
  ],
  useDebounceFn(() => {
    const phone = queryParams.value.phone
    if (phone && !isPhone(phone)) {
      ElMessage({ type: 'warning', message: '请输入合法的手机号码' })
      return
    }
    queryParams.value.pageNum = 1
    getParentData()
  }, 300)
)

watch([() => queryParams.value.pageNum, () => queryParams.value.pageSize], () => {
  getParentData()
})

function handleSizeChange(val: number) {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = val
  scrollTo(0, 300)
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  scrollTo(0, 300)
}
// 移除未使用的 searchKey 逻辑




const sortState = reactive<{ prop: string; order: 'ascending' | 'descending' | '' }>({ prop: '', order: '' })
const onSortChange = (p: { prop: string; order: 'ascending' | 'descending' | null }) => {
  sortState.prop = p.prop || ''
  sortState.order = (p.order || '') as any
}

const statusLabel = (v: string) => {
  return sys_normal_disable.value?.[v] ?? v
}


const edit = (_row: ParentType) => { }
const remove = (row: ParentType) => {

}




</script>

<style scoped lang="scss">
$primary-color: #409EFF;
$border-color: #EBEEF5;
$text-primary: #303133;
$success-color: #67C23A;
$danger-color: #F56C6C;
$warning-color: #E6A23C;
$card-bg: #FFFFFF;
$radius: 8px;

.app-container {

  width: 100%;
  padding: 10px;
  margin-left: 20px;

}

.btn_form {
  position: relative;
  left: 400px;
}

/* 正确选中表单标签：使用内置类名 + 深度选择器 */
.header :deep(.el-form-item__label) {

  font-family: "Microsoft YaHei", sans-serif;
  font-size: 14px;
  /* 字号调大，方便测试效果 */
  color: #292727;
  font-weight: bold;


}

.header :deep(.el-form-item) {
  padding-top: 20px;
}

/* 1. 统一控制所有按钮的尺寸（高度、内边距、字体） */
.workspace .el-button {
  height: 32px !important;
  /* 按钮高度（可调整为 28px/30px 等） */
  padding: 0 12px !important;
  /* 按钮内边距（左右 12px，上下 0 适配高度） */
  font-size: 12px !important;
  /* 按钮文字大小 */
}

/* 2. （可选）调整按钮图标与文字的间距（若图标过大） */
.workspace .el-button .el-icon {
  margin-right: 4px !important;
  /* 图标与文字的间距，可缩小为 4px */
  font-size: 14px !important;
  /* 图标大小，可缩小为 14px */
}

.header {
  width: 98%;
  padding-left: 20px;
  margin-bottom: 10px;
  background: #FAFAFC;
  border: 1px solid #EBEEF5;
  border-radius: 12px;


}

.workspace {
  background: #F6F7FB;
  border: 1px solid #EBEEF5;
  border-radius: 10px;
  padding: 8px 12px;
  margin-bottom: 12px;
}

.bottom :deep(.el-table__header-wrapper) th {
  background: #FAFAFC;
  border-bottom: 1px solid #EBEEF5;
  color: #606266;
}

.bottom :deep(.el-table__body tr:nth-child(odd) td) {
  background: #FBFCFF;
}

.bottom :deep(.el-table__row:hover td) {
  background: #F2F3F5;
}

.bottom {
  position: relative;
  padding-bottom: 64px;
}

.pagination {
  position: absolute;
  right: 40px;
  bottom: 12px;
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

.status-dot.warning {
  background: $warning-color;
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
</style>
