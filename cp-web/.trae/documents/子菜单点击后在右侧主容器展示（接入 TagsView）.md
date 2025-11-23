## 背景与目标

* 当前 `AppMain.vue` 只在路由为外链时调用 `addIframeView`，未将普通内部路由加入 `tagsView`；但右侧主容器的 `<keep-alive>` 依赖 `tagsViewStore.cachedViews`。

* 目标：点击任意子菜单项后，右侧 `AppMain` 主容器正常渲染对应页面，并将该页面加入访问标签与缓存列表，配合 `<keep-alive>` 生效。

## 变更范围

* 编辑 `src/layout/components/AppMain.vue`（仅逻辑层面增加对内部路由的 `addView` 接入）。

* 复用现有 `src/stores/tagsView.ts` 的 `addView`、`addVisitedView`、`addCachedView` 实现，无需改动。

## 实现步骤

1. 在 `AppMain.vue` 的生命周期与路由监听中，统一处理当前路由：

   * 若 `route.meta.link` 为真，调用 `useTagsViewStore().addIframeView(route)`（现有逻辑保留）。

   * 否则调用 `tagsViewStore.addView(route)`，把内部页面加入 `visitedViews` 与 `cachedViews`。

   * 代码位置：`src/layout/components/AppMain.vue:41-60`，将 `addIframe()` 合并为 `addTagsForRoute()` 并在 `onMounted` 与 `watchEffect` 中调用。
2. 保持 `<keep-alive :include="tagsViewStore.cachedViews">` 不变，使缓存名单来自 `tagsViewStore.cachedViews`。
3. 预期行为：点击子菜单通过 `SidebarItem.vue` 的 `router-link` 导航后，`AppMain` 渲染对应组件；同时记录并缓存该页面，支持后续切换快速恢复状态。

## 代码参考（关键位置）

* `src/layout/components/AppMain.vue:13-15` 保持 `<keep-alive :include="tagsViewStore.cachedViews">`。

* `src/layout/components/AppMain.vue:41-60` 将仅处理外链的 `addIframe()` 改为统一的 `addTagsForRoute()`：内部路由→`tagsViewStore.addView(route)`，外部路由→`addIframeView(route)`。

* `src/stores/tagsView.ts:22-25` `addView(view)` 会顺序调用 `addVisitedView` 与 `addCachedView`，满足我们接入需求。

## 验证方案

* 运行开发环境后，依次点击不同层级的子菜单：

  * 观察右侧主容器是否渲染对应页面组件。

  * 打开同一路由再切换其它页面，检查返回该路由是否从缓存恢复（`keep-alive` 生效取决于路由 `name` 是否存在）。

* 若页面为外链，确认在右侧通过 iframe 展示，并出现在 `iframeViews` 管理中。

## 注意事项

* 为保证缓存命中，各路由组件需配置唯一 `name`（无 `name` 仍可正常渲染，但不会被缓存）。

* `tagsView.ts` 的 `updateVisitedView` 存在只更新局部变量的问题（不影响本次接入）。如需后续优化，可改为直接修改数组项。

