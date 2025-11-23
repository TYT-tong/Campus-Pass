# 登录页面UI设计规范

## 1. 设计概述

### 1.1 设计理念
- **现代化**：采用2024年最新的设计趋势，简约而不简单
- **用户友好**：减少认知负担，提供清晰的视觉引导
- **响应式**：完美适配移动端、平板和桌面端
- **品牌一致性**：与企业VI系统保持统一

### 1.2 设计目标
- 提升用户登录体验
- 降低登录错误率
- 增强品牌识别度
- 支持多种登录方式

## 2. 页面布局设计

### 2.1 整体布局结构
```
┌─────────────────────────────────────────────────────────────┐
│                        登录页面                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌───────────────────────────────────────────────────────┐   │
│  │                    Logo区域                            │   │
│  │                  [公司Logo]                          │   │
│  └───────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌───────────────────────────────────────────────────────┐   │
│  │                  登录表单区域                          │   │
│  │  ┌─────────────────────────────────────────────────┐ │   │
│  │  │ 用户名输入框                                    │ │   │
│  │  └─────────────────────────────────────────────────┘ │   │
│  │  ┌─────────────────────────────────────────────────┐ │   │
│  │  │ 密码输入框                                      │ │   │
│  │  └─────────────────────────────────────────────────┘ │   │
│  │  ┌───┐ 记住我                                    │ │   │
│  │  │ ✓ │                                           │ │   │
│  │  └───┘                                           │ │   │
│  │  ┌─────────────────────────────────────────────────┐ │   │
│  │  │              [登录按钮]                        │ │   │
│  │  └─────────────────────────────────────────────────┘ │   │
│  │  忘记密码？  ┊  其他登录方式                        │ │   │
│  └───────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 响应式断点
- **移动端**：320px - 768px
- **平板端**：768px - 1024px  
- **桌面端**：1024px - 1920px
- **大屏**：1920px以上

## 3. 视觉设计规范

### 3.1 颜色方案

#### 主色调
- **主色**：#1890ff（科技蓝）
- **主色渐变**：linear-gradient(135deg, #1890ff 0%, #096dd9 100%)
- **主色悬停**：#40a9ff
- **主色激活**：#096dd9

#### 辅助色
- **成功色**：#52c41a
- **警告色**：#faad14
- **错误色**：#f5222d
- **信息色**：#1890ff

#### 中性色
- **标题色**：#262626（深灰）
- **正文色**：#595959（中灰）
- **辅助文字**：#8c8c8c（浅灰）
- **边框色**：#d9d9d9
- **背景色**：#f5f5f5
- **白色**：#ffffff

#### 背景渐变
- **默认背景**：linear-gradient(135deg, #667eea 0%, #764ba2 100%)
- **备选背景1**：linear-gradient(135deg, #f093fb 0%, #f5576c 100%)
- **备选背景2**：linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)

### 3.2 字体规范

#### 字体族
- **中文**：-apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif
- **英文**：-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif

#### 字体大小
- **标题**：28px（桌面端）/ 24px（移动端）
- **输入框文字**：16px（桌面端）/ 14px（移动端）
- **按钮文字**：16px（桌面端）/ 14px（移动端）
- **辅助文字**：14px（桌面端）/ 12px（移动端）
- **提示文字**：12px

#### 字重
- **标题**：600（Semi-Bold）
- **正文**：400（Regular）
- **按钮**：500（Medium）

### 3.3 间距规范

#### 基础间距
- **超小间距**：4px
- **小间距**：8px
- **中等间距**：16px
- **大间距**：24px
- **超大间距**：32px

#### 组件间距
- **输入框间距**：16px
- **按钮与输入框间距**：24px
- **表单区域边距**：32px（桌面端）/ 16px（移动端）
- **Logo与表单间距**：48px（桌面端）/ 32px（移动端）

## 4. 组件设计规范

### 4.1 Logo区域

#### 尺寸规范
- **桌面端**：120px × 40px
- **移动端**：100px × 32px

#### 位置规范
- **垂直位置**：距离顶部15vh
- **水平位置**：居中显示

#### 样式要求
- 支持SVG格式，确保高清显示
- 提供深色和浅色两种版本
- 支持Retina屏幕显示

### 4.2 登录表单容器

#### 尺寸规范
- **桌面端**：400px × 自动高度
- **平板端**：360px × 自动高度
- **移动端**：90% × 自动高度（最大320px）

#### 样式规范
- **背景色**：rgba(255, 255, 255, 0.95)
- **圆角**：12px
- **阴影**：0 8px 32px rgba(0, 0, 0, 0.1)
- **边框**：1px solid rgba(255, 255, 255, 0.2)
- **内边距**：32px（桌面端）/ 24px（移动端）

#### 动画效果
- **进入动画**：fadeInUp，持续时间0.5s
- **悬停效果**：轻微上浮2px，阴影加深

### 4.3 输入框组件

#### 基础样式
```css
.input-field {
  width: 100%;
  height: 48px;
  padding: 12px 16px;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background-color: #ffffff;
  transition: all 0.3s ease;
}
```

#### 交互状态
- **默认状态**：边框色 #d9d9d9，背景色 #ffffff
- **悬停状态**：边框色 #40a9ff，背景色 #fafafa
- **聚焦状态**：边框色 #1890ff，背景色 #ffffff，添加蓝色光晕效果
- **错误状态**：边框色 #f5222d，背景色 #fff1f0
- **成功状态**：边框色 #52c41a，背景色 #f6ffed

#### 图标集成
- **左侧图标**：20px × 20px，颜色 #8c8c8c
- **右侧操作图标**：20px × 20px，颜色 #595959
- **图标与文字间距**：8px

### 4.4 按钮组件

#### 主按钮样式
```css
.primary-button {
  width: 100%;
  height: 48px;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 500;
  color: #ffffff;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}
```

#### 交互状态
- **默认状态**：渐变背景，轻微阴影
- **悬停状态**：背景变亮，上浮2px，阴影加深
- **激活状态**：背景变暗，轻微下沉
- **禁用状态**：背景色 #f5f5f5，文字色 #bfbfbf，cursor: not-allowed
- **加载状态**：显示加载动画，按钮文字隐藏

#### 加载动画
```css
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #ffffff;
  border-top: 2px solid transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
```

### 4.5 复选框组件

#### 基础样式
```css
.checkbox {
  width: 18px;
  height: 18px;
  border: 2px solid #d9d9d9;
  border-radius: 4px;
  transition: all 0.2s ease;
  cursor: pointer;
}

.checkbox:checked {
  background-color: #1890ff;
  border-color: #1890ff;
}
```

#### 自定义样式
- **选中状态**：蓝色背景，白色对勾图标
- **悬停状态**：边框颜色加深
- **禁用状态**：透明度0.5，cursor: not-allowed

### 4.6 链接组件

#### 基础样式
```css
.link {
  color: #1890ff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s ease;
}

.link:hover {
  color: #40a9ff;
  text-decoration: underline;
}
```

## 5. 交互设计规范

### 5.1 表单验证

#### 验证时机
- **实时验证**：输入时进行格式验证
- **失焦验证**：输入框失去焦点时进行完整验证
- **提交验证**：点击登录按钮时进行最终验证

#### 错误提示样式
```css
.error-message {
  color: #f5222d;
  font-size: 12px;
  margin-top: 4px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}
```

#### 成功提示样式
```css
.success-message {
  color: #52c41a;
  font-size: 12px;
  margin-top: 4px;
  animation: fadeIn 0.3s ease;
}
```

### 5.2 键盘导航

#### Tab键顺序
1. 用户名输入框
2. 密码输入框
3. 记住我复选框
4. 登录按钮
5. 忘记密码链接

#### Enter键支持
- 在任意输入框按Enter键触发登录
- 登录按钮获得焦点时按Enter键触发登录

### 5.3 触摸交互

#### 移动端优化
- 输入框点击区域：48px × 48px（符合移动端触摸标准）
- 按钮点击区域：最小48px × 48px
- 触摸反馈：提供视觉反馈（颜色变化、轻微缩放）

## 6. 响应式设计

### 6.1 移动端适配（320px - 768px）

#### 布局调整
- 表单容器宽度：90%（最大320px）
- 内边距：24px
- 字体大小：标题24px，正文14px，辅助文字12px
- 输入框高度：44px
- 按钮高度：44px

#### 交互优化
- 增大触摸目标区域
- 简化动画效果
- 优化键盘弹出时的布局

### 6.2 平板端适配（768px - 1024px）

#### 布局调整
- 表单容器宽度：360px
- 内边距：28px
- 字体大小：标题26px，正文15px，辅助文字13px
- 输入框高度：46px
- 按钮高度：46px

### 6.3 桌面端适配（1024px - 1920px）

#### 布局调整
- 表单容器宽度：400px
- 内边距：32px
- 字体大小：标题28px，正文16px，辅助文字14px
- 输入框高度：48px
- 按钮高度：48px

### 6.4 大屏适配（1920px以上）

#### 布局调整
- 表单容器宽度：420px
- Logo尺寸：140px × 46px
- 增加背景装饰元素
- 优化空白区域利用

## 7. 可访问性设计

### 7.1 色彩对比度
- 文字与背景对比度：至少4.5:1
- 按钮文字与背景对比度：至少3:1
- 链接文字与背景对比度：至少4.5:1

### 7.2 键盘可访问性
- 所有交互元素支持键盘操作
- 提供清晰的焦点指示器
- 支持屏幕阅读器

### 7.3 语义化标记
```html
<main class="login-container">
  <section class="login-form-wrapper" role="main">
    <h1 class="login-title">用户登录</h1>
    <form class="login-form" @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username" class="visually-hidden">用户名</label>
        <input
          id="username"
          v-model="loginForm.username"
          type="text"
          name="username"
          autocomplete="username"
          required
          class="form-control"
          placeholder="请输入用户名"
        >
      </div>
      <!-- 其他表单元素 -->
    </form>
  </section>
</main>
```

## 8. 性能优化

### 8.1 资源优化
- 图片压缩：Logo使用SVG格式，小于5KB
- 字体优化：使用系统字体，避免额外加载
- CSS优化：使用CSS变量，减少重复代码

### 8.2 加载性能
- 首屏加载时间：< 2秒
- 交互响应时间：< 100ms
- 动画帧率：60fps

### 8.3 缓存策略
- 静态资源缓存：1年
- API响应缓存：根据业务需求设置
- 字体缓存：1年

## 9. 安全考虑

### 9.1 输入安全
- 防止XSS攻击：对用户输入进行转义
- 防止SQL注入：后端进行参数化查询
- 输入长度限制：用户名50字符，密码128字符

### 9.2 传输安全
- 使用HTTPS协议
- 敏感信息加密传输
- 实现CSRF保护

### 9.3 密码安全
- 密码强度要求：至少8位，包含大小写字母、数字、特殊字符
- 密码掩码显示
- 防止密码暴力破解：实现登录重试限制

## 10. 实现代码示例

### 10.1 Vue组件结构
```vue
<template>
  <div class="login-container">
    <div class="login-wrapper">
      <!-- Logo区域 -->
      <div class="logo-section">
        <img src="/logo.svg" alt="Logo" class="logo">
      </div>
      
      <!-- 登录表单 -->
      <div class="login-form-container">
        <h1 class="login-title">欢迎登录</h1>
        
        <form @submit.prevent="handleLogin" class="login-form">
          <!-- 用户名输入框 -->
          <div class="form-group">
            <label class="form-label">用户名</label>
            <div class="input-wrapper">
              <input
                v-model="loginForm.username"
                type="text"
                class="form-input"
                placeholder="请输入用户名"
                :class="{ 'error': errors.username }"
                @blur="validateUsername"
              >
              <span class="input-icon">
                <svg><!-- 用户图标 --></svg>
              </span>
            </div>
            <span v-if="errors.username" class="error-message">{{ errors.username }}</span>
          </div>
          
          <!-- 密码输入框 -->
          <div class="form-group">
            <label class="form-label">密码</label>
            <div class="input-wrapper">
              <input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-input"
                placeholder="请输入密码"
                :class="{ 'error': errors.password }"
                @blur="validatePassword"
              >
              <span class="input-icon" @click="togglePassword">
                <svg><!-- 密码图标 --></svg>
              </span>
            </div>
            <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
          </div>
          
          <!-- 记住我 -->
          <div class="form-group checkbox-group">
            <label class="checkbox-wrapper">
              <input type="checkbox" v-model="loginForm.remember">
              <span class="checkbox-custom"></span>
              <span class="checkbox-label">记住我</span>
            </label>
          </div>
          
          <!-- 登录按钮 -->
          <button
            type="submit"
            class="login-button"
            :disabled="loading || !isFormValid"
          >
            <span v-if="!loading">登录</span>
            <div v-else class="loading-spinner"></div>
          </button>
        </form>
        
        <!-- 辅助链接 -->
        <div class="form-footer">
          <a href="#" class="link">忘记密码？</a>
        </div>
        
        <!-- 其他登录方式 -->
        <div class="alternative-login" v-if="showAlternativeLogin">
          <div class="divider">
            <span>其他登录方式</span>
          </div>
          <div class="social-login">
            <button class="social-button">
              <svg><!-- 微信图标 --></svg>
              <span>微信登录</span>
            </button>
            <button class="social-button">
              <svg><!-- 企业微信图标 --></svg>
              <span>企业微信登录</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const errors = reactive({
  username: '',
  password: ''
})

const loading = ref(false)
const showPassword = ref(false)
const showAlternativeLogin = ref(true)

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const validateUsername = () => {
  if (!loginForm.username) {
    errors.username = '请输入用户名'
    return false
  }
  errors.username = ''
  return true
}

const validatePassword = () => {
  if (!loginForm.password) {
    errors.password = '请输入密码'
    return false
  }
  if (loginForm.password.length < 6) {
    errors.password = '密码长度至少6位'
    return false
  }
  errors.password = ''
  return true
}

const isFormValid = computed(() => {
  return loginForm.username && loginForm.password && loginForm.password.length >= 6
})

const handleLogin = async () => {
  if (!validateUsername() || !validatePassword()) {
    return
  }
  
  loading.value = true
  try {
    // 登录逻辑
    await login(loginForm)
  } catch (error) {
    // 错误处理
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 样式代码 */
</style>
```

### 10.2 主要CSS样式
```css
/* 全局样式变量 */
:root {
  /* 颜色变量 */
  --primary-color: #1890ff;
  --primary-hover: #40a9ff;
  --primary-active: #096dd9;
  --success-color: #52c41a;
  --warning-color: #faad14;
  --error-color: #f5222d;
  --text-primary: #262626;
  --text-secondary: #595959;
  --text-tertiary: #8c8c8c;
  --border-color: #d9d9d9;
  --bg-color: #f5f5f5;
  --white: #ffffff;
  
  /* 间距变量 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  
  /* 字体变量 */
  --font-size-sm: 12px;
  --font-size-base: 14px;
  --font-size-lg: 16px;
  --font-size-xl: 18px;
  --font-size-2xl: 20px;
  --font-size-3xl: 24px;
  
  /* 圆角变量 */
  --border-radius-sm: 4px;
  --border-radius-base: 6px;
  --border-radius-lg: 8px;
  --border-radius-xl: 12px;
  
  /* 阴影变量 */
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.1);
  --shadow-base: 0 4px 16px rgba(0, 0, 0, 0.12);
  --shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.15);
}

/* 登录容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100%" height="100%" fill="url(%23grain)"/></svg>');
  opacity: 0.3;
}

.login-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: var(--spacing-lg);
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Logo区域 */
.logo-section {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.logo {
  height: 40px;
  width: auto;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

/* 登录表单容器 */
.login-form-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--border-radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-lg);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.login-form-container:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.login-title {
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--text-primary);
  text-align: center;
  margin-bottom: var(--spacing-lg);
  background: linear-gradient(135deg, var(--primary-color), var(--primary-active));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 表单组 */
.form-group {
  margin-bottom: var(--spacing-lg);
}

.form-label {
  display: block;
  font-size: var(--font-size-base);
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-sm);
}

/* 输入框包装器 */
.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.form-input {
  width: 100%;
  height: 48px;
  padding: 12px 16px;
  padding-right: 48px;
  font-size: var(--font-size-lg);
  border: 2px solid var(--border-color);
  border-radius: var(--border-radius-base);
  background-color: var(--white);
  color: var(--text-primary);
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.1);
}

