import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

import IconsResolver from 'unplugin-icons/resolver'

export default function createComponent() {
  return Components({
    resolvers: [
      ElementPlusResolver({ importStyle: 'css' }),
      IconsResolver({ enabledCollections: ['ep'] }),
    ],
    dts: 'src/components.d.ts',
    dirs: ['src/components', 'src/views'],
    exclude: [/[\\/]node_modules[\\/]/, /[\\/]\.git[\\/]/],
  })
}