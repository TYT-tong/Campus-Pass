import { defineStore } from 'pinia'
import type { RouteLocationNormalized } from 'vue-router'

interface TagsViewState {
  visitedViews: RouteLocationNormalized[]
  cachedViews: string[]
  iframeViews: RouteLocationNormalized[]
}

export const useTagsViewStore = defineStore('tags-view', {
  state: (): TagsViewState => ({
    visitedViews: [],    // 已访问的页面标签列表（用于顶部标签栏显示）
    cachedViews: [],     // 需要缓存的页面组件名称列表（配合 keep-alive 使用）
    iframeViews: [],     // iframe 嵌入的外部链接页面列表
  }),

  actions: {
    /**
     * 添加页面到已访问和缓存列表
     * @param view - 路由信息对象
     */
    addView(view: RouteLocationNormalized) {
      this.addVisitedView(view)  // 添加到已访问标签
      this.addCachedView(view)   // 添加到缓存列表
    },

    /**
     * 添加外部链接页面到 iframe 管理列表
     * @param view - 路由信息对象（包含 meta.link 外部链接）
     */
    addIframeView(view: RouteLocationNormalized) {
      // 避免重复添加相同路径的 iframe 页面
      if (this.iframeViews.some((v) => v.path === view.path)) return
      this.iframeViews.push(
        Object.assign({}, view, {
          title: view.meta?.title || "no-name",  // 默认为 "no-name" 防止标题为空
        })
      )
    },

    /**
     * 添加页面到已访问标签列表
     * @param view - 路由信息对象
     */
    addVisitedView(view: RouteLocationNormalized) {
      // 避免重复添加相同路径的标签
      if (this.visitedViews.some((v) => v.path === view.path)) return
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta?.title || "no-name",  // 确保标题有值
        })
      )
    },

    /**
     * 添加页面到缓存列表（仅当页面配置不需要禁用缓存时）
     * @param view - 路由信息对象
     */
    addCachedView(view: RouteLocationNormalized) {
      // 若组件名已在缓存列表中，或页面配置了 noCache，则不添加
      if (!view.name || this.cachedViews.includes(view.name.toString())) return
      if (!view.meta?.noCache && view.name) {
        this.cachedViews.push(view.name.toString())
      }
    },

    /**
     * 删除指定页面（同时从已访问标签和缓存中删除）
     * @param view - 路由信息对象
     * @returns 包含更新后列表的Promise
     */
    delView(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.delVisitedView(view)  // 从已访问标签中删除
        this.delCachedView(view)   // 从缓存中删除
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        })
      })
    },

    /**
     * 从已访问标签列表中删除指定页面
     * @param view - 路由信息对象
     * @returns 更新后的已访问标签列表
     */
    delVisitedView(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        // 遍历找到对应路径的标签并删除
        for (const [i, v] of this.visitedViews.entries()) {
          if (v.path === view.path) {
            this.visitedViews.splice(i, 1)
            break
          }
        }
        // 同时从 iframe 列表中删除对应页面
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path !== view.path
        )
        resolve([...this.visitedViews])
      })
    },

    /**
     * 从 iframe 列表中删除指定页面
     * @param view - 路由信息对象
     * @returns 更新后的 iframe 列表
     */
    delIframeView(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path !== view.path
        )
        resolve([...this.iframeViews])
      })
    },

    /**
     * 从缓存列表中删除指定页面
     * @param view - 路由信息对象
     * @returns 更新后的缓存列表
     */
    delCachedView(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        if (!view.name) {
          resolve([...this.cachedViews])
          return
        }
        const index = this.cachedViews.indexOf(view.name.toString())
        // 找到组件名并从缓存中删除
        index > -1 && this.cachedViews.splice(index, 1)
        resolve([...this.cachedViews])
      })
    },

    /**
     * 删除除指定页面外的其他所有页面（同时操作标签和缓存）
     * @param view - 路由信息对象（保留的页面）
     * @returns 包含更新后列表的Promise
     */
    delOthersViews(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.delOthersVisitedViews(view)  // 保留指定标签，删除其他标签
        this.delOthersCachedViews(view)   // 保留指定缓存，删除其他缓存
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        })
      })
    },

    /**
     * 删除除指定页面外的其他已访问标签（保留固定标签和当前标签）
     * @param view - 路由信息对象（保留的页面）
     * @returns 更新后的已访问标签列表
     */
    delOthersVisitedViews(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.visitedViews = this.visitedViews.filter((v) => {
          // 保留固定标签（meta.affix: true）和当前标签
          return (v.meta?.affix as boolean) || v.path === view.path
        })
        // 只保留当前页面对应的 iframe
        this.iframeViews = this.iframeViews.filter(
          (item) => item.path === view.path
        )
        resolve([...this.visitedViews])
      })
    },

    /**
     * 删除除指定页面外的其他缓存
     * @param view - 路由信息对象（保留的页面）
     * @returns 更新后的缓存列表
     */
    delOthersCachedViews(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        if (!view.name) {
          this.cachedViews = []
          resolve([...this.cachedViews])
          return
        }
        const index = this.cachedViews.indexOf(view.name.toString())
        if (index > -1) {
          // 只保留当前页面的缓存
          this.cachedViews = this.cachedViews.slice(index, index + 1)
        } else {
          // 若当前页面不在缓存中，则清空缓存
          this.cachedViews = []
        }
        resolve([...this.cachedViews])
      })
    },

    /**
     * 删除所有页面（保留固定标签）
     * @param view - 路由信息对象（一般为当前页面）
     * @returns 包含更新后列表的Promise
     */
    delAllViews(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.delAllVisitedViews(view)  // 删除所有标签（保留固定标签）
        this.delAllCachedViews(view)   // 清空所有缓存
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews],
        })
      })
    },

    /**
     * 删除所有已访问标签（仅保留固定标签）
     * @param _view - 路由信息对象
     * @returns 更新后的已访问标签列表
     */
    delAllVisitedViews(_view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        // 筛选出固定标签（meta.affix: true）保留
        const affixTags = this.visitedViews.filter((tag) => tag.meta?.affix as boolean)
        this.visitedViews = affixTags
        // 清空所有 iframe 页面
        this.iframeViews = []
        resolve([...this.visitedViews])
      })
    },

    /**
     * 清空所有缓存
     * @param _view - 路由信息对象
     * @returns 空的缓存列表
     */
    delAllCachedViews(_view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        this.cachedViews = []
        resolve([...this.cachedViews])
      })
    },

    /**
     * 更新已访问标签的信息（如标题变化时）
     * @param view - 最新的路由信息对象
     */
    updateVisitedView(view: RouteLocationNormalized) {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          // 合并更新标签信息
          v = Object.assign(v, view)
          break
        }
      }
    },

    /**
     * 删除指定页面右侧的所有标签
     * @param view - 路由信息对象（基准页面）
     * @returns 更新后的已访问标签列表
     */
    delRightTags(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        const index = this.visitedViews.findIndex((v) => v.path === view.path)
        if (index === -1) {
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          // 保留左侧标签、当前标签和固定标签
          if (idx <= index || (item.meta?.affix as boolean)) {
            return true
          }
          // 从缓存中删除右侧标签对应的页面
          if (item.name) {
            const i = this.cachedViews.indexOf(item.name.toString())
            if (i > -1) {
              this.cachedViews.splice(i, 1)
            }
          }
          // 从 iframe 列表中删除右侧标签对应的外部页面
          if (item.meta?.link) {
            const fi = this.iframeViews.findIndex((v) => v.path === item.path)
            this.iframeViews.splice(fi, 1)
          }
          return false
        })
        resolve([...this.visitedViews])
      })
    },

    /**
     * 删除指定页面左侧的所有标签
     * @param view - 路由信息对象（基准页面）
     * @returns 更新后的已访问标签列表
     */
    delLeftTags(view: RouteLocationNormalized) {
      return new Promise((resolve) => {
        const index = this.visitedViews.findIndex((v) => v.path === view.path)
        if (index === -1) {
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          // 保留右侧标签、当前标签和固定标签
          if (idx >= index || (item.meta?.affix as boolean)) {
            return true
          }
          // 从缓存中删除左侧标签对应的页面
          if (item.name) {
            const i = this.cachedViews.indexOf(item.name.toString())
            if (i > -1) {
              this.cachedViews.splice(i, 1)
            }
          }
          // 从 iframe 列表中删除左侧标签对应的外部页面
          if (item.meta?.link) {
            const fi = this.iframeViews.findIndex((v) => v.path === item.path)
            this.iframeViews.splice(fi, 1)
          }
          return false
        })
        resolve([...this.visitedViews])
      })
    },
  },
})