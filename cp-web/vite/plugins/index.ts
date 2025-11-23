import vue from '@vitejs/plugin-vue'

import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'
import createComponent from './Components'
import VueDevTools from 'vite-plugin-vue-devtools' // 导入插件

export default function createVitePlugins(viteEnv: any, isBuild = false) {
  const vitePlugins = [vue(), VueDevTools()]
  vitePlugins.push(createAutoImport())
  vitePlugins.push(createSetupExtend())
  vitePlugins.push(createSvgIcon(isBuild))
  vitePlugins.push(createComponent())
  isBuild && vitePlugins.push(...createCompression(viteEnv))
  return vitePlugins
}