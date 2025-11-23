// API 响应类型定义
export interface ApiResponse<T = any> {
  msg: string
  code: number
  data: T
}

// 用户查询参数接口
export interface UserQuery {
  pageNum?: number
  pageSize?: number
  userName?: string
  phonenumber?: string
  status?: string
  beginTime?: string
  endTime?: string
}

// 用户数据接口
export interface UserItem {
  userId: number
  deptId: number
  userName: string
  nickName: string
  userType: string
  email: string
  phonenumber: string
  sex: string
  avatar: string
  password: string
  status: string
  delFlag: string
  loginIp: string
  loginDate: string
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
  remark: string
  dept: {
    deptId: number
    parentId: number
    deptName: string
    orderNum: number
    leader: string
    phone: string
    email: string
    status: string
    delFlag: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
  }
  roles: Array<{
    roleId: number
    roleName: string
    roleKey: string
    roleSort: number
    dataScope: string
    menuCheckStrictly: boolean
    deptCheckStrictly: boolean
    status: string
    delFlag: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
    remark: string
  }>
  roleIds: number[]
  postIds: number[]
}

// 用户个人信息接口
export interface UserProfile {
  user: UserItem
  roleGroup: string
  postGroup: string
}

// 密码重置接口
export interface ResetPwdData {
  userId: number
  password: string
}

// 状态修改接口
export interface ChangeStatusData {
  userId: number
  status: string
}

// 密码修改接口
export interface UpdatePwdData {
  oldPassword: string
  newPassword: string
}

// 角色授权接口
export interface AuthRoleData {
  userId: number
  roleIds: number[]
}
// 用户信息类型
export interface UserInfo {
  id: number | string
  username: string
  nickname?: string
  avatar?: string
  userId?: string
  userName?: string
  nickName?: string
}

// 登录响应类型
export interface LoginResponse {
  access_token?: string
  expires_in?: number
}

// 用户详细信息类型
export interface UserDetail {
  user: {
    userId: string
    userName: string
    nickName: string
    avatar?: string
  }
  roles: string[]
  permissions: string[]
  isDefaultModifyPwd?: boolean
  isPasswordExpired?: boolean
}

// 菜单类型
export interface MenuItem {
  id: number
  name: string
  path: string
  component?: string
  redirect?: string
  meta?: {
    title: string
    icon?: string
    noCache?: boolean
    link?: string
  }
  children?: MenuItem[]
}

// 系统配置类型
export interface SystemConfig {
  configId: number
  configName: string
  configKey: string
  configValue: string
  configType: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 字典类型
export interface DictType {
  dictId: number
  dictName: string
  dictType: string
  status: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 字典数据类型
export interface DictData {
  dictCode: number
  dictSort: number
  dictLabel: string
  dictValue: string
  dictType: string
  cssClass?: string
  listClass?: string
  isDefault: string
  status: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 缓存监控类型
export interface CacheInfo {
  info: {
    dbSize: number
    commandStats: Array<{
      name: string
      value: number
    }>
  }
}

// 操作日志类型
export interface OperLog {
  operId: number
  title: string
  businessType: string
  method: string
  requestMethod: string
  operatorType: string
  operName: string
  deptName?: string
  operUrl: string
  operIp: string
  operLocation?: string
  operParam: string
  jsonResult: string
  status: string
  errorMsg?: string
  operTime: string
  costTime: number
}

// 登录日志类型
export interface LoginLog {
  infoId: number
  userName: string
  ipaddr: string
  loginLocation?: string
  browser: string
  os: string
  status: string
  msg: string
  loginTime: string
}

// 部门类型
export interface Dept {
  deptId: number
  parentId: number
  ancestors: string
  deptName: string
  orderNum: number
  leader?: string
  phone?: string
  email?: string
  status: string
  delFlag: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  children?: Dept[]
}

// 岗位类型
export interface Post {
  postId: number
  postCode: string
  postName: string
  postSort: number
  status: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 角色类型
export interface Role {
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  dataScope: string
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
  status: string
  delFlag: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  flag?: boolean
}

// 用户类型
export interface User {
  userId: number
  deptId?: number
  userName: string
  nickName: string
  userType?: string
  email?: string
  phonenumber?: string
  sex?: string
  avatar?: string
  password?: string
  status: string
  delFlag: string
  loginIp?: string
  loginDate?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  dept?: Dept
  roles?: Role[]
  postIds?: number[]
  roleIds?: number[]
  admin?: boolean
}

// 通知公告类型
export interface Notice {
  noticeId: number
  noticeTitle: string
  noticeType: string
  noticeContent?: string
  status: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 分页响应类型
export interface PageResponse<T = any> {
  total: number
  rows: T[]
}

// 服务器监控信息类型
export interface ServerInfo {
  cpu: CPUInfo
  mem: MemoryInfo
  sys: SystemInfo
  jvm: JVMInfo
  disk: DiskInfo[]
}

// CPU信息类型
export interface CPUInfo {
  cpuNum: number
  total: number
  sys: number
  used: number
  wait: number
  free: number
}

// 内存信息类型
export interface MemoryInfo {
  total: number
  used: number
  free: number
  usage: number
}

// 系统信息类型
export interface SystemInfo {
  computerName: string
  computerIp: string
  userDir: string
  osName: string
  osArch: string
}

// JVM信息类型
export interface JVMInfo {
  name: string
  version: string
  startTime: string
  runTime: string
  home: string
  total: number
  max: number
  free: number
  used: number
  usage: number
  jdkVersion: string
}

// 磁盘信息类型
export interface DiskInfo {
  dirName: string
  sysTypeName: string
  typeName: string
  total: string
  free: string
  used: string
  usage: number
}
