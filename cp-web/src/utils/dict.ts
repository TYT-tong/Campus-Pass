// 导入字典状态管理仓库（用于缓存和获取字典数据）
import { useDictStore } from '@/stores/modules/dict'
// 导入获取字典数据的接口函数（从后端API请求字典）
import { getDicts } from '@/api/system/dict/data'
// 导入Vue的响应式相关工具：ref创建响应式对象，toRefs将对象属性转为ref
import { ref, toRefs } from 'vue'

/**
 * 获取字典数据的组合式函数
 * 支持同时获取多个字典类型，优先从本地缓存读取，缓存不存在则请求接口并缓存
 * @param args - 一个或多个字典类型字符串（如 'user_status'、'sex' 等）
 * @return 包含各字典类型对应数据的响应式对象（属性为字典类型，值为字典数组）
 */

export function useDict(...args: string[]) {
  // 创建响应式对象res，用于存储所有字典数据（键为字典类型，值为字典数组）
  const res = reactive<Record<string, any[]>>({})
  // 处理每个字典类型
  args.forEach((dictType: string) => {

    // 初始化当前字典类型的数据为空数组
    res[dictType] = []

    // 从字典仓库中获取缓存数据
    const dicts = useDictStore().getDict(dictType)

    if (dicts) {
      // 若缓存存在，直接使用缓存数据
      res[dictType] = dicts
    } else {
      // 若缓存不存在，调用接口请求数据
      getDicts(dictType).then((resp: any) => {
        // 格式化字典结构
        res[dictType] = resp.data.map((p: any) => ({
          label: p.dictLabel,
          value: p.dictValue,
          elTagType: p.listClass,
          elTagClass: p.cssClass,
        }))
        // 存入仓库缓存
        useDictStore().setDict(dictType, res[dictType])
      })
    }
  })
  // 通过函数名返回转换后的响应式对象
  return toRefs(res)
}


export function useDictMap(...args: string[]) {
  // 创建响应式对象，结构为：{ 字典类型: { value: label, ... } }
  const resMap = ref<Record<string, Record<string, string>>>({})

  args.forEach((dictType: string) => {
    // 初始化当前字典类型的映射对象（避免 undefined）
    resMap.value[dictType] = {}

    // 从缓存获取字典数据
    const cachedDicts = useDictStore().getDict(dictType)

    if (cachedDicts) {
      // 缓存存在：直接将缓存的 { value, label } 转为键值对映射
      cachedDicts.forEach((item: { value: string; label: string }) => {
        resMap.value[dictType][item.value] = item.label
      })
    } else {
      // 缓存不存在：请求接口并格式化
      getDicts(dictType).then((resp: any) => {
        // 1. 先格式化一份标准字典数据（供缓存，保持与原 useDict 兼容）
        const formattedDicts = resp.data.map((p: any) => ({
          label: p.dictLabel,
          value: p.dictValue,
        }))


        // 2. 同时生成键值对映射（供当前 useDictMap 使用）
        formattedDicts.forEach((item: { value: string; label: string }) => {
          resMap.value[dictType][item.value] = item.label
        })


        // 3. 存入缓存（后续无论是 useDict 还是 useDictMap 都能复用）
        useDictStore().setDict(dictType, formattedDicts)
      })
    }
  })



  return toRefs(resMap.value)
}
