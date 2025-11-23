<!--
 * 登录页面组件
 * 基于Vue 3 + Element Plus + Composition API
 * 包含完整的表单验证、状态管理和交互逻辑
-->

<template>
  <div class="login-container">
    <!-- 背景装饰元素 -->
    <div class="floating-element" v-for="n in 3" :key="n"></div>
    
    <div class="login-wrapper">
      <!-- Logo区域 -->
      <div class="logo-section">
        <img src="/logo.svg" alt="Logo" class="logo" />
      </div>
      
      <!-- 登录表单容器 -->
      <div class="login-form-container">
        <h1 class="login-title">{{ title }}</h1>
        
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @submit.prevent="handleLogin"
        >
          <!-- 用户名输入框 -->
          <el-form-item prop="username">
            <template #label>
              <span class="form-label">用户名</span>
            </template>
            <div class="input-wrapper">
              <el-input
                v-model="loginForm.username"
                type="text"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
                @keyup.enter="handleLogin"
              />
            </div>
          </el-form-item>
          
          <!-- 密码输入框 -->
          <el-form-item prop="password">
            <template #label>
              <span class="form-label">密码</span>
            </template>
            <div class="input-wrapper">
              <el-input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                @keyup.enter="handleLogin"
              >
                <template #suffix>
                  <el-icon
                    class="password-toggle"
                    @click="togglePassword"
                    style="cursor: pointer;"
                  >
                    <View v-if="showPassword" />
                    <Hide v-else />
                  </el-icon>
                </template>
              </el-input>
            </div>
          </el-form-item>
          
          <!-- 记住我和验证码 -->
          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <el-form-item>
                <el-checkbox v-model="loginForm.remember">
                  记住我
                </el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-link type="primary" :underline="false" @click="handleForgotPassword">
                忘记密码？
              </el-link>
            </el-col>
          </el-row>
          
          <!-- 登录按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-button"
              :loading="loading"
              :disabled="!isFormValid"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <!-- 其他登录方式 -->
        <div class="alternative-login" v-if="showAlternativeLogin">
          <div class="divider">
            <span>其他登录方式</span>
          </div>
          <div class="social-login">
            <el-button
              class="social-button"
              @click="handleSocialLogin('wechat')"
            >
              <el-icon size="16"><ChatDotRound /></el-icon>
              <span>微信登录</span>
            </el-button>
            <el-button
              class="social-button"
              @click="handleSocialLogin('workwechat')"
            >
              <el-icon size="16"><Connection /></el-icon>
              <span>企业微信</span>
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 版本信息 -->
      <div class="version-info" v-if="showVersion">
        <span>版本 {{ version }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, View, Hide, ChatDotRound, Connection } from '@element-plus/icons-vue'
import { login } from '@/api/login'
import useUserStore from '@/stores/modules/user'

// Props定义
const props = defineProps({
  title: {
    type: String,
    default: '欢迎登录'
  },
  showAlternativeLogin: {
    type: Boolean,
    default: true
  },
  showVersion: {
    type: Boolean,
    default: true
  },
  version: {
    type: String,
    default: 'v1.0.0'
  }
})

// 路由相关
const router = useRouter()
const route = useRoute()

// 状态管理
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref()

// 响应式状态
const loading = ref(false)
const showPassword = ref(false)
const redirect = ref(undefined)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  remember: false,
  captcha: ''
})

// 表单验证规则
const loginRules = reactive({
  username: [
    { required: true, trigger: 'blur', message: '请输入用户名' },
    { min: 3, max: 20, trigger: 'blur', message: '用户名长度在3-20个字符之间' }
  ],
  password: [
    { required: true, trigger: 'blur', message: '请输入密码' },
    { min: 6, max: 20, trigger: 'blur', message: '密码长度在6-20个字符之间' }
  ]
})

// 计算属性
const isFormValid = computed(() => {
  return loginForm.username && loginForm.password && 
         loginForm.username.length >= 3 && loginForm.password.length >= 6
})

// 方法定义

/**
 * 切换密码显示/隐藏
 */
const togglePassword = () => {
  showPassword.value = !showPassword.value
}

/**
 * 处理登录
 */
