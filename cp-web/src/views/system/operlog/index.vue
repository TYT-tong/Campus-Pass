<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="操作地址" prop="operIp">
        <el-input v-model="queryParams.operIp" placeholder="请输入操作地址" clearable style="width: 240px;"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="系统模块" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入系统模块" clearable style="width: 240px;"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="操作人员" prop="operName">
        <el-input v-model="queryParams.operName" placeholder="请输入操作人员" clearable style="width: 240px;"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="businessType">
        <el-select v-model="queryParams.businessType" placeholder="操作类型" clearable style="width: 240px">
          <el-option v-for="dict in sys_oper_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="操作状态" clearable style="width: 240px">
          <el-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间" style="width: 308px">
        <el-date-picker v-model="dateRange" value-format="YYYY-MM-DD HH:mm:ss" type="daterange" range-separator="-"
          start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:operlog:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" @click="handleClean"
          v-hasPermi="['system:operlog:remove']">清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport"
          v-hasPermi="['system:operlog:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="operlogRef" v-loading="loading" :data="operlogList" @selection-change="handleSelectionChange"
      :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="日志编号" align="center" prop="operId" />
      <el-table-column label="系统模块" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="操作类型" align="center" prop="businessType">
        <template #default="scope">
          <dict-tag :options="sys_oper_type" :value="scope.row.businessType" />
        </template>
      </el-table-column>
      <el-table-column label="请求方式" align="center" prop="requestMethod" />
      <el-table-column label="操作人员" align="center" prop="operName" width="110" :show-overflow-tooltip="true"
        sortable="custom" :sort-orders="['descending', 'ascending']" />
      <el-table-column label="操作地址" align="center" prop="operIp" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="操作状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="sys_common_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作日期" align="center" prop="operTime" width="180" sortable="custom"
        :sort-orders="['descending', 'ascending']">
        <template #default="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消耗时间" align="center" prop="costTime" width="110" :show-overflow-tooltip="true"
        sortable="custom" :sort-orders="['descending', 'ascending']">
        <template #default="scope">
          <span>{{ scope.row.costTime }}毫秒</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)"
            v-hasPermi="['system:operlog:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 操作日志详细 -->
    <el-dialog title="操作日志详细" v-model="open" width="800px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模块：">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
            <el-form-item label="登录信息：">{{ form.operName }} / {{ form.operIp }} </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求地址：">{{ form.operUrl }}</el-form-item>
            <el-form-item label="请求方式：">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作状态：">
              <div v-if="form.status === 0">正常</div>
              <div v-else-if="form.status === 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="消耗时间：">{{ form.costTime }}毫秒</el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间：">{{ parseTime(form.operTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Operlog">
// 导入操作日志相关的API方法：获取列表、删除、清空
import { list, delOperlog, cleanOperlog } from "@/api/system/operlog"
import { getCurrentInstance } from "vue"
import { parseTime } from "@/utils/ruoyi"

// 获取当前组件实例的代理对象，用于访问全局方法和属性
const { proxy } = getCurrentInstance() as any
// 通过全局方法useDict获取字典数据：操作类型字典(sys_oper_type)和通用状态字典(sys_common_status)
const { sys_oper_type, sys_common_status } = proxy.useDict("sys_oper_type", "sys_common_status")

// 响应式变量定义
const operlogList = ref<any[]>([]) // 操作日志列表数据
const open = ref(false) // 控制详情弹窗是否显示
const loading = ref(true) // 控制加载状态
const showSearch = ref(true) // 控制搜索区域是否显示
const ids = ref<number[]>([]) // 选中的日志ID数组（用于批量操作）
// const single = ref(true) // 控制是否允许单选（实际未在代码中使用，可能为预留）
const multiple = ref(true) // 控制是否允许多选（根据选中状态动态变化）
const total = ref(0) // 总记录数
// const title = ref("") // 弹窗标题（实际未在代码中使用，可能为预留）
const dateRange = ref<string[]>([]) // 日期范围选择器的值
const defaultSort = ref({ prop: "operTime", order: "descending" as const }) // 默认排序规则：按操作时间降序

// 响应式数据对象，包含表单和查询参数
const data = reactive({
  form: {} as any, // 用于详情弹窗的表单数据
  queryParams: { // 查询参数
    pageNum: 1, // 当前页码
    pageSize: 10, // 每页条数
    operIp: undefined, // 操作IP
    title: undefined, // 操作标题
    operName: undefined, // 操作人员
    businessType: undefined, // 业务类型
    status: undefined, // 操作状态
    orderByColumn: undefined, // 排序字段
    isAsc: undefined // 排序方式
  }
})

// 将响应式对象的属性转为ref，方便在模板中使用
const { queryParams, form } = toRefs(data)

/**
 * 查询操作日志列表
 * 调用API获取数据，更新列表和总记录数，关闭加载状态
 */
function getList() {
  loading.value = true // 开始加载
  // 调用列表API，通过proxy.addDateRange方法给查询参数添加日期范围
  list(proxy.addDateRange(queryParams.value, dateRange.value)).then((response: any) => {
    operlogList.value = response.rows // 赋值列表数据
    total.value = response.total // 赋值总记录数
    loading.value = false // 结束加载
  })
}

/**
 * 操作日志类型字典翻译
 * 将业务类型编码转换为对应的中文标签
 * @param {Object} row - 当前行数据
 * @param {Object} column - 当前列配置（未使用）
 * @returns {string} 翻译后的中文标签
 */
function typeFormat(row: any) {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType)
}

/**
 * 搜索按钮操作
 * 重置页码为1，重新查询列表
 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/**
 * 重置按钮操作
 * 清空日期范围，重置查询表单，重置页码，恢复默认排序
 */
function resetQuery() {
  dateRange.value = [] // 清空日期范围
  proxy.resetForm("queryRef") // 重置查询表单（需配合表单ref使用）
  queryParams.value.pageNum = 1 // 重置页码
  // 恢复默认排序（需配合表格ref使用）
  proxy.$refs["operlogRef"].sort(defaultSort.value.prop, defaultSort.value.order)
}

/**
 * 多选框选中数据变化时触发
 * 更新选中的ID数组，控制多选按钮状态
 * @param {Array} selection - 选中的行数据数组
 */
function handleSelectionChange(selection: any[]) {
  ids.value = selection.map(item => item.operId) // 提取选中行的operId组成数组
  multiple.value = !selection.length // 若没有选中项，禁用批量操作按钮
}

/**
 * 排序触发事件
 * 更新查询参数中的排序字段和排序方式，重新查询
 * @param {Object} column - 排序的列配置
 * @param {string} prop - 排序字段（未使用，从column获取）
 * @param {string} order - 排序方式（未使用，从column获取）
 */
function handleSortChange(column: any) {
  queryParams.value.orderByColumn = column.prop // 设置排序字段
  queryParams.value.isAsc = column.order // 设置排序方式（ascending/descending）
  getList() // 重新查询
}

/**
 * 详细按钮操作
 * 打开详情弹窗，赋值当前行数据到表单
 * @param {Object} row - 当前行数据
 */
function handleView(row: any) {
  open.value = true // 打开弹窗
  form.value = row // 赋值表单数据
}

/**
 * 删除按钮操作
 * 支持单条删除（传入row）和批量删除（使用已选中的ids）
 * @param {Object} [row] - 可选，当前行数据（单条删除时使用）
 */
function handleDelete(row?: any) {
  // 确定要删除的ID（单条取row.operId，批量取ids.value）
  const operIds = row?.operId || ids.value
  // 显示确认弹窗
  proxy.$modal.confirm('是否确认删除日志编号为"' + operIds + '"的数据项?').then(function () {
    return delOperlog(operIds) // 调用删除API
  }).then(() => {
    getList() // 重新查询列表
    proxy.$modal.msgSuccess("删除成功") // 显示成功提示
  }).catch(() => { }) // 取消或失败时不做处理
}

/**
 * 清空按钮操作
 * 清空所有操作日志
 */
function handleClean() {
  // 显示确认弹窗
  proxy.$modal.confirm("是否确认清空所有操作日志数据项?").then(function () {
    return cleanOperlog() // 调用清空API
  }).then(() => {
    getList() // 重新查询列表
    proxy.$modal.msgSuccess("清空成功") // 显示成功提示
  }).catch(() => { }) // 取消或失败时不做处理
}

/**
 * 导出按钮操作
 * 导出符合当前查询条件的操作日志为Excel
 */
function handleExport() {
  proxy.download(
    "system/operlog/export", // 导出接口地址
    { ...queryParams.value }, // 携带当前查询参数
    `config_${new Date().getTime()}.xlsx` // 导出文件名（带时间戳防重名）
  )
}

// 组件初始化时自动查询日志列表
getList()
</script>
