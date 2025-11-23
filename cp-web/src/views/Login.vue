<template>
  <div class="login-container">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <router-link to="/" class="nav-logo">
        <img class="nav-logo-icon" src="@/assets/logo/logo.png" alt="">
        <span>校园通后台管理系统</span>
      </router-link>
      <div class="nav-links">
        <a class="nav-home" @click="() => swichTab('phone')">
          登录
        </a>
        <a class="nav-home" @click="() => swichTab('register')">
          注册
        </a>
      </div>
    </nav>

    <!-- 主容器 -->
    <div class="login-main-container">
      <!-- 左侧品牌区域 -->
      <div class="brand-section">
        <div class="geometric-shapes">
          <div class="shape"></div>
          <div class="shape"></div>
          <div class="shape"></div>
          <div class="shape"></div>
        </div>


        <div class="brand-content">
          <div class="brand-logo">
            <div class="brand-icon">🚀</div>
            <span>校园通</span>
          </div>
          <h1 class="brand-title">校园通后台管理，</h1>
          <h2 class="brand-subtitle">让校园事务，尽在 "掌" 控</h2>
          <p class="brand-description">
            一站式统筹校园信息、权限分配与数据监控,<br />
            用高效工具支撑校园运转的每一个细节，让管理更精准，校园更有序。
          </p>
        </div>
      </div>

      <!-- 右侧登录区域 -->
      <div class="login-section">
        <div class="login-content">
          <div class="login-header">
            <h2 class="login-title">
              <span v-if="currentTab !== 'register'">登 录</span>
              <span v-else>注 册</span>
            </h2>

          </div>

          <!-- 登录方式切换 -->
          <el-tabs v-if="currentTab !== 'register'" v-model="currentTab" class="login-tabs"
            @tab-change="resetFormStates">
            <el-tab-pane label="手机号登录" name="phone" />
            <el-tab-pane label="账号密码登录" name="password" />

          </el-tabs>

          <!-- 手机号登录表单 -->
          <div class="login-form-content" v-show="currentTab === 'phone'">
            <el-form ref="phoneFormRef" :model="phoneForm" :rules="phoneRules" class="login-form"
              @submit.prevent="handlePhoneLogin">
              <el-form-item prop="phoneNumber">
                <el-input v-model="phoneForm.phoneNumber" type="tel" placeholder="请输入手机号" maxlength="11"
                  :prefix-icon="Phone" @input="onPhoneInput">
                  <template #prepend>+86</template>
                </el-input>
              </el-form-item>

              <el-form-item prop="code">
                <div class="code-input-group">
                  <el-input v-model="phoneForm.code" placeholder="请输入验证码" maxlength="6" :prefix-icon="Key"
                    @input="onCodeInput" />
                  <el-button type="primary" :disabled="!canSendCode || countdown > 0" :loading="sendingCode"
                    @click="getCode" class="send-code-btn">
                    <span v-if="countdown > 0">{{ countdown }}秒后重试</span>
                    <span v-else>发送验证码</span>
                  </el-button>
                </div>
              </el-form-item>

              <el-form-item>
                <el-button :loading="loading" type="primary" class="login-btn" @click.prevent="handleLogin">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 账号密码登录表单 -->
          <div class="login-form-content" v-show="currentTab === 'password'">
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form"
              @submit.prevent="handlePasswordLogin">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" placeholder="请输入账号" :prefix-icon="User" />
              </el-form-item>

              <el-form-item prop="password">
                <el-input v-model="loginForm.password" :type="passwordVisible ? 'text' : 'password'" placeholder="请输入密码"
                  :prefix-icon="Lock">
                  <template #suffix>
                    <el-button link :icon="passwordVisible ? Hide : View" @click="togglePasswordVisibility"
                      class="password-toggle" />
                  </template>
                </el-input>
              </el-form-item>



              <el-form-item prop="code" v-if="captchaEnabled">
                <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" @keyup.enter="handleLogin"
                  style="width: 63%">
                  <template #prefix>
                    <SvgIcon icon-class="validCode" class="el-input__icon input-icon" />
                  </template>
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
              </el-form-item>
              <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>


              <el-form-item style="width:100%;">



                <el-button :loading="loading" type="primary" class="login-btn" @click.prevent="handleLogin">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
                <div style="float: right;" v-if="register">
                  <router-link class="link-type" :to="'/register'">立即注册</router-link>
                </div>
              </el-form-item>
            </el-form>
          </div>


          <!-- 账号注册表单 -->
          <div class="login-form-content" v-show="currentTab === 'register'">
            <e-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="login-form">
              <el-divider content-position="center">账号信息</el-divider>
              <el-form-item prop="username">
                <el-input v-model="registerForm.username" placeholder="请输入用户名" :prefix-icon="User" class="form-input" />
              </el-form-item>
              <el-form-item prop="phoneNumber">
                <el-input v-model="registerForm.phoneNumber" type="tel" maxlength="11" placeholder="请输入手机号"
                  :prefix-icon="Phone" class="form-input" />
                <div class="inline-tips">用于找回密码与安全验证</div>
              </el-form-item>
              <el-row :gutter="12">
                <el-col :span="12">
                  <el-form-item prop="password">
                    <el-input v-model="registerForm.password" :type="passwordVisible ? 'text' : 'password'"
                      placeholder="请输入密码" :prefix-icon="Lock" class="form-input">
                      <template #suffix>

                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item prop="affirmPassword">
                    <el-input v-model="registerForm.affirmPassword" :type="passwordVisible ? 'text' : 'password'"
                      placeholder="请再次输入密码" :prefix-icon="Lock" class="form-input" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="password-strength">
                <el-button link :icon="passwordVisible ? Hide : View" class="password-toggle" />
                <span class="strength-text_show_pas" @click="togglePasswordVisibility">显示密码</span>
                <el-progress class="strength-icon" :percentage="passwordStrength" :stroke-width="6"
                  :status="passwordStrengthStatus" />
                <span class="strength-text">{{ passwordStrengthText }}</span>
              </el-row>
              <el-form-item>
                <el-checkbox v-model="agreeProtocol" class="agree-checkbox">
                  我已阅读并同意
                  <el-link class="agree-checkbox-text" type="primary" :underline="false">服务协议</el-link>
                  和
                  <el-link class="agree-checkbox-text" type="primary" :underline="false">隐私政策</el-link>
                </el-checkbox>
              </el-form-item>
              <el-divider content-position="center">完成注册</el-divider>
              <el-form-item>
                <el-button :loading="loading" type="primary" class="login-btn" @click.prevent="handleRegister">
                  <span v-if="!loading">注 册</span>
                  <span v-else>注 册 中...</span>
                </el-button>
              </el-form-item>
              <div style="text-align:center;margin-top:10px;">
                <el-link type="primary" @click="swichTab('phone')">已有账号？去登录</el-link>
              </div>
            </e-form>
          </div>



          <div class="login-footer">
            <div class="footer-logo">
              <div class="footer-icon">🪶</div>
              <span>校园通后台管理系统</span>
            </div>
            <div class="footer-links">
              <a href="#">服务协议</a>
              <span>|</span>
              <a href="#">隐私政策</a>
              <span>|</span>
              <span>京ICP备19052848-2</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Phone, Key, User, Lock, View, Hide } from '@element-plus/icons-vue'
