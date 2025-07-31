import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import copy from 'rollup-plugin-copy'
import vueDevTools from 'vite-plugin-vue-devtools'
//下面是关于vant和element-plus的按需引入
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [
        VantResolver({
          importStyle: 'css' // 添加 Vant 样式按需引入
        }),
        ElementPlusResolver()
      ],
    }),
    Components({
      // 1️.默认扫描 src/components 下的 .vue 文件
      dirs: ['src/components'],

      // 2.支持的文件扩展名（默认就包含 vue）
      extensions: ['vue'],

      // 3.生成全局组件类型声明文件（自动维护，无需手写）
      dts: 'src/components.d.ts',

      // 4.解析 vant 和 element-plus 的组件，并支持样式按需引入
      resolvers: [
        VantResolver({
          importStyle: 'css' // 添加 Vant 样式按需引入
        }),
        ElementPlusResolver({
          importStyle: 'sass' // 使用 sass 样式，也可以改为 'css'
        })
      ],
    }),
    copy({
      targets: [
        { src: './node_modules/libpag/lib/libpag.wasm', dest: process.env.NODE_ENV === 'production' ? 'dist/' : 'public/' },
        { src: './src/assets/ai-images/ai-help.pag', dest: process.env.NODE_ENV === 'production' ? 'dist/assets/ai-images/' : 'public/assets/ai-images/' },
      ],
      hook: process.env.NODE_ENV === 'production' ? 'writeBundle' : "buildStart",
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        // 开发环境下切换
        // target: 'http://192.168.2.185:8080', //小潘
        // target: 'http://192.168.2.177:8080', //小杨 
         target: 'http://localhost:8099', //本机
        //target: 'http://120.25.120.221:8080', //服务器
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '') // 去掉前缀 /api
      }
    }
  }
})
