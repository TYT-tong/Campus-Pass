/**
 * 通用TypeScript工具方法封装
 * 包含日期格式化、表单处理、数据转换等常用功能
 * Copyright (c) 2019 ruoyi
 */

/**
 * 日期格式化函数
 * 将时间转换为指定格式的字符串
 * @param time - 待格式化的时间，可以是时间戳、日期字符串或Date对象
 * @param pattern - 格式化模板，默认值为"{y}-{m}-{d} {h}:{i}:{s}"
 *                             支持的占位符：y(年)、m(月)、d(日)、h(时)、i(分)、s(秒)、a(星期)
 * @returns 格式化后的日期字符串，若参数无效则返回null
 */
export function parseTime(time?: string | number | Date, pattern?: string): string | null {
  // 若未传入时间或时间为空，返回null
  if (arguments.length === 0 || !time) {
    return null
  }
  // 确定格式化模板，默认使用年月日时分秒格式
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date: Date

  // 处理不同类型的时间参数，统一转换为Date对象
  if (typeof time === 'object') {
    // 若已为Date对象，直接使用
    date = time
  } else {
    // 处理字符串类型时间
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      // 纯数字字符串转换为数字类型
      time = parseInt(time)
    } else if (typeof time === 'string') {
      // 处理ISO格式等字符串，替换分隔符便于解析
      time = time
        .replace(new RegExp(/-/gm), '/') // 将-替换为/，兼容Safari
        .replace('T', ' ') // 将T替换为空格，分离日期和时间
        .replace(new RegExp(/\.[\d]{3}/gm), '') // 移除毫秒部分
    }

    // 处理时间戳（10位时间戳转换为13位）
    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000
    }

    // 转换为Date对象
    date = new Date(time)
  }

  // 提取日期各部分信息
  const formatObj = {
    y: date.getFullYear(), // 年份
    m: date.getMonth() + 1, // 月份（月份从0开始，需+1）
    d: date.getDate(), // 日期
    h: date.getHours(), // 小时
    i: date.getMinutes(), // 分钟
    s: date.getSeconds(), // 秒
    a: date.getDay(), // 星期（0表示周日）
  }

  // 替换模板中的占位符
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key as keyof typeof formatObj]
    // 处理星期占位符，转换为中文
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value as number]
    }
    // 补零处理（如：1 -> "01"）
    if (result.length > 0 && value < 10) {
      return '0' + value
    }
    return String(value || 0)
  })
  return time_str
}

/**
 * 表单重置函数（适配Vue3 + <script setup>）
 * 调用Element Plus表单的resetFields方法重置表单
 * @param formRef - Vue3的表单引用对象（ref）
 */
export function resetForm(formRef: any): void {
  // 检查引用是否有效且包含resetFields方法
  if (formRef && formRef.value && typeof formRef.value.resetFields === 'function') {
    formRef.value.resetFields()
  }
}

/**
 * 为请求参数添加日期范围
 * 通常用于查询时的开始时间和结束时间参数处理
 * @param params - 基础请求参数对象
 * @param dateRange - 日期范围数组，格式为[开始时间, 结束时间]
 * @param propName - 时间字段名前缀，默认使用"beginTime"和"endTime"
 * @returns 处理后的参数对象
 */
export function addDateRange(params: any, dateRange: any[], propName?: string): any {


  let search = params
  // 确保params.params为对象类型
  search.params =
    typeof search.params === 'object' && search.params !== null && !Array.isArray(search.params)
      ? search.params
      : {}


  // 确保dateRange为数组
  dateRange = Array.isArray(dateRange) ? dateRange : []

  if (typeof propName === 'undefined' || propName === null || propName === '') {
    // 无字段前缀时使用默认字段名
    search.params['beginTime'] = dateRange[0]
    search.params['endTime'] = dateRange[1]
  } else {
    // 有前缀时拼接字段名（如：propName为"CreateTime"时，生成"beginCreateTime"和"endCreateTime"）
    search.params['begin' + propName] = dateRange[0]
    search.params['end' + propName] = dateRange[1]
  }
  return search
}

/**
 * 根据值回显数据字典的标签
 * 用于将字典编码转换为对应的显示文本
 * @param datas - 数据字典数组，每个元素需包含value和label属性
 * @param value - 待匹配的字典值
 * @returns 匹配到的标签文本，若未匹配到则返回原始值
 */
export function selectDictLabel(datas: Record<string, any>, value: string | number): string {
  if (value === undefined) {
    return ''
  }
  const actions: string[] = []
  // 遍历字典查找匹配的值
  Object.keys(datas).some((key) => {
    if (datas[key].value == '' + value) {
      // 转换为字符串比较，避免类型问题
      actions.push(datas[key].label)
      return true // 找到后终止循环
    }
  })
  // 若未找到匹配项，使用原始值
  if (actions.length === 0) {
    actions.push(value as string)
  }
  return actions.join('')
}

/**
 * 根据值（字符串或数组）回显数据字典的标签集合
 * 支持多值拼接，适用于多选场景
 * @param datas - 数据字典数组，每个元素需包含value和label属性
 * @param value - 待匹配的值，可为字符串（用分隔符分隔）或数组
 * @param separator - 分隔符，默认使用逗号
 * @returns 拼接后的标签文本
 */
