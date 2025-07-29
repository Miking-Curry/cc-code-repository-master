<script setup lang="ts">
import { PAGInit } from 'libpag'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const props = defineProps<{
    /** `.pag` 文件的完整路径 */
    src: string
    /** 播放次数，0 表示无限循环 */
    repeat?: number
    //** canvas 的宽度（如：'100vw'、'300px'、300） */
    width?: string | number
    /** canvas 的高度（如：'100vh'、'300px'、300） */
    height?: string | number
}>()

const emit = defineEmits<{
    (e: 'ended'): void
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
const isVisible = ref(true)

const canvasStyle = computed(() => ({
    width: typeof props.width === 'number' ? `${props.width}px` : props.width || 'auto',
    height: typeof props.height === 'number' ? `${props.height}px` : props.height || 'auto',
}))

let PAGViewInstance: any

async function playPag() {
    const PAG = await PAGInit()

    try {
        const response = await fetch(props.src)
        const blob = await response.blob()

        // 根据 URL 获取文件名
        const fileName = props.src.split('/').pop() ?? 'animation.pag'
        const file = new window.File([blob], fileName)

        const pagFile = await PAG.PAGFile.load(file)

        const canvas = canvasRef.value
        if (!canvas) {
            console.error('Canvas element not found')
            return
        }

        // 设置画布内部尺寸（必须设置实际像素，否则播放区域为空）
        canvas.width = pagFile.width()
        canvas.height = pagFile.height()

        PAGViewInstance = await PAG.PAGView.init(pagFile, `#${canvas.id}`)
        if (!PAGViewInstance) {
            console.error('Failed to create PAGView')
            return
        }

        // 如果 props.repeat 是 null 或 undefined，就使用 0 作为默认值。
        PAGViewInstance.setRepeatCount(props.repeat ?? 0)
        // 监听播放结束事件（仅当 repeat !== 0 有意义）
        if ((props.repeat ?? 0) > 0) {
            PAGViewInstance.addListener('onAnimationEnd', () => {
                emit('ended')  // 向父组件发出播放完成事件
                isVisible.value = false // 播放完成后隐藏
            })
        }
        await PAGViewInstance.play()
    } catch (err) {
        console.error('Failed to play PAG animation:', err)
    }
}

onMounted(() => {
    playPag()
})

// PAGViewInstance 会启动渲染线程、计时器等；
// 如果不销毁，切换页面或组件时会内存泄漏；
// 多次进入页面可能导致多个实例重叠播放、卡顿或白屏。
onBeforeUnmount(() => {
    if (PAGViewInstance.isDestroyed)
        return
    PAGViewInstance.destroy()
})
</script>

<template>
    <div>
        <canvas v-if="isVisible" ref="canvasRef" id="pag-canvas" :style="canvasStyle" />
    </div>
</template>