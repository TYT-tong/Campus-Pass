import { defineConfig, globalIgnores } from 'eslint/config'
import globals from 'globals'
import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'
// 1. 引入 TS 相关依赖（需先安装）
import tseslint from 'typescript-eslint'
// 2. 引入路径别名解析依赖（需先安装）
import { resolve } from 'path'

// 辅助函数：解析项目根目录路径
const resolveRoot = (path) => resolve(__dirname, path)

export default defineConfig([
  {
    name: 'app/files-to-lint',
    // 3. 新增 .ts/.tsx 后缀，让 ESLint 处理 TS 文件
    files: ['**/*.{js,mjs,jsx,vue,ts,tsx}'],
    // 排除无需 lint 的文件
    ignores: ['**/dist/**', '**/dist-ssr/**', '**/coverage/**'],
  },

  // 4. 配置 TypeScript 解析器 + 路径别名
  {
    name: 'app/ts-config',
    files: ['**/*.{ts,tsx,vue}'], // 对 TS/Vue 文件生效
    languageOptions: {
      // 核心：指定 TS 解析器（替代默认的 JS 解析器）
      parser: tseslint.parser,
      parserOptions: {
        // 指向 tsconfig.json，让解析器复用 TS 的路径别名配置
        project: resolveRoot('tsconfig.json'),
        tsconfigRootDir: __dirname,
        // 支持最新 ES 语法
        ecmaVersion: 'latest',
        sourceType: 'module',
      },
      // 环境全局变量（保持原有配置）
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    // 5. 配置路径别名解析（让 ESLint 识别 #/ 开头的路径）
    settings: {
      'import/resolver': {
        typescript: {
          project: resolveRoot('tsconfig.json'), // 复用 TS 的 paths 配置
        },
        node: {
          extensions: ['.js', '.ts', '.tsx', '.vue', '.json'],
        },
      },
      // Vue 插件的路径解析配置（可选，适配 Vue SFC）
      'vue/resolver': {
        alias: {
          '#': resolveRoot('src'), // 映射 # 到 src 目录（与 tsconfig.json 一致）
        },
      },
    },
  },

  // 6. Vue 基础配置（Flat 格式）
  {
    name: 'app/vue-config',
    files: ['**/*.vue'],
    ...pluginVue.configs['flat/essential'],
    rules: {
      'vue/multi-word-component-names': 'off', // 关闭组件名多单词校验（保持原有配置）
      'vue/valid-template-root': 'off', // 可选：关闭模板根节点校验（适配特殊场景）
    },
  },

  // 7. TS 推荐规则（启用后会提供 TS 专属语法校验）
  ...tseslint.configs.recommended,

  // 原有基础配置（保持不变）
  js.configs.recommended,
  skipFormatting,
])
