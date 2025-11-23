import autoImport from 'unplugin-auto-import/vite'

import IconsResolver from 'unplugin-icons/resolver'

import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default function createAutoImport() {
  return autoImport({
    resolvers: [
      // 1. 自动导入 Element Plus API
      ElementPlusResolver({
        importStyle: 'css', // 关键：避免 es 路径错误
        exclude: new RegExp(/^(?!ElMessage|ElMessageBox).*$/),
      }),
      // 2. 自动导入图标组件
      IconsResolver({ prefix: 'Icon' }),
    ],
    imports: [
      'vue',
      'vue-router',
      {
        pinia: ['defineStore', 'storeToRefs', 'acceptHMRUpdate'], // 自动导入 Pinia API
      },
    ],
    dts: 'src/auto-imports.d.ts',
    eslintrc: {
      enabled: true,
      filepath: './.eslintrc-auto-import.json',
    },
  })
}
