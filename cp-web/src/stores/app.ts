import { defineStore } from 'pinia'
import type { AppState } from 'types/store'

export const useAppStore = defineStore('app', {
  state: (): AppState => ({
    sidebar: {
      opened: true,
      withoutAnimation: false,
    },
    device: 'desktop',
    size: 'default',
  }),

  getters: {
    sidebarOpened: (state) => state.sidebar.opened,
    deviceType: (state) => state.device,
    sizeType: (state) => state.size,
  },

  actions: {
    /**
     * 切换侧边栏
     */
    toggleSidebar(withoutAnimation = false) {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = withoutAnimation
    },

    /**
     * 关闭侧边栏
     */
    closeSidebar(withoutAnimation = false) {
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
    },

    /**
     * 打开侧边栏
     */
    openSidebar(withoutAnimation = false) {
      this.sidebar.opened = true
      this.sidebar.withoutAnimation = withoutAnimation
    },

    /**
     * 切换设备类型
     */
    toggleDevice(device: 'desktop' | 'mobile') {
      this.device = device
    },

    /**
     * 设置尺寸
     */
    setSize(size: 'large' | 'default' | 'small') {
      this.size = size
      localStorage.setItem('size', size)
    },

    /**
     * 初始化尺寸
     */
    initSize() {
      const size = localStorage.getItem('size') as 'large' | 'default' | 'small'
      if (size && ['large', 'default', 'small'].includes(size)) {
        this.size = size
      }
    },
  },
})
