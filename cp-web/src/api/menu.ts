import request from '@/utils/request'
import type { ApiResponse } from '@/types/api'

/**
 * 获取菜单列表
 */
export function getMenuList(): Promise<ApiResponse<any[]>> {
  return request({
    url: '/system/menu/getRouters',
    method: 'get',
  })
}

/**
 * 获取菜单树结构
 */
export function getMenuTree(): Promise<ApiResponse<any[]>> {
  return request({
    url: '/system/menu/treeselect',
    method: 'get',
  })
}

/**
 * 新增菜单
 */
export function addMenu(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/system/menu',
    method: 'post',
    data,
  })
}

/**
 * 修改菜单
 */
export function updateMenu(data: any): Promise<ApiResponse<any>> {
  return request({
    url: '/system/menu',
    method: 'put',
    data,
  })
}

/**
 * 删除菜单
 */
export function deleteMenu(menuId: number): Promise<ApiResponse<any>> {
  return request({
    url: `/system/menu/${menuId}`,
    method: 'delete',
  })
}

// 查询菜单下拉树结构
export function selectTree() {
  return request({
    url: '/system/menu/selectTree',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeSelect(roleId: any): Promise<ApiResponse<any>> {
  return request({
    url: '/system/menu/roleMenuTreeSelect/' + roleId,
    method: 'get'
  })
}

/**
 * 获取菜单详情
 */
export function getMenuDetail(menuId: number): Promise<ApiResponse<any>> {
  return request({
    url: `/system/menu/${menuId}`,
    method: 'get',
  })
}

/**
 * 获取路由列表（兼容旧版JS调用）
 */
export function getRouters(): Promise<ApiResponse<any[]>> {
  return request({
    url: '/system/menu/getRouters',
    method: 'get',
  })
}


