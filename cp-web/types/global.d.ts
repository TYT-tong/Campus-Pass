/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module '*.svg' {
  const content: string
  export default content
}

declare module '*.png' {
  const content: string
  export default content
}

declare module '*.jpg' {
  const content: string
  export default content
}

declare module '*.jpeg' {
  const content: string
  export default content
}

declare module '*.gif' {
  const content: string
  export default content
}

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_BASE_API: string
  readonly VITE_APP_PORT: string
  readonly VITE_APP_ENV: string
  readonly VITE_OPEN_PROXY: string
  readonly VITE_PROXY_PREFIX: string
  readonly VITE_PROXY_TARGET: string
  readonly VITE_DROP_CONSOLE: string
  readonly VITE_DROP_DEBUGGER: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}