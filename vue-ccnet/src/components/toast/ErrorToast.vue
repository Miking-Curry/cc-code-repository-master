<template>
    <transition name="fade">
        <div v-if="visible" class="error-toast">
            {{ message }}
        </div>
    </transition>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const props = defineProps({
    message: String,
    // 组件完全显示的时间，不含进入或离开的时间
    duration: {
        type: Number,
        default: 3000
    }
});

const visible = ref(false);

onMounted(() => {
    visible.value = true;
    setTimeout(() => {
        visible.value = false;
    }, props.duration);
});
</script>

<style scoped>
.error-toast {
    position: fixed;
    bottom: 50%;
    left: 50%;
    transform: translateX(-50%);
    background-color: #feeaea;
    border-color: #feecec;
    border: solid 2px;
    box-shadow: 5px 10px 10px 30px #111 outset;
    color: #f57171;
    padding: 12px 20px;
    border-radius: 8px;
    font-size: 14px;
    z-index: 500;
}

/* 进入或离开时，使用 0.3 秒的透明度动画 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s;
}

/* 进入开始时（enter-from）是透明的 
离开结束时（leave-to）也变透明 */
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>