export function selectDictLabels(datas: Record<string, any>, value: string | any[], separator?: string): string {
  if (value === undefined || value.length === 0) {
    return ''
  }
  // 统一将值转换为字符串（数组转为用分隔符连接的字符串）
  if (Array.isArray(value)) {
    value = value.join(',')
  }
  const actions: string[] = []
  const currentSeparator = undefined === separator ? ',' : separator
  const temp = (value as string).split(currentSeparator) // 分割为单个值数组

  // 遍历每个值查找匹配的标签
  temp.forEach((item) => {
    let match = false
    Object.keys(datas).some((key) => {
      if (datas[key].value == '' + item) {
        // 字符串比较
        actions.push(datas[key].label + currentSeparator)
        match = true
      }
    })
    // 未匹配到则使用原始值
    if (!match) {
      actions.push(item + currentSeparator)
    }
  })

  // 移除最后一个分隔符
  return actions.join('').substring(0, actions.join('').length - 1)
}

/**
 * 字符串格式化函数（类似C语言的sprintf）
 * 用参数替换字符串中的%s占位符
 * @param str - 包含%s占位符的字符串
 * @param args - 用于替换占位符的参数列表
 * @returns 格式化后的字符串，若参数不足则返回空字符串
 */
export function sprintf(str: string, ...args: any[]): string {
  let flag = true // 标记是否所有占位符都有对应的参数
  let i = 0 // 参数索引

  str = str.replace(/%s/g, function () {
    const arg = args[i++]
    if (typeof arg === 'undefined') {
      flag = false // 发现未匹配的占位符
      return ''
    }
    return arg
  })

  // 若存在未匹配的占位符，返回空字符串
  return flag ? str : ''
}

/**
 * 转换空字符串
 * 将undefined、null等转换为空白字符串
 * @param str - 待转换的值
 * @returns 转换后的字符串
 */
export function parseStrEmpty(str: any): string {
  if (!str || str == 'undefined' || str == 'null') {
    return ''
  }
  return str
}

/**
 * 递归合并对象
 * 将目标对象的属性合并到源对象中（深层合并）
 * @param source - 源对象（合并后的结果将存储于此）
 * @param target - 目标对象（待合并的属性来源）
 * @returns 合并后的源对象
 */
export function mergeRecursive(source: any, target: any): any {
  for (const p in target) {
    try {
      // 若目标属性为对象，则递归合并
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p])
      } else {
        // 否则直接赋值
        source[p] = target[p]
      }
    } catch {
      // 异常情况下直接赋值（如源对象属性不存在时）
      source[p] = target[p]
    }
  }
  return source
}

/**
 * 构造树形结构数据
 * 将扁平的数组数据转换为嵌套的树形结构
 * @param data - 数据源（扁平数组）
 * @param id - 节点ID字段名，默认"id"
 * @param parentId - 父节点ID字段名，默认"parentId"
 * @param children - 子节点数组的字段名，默认"children"
 * @returns 树形结构数组
 */
export function handleTree(data: any[], id?: string, parentId?: string, children?: string): any[] {
  const config = {
    id: id || 'id', // 节点ID字段
    parentId: parentId || 'parentId', // 父节点ID字段
    childrenList: children || 'children', // 子节点数组字段
  }

  const childrenListMap: Record<string, any> = {} // 用于快速查找节点的映射表（ID -> 节点）
  const tree: any[] = [] // 最终的树形结构数组

  // 初始化每个节点的子节点数组，并构建映射表
  for (const d of data) {
    const id = d[config.id]
    childrenListMap[id] = d
    if (!d[config.childrenList]) {
      d[config.childrenList] = []
    }
  }

  // 构建树形结构
  for (const d of data) {
    const parentId = d[config.parentId]
    const parentObj = childrenListMap[parentId] // 查找父节点
    if (!parentObj) {
      // 无父节点的节点作为根节点
      tree.push(d)
    } else {
      // 有父节点的节点添加到对应父节点的子节点数组中
      parentObj[config.childrenList].push(d)
    }
  }
  return tree
}

/**
 * 参数处理函数
 * 将对象参数转换为URL查询字符串格式（支持嵌套对象）
 * @param params - 待转换的参数对象
 * @returns 转换后的查询字符串（末尾带&符号）
 */
// 移除重复的参数序列化实现，统一由网络层处理

/**
 * 规范化路径字符串
 * 移除多余的斜杠并处理末尾斜杠
 * @param p - 待处理的路径
 * @returns 规范化后的路径
 */
export function getNormalPath(p: string): string {
  if (p.length === 0 || !p || p == 'undefined') {
    return p
  }
  // 替换双斜杠为单斜杠
  let res = p.replace('//', '/')
  // 移除末尾的斜杠
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1)
  }
  return res
}

/**
 * 验证数据是否为Blob格式
 * 用于判断响应数据是否为文件流（非JSON格式）
 * @param data - 待验证的数据
 * @returns 若为Blob且类型不是application/json则返回true，否则返回false
 */
export function blobValidate(data: Blob): boolean {
  return data.type !== 'application/json'
}
