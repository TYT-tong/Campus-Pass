import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const expiresInKey = 'Admin-Expires-In'

/**
 * 获取Token
 */
export function getToken(): string {
  return Cookies.get(TokenKey) || ''
}

/**
 * 设置Token
 */
export function setToken(token: string): void {
  Cookies.set(TokenKey, token)
}

/**
 * 删除Token
 */
export function removeToken(): void {
  Cookies.remove(TokenKey)
}

/**
 * 获取过期时间
 */
export function getExpiresIn(): string {
  return Cookies.get(expiresInKey) || ''
}

/**
 * 设置过期时间
 */
export function setExpiresIn(time: string): void {
  Cookies.set(expiresInKey, time)
}

/**
 * 删除过期时间
 */
export function removeExpiresIn(): void {
  Cookies.remove(expiresInKey)
}