<template>
  <div class="app-container">

    <div class="header">
      <el-form label-width="68px" :inline="true">
        <el-form-item label="用户名称">
          <el-input placeholder="请输入用户名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input placeholder="请输入手机号码" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select placeholder="用户状态" clearable style="width: 180px">
            <el-option label="启用" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" style="width: 340px"> <!-- 缩小日期选择器宽度 -->
          <el-date-picker value-format="YYYY-MM-DD" type="daterange" range-separator="-" start-placeholder="开始日期"
            end-placeholder="结束日期" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" style="width: 70px;height: 30px;">搜索</el-button>
          <el-button icon="Refresh" style="width: 70px;height: 30px;">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="workspace">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
            v-hasPermi="['system:user:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['system:user:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" plain icon="Upload" @click="handleImport"
            v-hasPermi="['system:user:import']">导入</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="Download" @click="handleExport"
            v-hasPermi="['system:user:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
      </el-row>
    </div>

    <div class="bottom">
      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="用户编号" align="center" key="userId" prop="userId" v-if="columns.userId.visible" />
        <el-table-column label="用户名称" align="center" key="userName" prop="userName" v-if="columns.userName.visible"
          :show-overflow-tooltip="true" />
        <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" v-if="columns.nickName.visible"
          :show-overflow-tooltip="true" />
        <el-table-column label="用户类型" align="center" key="userType" :formatter="formatUserType"
          v-if="columns.userType.visible" :show-overflow-tooltip="true" />
        <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber"
          v-if="columns.phonenumber.visible" width="120" />
        <el-table-column label="状态" align="center" key="status" v-if="columns.status.visible">

          <template #default="scope">
            <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
              @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns.createTime.visible" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top" v-if="scope.row.userId !== 1">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top" v-if="scope.row.userId !== 1">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']"></el-button>
            </el-tooltip>
            <el-tooltip content="重置密码" placement="top" v-if="scope.row.userId !== 1">
              <el-button link type="primary" icon="Key" @click="handleResetPwd(scope.row)"
                v-hasPermi="['system:user:resetPwd']"></el-button>
            </el-tooltip>
            <el-tooltip content="分配角色" placement="top" v-if="scope.row.userId !== 1">
              <el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row)"
                v-hasPermi="['system:user:edit']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页器 -->
      <div class="pagination">
        <el-pagination v-model:current-page="pager.pageNum" v-model:page-size="pager.pageSize"
          :page-sizes="[10, 15, 25, 40]" :size="size" :disabled="disabled" :background="background"
          layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>


    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="formRules" ref="userRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label
                  }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleIds" multiple placeholder="请选择">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
                  :disabled="item.status == 1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :on-change="handleFileChange"
        :on-remove="handleFileRemove" :auto-upload="false" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>

</template>

<script setup lang="ts" name="User">
// 导入工具函数：获取用户令牌（用于接口身份验证）
import { getToken } from "@/utils/auth"
// 导入应用状态管理模块
// 导入用户相关API接口函数

import { useDict, useDictMap } from '@/utils/dict'
import {
  changeUserStatus,  // 修改用户状态
  listUser,         // 获取用户列表
  resetUserPwd,     // 重置用户密码
  delUser,          // 删除用户
  getUser,          // 获取用户详情
  updateUser,       // 更新用户信息
  addUser,          // 添加用户

} from "@/api/system/user"
import { useRouter } from "vue-router"
import { getCurrentInstance, reactive, toRefs, onMounted, ref } from "vue"
import { getRoleList } from '@/api/system'


const router = useRouter()
// 获取当前组件实例的代理（用于访问组件内部方法）
const proxy = getCurrentInstance()?.proxy as any;
// 获取字典数据：用户状态和用户性别
const { sys_normal_disable, sys_user_sex } = useDict("sys_normal_disable", "sys_user_sex")
const { sys_user_type } = useDictMap("sys_user_type");


// 定义格式化函数：根据 userType 值匹配字典标签
const formatUserType = (row: any) => {
  // row 是当前行数据，row.userType 是接口返回的字典值（如 "01"）
  // sys_user_type.value 是字典映射对象（如 { "01": "家长", ... }）
  return sys_user_type.value?.[row.userType] || '未知'; // 兼容未匹配的情况
};

