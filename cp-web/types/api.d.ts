// API响应基础类型
export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

// 分页响应基础类型
export interface PageResponse<T = any> {
  total: number
  rows: T[]
  code?: number
  msg?: string
}

// 登录相关类型
export interface LoginParams {
  username?: string
  password?: string
  rememberMe?: boolean
  code?: string
  uuid?: string
}
//手机登录参数

// 登录相关类型
export interface PhoneLoginParams {
  phone: string
  code?: string
}

export interface LoginResponse {
  code?: number
  msg?: string
  data: LoginUserInfo
}

export interface LoginUserInfo {
  access_token: string
  token_type: string
}

export interface UserInfo {
  id: number
  username: string
  nickname: string
  avatar?: string
  email?: string
  phonenumber?: string
  sex?: string
  dept?: DeptInfo
  roles?: RoleInfo[]
  permissions?: string[]
}

export interface DeptInfo {
  deptId: number
  deptName: string
  ancestors: string
}

export interface RoleInfo {
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
}

// 验证码相关类型
export interface CaptchaParams {
  phone: string
  type: 'login' | 'register' | 'reset'
}

export interface CaptchaResponse {
  captchaOnOff: boolean
  img: string
  uuid: string
}

// 菜单相关类型
export interface MenuItem {
  menuId: number
  menuName: string
  parentId: number
  orderNum: number
  path: string
  component?: string
  query?: string
  isFrame: number
  isCache: number
  menuType: 'M' | 'C' | 'F'
  visible: '0' | '1'
  status: '0' | '1'
  perms?: string
  icon: string
  children?: MenuItem[]
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 系统配置相关类型
export interface SystemConfig {
  configId: number
  configName: string
  configKey: string
  configValue: string
  configType: 'Y' | 'N'
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 字典相关类型
export interface DictType {
  dictId: number
  dictName: string
  dictType: string
  status: '0' | '1'
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

export interface DictData {
  dictCode: number
  dictSort: number
  dictLabel: string
  dictValue: string
  dictType: string
  cssClass?: string
  listClass?: string
  isDefault: 'Y' | 'N'
  status: '0' | '1'
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 操作日志相关类型
export interface OperLog {
  operId: number
  title: string
  businessType: number
  method: string
  requestMethod: string
  operatorType: number
  operName: string
  deptName: string
  operUrl: string
  operIp: string
  operLocation: string
  operParam: string
  jsonResult: string
  status: number
  errorMsg?: string
  operTime: string
  costTime: number
}

// 监控相关类型
export interface ServerInfo {
  cpu: CPUInfo
  mem: MemoryInfo
  sys: SystemInfo
  sysFiles: DiskInfo[]
  jvm: JVMInfo
}

export interface CPUInfo {
  cpuNum: number
  total: number
  sys: number
  used: number
  wait: number
  free: number
}

export interface MemoryInfo {
  total: number
  used: number
  free: number
  usage: number
}

export interface SystemInfo {
  computerName: string
  computerIp: string
  userDir: string
  osName: string
  osArch: string
}

export interface DiskInfo {
  dirName: string
  sysTypeName: string
  typeName: string
  total: string
  free: string
  used: string
  usage: number
}

export interface JVMInfo {
  name: string
  version: string
  startTime: string
  runTime: string
  home: string
  userDir: string
  inputArgs: string[]
  memory: {
    total: number
    free: number
    max: number
    usage: number
  }
}
