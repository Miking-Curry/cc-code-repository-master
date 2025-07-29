<script setup>
import { ElConfigProvider } from 'element-plus';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
</script>

<template>
  <el-config-provider :locale="zhCn">
    <router-view v-slot="{ Component }">
      <keep-alive include="Ai">
        <component :is="Component" />
      </keep-alive>
    </router-view>
  </el-config-provider>
</template>

<style scoped></style>
