import { defineStore } from 'pinia'

export interface DictItem {
  label: string
  value: string
  elTagType?: string
  elTagClass?: string
}

export interface DictState {
  /**
   * 字典数据存储对象
   * 结构：{ [dictType: string]: Array<{ label: string; value: string; elTagType?: string; elTagClass?: string }> }
   * 键为字典类型（如 'sex'），值为字典数组
   */
  dict: Record<string, DictItem[]> // 用对象替代数组，提升查询效率
}

/**
 * 字典状态管理仓库（Pinia）
 * 用于缓存和管理前端字典数据，提供高效的读写操作
 */
export const useDictStore = defineStore('dict', {
  state: (): DictState => ({
    dict: {},
  }),

  actions: {
    /**
     * 根据字典类型获取缓存的字典数据
     * @param key - 字典类型（非空字符串）
     * @returns 字典数组（未找到或key无效时返回null）
     */
    getDict(key: string): DictItem[] | null {
      // 严格校验key：必须是字符串且非空
      if (typeof key !== 'string' || key.trim() === '') {
        console.warn('getDict 失败：字典类型必须是非空字符串')
        return null
      }
      // 直接通过对象属性访问（O(1) 复杂度）
      return this.dict[key] || null
    },

    /**
     * 存储或更新字典数据（自动去重，覆盖旧数据）
     * @param key - 字典类型（非空字符串）
     * @param value - 字典数组（需包含label和value字段）
     * @returns 存储成功返回true，失败返回false
     */
    setDict(key: string, value: DictItem[]): boolean {
      // 校验key有效性
      if (typeof key !== 'string' || key.trim() === '') {
        console.warn('setDict 失败：字典类型必须是非空字符串')
        return false
      }
      // 校验value有效性（至少是数组，避免非数组数据污染）
      if (!Array.isArray(value)) {
        console.warn('setDict 失败：字典值必须是数组')
        return false
      }
      // 存储/覆盖数据（自动去重，相同key会被更新）
      this.dict[key] = value
      return true
    },

    /**
     * 根据字典类型删除缓存数据
     * @param key - 字典类型（非空字符串）
     * @returns 删除成功返回true（含key不存在的情况），失败返回false
     */
    removeDict(key: string): boolean {
      if (typeof key !== 'string' || key.trim() === '') {
        console.warn('removeDict 失败：字典类型必须是非空字符串')
        return false
      }
      // 直接删除对象属性（即使key不存在也不会报错）
      delete this.dict[key]
      return true
    },

    /**
     * 清空所有字典缓存
     */
    cleanDict(): void {
      this.dict = {} // 重置为空对象，比数组清空更高效
    },

    /**
     * 初始化字典（预加载常用字典）
     * @param initialDicts - 初始字典数组
     */
    initDict(initialDicts: { key: string; value: DictItem[] }[] = []): void {
      if (!Array.isArray(initialDicts)) {
        console.warn('initDict 失败：初始字典必须是数组')
        return
      }
      // 批量初始化字典（自动去重）
      initialDicts.forEach(({ key, value }) => {
        this.setDict(key, value)
      })
    },
  },
})

export default useDictStore