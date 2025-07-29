<template>
    <transition name="dialog">
        <!-- 遮罩层 -->
        <div v-if="showDialog" class="overlay" @click="closeDialog"></div>
    </transition>

    <transition name="dialog">
        <!-- 对话框 -->
        <div v-if="showDialog" class="dialog">
            <div class="header">
                提示
            </div>
            <div class="middle">
                功能未完成，敬请期待！
            </div>
        </div>
    </transition>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const showDialog = ref(true);

function closeDialog() {
    showDialog.value = false;
}

function openDialog() {
    showDialog.value = true;
}

// 关键：暴露 openDialog 给外部调用
defineExpose({ openDialog })
</script>

<style scoped>
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    /* 半透明黑色 */
    z-index: 1000;
}

.dialog {
    position: fixed;
    top: 45%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    border-radius: 16px;
    z-index: 1001;
    width: 240px;

    .header {
        padding-top: 26px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: 323233;
        font-weight: 600;
        line-height: 24px;
    }

    .middle {
        padding-top: 8px;
        padding: 8px 24px 26px 24px;
        color: #646566;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 14px;
    }
}

.dialog-enter-active,
.dialog-leave-active {
    transition: opacity 0.3s ease;
}

.dialog-enter-from,
.dialog-leave-to {
    opacity: 0;
}

.dialog-enter-to,
.dialog-leave-from {
    opacity: 1;
}
</style>