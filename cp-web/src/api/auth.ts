import request from '@/utils/request'
import type { ApiResponse, LoginParams, LoginResponse, CaptchaParams, CaptchaResponse, PhoneLoginParams } from 'types/api'

/**
 * 用户登录
 */
export function login(data: LoginParams): Promise<ApiResponse<LoginResponse>> {
  return request({
    url: '/auth/login',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data,
  })
}
/**
 * 用户手机登录
 */
export function phoneLogin(data: PhoneLoginParams): Promise<ApiResponse<LoginResponse>> {
  return request({
    url: '/auth/sms/login',
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: 'post',
    data,
  })
}
/**
 * 用户注册
 */
export function register(data: any): Promise<ApiResponse<any>> {
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
 * 刷新Token
 */
export function refreshToken(): Promise<ApiResponse<any>> {
  return request({
    url: '/auth/refresh',
    method: 'post',
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo(): Promise<ApiResponse<any>> {
  return request({
    url: '/system/user/getInfo',
    method: 'get',
  })
}

/**
 * 用户退出
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
export function getCaptcha(): Promise<ApiResponse<CaptchaResponse>> {
  return request({
    url: '/code',
    headers: {
      isToken: false,
    },
    method: 'get',
    timeout: 20000,
  })
}

/**
 * 获取短信验证码
 */
export function getCode(data: CaptchaParams): Promise<ApiResponse<any>> {
  return request({
    url: '/sms/code',
    headers: {
      isToken: false,
    },
    method: 'post',
    data,
  })
}
