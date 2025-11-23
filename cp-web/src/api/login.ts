/**
 * 登录相关API接口
 */

import request from '@/utils/request'
import type { ApiResponse } from '@/types/api'
import type { registerFormType } from '#/user'

/**
 * 登录请求参数
 */
export interface LoginParams {
  username: string
  password: string
  rememberMe: boolean
  code: string
  uuid: string
}

/**
 * 手机登录请求参数
 */
export interface PhoneLoginParams {
  phone: string
}


/**
 * 用户信息
 */
export interface UserInfo {
  id: string
  username: string
  nickName: string
  email?: string
  phonenumber?: string
  sex?: string
  avatar?: string
  dept?: any
  roles: string[]
  permissions: string[]
}



/**
 * 登录响应数据
 */
export interface LoginResponse {
  token: string
  expires_in: number
}

/**
 * 登录方法
 */
export function login(params: LoginParams): Promise<ApiResponse<LoginResponse>> {
  return request({
    url: '/auth/login',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data: params,
  })
}

/**
 * 注册方法
 */
export function register(data: registerFormType): Promise<ApiResponse<any>> {
  return request({
    url: '/auth/register',
    headers: {
      isToken: false,
    },
    method: 'post',
    data,
  })
}

/**
 * 刷新Token方法
 */
export function refreshToken(): Promise<ApiResponse<LoginResponse>> {
  return request({
    url: '/auth/refresh',
    method: 'post',
  })
}

/**
 * 获取用户详细信息
 */
export function getInfo(): Promise<ApiResponse<UserInfo>> {
  return request({
    url: '/system/user/getInfo',
    method: 'get',
  })
}

/**
 * 退出登录方法
 */
export function logout(): Promise<ApiResponse<any>> {
  return request({
    url: '/auth/logout',
    method: 'delete',
  })
}

/**
 * 获取验证码
 */
export function getCodeImg(): Promise<ApiResponse<any>> {
  return request({
    url: '/code',
    headers: {
      isToken: false,
    },
    method: 'get',
    timeout: 20000,
  })
}


/** * 获取手机验证码
 */
export function getPhoneCode(params: PhoneLoginParams): Promise<ApiResponse<any>> {
  return request({
    url: '/auth/sms/send',
    headers: {
      isToken: false,
    },
    method: 'post',
    timeout: 20000,
    data: params,
  })
}