.form-input.error {
  border-color: var(--error-color);
  box-shadow: 0 0 0 3px rgba(245, 34, 45, 0.1);
}

.form-input.success {
  border-color: var(--success-color);
  box-shadow: 0 0 0 3px rgba(82, 196, 26, 0.1);
}

.form-input::placeholder {
  color: var(--text-tertiary);
}

/* 输入框图标 */
.input-icon {
  position: absolute;
  right: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  color: var(--text-tertiary);
  cursor: pointer;
  transition: color 0.2s ease;
}

.input-icon:hover {
  color: var(--text-secondary);
}

/* 错误消息 */
.error-message {
  display: block;
  font-size: var(--font-size-sm);
  color: var(--error-color);
  margin-top: var(--spacing-xs);
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-4px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 复选框 */
.checkbox-group {
  margin-bottom: var(--spacing-lg);
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}

.checkbox-wrapper input[type="checkbox"] {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.checkbox-custom {
  width: 18px;
  height: 18px;
  border: 2px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  margin-right: var(--spacing-sm);
  position: relative;
  transition: all 0.2s ease;
}

.checkbox-wrapper input[type="checkbox"]:checked + .checkbox-custom {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.checkbox-wrapper input[type="checkbox"]:checked + .checkbox-custom::after {
  content: '';
  position: absolute;
  left: 5px;
  top: 2px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  font-size: var(--font-size-base);
  color: var(--text-secondary);
}

/* 登录按钮 */
.login-button {
  width: 100%;
  height: 48px;
  padding: 12px 24px;
  font-size: var(--font-size-lg);
  font-weight: 500;
  color: var(--white);
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-active) 100%);
  border: none;
  border-radius: var(--border-radius-base);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: var(--shadow-base);
}

.login-button:active:not(:disabled) {
  transform: translateY(0);
}

.login-button:disabled {
  background: var(--bg-color);
  color: var(--text-tertiary);
  cursor: not-allowed;
}

/* 加载动画 */
.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid var(--white);
  border-top: 2px solid transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 表单底部 */
.form-footer {
  text-align: center;
  margin-top: var(--spacing-lg);
}

.link {
  color: var(--primary-color);
  text-decoration: none;
  font-size: var(--font-size-base);
  transition: color 0.2s ease;
}

.link:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

/* 其他登录方式 */
.alternative-login {
  margin-top: var(--spacing-xl);
  text-align: center;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: var(--spacing-lg);
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: var(--border-color);
}

.divider span {
  position: relative;
  background: var(--white);
  padding: 0 var(--spacing-md);
  color: var(--text-tertiary);
  font-size: var(--font-size-sm);
}

.social-login {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
}

.social-button {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--white);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-base);
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all 0.2s ease;
}

