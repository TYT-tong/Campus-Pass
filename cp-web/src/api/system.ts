import request from '@/utils/request'
import type { ApiResponse, PageResponse, UserInfo, DictType, DictData, SystemConfig, OperLog } from '@/types/api'

/**
 * 获取用户列表
 */
export function getUserList(params: any): Promise<ApiResponse<PageResponse<UserInfo>>> {
  return request({
    url: '/system/user/list',
    method: 'get',
    params,
  })
}

/**
 * 获取用户详情
 */
export function getUserDetail(userId: number): Promise<ApiResponse<UserInfo>> {
  return request({
    url: `/system/user/${userId}`,
    method: 'get',
  })
}

/**
 * 新增用户
 */
export function addUser(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user',
    method: 'post',
    data,
  })
}

/**
 * 修改用户
 */
export function updateUser(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user',
    method: 'put',
    data,
  })
}

/**
 * 删除用户
 */
export function deleteUser(userIds: number[]): Promise<ApiResponse<any>> {
  return request({
    url: `/system/user/${userIds.join(',')}`,
    method: 'delete',
  })
}

/**
 * 重置密码
 */
export function resetUserPwd(userId: number, password: string): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: { userId, password },
  })
}

/**
 * 修改用户状态
 */
export function changeUserStatus(userId: number, status: string): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: { userId, status },
  })
}

/**
 * 获取角色列表
 */
export function getRoleList(params: any): Promise<ApiResponse<PageResponse<any>>> {
  return request({
    url: '/system/role/list',
    method: 'get',
    params,
  })
}





/**
 * 获取字典类型列表
 */
export function getDictTypeList(params: any): Promise<ApiResponse<PageResponse<DictType>>> {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params,
  })
}

/**
 * 获取字典数据列表
 */
export function getDictDataList(params: any): Promise<ApiResponse<PageResponse<DictData>>> {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params,
  })
}

/**
 * 根据字典类型获取字典数据
 */
export function getDicts(dictType: string): Promise<ApiResponse<DictData[]>> {
  return request({
    url: `/system/dict/data/type/${dictType}`,
    method: 'get',
  })
}

/**
 * 获取系统配置列表
 */
export function getConfigList(params: any): Promise<ApiResponse<PageResponse<SystemConfig>>> {
  return request({
    url: '/system/config/list',
    method: 'get',
    params,
  })
}

/**
 * 获取操作日志列表
 */
export function getOperLogList(params: any): Promise<ApiResponse<PageResponse<OperLog>>> {
  return request({
    url: '/monitor/operlog/list',
    method: 'get',
    params,
  })
}

/**
 * 删除操作日志
 */
export function deleteOperLog(operIds: number[]): Promise<ApiResponse<any>> {
  return request({
    url: `/monitor/operlog/${operIds.join(',')}`,
    method: 'delete',
  })
}

/**
 * 清空操作日志
 */
export function cleanOperLog(): Promise<ApiResponse<any>> {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete',
  })
}
