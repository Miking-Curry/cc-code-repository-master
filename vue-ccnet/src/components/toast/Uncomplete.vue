<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps({
    message: {
        type: String,
        default: '功能未完成，敬请期待！'
    },
    showCancel: {
        type: Boolean,
        default: false
    },
    confirmText: {
        type: String,
        default: '我知道了'
    },
    onConfirm: Function,
    onCancel: Function
});

const showDialog = ref(true);

function closeDialog() {
    showDialog.value = false;
    props.onCancel && props.onCancel();
}

function confirmDialog() {
    showDialog.value = false;
    props.onConfirm && props.onConfirm();
}
</script>

<template>
    <transition name="dialog">
        <div v-if="showDialog" class="overlay" @click="closeDialog"></div>
    </transition>
    <transition name="dialog">
        <div v-if="showDialog" class="dialog">
            <div class="header">提示</div>
            <div class="middle">{{ message }}</div>
            <div class="bottom">
                <div v-if="showCancel" class="left" @click="closeDialog">取消</div>
                <div class="right" @click="confirmDialog">{{ confirmText }}</div>
            </div>
        </div>
    </transition>
</template>