.social-button:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    max-width: 320px;
    padding: var(--spacing-md);
  }
  
  .login-form-container {
    padding: var(--spacing-lg);
  }
  
  .login-title {
    font-size: var(--font-size-2xl);
  }
  
  .form-input {
    height: 44px;
    font-size: var(--font-size-base);
  }
  
  .login-button {
    height: 44px;
    font-size: var(--font-size-base);
  }
  
  .social-login {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .login-wrapper {
    max-width: 100%;
    padding: var(--spacing-sm);
  }
  
  .login-form-container {
    padding: var(--spacing-lg);
  }
  
  .logo {
    height: 32px;
  }
}
```

## 11. 设计交付物

### 11.1 设计文件
- [ ] Figma/Sketch设计源文件
- [ ] 设计规范文档
- [ ] 组件库文件
- [ ] 图标库文件

### 11.2 切图资源
- [ ] Logo（SVG格式）
- [ ] 背景图片（多种尺寸）
- [ ] 图标（SVG格式）
- [ ] 加载动画（CSS/SVG）

### 11.3 技术文档
- [ ] HTML结构文档
- [ ] CSS样式文档
- [ ] JavaScript交互文档
- [ ] 响应式适配说明

## 12. 测试用例

### 12.1 功能测试
- [ ] 用户名输入验证
- [ ] 密码输入验证
- [ ] 记住我功能
- [ ] 登录按钮状态
- [ ] 错误提示显示
- [ ] 成功登录流程

### 12.2 兼容性测试
- [ ] Chrome浏览器
- [ ] Firefox浏览器
- [ ] Safari浏览器
- [ ] Edge浏览器
- [ ] 移动端浏览器
- [ ] 微信内置浏览器

### 12.3 性能测试
- [ ] 页面加载时间
- [ ] 动画流畅度
- [ ] 内存占用
- [ ] 网络请求优化

## 13. 后续优化建议

### 13.1 功能增强
- 支持扫码登录
- 集成第三方登录（微信、QQ、微博等）
- 支持多语言切换
- 添加验证码功能
- 支持生物识别登录

### 13.2 体验优化
- 添加骨架屏
- 优化加载动画
- 增强错误处理
- 添加操作引导
- 支持深色模式

### 13.3 安全增强
- 实现双因素认证
- 添加登录异常检测
- 支持登录历史查看
- 实现单点登录（SSO）
- 添加设备管理功能

---

**设计完成时间**：2024年
**设计版本**：v1.0
**适用框架**：Vue 3 + Element Plus（可适配其他框架）
**设计工具**：Figma + VS Code

这个设计文档提供了完整的登录页面UI设计方案，包含了所有必要的设计规范、代码示例和实现细节。你可以根据具体需求进行调整和定制。