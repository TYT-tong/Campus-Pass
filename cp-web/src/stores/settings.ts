/**
 * 系统设置状态管理（TypeScript版本）
 */

import defaultSettings from '@/settings'
import { useDark, useToggle } from '@vueuse/core'
import { defineStore } from 'pinia'

// 定义设置状态接口
interface SettingsState {
  title: string
  theme: string
  sideTheme: string
  showSettings: boolean
  topNav: boolean
  tagsView: boolean
  tagsIcon: boolean
  fixedHeader: boolean
  sidebarLogo: boolean
  dynamicTitle: boolean
  footerVisible: boolean
  footerContent: string
  isDark: boolean
}

// 定义设置变更数据接口
interface SettingsChangeData {
  key: keyof SettingsState
  value: any
}

// 初始化暗黑模式相关状态与方法
const isDark = useDark()
const toggleDark = useToggle(isDark)

// 从默认配置中解构需要的布局参数
const {
  sideTheme,
  showSettings,
  topNav,
  tagsView,
  tagsIcon,
  fixedHeader,
  sidebarLogo,
  dynamicTitle,
  footerVisible,
  footerContent,
} = defaultSettings

// 从 localStorage 读取持久化的配置
const storageSetting = JSON.parse(localStorage.getItem('layout-setting') || '{}')

export const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => ({
    title: '',
    // 主题色：优先用本地存储的主题色，无则用默认配置的 #409EFF
    theme: storageSetting.theme || '#409EFF',
    // 侧边栏主题：优先用本地存储值，无则用默认配置
    sideTheme: storageSetting.sideTheme || sideTheme,
    // 是否显示配置面板：直接用默认配置
    showSettings: showSettings,
    // 顶部导航是否启用：本地存储有值则用存储值，无则用默认配置
    topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
    // 标签栏是否显示：同 topNav 逻辑
    tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
    // 标签栏是否显示图标：同 topNav 逻辑
    tagsIcon: storageSetting.tagsIcon === undefined ? tagsIcon : storageSetting.tagsIcon,
    // 顶部导航是否固定：同 topNav 逻辑
    fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
    // 侧边栏是否显示 Logo：同 topNav 逻辑
    sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
    // 是否启用动态标题：同 topNav 逻辑
    dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
    // 页脚是否显示：同 topNav 逻辑
    footerVisible: storageSetting.footerVisible === undefined ? footerVisible : storageSetting.footerVisible,
    // 页脚内容：直接用默认配置
    footerContent: footerContent,
    // 暗黑模式状态
    isDark: isDark.value,
  }),

  actions: {
    /**
     * 修改单个布局配置
     */
    changeSetting(data: SettingsChangeData): void {
      const { key, value } = data
      
      // 先判断 key 是否在 state 中存在
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        // 修改对应的 state 值
        ;(this as any)[key] = value

        // 同步到 localStorage
        const newStorageSetting = { ...storageSetting, [key]: value }
        localStorage.setItem('layout-setting', JSON.stringify(newStorageSetting))
      }
    },

    /**
     * 切换暗黑模式
     */
    toggleTheme(): void {
      // 反转 Store 中的 isDark 状态
      this.isDark = !this.isDark
      // 调用 @vueuse 的 toggleDark 方法
      toggleDark()
    },
  },
})

// 默认导出，保持兼容性
export default useSettingsStore