import { registerFormType } from '#/user'
import { useUserStore } from '@/stores/user'
import Cookies from 'js-cookie'
import { getCodeImg, getPhoneCode } from '@/api/login'
import { register as registerApi } from '@/api/login'
import { encrypt, decrypt } from '@/utils/crypto'
import SvgIcon from '@/components/SvgIcon/index.vue'


const route = useRoute()
const router = useRouter()


// 登录表单数据
const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

// 手机表单数据
const phoneForm = reactive({
  phoneNumber: '18978800123',
  code: ''
})

//注册表单数据
const registerForm = reactive<registerFormType>({
  username: '',
  password: '',
  affirmPassword: '',
  phoneNumber: ''
})




// 响应式变量
// const title = ref(import.meta.env.VITE_APP_TITLE || "默认标题")

const userStore = useUserStore() // 获取用户 Store 实例
const loading = ref(false)
const captchaEnabled = ref(true) // 验证码开关
const redirect = ref('')
const register = ref(false)// 是否显示注册链接
const passwordVisible = ref(false)
const agreeProtocol = ref(false)
const codeUrl = ref('') // 验证码图片地址（关键修复：用 ref 声明）
const loginFormRef = ref<any>(null) // 表单 ref（替代 this.$refs.loginForm）





// 表单验证规则
const loginRules = {
  username: [
    { required: true, trigger: "blur", message: "请输入您的账号" }
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" }
  ],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
} as any


