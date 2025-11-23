import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { setupStore } from './stores'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {
  parseTime,
  resetForm,
  addDateRange,
  handleTree,
  selectDictLabel,
  selectDictLabels,
} from "@/utils/ruoyi";
import { useDict } from "@/utils/dict";
import { getConfigKey } from "@/api/system/config";



import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 注册SVG图标
import 'virtual:svg-icons-register'

// 全局样式
import '@/assets/styles/index.scss'

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 配置Element Plus
app.use(ElementPlus, {
  locale: zhCn,
})

// 全局方法挂载
app.config.globalProperties.useDict = useDict;
// app.config.globalProperties.download = download;
app.config.globalProperties.parseTime = parseTime;
app.config.globalProperties.resetForm = resetForm;
app.config.globalProperties.handleTree = handleTree;
app.config.globalProperties.addDateRange = addDateRange;
app.config.globalProperties.getConfigKey = getConfigKey;
app.config.globalProperties.selectDictLabel = selectDictLabel;
app.config.globalProperties.selectDictLabels = selectDictLabels;

// 配置路由
app.use(router)

// 配置状态管理
setupStore(app)

// 全局错误处理
app.config.errorHandler = (err, _instance, info) => {
  console.error('Global error:', err)
  console.error('Error info:', info)
}

// 挂载应用
app.mount('#app')