const parseTime = proxy?.parseTime as any
// 响应式变量定义
const userList = ref<any[]>([])
const open = ref(false)              // 表单弹窗是否显示
const loading = ref(true)            // 列表加载状态
const showSearch = ref(true)         // 是否显示搜索区域
const ids = ref<any[]>([])
const single = ref(true)             // 是否单选（用于控制按钮状态）
const multiple = ref(true)           // 是否多选（用于控制按钮状态）
const total = ref(0)                 // 总记录数
const title = ref("")                // 表单弹窗标题
const dateRange = ref([])            // 日期范围选择

const initPassword = ref(undefined)
const roleOptions = ref<any[]>([])

// 分页相关变量
const size = ref<'small' | 'default' | 'large'>('default')
const disabled = ref(false)
const background = ref(true)

/*** 用户导入参数配置 ***/
const upload = reactive<{ open: boolean; title: string; isUploading: boolean; updateSupport: number; headers: any; url: string; selectedFile: any | null }>({
  open: false,
  title: "",
  isUploading: false,
  updateSupport: 0,
  headers: { Authorization: "Bearer " + getToken() },
  url: import.meta.env.VITE_APP_BASE_API + "/system/user/importData",
  selectedFile: null,
})
  // 选中文件
  ; (upload as any).selectedFile = null
// 列显隐配置（控制表格列的显示与隐藏）
const columns = ref({
  userId: { label: '用户编号', visible: true },
  userName: { label: '用户名称', visible: true },
  nickName: { label: '用户昵称', visible: true },
  userType: { label: '用户类型', visible: true },
  phonenumber: { label: '手机号码', visible: true },
  status: { label: '状态', visible: true },
  createTime: { label: '创建时间', visible: true }
})