// 表单验证规则（可根据实际需求调整）
const registerRules = {
  // 用户名验证
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3-20 个字符之间', trigger: 'blur' },
    {
      pattern: /^[a-zA-Z0-9_]+$/,
      message: '用户名只能包含字母、数字和下划线',
      trigger: 'blur'
    }
  ],
  // 密码验证
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6-20 个字符之间', trigger: 'blur' },
    {
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/,
      message: '密码需包含大小写字母和数字',
      trigger: 'blur'
    }
  ],
  // 确认密码验证
  affirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        void rule
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  // 手机号验证
  phoneNumber: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入正确的手机号码格式',
      trigger: 'blur'
    }
  ]
};

// 组件挂载时执行（替代 created）
onMounted(() => {

  getCookie()
  // 监听重定向参数
  redirect.value = route.query.redirect as string || ''
})

const startCountdown = () => {

  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}


// 获取验证码方法
const getCode = async () => {



  if (currentTab.value === 'password') {
    try {
      const res = await getCodeImg()

      // 处理验证码开关
      captchaEnabled.value = (res as any).captchaEnabled === undefined ? true : (res as any).captchaEnabled
      if (captchaEnabled.value && (res as any).img) {
        // 拼接 base64 图片 URL
        codeUrl.value = `data:image/gif;base64,${(res as any).img}`
        loginForm.value.uuid = (res as any).uuid || ''


      } else {
        codeUrl.value = ''
        ElMessage.error('验证码加载失败')
      }
    } catch (error: any) {
      codeUrl.value = ''
      ElMessage.error(`获取验证码失败: ${error.message || '未知错误'}`)
    }
  } else if (currentTab.value === 'phone') {
    sendingCode.value = true
    if (!phoneValid.value) {
      ElMessage.error(`手机号码格式错误`)
      return
    }
    sendingCode.value = true
    //获取验证码

    await getPhoneCode({ phone: phoneForm.phoneNumber }).then((res) => {
      sendingCode.value = false
      startCountdown()
      // 模拟自动填充验证码
      setTimeout(() => {
        phoneForm.code = res.data.code
      }, 1000)
    }).catch((error: any) => {
      ElMessage.error(`获取验证码失败: ${error.message || '未知错误'}`)
    }).finally(() => {
      sendingCode.value = false
    })

  }




}

// 从 Cookie 读取记住的账号密码
const getCookie = () => {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get('rememberMe')
  // 直接修改响应式变量（无需 this）
  loginForm.value.username = username || loginForm.value.username
  loginForm.value.password = password ? decrypt(password) : loginForm.value.password
  loginForm.value.rememberMe = rememberMe ? Boolean(rememberMe) : false
}

