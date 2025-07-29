<template>
    <div class="gauge-container">
        <svg viewBox="0 0 84 44" fill="none">
            <!-- 背景圆弧 -->
            <path d="M2 42 A40 40 0 0 1 82 42" :stroke="backgroundColor" stroke-width="4" stroke-linecap="round" fill="none" />

            <!-- 动态进度圆弧 -->
            <path :d="progressPath" :stroke="progressColor" stroke-width="4" stroke-linecap="round" fill="none" />

            <!-- 匹配度文字 -->
            <text x="42" y="22" text-anchor="middle" fill="#888" font-size="10" font-weight="400">
                匹配度
            </text>
            <!-- 百分比文字 -->
            <text x="42" y="42" text-anchor="middle" fill="#000" font-size="20" font-weight="500">
                {{ percentage }}%
            </text>
        </svg>
    </div>
</template>

<script setup>
import {  computed, ref, onMounted } from 'vue'

const greenBackground =ref('#d3f8c8')
const greenProgress = ref('#39bf1d')

const blueBackground = ref('#dae7ff')
const blueProgress = ref('#4177fc')

const yellowBackground = ref('#fceac8')
const yellowProgress = ref('#f2c24b')

const props = defineProps({
    percentage: {
        type: Number,
        default: 90,
        validator: (val) => val >= 0 && val <= 100,
    },
})

const radius = 40
const centerX = 42
const centerY = 42

const progressPath = computed(() => {
    const angle = (props.percentage / 100) * Math.PI
    const x = centerX + radius * Math.cos(Math.PI - angle)
    const y = centerY - radius * Math.sin(Math.PI - angle)
    return `M ${centerX - radius},${centerY} A ${radius},${radius} 0 0,1 ${x},${y}`
})

const backgroundColor = computed(() => {
    if (props.percentage < 80) return yellowBackground.value
    else if (props.percentage < 90) return blueBackground.value
    else return greenBackground.value
})

const progressColor = computed(() => {
    if (props.percentage < 80) return yellowProgress.value
    else if (props.percentage < 90) return blueProgress.value
    else return greenProgress.value
})
</script>

<style scoped>
.gauge-container {
    width: 84px;
    height: 44px;

    svg {
        width: 100%;
        height: 100%;
        display: block;

        path {
            transition: d 0.4s ease;
        }
    }
}
</style>