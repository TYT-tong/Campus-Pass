import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";
import type {
  ApiResponse,
  PageResponse,
  UpdatePwdData,
  UserProfile,
  ChangeStatusData,
  UserQuery,
  UserItem,
  ResetPwdData,
  AuthRoleData
} from '@/types/api'


// 查询用户列表
export function listUser(query: UserQuery): Promise<ApiResponse<PageResponse<UserItem>>> {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(userId: number): Promise<ApiResponse<UserItem>> {
  return request({
    url: '/system/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data: UserItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data: UserItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(userId: number): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 用户密码重置
export function resetUserPwd(userId: number, password: string): Promise<ApiResponse<any>> {
  const data: ResetPwdData = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(userId: number, status: string): Promise<ApiResponse<any>> {
  const data: ChangeStatusData = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile(): Promise<ApiResponse<UserProfile>> {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data: UserItem): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword: string, newPassword: string): Promise<ApiResponse<any>> {
  const data: UpdatePwdData = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data: FormData): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: data
  })
}

// 查询授权角色
export function getAuthRole(userId: number): Promise<ApiResponse<{ user: UserItem; roles: any[] }>> {
  return request({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data: AuthRoleData): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/authRole',
    method: 'put',
    params: data
  })
}

// 查询部门下拉树结构
export function deptTreeSelect(): Promise<ApiResponse<any[]>> {
  return request({
    url: '/system/user/deptTree',
    method: 'get'
  })
}