// 登录处理
const handleLogin = async () => {

  loading.value = true
  try {
    if (currentTab.value === "password") {
      // 表单规则验证
      const validateResult = await loginFormRef.value.validate()
      // Element Plus 中，验证失败会抛错进入 catch，成功时 validateResult 为 undefined
      if (validateResult === false) return

      // 调用 Store 的 login 方法（传入表单数据）
      await userStore.login({
        username: loginForm.value.username,
        password: loginForm.value.password,
        rememberMe: loginForm.value.rememberMe,
        code: loginForm.value.code,
        uuid: loginForm.value.uuid
      });


      // 记住密码逻辑
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set('rememberMe', String(loginForm.value.rememberMe), { expires: 30 })
      } else {
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove('rememberMe')
      }
    } else if (currentTab.value === "phone") {
      console.log("手机号登录");

      if (!codeValid.value) {
        ElMessage.error(`验证码格式错误`)
        return
      }
      // 手机号登录
      await userStore.phoneLogin({
        phone: phoneForm.phoneNumber,
        code: phoneForm.code
      });

    }

    // 登录成功：跳转 redirect 地址（之前监听的重定向地址）
    await router.push(redirect.value || '/')
    ElMessage.success('登录成功')
  } catch (err: any) {
    ElMessage.error(err.message || '登录失败')
    // 登录失败刷新验证码
    if (captchaEnabled.value && currentTab.value === 'password') {
      getCode()
    }
  } finally {
    // 修复2：删除此处的 getCode()，避免重复刷新
    loading.value = false
  }
}

//注册处理
const handleRegister = async () => {
  loading.value = true
  try {
    if (!agreeProtocol.value) {
      ElMessage.error('请阅读并同意服务协议与隐私政策')
      return
    }

    const payload: registerFormType = {
      username: registerForm.username,
      password: registerForm.password,
      affirmPassword: registerForm.affirmPassword,
      phoneNumber: registerForm.phoneNumber,
    }
    const res = await registerApi(payload)
    if ((res as any).code === 200) {
      ElMessage.success('注册成功，请登录')
      return
    } else {
      ElMessage.error((res as any).msg || '注册失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '注册失败')
  } finally {
    loading.value = false
  }
}


// 表单引用
const phoneFormRef = ref<any>()
const registerFormRef = ref<any>()
const passwordFormRef = ref<any>()

// 状态管理
const currentTab = ref('phone')
const countdown = ref(0)
const sendingCode = ref(false)
const phoneLoginLoading = ref(false)
const phoneLoginSuccess = ref(false)
const passwordLoginLoading = ref(false)
const passwordLoginSuccess = ref(false)
const phoneError = ref(false)
const phoneSuccess = ref(false)
const codeError = ref(false)
const codeSuccess = ref(false)
const usernameError = ref(false)
const usernameSuccess = ref(false)
const passwordError = ref(false)
const passwordSuccess = ref(false)




// 表单验证规则
const phoneRules = {
  phoneNumber: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur'
    }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位数字', trigger: 'blur' }
  ]
} as any



// 计算属性
const canSendCode = computed(() => {
  return phoneForm.phoneNumber.length === 11 && /^1[3-9]\d{9}$/.test(phoneForm.phoneNumber)
})

const phoneValid = computed(() => {
  return phoneForm.phoneNumber.length === 11 && /^1[3-9]\d{9}$/.test(phoneForm.phoneNumber)
})

const codeValid = computed(() => {
  return phoneForm.code.length === 6
})



// 方法
const onPhoneInput = (value: any) => {
  phoneForm.phoneNumber = value.replace(/\D/g, '')
}

const onCodeInput = (value: any) => {
  phoneForm.code = value.replace(/\D/g, '')
}







const handlePhoneLogin = () => handleLogin()
const handlePasswordLogin = () => handleLogin()

const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}

const passwordStrength = computed(() => {
  const v = registerForm.password || ''
  let s = 0
  if (v.length >= 6) s += 20
  if (/[A-Z]/.test(v)) s += 25
  if (/[a-z]/.test(v)) s += 20
  if (/\d/.test(v)) s += 20
  if (/[^A-Za-z0-9]/.test(v)) s += 15
  return Math.min(100, s)
})