// 数据对象（包含表单数据、查询参数和验证规则）
const data = reactive({
  form: {
    userId: undefined,
    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    email: undefined,
    sex: undefined,
    status: "0",
    remark: undefined,
    postIds: [],
    roleIds: [],
  },
  queryParams: {
    pageNum: 1,       // 页码
    pageSize: 10,     // 每页条数
    userName: undefined,  // 用户名
    phonenumber: undefined, // 手机号
    status: undefined,     // 状态
  },
  rules: {  // 表单验证规则
    userName: [
      { required: true, message: "用户名称不能为空", trigger: "blur" },
      { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur" }
    ],
    nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
    password: [
      { required: true, message: "用户密码不能为空", trigger: "blur" },
      { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
      { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
    ],
    email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
    phonenumber: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
  }
})

// 将响应式对象转为ref引用（方便在模板中使用）
const { queryParams, form, rules } = toRefs(data)
const formRules = rules as any

// 分页查询参数

const pager = queryParams.value


const getList = async () => {
  loading.value = true; // 开始加载

  try {
    // 调用接口获取数据，添加日期范围参数
    const res = await listUser(proxy.addDateRange(queryParams.value, dateRange.value));
    // 注意：如果 listUser 返回 Promise，推荐用 await 替代 .then()，更符合 async 函数的写法
    loading.value = false;
    userList.value = (res as any).rows;
    total.value = (res as any).total;
  } catch (error) {
    console.error('获取数据失败：', error);
    loading.value = false; // 错误时也需要关闭加载状态
  }
};

function handleSizeChange(val: number) {
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = val
  getList()
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  getList()
}





/** 文件选择处理 */
const handleFileChange = (file: any) => {
  upload.selectedFile = file
}


/**
 * 搜索按钮点击事件（重置页码并查询）
 */
function handleQuery(): void {
  queryParams.value.pageNum = 1  // 重置页码为1
  getList()  // 查询列表
}

/**
 * 重置查询条件
 */
function resetQuery(): void {
  dateRange.value = []  // 清空日期范围
  proxy.resetForm("queryRef")  // 重置查询表单

  proxy.$refs.deptTreeRef.setCurrentKey(null)  // 清空部门树选中状态
  handleQuery()  // 重新查询
}

/**
 * 删除用户操作
 * @param {Object} row - 行数据（包含用户ID）
 */
function handleDelete(row: any): void {
  const userIds = row.userId || ids.value  // 获取要删除的用户ID
  if (!userIds || (Array.isArray(userIds) && userIds.length === 0)) {
    proxy.$modal.msgError('请选择要删除的数据')
    return
  }
  // 显示确认弹窗
  proxy.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？')
    .then(function () {
      return delUser(userIds)  // 调用删除接口
    }).then(() => {
      getList()  // 重新加载列表
      proxy.$modal.msgSuccess("删除成功")  // 显示成功提示
    }).catch(() => {
      // 取消删除时可留空或添加提示
      proxy.$modal.msgInfo("已取消删除")
    })
}

/**
 * 导出用户数据
 */
function handleExport(): void {
  proxy.download(
    "system/user/export",  // 导出接口
    { ...queryParams.value },  // 携带查询参数
    `user_${new Date().getTime()}.xlsx`  // 文件名（带时间戳）
  )
}

/**
 * 修改用户状态（启用/停用）
 * @param {Object} row - 行数据
 */
function handleStatusChange(row: any): void {
  let text = row.status === "0" ? "启用" : "停用"  // 确定操作文本
  // 显示确认弹窗
  proxy.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗?').then(function () {
    return changeUserStatus(row.userId, row.status)  // 调用修改状态接口
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")  // 显示成功提示
  }).catch(function () {
    // 取消操作时恢复原始状态
    row.status = row.status === "0" ? "1" : "0"
  })
}


/**
 * 更多操作处理
 * @param {string} command - 操作命令
 * @param {Object} row - 行数据
 */
function handleCommand(command: string, row: any): void {
  switch (command) {
    case "handleResetPwd":
      handleResetPwd(row)  // 重置密码
      break
    case "handleAuthRole":
      handleAuthRole(row)  // 角色分配
      break
    default:
      break
  }
}

/**
 * 跳转角色分配页面
 * @param {Object} row - 行数据
 */
function handleAuthRole(row: any): void {
  const userId = row.userId
  router.push("/system/user-auth/role/" + userId)  // 跳转到角色分配路由
}

function handleResetPwd(row: any): void {
  // 显示密码输入弹窗
  proxy.$prompt(
    '请输入"' + row.userName + '"的新密码',
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      closeOnClickModal: false,
      inputPattern: /^.{5,20}$/,  // 密码长度验证
      inputErrorMessage: "用户密码长度必须介于 5 和 20 之间",
      inputValidator: (value: string) => {
        // 移除了不必要的转义符，仅保留对 \ 的转义（因为 \ 在正则中是特殊字符）
        if (/[<>"'|\\]/.test(value)) {
          return '不能包含非法字符：< > " \' | \\'
        }
      },
    }
  ).then(({ value }: { value: string }) => {
    // 调用重置密码接口
    resetUserPwd(row.userId, value).then(_response => {
      proxy.$modal.msgSuccess("修改成功，新密码是：" + value)
    })
  }).catch(() => { })  // 取消操作不做处理
}

/**
 * 表格选择项变化事件
 * @param {Array} selection - 选中的行数据
 */
function handleSelectionChange(selection: any[]) {
  ids.value = selection.map((item: any) => item.userId)
  single.value = selection.length != 1  // 控制单选按钮状态
  multiple.value = !selection.length   // 控制多选按钮状态
}

/**
 * 打开用户导入弹窗
 */
function handleImport() {
  upload.title = "用户导入"
  upload.open = true
  upload.selectedFile = null  // 清空已选文件
}
/**
 * 下载用户导入模板
 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",  // 模板下载接口
    {},  // 无参数
    `user_template_${new Date().getTime()}.xlsx`  // 模板文件名
  )
}
/**
 * 文件上传中处理
 */
const handleFileUploadProgress = (event: any, _file: any, _fileList: any[]): void => {
  void event
  upload.isUploading = true
}

/**
 * 文件删除处理
 */
const handleFileRemove = (_file: any, _fileList: any[]): void => {
  upload.selectedFile = null  // 清空选中的文件
}

/**
 * 文件上传成功处理
 */
const handleFileSuccess = (response: any, _file: any, _fileList: any[]): void => {
  upload.open = false  // 关闭导入弹窗
  upload.isUploading = false  // 重置上传状态
  proxy.$refs["uploadRef"].handleRemove(_file)  // 移除上传文件
  // 显示导入结果
  proxy.$alert(
    `<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>${response.msg}</div>`,
    "导入结果",
    { dangerouslyUseHTMLString: true }
  )
  getList()  // 重新加载列表
}

/**
 * 提交上传文件
 */
function submitFileForm(): void {
  const file = upload.selectedFile
  console.log("upload", upload);

  // 验证文件格式
  if (!file || file.length === 0 || !file.name.toLowerCase().endsWith('.xls') && !file.name.toLowerCase().endsWith('.xlsx')) {
    proxy.$modal.msgError("请选择后缀为 “xls”或“xlsx”的文件。")
    return
  }
  // 提交上传
  proxy.$refs["uploadRef"].submit()
}


/**
 * 重置表单数据
 */
function reset(): void {
  form.value = {
    userId: undefined,

    userName: undefined,
    nickName: undefined,
    password: undefined,
    phonenumber: undefined,
    email: undefined,
    sex: undefined,
    status: "0",  // 默认启用状态
    remark: undefined,
    postIds: [],
    roleIds: []
  }
  proxy.resetForm("userRef")  // 重置表单验证状态
}

/**
 * 取消操作（关闭弹窗并重置表单）
 */
function cancel(): void {
  open.value = false
  reset()
}

/**
 * 打开新增用户弹窗
 */
function handleAdd(): void {
  reset()  // 重置表单
  Promise.all([getRoleList(form.value.userId)]).then(([roleRes]) => {
    roleOptions.value = (roleRes as any).rows || (roleRes as any).data || []
    open.value = true
    title.value = "添加用户"
    form.value.password = initPassword.value
  })
}

/**
 * 打开修改用户弹窗
 * @param {Object} row - 行数据
 */
function handleUpdate(row: any): void {
  reset()  // 重置表单
  const userId = row.userId || ids.value  // 获取用户ID
  // 获取用户详情及相关数据
  getUser(userId).then(response => {
    form.value = (response as any).data || {
      userId: undefined,

      userName: undefined,
      nickName: undefined,
      password: undefined,
      phonenumber: undefined,
      email: undefined,
      sex: undefined,
      status: "0",
      remark: undefined,
      postIds: [],
      roleIds: [],
    }
    Promise.all([getRoleList({})]).then(([roleRes]) => {
      roleOptions.value = (roleRes as any).rows || (roleRes as any).data || []
    })
    if ((response as any).data) {

      form.value.roleIds = (response as any).data.roleIds || []
    }
    open.value = true  // 显示弹窗
    title.value = "修改用户"  // 设置标题
      ; (form.value as any).password = ""  // 清空密码
  })
}

/**
 * 提交表单（处理用户的新增或修改操作）
 * 该函数会先验证表单合法性，再根据是否存在userId判断是新增还是修改，最后执行对应接口并刷新列表
 */
function submitForm(): void {
  // 调用表单的validate方法进行表单验证（基于Element UI/Plus的表单验证机制）
  // proxy通常是Vue实例的代理对象（在setup语法糖中通过getCurrentInstance获取）
  // $refs["userRef"]获取表单组件的引用，validate方法接收一个回调函数，参数valid为验证结果（布尔值）
  proxy.$refs["userRef"].validate((valid: boolean) => {
    // 当表单验证通过（valid为true）时执行后续逻辑
    if (valid) {
      // 判断表单数据中是否存在userId：
      // - 若存在userId（不为undefined），说明是修改已有用户
      // - 若不存在userId，说明是新增用户
      if (form.value.userId != undefined) {
        // 调用更新用户的接口（updateUser），传入表单数据form.value
        updateUser((form.value as any)).then(_response => {
          // 接口调用成功后，通过模态框提示"修改成功"
          proxy.$modal.msgSuccess("修改成功")
          // 关闭表单弹窗（open是控制弹窗显示/隐藏的响应式变量）
          open.value = false
          // 重新加载用户列表数据，刷新页面显示
          getList()
        })
      } else {
        // 调用新增用户的接口（addUser），传入表单数据form.value
        addUser((form.value as any)).then(_response => {
          // 接口调用成功后，通过模态框提示"新增成功"
          proxy.$modal.msgSuccess("新增成功")
          // 关闭表单弹窗
          open.value = false
          // 重新加载用户列表数据，刷新页面显示
          getList()
        })
      }
    }
    // 若表单验证失败（valid为false），则不执行任何操作，表单会自动显示验证错误提示
  })
}

/**
 * 组件挂载时执行
 */
onMounted((): void => {

  getList()      // 加载用户列表
  // 获取初始密码配置
  proxy.getConfigKey("sys.user.initPassword").then((response: any) => {
    initPassword.value = (response as any).msg
  })
})
void [resetQuery, handleCommand,]
</script>


<style scoped>
.app-container {

  width: 100%;
  padding: 10px;
  margin-left: 20px;

}

/* 正确选中表单标签：使用内置类名 + 深度选择器 */
.header :deep(.el-form-item__label) {
  font-family: "Microsoft YaHei", sans-serif;
  font-size: 14px;
  /* 字号调大，方便测试效果 */
  color: #292727;
  font-weight: bold;
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
  background: #FAFAFC;
  border: 1px solid #EBEEF5;
  border-radius: 12px;
  padding: 12px 16px;
  margin: 10px 0 12px;
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
</style>
