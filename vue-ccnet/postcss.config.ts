import type { Config } from 'postcss-px-to-viewport'

const config: Config = {
    plugins: {
        'postcss-px-to-viewport': {
            unitToConvert: 'px',         // 要转的单位
            viewportWidth: 375,          // 设计稿宽度
            unitPrecision: 6,            // 保留小数位
            propList: ['*'],             // 转换的属性 ['*'] 表示全部
            viewportUnit: 'vw',          // 转换成的单位
            fontViewportUnit: 'vw',      // 字体用的单位
            selectorBlackList: ['.ignore', '.van-'], // 忽略的选择器（例如不转换UI库）
            minPixelValue: 1,            // 小于这个值不转换
            mediaQuery: false,           // 是否转换媒体查询中的 px
        }
    }
}

export default config 