const handleLogin = async () => {
  // 表单验证
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
  } catch (error) {
    console.error('表单验证失败:', error)
    return
  }
  
  // 开始登录
  loading.value = true
  
  try {
    // 构建登录参数
    const loginData = {
      username: loginForm.username.trim(),
      password: loginForm.password,
      remember: loginForm.remember
    }
    
    // 调用登录API
    const response = await login(loginData)
    
    if (response.code === 200) {
      // 登录成功
      ElMessage.success('登录成功！')
      
      // 保存用户信息
      await userStore.login(response.data)
      
      // 跳转到首页或原始页面
      const path = redirect.value || '/'
      await router.push(path)
      
    } else {
      // 登录失败
      ElMessage.error(response.msg || '登录失败')
    }
    
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error('登录失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

/**
 * 处理忘记密码
 */
const handleForgotPassword = () => {
  ElMessage.info('忘记密码功能开发中...')
  // 可以跳转到忘记密码页面
  // router.push('/forgot-password')
}

/**
 * 处理社交登录
 */
const handleSocialLogin = (type) => {
  console.log('社交登录类型:', type)
  ElMessage.info(`${type === 'wechat' ? '微信' : '企业微信'}登录功能开发中...`)
}

/**
 * 获取验证码
 */
const getCaptcha = async () => {
  // 验证码逻辑
  console.log('获取验证码')
}

/**
 * 初始化
 */
const init = () => {
  // 获取重定向地址
  redirect.value = route.query.redirect || '/'
  
  // 从本地存储恢复记住的用户名
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    loginForm.remember = true
  }
}

// 生命周期
onMounted(() => {
  init()
})
</script>

<style scoped lang="scss">
/* 样式变量 */
$primary-color: #1890ff;
$primary-hover: #40a9ff;
$primary-active: #096dd9;
$error-color: #f5222d;
$success-color: #52c41a;
$text-primary: #262626;
$text-secondary: #595959;
$text-tertiary: #8c8c8c;
$border-color: #d9d9d9;
$bg-color: #f5f5f5;
$white: #ffffff;

/* 主容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100%" height="100%" fill="url(%23grain)"/></svg>');
    opacity: 0.3;
    z-index: 1;
  }
}

/* 背景装饰元素 */
.floating-element {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
  z-index: 2;

  &:nth-child(1) {
    width: 80px;
    height: 80px;
    top: 20%;
    left: 10%;
    animation-delay: 0s;
  }

  &:nth-child(2) {
    width: 120px;
    height: 120px;
    top: 60%;
    right: 15%;
    animation-delay: 2s;
  }

  &:nth-child(3) {
    width: 60px;
    height: 60px;
    bottom: 20%;
    left: 20%;
    animation-delay: 4s;
  }
}

@keyframes float {
  0%, 100% { 
    transform: translateY(0px) rotate(0deg); 
  }
  50% { 
    transform: translateY(-20px) rotate(180deg); 
  }
}

/* 登录包装器 */
.login-wrapper {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 400px;
  padding: 24px;
  animation: fadeInUp 0.8s ease-out;
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
  margin-bottom: 32px;
}

.logo {
  height: 40px;
  width: auto;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.05);
  }
}

/* 登录表单容器 */
.login-form-container {
  background: rgba($white, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  border: 1px solid rgba($white, 0.2);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  }
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: $text-primary;
  text-align: center;
  margin-bottom: 24px;
  background: linear-gradient(135deg, $primary-color, $primary-active);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 表单样式重置 */
:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  padding: 0 0 8px 0;
  font-size: 14px;
  font-weight: 500;
  color: $text-primary;
}

:deep(.el-input__wrapper) {
  height: 48px;
  padding: 0 16px;
  border: 2px solid $border-color;
  border-radius: 8px;
  transition: all 0.3s ease;
  box-shadow: none;

  &:hover {
    border-color: $primary-hover;
  }

  &.is-focus {
    border-color: $primary-color;
    box-shadow: 0 0 0 3px rgba($primary-color, 0.1);
  }
}

:deep(.el-input__inner) {
  font-size: 16px;
  color: $text-primary;
  
  &::placeholder {
    color: $text-tertiary;
  }
}

:deep(.el-input__prefix) {
  color: $text-tertiary;
  margin-right: 8px;
}

:deep(.el-input__suffix) {
  color: $text-tertiary;
  cursor: pointer;
  transition: color 0.2s ease;

  &:hover {
    color: $text-secondary;
  }
}

/* 表单行 */
.form-row {
  margin-bottom: 20px;
}

.text-right {
  text-align: right;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, $primary-color 0%, $primary-active 100%);

  &:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba($primary-color, 0.3);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }

  &:disabled {
    background: $bg-color;
    color: $text-tertiary;
    cursor: not-allowed;
  }
}

/* 其他登录方式 */
.alternative-login {
  margin-top: 32px;
  text-align: center;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 24px;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background: $border-color;
  }

  span {
    position: relative;
    background: $white;
    padding: 0 16px;
    color: $text-tertiary;
    font-size: 12px;
  }
}

.social-login {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.social-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: $white;
  border: 1px solid $border-color;
  border-radius: 6px;
  color: $text-secondary;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: $primary-color;
    color: $primary-color;
  }
}

/* 版本信息 */
.version-info {
  text-align: center;
  margin-top: 16px;
  
  span {
    font-size: 12px;
    color: rgba($white, 0.8);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    max-width: 320px;
    padding: 16px;
  }
  
  .login-form-container {
    padding: 24px;
  }
  
  .login-title {
    font-size: 20px;
  }
  
  :deep(.el-input__wrapper) {
    height: 44px;
  }
  
  .login-button {
    height: 44px;
    font-size: 14px;
  }
  
  .social-login {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .login-wrapper {
    max-width: 100%;
    padding: 12px;
  }
  
  .login-form-container {
    padding: 20px;
  }
  
  .logo {
    height: 32px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .login-form-container {
    background: rgba(#1f1f1f, 0.95);
    border: 1px solid rgba(#fff, 0.1);
  }
  
  :deep(.el-input__wrapper) {
    background: #141414;
    border-color: #434343;
    
    &.is-focus {
      border-color: $primary-color;
    }
  }
  
  :deep(.el-input__inner) {
    color: #fff;
    
    &::placeholder {
      color: #8c8c8c;
    }
  }
}
</style>