/// <reference types="vite/client" />
// 支持 import.meta、import.meta.env 等 Vite 特性

// 让 TS 正确识别和推断 .vue 文件是 Vue 组件
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}

declare module '*.png' {
    const src: string;
    export default src;
} 