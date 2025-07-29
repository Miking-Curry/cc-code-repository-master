<script setup lang="ts">
import { useUserStore } from '@/stores/user';
import { getTalentRating } from '@/utils/api/test';
import { TalentRatingResult } from '@/utils/api/type';
import { showError } from '@/utils/toast';
import { onMounted, ref } from 'vue';

const userStore = useUserStore()
const token = ref<string>('')
token.value = userStore.token

const loading = ref<boolean>(false)
const userId = ref<number>(0)
const error = ref<string>('')

const talentRating = ref<TalentRatingResult | null>()

// 添加获取岗位列表的函数
const fetchTalentRating = async () => {
    if (!userId.value) {
        showError('请输入用户ID')
        return
    }

    try {
        loading.value = true
        error.value = ''
        talentRating.value = null

        const response = await getTalentRating(userId.value)
        if (response.code === 200 && response.data) {
            talentRating.value = response.data
        } else {
            error.value = response.message
            showError('获取人才评分失败')
        }
    } catch (err) {
        console.log(err)
        if (err && typeof err === 'object' && 'response' in err) {
            error.value = (err as any).response.data.msg;
        } else {
            error.value = '获取人才评分失败'
        }
        showError('获取人才评分失败')
        console.error('获取人才评分失败:', err)
    } finally {
        loading.value = false
    }
}

// onMounted(() => {
//     fetchTalentRating()
// })
</script>

<template>
    <div class="container" style="touch-action: manipulation;">
        <div class="input-section">
            <input v-model="userId" type="number" placeholder="请输入用户ID" class="user-input" />
            <button @click="fetchTalentRating" class="fetch-btn">获取评分</button>
        </div>
        <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <div class="loading-text">加载中...</div>
        </div>
        <div v-else-if="error" class="error-container">
            <div class="error-text">{{ error }}</div>
        </div>
        <div v-else-if="talentRating" class="content-container">
            <div class="title">
                用户ID：{{ talentRating.userId }}
                <br>
                该人才评分：{{ talentRating.overallScore }}（{{ talentRating.overallLevel }}）
            </div>
            <div class="overall-evaluate">{{ talentRating.overallEvaluate }}</div>
            <div class="reason">
                得分原因如下：
            </div>
            <div v-for="item in talentRating.dimensionResults" :key="item.dimension" class="dimension-block">
                <div class="dimension-title">{{ item.dimension }}：{{ item.score }}（{{ item.level }}）</div>
                <div>{{ item.reason }}</div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.container {
    display: flex;
    width: 375px;
    min-height: 100vh;
    background-color: #f8f8f8;
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
    overflow-y: auto;
    flex-direction: column;
    padding-bottom: 15px;
    -webkit-touch-callout: none;
    -webkit-tap-highlight-color: transparent;
}

.title {
    padding: 24px;
    font-size: 25px;
    font-weight: 600;
}

.overall-evaluate {
    padding: 0px 24px;
}

.reason {
    padding: 10px 24px 10px 24px;
}

.dimension-block {
    background: #fff;
    border-radius: 8px;
    padding: 12px 16px;
    margin: 12px 24px 16px 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);

    .dimension-title {
        font-size: 15px;
        font-weight: 500;
        margin-bottom: 10px;
    }
}

.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    flex: 1;
}

.loading-spinner {
    width: 40px;
    height: 40px;
    border: 4px solid #f3f3f3;
    border-top: 4px solid #3498db;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 16px;
}

.loading-text {
    font-size: 16px;
    color: #666;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

.input-section {
    display: flex;
    gap: 12px;
    padding: 16px 24px;
    border-bottom: 1px solid #eee;
}

.user-input {
    flex: 1;
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
}

.fetch-btn {
    padding: 8px 16px;
    background: #3498db;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
}

.fetch-btn:hover {
    background: #2980b9;
}

.error-container {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
    padding: 24px;
}

.error-text {
    color: #e74c3c;
    font-size: 16px;
    text-align: center;
}
</style>