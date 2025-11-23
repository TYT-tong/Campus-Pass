## 问题说明

* 运行时报错：`AppLayout.vue:21 ... '/src/assets/styles/variables.scss' does not provide an export named 'menuBackground'`。

* 原因：Vite 不支持从普通 `.scss` 文件进行“命名导出”。`variables.scss` 中的 `:export` 写法是 webpack 的用法；在 Vite 中需改为 CSS Modules（`.module.scss`），并以“默认导出对象”读取。

* 现状定位：

  * 错误导入在 `src/layout/components/Sidebar/AppLayout.vue:21`：`import { menuBackground } from '@/assets/styles/variables.scss'`。

  * 使用位置在 `src/layout/components/Sidebar/AppLayout.vue:28`：`const getMenuBackground = computed(() => menuBackground)`。

  * `src/assets/styles/variables.scss:44-59` 含 `:export`，但作为普通 scss 导入在 Vite 下无效。

## 修复方案（推荐）

1. 新增 `src/assets/styles/variables.module.scss`，以 CSS Modules 方式导出变量：

   ```scss
   @use './variables.scss' as v;
   :export {
     menuBackground: v.$base-menu-background;
     menuText: v.$base-menu-color;
     menuActiveText: v.$base-menu-color-active;
     sidebarBackground: v.$base-sidebar-background;
   }
   ```

   * 保留原 `variables.scss` 供样式 `@use`，避免影响现有样式文件。
2. 修改脚本导入与使用方式：

   * 替换 `src/layout/components/Sidebar/AppLayout.vue:21` 为：

     ```ts
     import vars from '@/assets/styles/variables.module.scss'
     ```

   * 替换 `src/layout/components/Sidebar/AppLayout.vue:28` 为：

     ```ts
     const getMenuBackground = computed(() => vars.menuBackground)
     ```

   * 如需文本颜色，同理改为 `vars.menuText`；激活色改为 `vars.menuActiveText`。
3. 类型支持（可选）：

   * 在 `src/env.d.ts`（或现有 `src/end.d.ts`）补充：

     ```ts
     declare module '*.module.scss' {
       const classes: Record<string, string>
       export default classes
     }
     ```

   * 现有的 `declare module '*.scss'` 可保留，`.module.scss` 的默认导出对象更符合 Vite 的使用方式。

## 备选方案（最小改动）

* 移除脚本对 scss 的导入，直接在脚本中用固定色值：

  * `const getMenuBackground = computed(() => '#304156')` 或从设置 store 读取。

* 代价：失去与 scss 变量的同步；后续维护不便。

## 验证步骤

* 启动开发服务后，确认不再出现命名导出错误；侧边栏背景与文字颜色能正确随 `variables.module.scss` 的值变化。

* 快速回归：检查 `AppLayout.vue` 渲染的 `el-menu` 的 `background-color` 与文本色是否与导入值一致。

## 风险与回滚

* 此改动不影响现有样式 `@use` 引用；仅新增一个模块化变量文件与替换脚本导入语法。

* 如需回滚，删除 `.module.scss` 并改回脚本中使用固定色值即可。

