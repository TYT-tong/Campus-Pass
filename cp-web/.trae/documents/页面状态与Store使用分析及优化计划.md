## 总体结论
- 已有全局 Store（user、permission、settings、dict）覆盖应用级状态：鉴权、动态路由、主题设置、字典缓存，均应保留并继续使用。
- 大多数业务页面（用户、角色、菜单、操作日志、家长、学生）列表与筛选属于页面局部状态，不需要新增全局 Store。
- 存在“跨页面可复用的持久化偏好”场景（表格列显隐、分页尺寸等），当前通过不同页面各自直用 `localStorage`，建议抽象为统一 Store 管理，提升一致性与维护性。

## 逐页建议
- 登录/个人信息（views/Login.vue、Profile）
  - 使用 `useUserStore`（src/stores/user.ts）管理 Token、用户信息与提示；无需新增 Store。
- 权限与路由（layout、菜单）
  - 使用 `usePermissionStore`（src/stores/permission.ts）生成路由与加载组件；无需新增 Store。
- 系统用户（src/views/system/user/index.vue）
  - 列显隐（`columns`）与筛选表单属于页面状态；可选将列配置持久化并统一管理。
- 操作日志、配置、字典管理（src/views/system/**）
  - 列表状态与筛选为局部状态；无需新增 Store。
- 人员模块（家长/学生）（src/views/personnel/**/index.vue）
  - 当前用 `localStorage` 分别持久化列显隐（parent-columns、student-columns），建议统一 Store 管理，减少重复代码与键名分裂。

## 建议新增的 Store 模块
- TablePrefsStore（表格偏好持久化）
  - 职责：按路由或页面标识持久化列显隐、分页尺寸、是否显示搜索等表格偏好；封装读写命名空间与版本升级策略。
  - 使用方式：页面通过 `useTablePrefs('personnel-parent')` 获取/保存 `columnsVisible` 与 `pageSize` 等；替换各页面的 `localStorage.getItem/setItem` 逻辑。
- FiltersStore（可选，搜索条件记忆）
  - 职责：按路由缓存最近一次搜索条件，返回页面时自动恢复（可设置过期时间）。
  - 使用方式：页面在 `onMounted` 读取，在 `watch` 中写入；不影响后端查询参数结构。

## 实施步骤
1. 新增 `src/stores/tablePrefs.ts`：提供 `getPrefs(pageKey)`、`setPrefs(pageKey, partial)`，内部持久化到 `localStorage`，统一命名空间（如 `layout-setting` 同类）
2. 改造 `personnel/parent` 与 `personnel/student` 页面：用 `useTablePrefs('personnel-parent')` / `useTablePrefs('personnel-student')` 管理列显隐与分页尺寸；移除手写 `localStorage` 读写
3. 视情况改造 `system/user` 的列显隐为可持久化（与右侧工具栏联动）
4. 文档补充到《工具类使用指南.md》：新增“状态与持久化”章节，说明 TablePrefs 的使用与最佳实践

## 验证
- 变更后切换路由、刷新页面，列显隐与分页尺寸能正确恢复；无重复键名与残留引用
- 搜索与分页交互正常；开发服务无警告与动态导入错误

## 说明
- 未涉及的页面（如弹窗、表单）继续使用组件本地状态即可；只有当状态需要跨页面共享或持久化时才引入 Store。
- 若你希望我直接实现 TablePrefsStore 并迁移家长/学生页面，我可以在你确认后立即落地。