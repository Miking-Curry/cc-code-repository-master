<template>
    <div class="container">
        <svg :width="width" :height="height" :viewBox="`0 0 ${width} ${height}`">
            <!-- 背景半圆 -->
            <path :d="arcPath" fill="none" stroke="#e0eaff" :stroke-width="strokeWidth" stroke-linecap="round" />

            <!-- 动态进度条 -->
            <path :d="arcPath" fill="none" stroke="#3c80f7" :stroke-width="strokeWidth" stroke-linecap="round"
                :stroke-dasharray="arcLength" :stroke-dashoffset="dashOffset" />

            <!-- 圆点 -->
            <circle v-if="showDot" :cx="dotX" :cy="dotY" r="6" fill="#fff" stroke="#3c80f7" stroke-width="4" />

            <!-- 信用分标题 -->
            <text :x="width / 2" :y="height / 2 - 10" text-anchor="middle" font-size="10" font-weight="500"
                fill="#90BBFF">信用分</text>

            <!-- 分数文本 -->
            <text :x="width / 2" :y="height / 2 + 40" text-anchor="middle" font-size="50" font-weight="500"
                fill="#3c80f7">
                {{ score }}
            </text>
        </svg>
    </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    score: {
        type: Number,
        required: true,
        default: 0,
        validator: (val) => val >= 0 && val <= 10
    },
    width: {
        type: Number,
        default: 176
    },
    height: {
        type: Number,
        default: 88
    },
    strokeWidth: {
        type: Number,
        default: 12
    },
    showDot: {
        type: Boolean,
        default: true
    }
})

const radius = computed(() => (props.width - 2 * props.strokeWidth) / 2)
const arcLength = computed(() => Math.PI * radius.value)

const arcPath = computed(() => {
    const r = radius.value
    const cx = props.width / 2
    const cy = props.height
    const startX = cx - r
    const endX = cx + r
    return `M ${startX} ${cy} A ${r} ${r} 0 0 1 ${endX} ${cy}`
})

const dashOffset = computed(() =>
    arcLength.value * (1 - props.score / 10)
)

// 计算圆点坐标（用于显示当前进度位置）
const dotX = computed(() => {
    const angle = Math.PI * (props.score / 10)
    return props.width / 2 + radius.value * Math.cos(angle - Math.PI)
})
const dotY = computed(() => {
    const angle = Math.PI * (props.score / 10)
    return props.height + radius.value * Math.sin(angle - Math.PI)
})
</script>

<style scoped>
.container {
    svg {
        width: 100%;
        height: 100%;
        display: block;
        overflow: visible;

        path {
            transition: d 0.4s ease;
        }
    }
}
</style>
