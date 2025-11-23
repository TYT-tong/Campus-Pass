# TypeScript 迁移指南

## 🎯 项目概述

本项目已成功从 JavaScript 迁移到 TypeScript，并采用了现代化的开发架构：

- **Vue 3** + **TypeScript** + **Element Plus** + **Pinia** + **Vite**
- 完整的类型定义和接口管理
- 统一的 API 管理架构
- 现代化的状态管理
- 改进的开发体验

## 📁 项目结构

```
src/
├── api/                    # API 接口管理
│   ├── auth.ts            # 认证相关接口
│   ├── system.ts          # 系统管理接口
│   ├── monitor.ts         # 监控相关接口
│   ├── menu.ts            # 菜单管理接口
│   └── index.ts           # API 统一导出
├── stores/                # Pinia 状态管理
│   ├── user.ts            # 用户状态管理
│   ├── app.ts             # 应用状态管理
│   ├── permission.ts      # 权限状态管理
│   └── index.ts           # Store 统一导出
├── utils/                 # 工具函数
│   ├── auth.ts            # 认证工具
│   ├── validate.ts        # 验证工具
│   └── request.ts         # HTTP 请求封装
├── views/                 # 页面组件
│   ├── Login.vue          # 登录页面（TypeScript + Element Plus）
│   └── Index.vue          # 首页
├── router/                # 路由管理
│   └── index.ts           # 路由配置（TypeScript）
├── main.ts                # 应用入口（TypeScript）
└── App.vue                # 根组件

types/                     # 全局类型定义
├── api.d.ts              # API 接口类型
├── store.d.ts            # Store 状态类型
└── global.d.ts           # 全局类型声明
```

## 🚀 主要改进

### 1. 类型安全
- ✅ 完整的 TypeScript 类型定义
- ✅ API 响应类型自动推断
- ✅ 组件 Props 和 Emit 类型检查
- ✅ 状态管理类型安全

### 2. API 管理优化
- ✅ 统一的 API 接口管理
- ✅ 完整的请求/响应类型定义
- ✅ 错误处理标准化
- ✅ 请求拦截器类型化

### 3. 状态管理升级
- ✅ Pinia 替代 Vuex（更轻量、更现代）
- ✅ 完整的 Store 类型定义
- ✅ 状态持久化配置
- ✅ 更好的开发体验

### 4. UI 组件现代化
- ✅ Element Plus 组件库
- ✅ 响应式设计优化
- ✅ 更好的用户体验
- ✅ 无障碍访问支持

### 5. 开发体验提升
- ✅ 热更新支持
- ✅ 类型检查实时反馈
- ✅ 更好的错误提示
- ✅ 现代化的开发工具

## 📋 迁移清单

### 已完成的迁移

1. **配置文件**
   - [x] `tsconfig.json` - TypeScript 配置
   - [x] `vite.config.ts` - Vite 配置更新
   - [x] `package.json` - 依赖管理

2. **核心文件**
   - [x] `main.ts` - 应用入口
   - [x] `router/index.ts` - 路由配置
   - [x] `stores/index.ts` - 状态管理

3. **API 接口**
   - [x] `api/auth.ts` - 认证接口
   - [x] `api/system.ts` - 系统接口
   - [x] `api/monitor.ts` - 监控接口
   - [x] `api/menu.ts` - 菜单接口

4. **工具函数**
   - [x] `utils/auth.ts` - 认证工具
   - [x] `utils/validate.ts` - 验证工具
   - [x] `utils/request.ts` - HTTP 请求

5. **状态管理**
   - [x] `stores/user.ts` - 用户状态
   - [x] `stores/app.ts` - 应用状态
   - [x] `stores/permission.ts` - 权限状态

6. **组件页面**
   - [x] `views/Login.vue` - 登录页面
   - [x] 类型定义文件

### 待迁移的文件

需要逐步迁移其他 Vue 组件和 JavaScript 文件：

- [ ] 其他页面组件（如系统管理、用户管理等）
- [ ] 布局组件
- [ ] 通用组件
- [ ] 工具函数
- [ ] 配置文件

## 🔧 开发指南

### 运行项目

```bash
# 安装依赖
pnpm install

# 开发模式运行
pnpm dev

# 类型检查
pnpm type-check

# 构建项目
pnpm build

# 代码格式化
pnpm format
```

### 添加新功能

1. **创建 API 接口**
   ```typescript
   // src/api/newFeature.ts
   import request from '@/utils/request'
   import type { ApiResponse } from '#types/api'
   
   export function getNewFeature(): Promise<ApiResponse<any>> {
     return request({
       url: '/api/new-feature',
       method: 'get',
     })
   }
   ```

2. **创建 Store**
   ```typescript
   // src/stores/newFeature.ts
   import { defineStore } from 'pinia'
   
   export const useNewFeatureStore = defineStore('newFeature', {
     state: () => ({
       data: [],
       loading: false,
     }),
     
     actions: {
       async fetchData() {
         this.loading = true
         try {
           const response = await getNewFeature()
           this.data = response.data
         } finally {
           this.loading = false
         }
       }
     }
   })
   ```

3. **创建组件**
   ```vue
   <!-- src/views/NewFeature.vue -->
   <template>
     <div class="new-feature">
       <!-- 组件内容 -->
     </div>
   </template>
   
   <script setup lang="ts">
   import { ref } from 'vue'
   
   interface Props {
     title: string
   }
   
   const props = withDefaults(defineProps<Props>(), {
     title: '默认标题'
   })
   </script>
   ```

### 类型定义

在 `types/` 目录下添加新的类型定义：

```typescript
// types/newFeature.d.ts
export interface NewFeatureData {
  id: number
  name: string
  description?: string
  createdAt: string
}
```

## 🎨 样式指南

### CSS 预处理器

项目使用 SCSS 作为 CSS 预处理器：

```vue
<style lang="scss" scoped>
.component {
  // 使用嵌套语法
  &-header {
    color: #333;
    
    &:hover {
      color: #666;
    }
  }
}
</style>
```

### 响应式设计

使用媒体查询实现响应式设计：

```scss
@media (max-width: 768px) {
  .component {
    // 移动端样式
  }
}
```

## 🐛 常见问题

### 1. 类型检查失败

确保所有依赖项都已正确安装：
```bash
pnpm install
```

### 2. 模块导入错误

检查路径别名配置：
```typescript
// tsconfig.json
{
  "compilerOptions": {
    "paths": {
      "@/*": ["src/*"],
      "#/*": ["types/*"]
    }
  }
}
```

### 3. Element Plus 组件类型错误

确保已安装 Element Plus 类型定义：
```bash
pnpm add -D @element-plus/icons-vue
```

## 📚 相关文档

- [Vue 3 官方文档](https://vue3js.cn/)
- [TypeScript 官方文档](https://www.tslang.cn/)
- [Element Plus 官方文档](https://element-plus.org/)
- [Pinia 官方文档](https://pinia.vuejs.org/)
- [Vite 官方文档](https://vitejs.dev/)

## 🤝 贡献指南

1. 遵循 TypeScript 编码规范
2. 为新功能添加完整的类型定义
3. 编写清晰的代码注释
4. 保持代码风格一致性
5. 在提交前运行类型检查

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。