/**
 * 判断是否为空
 */
export function isEmpty(value: any): boolean {
  if (value == null || value === undefined || value === '') {
    return true
  }
  if (Array.isArray(value) && value.length === 0) {
    return true
  }
  if (typeof value === 'object' && Object.keys(value).length === 0) {
    return true
  }
  return false
}

/**
 * 判断是否为外部链接
 */
export function isExternal(path: string): boolean {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 判断是否为数组
 */
export function isArray(value: any): value is any[] {
  return Array.isArray(value)
}

/**
 * 判断是否为字符串
 */
export function isString(value: any): value is string {
  return typeof value === 'string'
}

/**
 * 判断是否为数字
 */
export function isNumber(value: any): value is number {
  return typeof value === 'number' && !isNaN(value)
}

/**
 * 判断是否为对象
 */
export function isObject(value: any): value is Record<string, any> {
  return value !== null && typeof value === 'object'
}

/**
 * 判断是否为函数
 */
export function isFunction(value: any): value is Function {
  return typeof value === 'function'
}

/**
 * 判断是否为Promise
 */
export function isPromise(value: any): value is Promise<any> {
  return value instanceof Promise || (isObject(value) && isFunction(value.then) && isFunction(value.catch))
}

/**
 * 手机号验证
 */
export function isPhone(phone: string): boolean {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 邮箱验证
 */
export function isEmail(email: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

/**
 * URL验证
 */
export function isUrl(url: string): boolean {
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

/**
 * 判断是否为HTTP链接（兼容旧版）
 */
export function isHttp(url: string): boolean {
  return isExternal(url)
}

/**
 * 身份证号验证
 */
export function isIdCard(idCard: string): boolean {
  return /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCard)
}

/**
 * 深拷贝
 */
// 移除未使用的深拷贝实现

/**
 * 防抖
 */
// 移除未使用的防抖实现

/**
 * 节流
 */
// 移除未使用的节流实现