const passwordStrengthText = computed(() => {
  const p = passwordStrength.value
  if (p < 40) return '密码强度：弱'
  if (p < 70) return '密码强度：中'
  return '密码强度：强'
})

const passwordStrengthStatus = computed(() => {
  const p = passwordStrength.value
  if (p < 40) return 'exception'
  if (p < 70) return 'warning'
  return 'success'
})




const resetFormStates = () => {

  if (currentTab.value === "password") {
    getCode()

  }

  // 重置所有验证状态
  phoneError.value = false
  phoneSuccess.value = false
  codeError.value = false
  codeSuccess.value = false
  usernameError.value = false
  usernameSuccess.value = false
  passwordError.value = false
  passwordSuccess.value = false

  // 重置登录状态
  phoneLoginLoading.value = false
  phoneLoginSuccess.value = false
  passwordLoginLoading.value = false
  passwordLoginSuccess.value = false

  // 重置表单验证
  if (phoneFormRef.value) {
    phoneFormRef.value.clearValidate()
  }
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate()
  }
}

const swichTab = (tabName: string) => {
  currentTab.value = tabName

  resetFormStates()
}


// 鼠标跟随效果
const handleMouseMove = (e: MouseEvent) => {
  const shapes = document.querySelectorAll('.shape')
  const mouseX = e.clientX / window.innerWidth
  const mouseY = e.clientY / window.innerHeight

  shapes.forEach((shape, index) => {
    const speed = (index + 1) * 0.3
    const x = (mouseX - 0.5) * speed * 20
    const y = (mouseY - 0.5) * speed * 20
      ; (shape as HTMLElement).style.transform = `translate(${x}px, ${y}px)`
  })
}

// 输入框焦点效果
const addInputFocusEffects = () => {
  const inputs = document.querySelectorAll('.form-input')
  inputs.forEach((input) => {
    input.addEventListener('focus', function (this: HTMLElement) {
      this.parentElement!.style.transform = 'scale(1.02)'
    })

    input.addEventListener('blur', function (this: HTMLElement) {
      this.parentElement!.style.transform = 'scale(1)'
    })
  })
}

// 生命周期
onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove)
  addInputFocusEffects()
})


</script>

<style lang="scss" scoped>
@use '@/assets/styles/login.scss';

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
    width: 100%;
    height: 100%;
  }
}

.login-btn {
  border-radius: 16px;
  width: 100%;
  height: 40px;
}

.el-input {
  height: 38px;

  input {
    height: 38px;
  }
}

.login-code-img {
  height: 38px;
}

.input-icon {
  height: 39px;
  width: 14px;
  margin-left: 2px;
}

.inline-tips {
  margin-top: 6px;
  font-size: 14px;
  color: #808085;
}

.password-strength {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 6px 0 12px;
}

/* 针对进度条的图标进行样式调整 */
.strength-icon {
  margin-left: 30px;
  width: 20px;

  /* 定位到进度条内部的图标容器 */
  & ::v-deep .el-progress__icon {
    /* 修改图标大小（默认通常是 14px 左右，根据需要调整） */
    font-size: 32px;
    /* 例如设置为 20px */
  }

  /* 可选：如果需要单独调整不同状态的图标（成功/警告/错误） */
  & ::v-deep .el-progress__success-icon {
    font-size: 32px;
    /* 成功状态图标更大 */
  }

  & ::v-deep .el-progress__warning-icon {
    font-size: 32px;
    /* 警告状态图标 */
  }
}


.strength-text {
  font-size: 12px;
  color: #606266;
}

.strength-text_show_pas {
  font-size: 15px;
  color: #6086cd;
  cursor: pointer;
  user-select: none;
}

.agree-checkbox {
  margin-top: 10px;
  width: 100%;
  display: flex;
  align-items: center;
  gap: 3px;
  text-align: center;
}

.agree-checkbox-text {
  margin-bottom: 4px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 16px;
}

.login-form :deep(.el-input) {
  border-radius: 12px;
}

.password-toggle {
  color: #909399;
}
</style>
