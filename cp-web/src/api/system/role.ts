import request from '@/utils/request'
import type {
  ApiResponse
} from '@/types/api'
// 查询用户列表
export function getRolesData(): Promise<ApiResponse<RoleItem[]>> {
  return request({
    url: '/system/role/cards',
    method: 'get'
  })
}

export function updateRole(data: { roleId: number; roleName?: string; description?: string; menuIds: number[] }): Promise<ApiResponse<any>> {
  return request({
    url: '/system/role',
    method: 'put',
    data,
  })
}

export function addRole(data: { roleName: string; description?: string; menuIds: number[] }): Promise<ApiResponse<any>> {
  return request({
    url: '/system/role',
    method: 'post',
    data,
  })
}
