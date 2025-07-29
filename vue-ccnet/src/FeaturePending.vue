<template>
    <div class="feature-pending-container">
        <img src="@/assets/point-icons/help.png" alt="功能待开放" class="feature-pending-img" />
        <div class="feature-pending-title">功能待开放</div>
        <div class="feature-pending-desc">该功能正在开发中，敬请期待！</div>
    </div>
</template>

<script setup lang="ts">
// 这里可以根据需要添加 props 或 emits
</script>

<style scoped>
.feature-pending-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 70vh;
    text-align: center;
    color: #666;
}

.feature-pending-img {
    width: 80px;
    height: 80px;
    margin-bottom: 24px;
}

.feature-pending-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 12px;
    color: #333;
}

.feature-pending-desc {
    font-size: 16px;
    color: #888;
}
</style>