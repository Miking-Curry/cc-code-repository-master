<script setup lang="ts">
defineOptions({ name: 'Ai' })
import JobItem from '@/components/ai/JobItem.vue';
import router from '@/router';
import { getAiConclusion, sendResume, getJobList } from '@/utils/api/ai';
import { AiConclusion, AiConclusionResponse, MatchJobItem } from '@/utils/api/type';
import { showError } from '@/utils/toast';
import { ref, computed, onMounted } from 'vue';

function goBack(){
    window.location.href = 'http://caicainet.com/#/jobs';
}

const point = ref<number>(0)

const images = import.meta.glob('../../assets/ai-images/*.png', { eager: true, import: 'default' })

// 添加计算属性来获取图片路径
const robotImagePath = computed(() => {
    let range = ''
    if (point.value >= 90) {
        range = '90-99'
    } else if (point.value >= 80) {
        range = '80-89'
    } else if (point.value >= 61) {
        range = '61-79'
    } else {
        range = '0-60'
    }
    // 随机选择01或02
    const randomNum = Math.random() < 0.5 ? '01' : '02'
    const key = `../../assets/ai-images/${range}-${randomNum}.png`
    return images[key] as string
})

function goToImprove() {
    router.push('/ai/improve')
}

const analyzedTextList: any[] = []

// 获取可完成的任务
const aiConclusion = ref<AiConclusion | null>()
const loading = ref(false)
const scoreError = ref(false)
const fetchAiConclusions = async () => {
    try {
        loading.value = true
        scoreError.value = false
        const res: AiConclusionResponse = await getAiConclusion()
        if (res.code === 200 && res.data) {
            aiConclusion.value = res.data
            point.value = aiConclusion.value.score
            // 高亮处理
            analyzedTextList[0] = highlightKeywords(aiConclusion.value.conclusion, aiConclusion.value.keywords)
            if (res.data.conclusion === '系统正在升级中，请稍后再试') {
                point.value = 0
                scoreError.value = false
                analyzedTextList[0] = '系统正在升级中，请稍后再试！'
            }
        } else if (res.code === 500 && res.msg === "未找到简历信息") {
            point.value = 0
            scoreError.value = false
            analyzedTextList[0] = '你还没有简历信息哦！'
        } else {
            scoreError.value = true
        }
    } catch (error: any) {
        // 展开打印 error
        console.error('error:', error, JSON.stringify(error));

        // 兼容各种嵌套
        let msg = '';
        if (typeof error === 'string') {
            msg = error;
        } else if (error?.msg) {
            msg = error.msg;
        } else if (error?.message) {
            msg = error.message;
        } else if (error?.response?.data?.msg) {
            msg = error.response.data.msg;
        } else if (error?.response?.data?.message) {
            msg = error.response.data.message;
        } else {
            // 尝试序列化 error
            msg = JSON.stringify(error);
        }

        if (msg.includes("未找到简历信息")) {
            point.value = 0
            scoreError.value = false
            analyzedTextList[0] = '您还没有简历信息哦！'
        } else {
            showError('获取AI总结失败')
            analyzedTextList[0] = 'AI给你的建议失败，请重新刷新获取哦！'
            scoreError.value = true
        }
        loading.value = false
    }
    finally {
        loading.value = false
    }
}

let ifChooseSelectAll = ref(false)

//获取三个匹配岗位
const matchJobItems = ref<MatchJobItem[]>([])
const showBottom = computed(() => {
    return selectedIndex.value.size > 0 ? true : false
})

const selectedIndex = ref<Set<number>>(new Set())

function selecAll() {
    ifChooseSelectAll.value = !ifChooseSelectAll.value
    if (ifChooseSelectAll.value === true) {
        const allIds = matchJobItems.value.map(item => item.postId);
        selectedIndex.value = new Set(allIds)
    } else if (ifChooseSelectAll.value === false) {
        selectedIndex.value = new Set()
    }
}

function clickJobItem(index: number) {
    if (selectedIndex.value.has(index)) {
        selectedIndex.value.delete(index)
    } else {
        selectedIndex.value.add(index)
    }

    //判断0,1,2是否都在set里面有
    const allIds = matchJobItems.value.map(item => item.postId);
    if (allIds.every(i => selectedIndex.value.has(i))) {
        ifChooseSelectAll.value = true
    } else {
        ifChooseSelectAll.value = false
    }

    // 强制触发视图更新
    selectedIndex.value = new Set(selectedIndex.value)
}

const pagEnded = ref(false)
const showAllAnimation = ref(false)

function onPagEnded() {
    pagEnded.value = true
    // 延迟等待渐隐完成后再显示 all-animation
    setTimeout(() => {
        showAllAnimation.value = true
    }, 600) // 500ms 要和渐隐动画时间一致
}

const showSuccessDialog = ref(false);

function closeSuccessDialog() {
    showSuccessDialog.value = false;
}

const confirm = () => {
    // 执行确认操作
    alert('已确认！');
    closeSuccessDialog();
};

function openSuccessDialog() {
    showSuccessDialog.value = true;
}

async function submitResume() {
    let allSuccess = true;
    for (const item of selectedIndex.value) {
        const res = await sendResume(item);
        if (!(res && (res.code === 0 || res.code === 200))) {
            allSuccess = false;
            // 可以在这里直接 break; 如果只要有一个失败就不需要继续
            break;
        }
    }
    return allSuccess;
}

async function handleSubmitClick() {
    const allSuccess = await submitResume();
    if (allSuccess) {
        openSuccessDialog();
    } else {
        // 这里可以自定义错误提示
        // showError('有岗位投递失败，请检查登录状态或稍后重试');
    }
}

function highlightKeywords(text: string, keywords: string[] = []): string {
    if (!text || !Array.isArray(keywords) || keywords.length === 0) return text;

    // 按长度排序，优先匹配长的词，防止子串先被替换
    keywords = [...keywords].sort((a, b) => b.length - a.length);

    const escapedKeywords = keywords.map(k =>
        k.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') // 转义正则关键字符
    );
    const pattern = new RegExp(`(${escapedKeywords.join('|')})`, 'g');

    return text.replace(pattern, match => `<span class="highlight">${match}</span>`);
}

const bubbleText = ref<string[]>(['叮！检测到您已达到"猎头重点关注层"！', '正在加载您的简历哦！', '你的简历欠佳，仍需提升哦！'])

const bubbleSelecetedText = computed(() => {
    if (loading.value === true) {
        return bubbleText.value[1]
    } else if (point.value < 60) {
        return bubbleText.value[2]
    } else {
        return bubbleText.value[0]
    }
})

const bottomText = computed(() => {
    return point.value === 0 ? '我建议您先完善您的简历信息！' : `竞争力良好，还可提升${100 - point.value}分`
})

const isRefreshing = ref(false)

// 添加岗位列表加载状态
const jobListLoading = ref(false)
const jobListError = ref(false)

function handleRefresh() {
    if (isRefreshing.value) return; // 防止重复点击
    isRefreshing.value = true;
    setTimeout(() => {
        isRefreshing.value = false;
    }, 2000);
    selectedIndex.value = new Set()
    ifChooseSelectAll.value = false
    // 调用真实的API刷新岗位列表
    fetchJobList();
}

// 添加获取岗位列表的函数
const fetchJobList = async () => {
    try {
        jobListLoading.value = true
        jobListError.value = false
        const response = await getJobList()
        if (response.code === 200 && response.data) {
            matchJobItems.value = response.data
        } else {
            showError('获取岗位列表失败')
            jobListError.value = true
        }
    } catch (error) {
        showError('获取岗位列表失败')
        jobListError.value = true
        console.error('获取岗位列表失败:', error)
    } finally {
        jobListLoading.value = false
    }
}

onMounted(() => {
    fetchAiConclusions()
    fetchJobList() // 首次加载时获取岗位列表
})
</script>

<template>
    <div class="container">
        <transition name="fade-out">
            <PagPlayer v-if="!pagEnded" src="/assets/ai-images/ai-help.pag" :repeat="1" width="100vw"
                class="ai-help-animation" @ended="onPagEnded" />
        </transition>

        <!-- 渐显 all-animation -->
        <transition name="fade-in">
            <div class="all-animation" v-if="showAllAnimation"
                element-loading-custom-class="analyzed-point-loading-class">
                <div class="top-bar">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none"
                        @click="goBack">
                        <path d="M11.9993 15.3332L4.66602 7.99984L11.9993 0.666504" stroke="#333333"
                            stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </div>
                <div class="point-container">
                    <div class="point-text">
                        <span class="number" v-if="!loading && !scoreError">
                            {{ point }}
                        </span>
                        <div class="loading-number" v-else>
                            <div class="loading-dots">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </div>
                        <span class="word">
                            分
                        </span>
                    </div>
                    <div class="bottom-text" v-if="!loading && !scoreError">
                        <span class="left-text">
                            {{ bottomText }}
                        </span>
                        <div class="right-text" @click="goToImprove">
                            <span class="text">
                                去提升
                            </span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 8 8" fill="none">
                                <path d="M2 7L6 4L2 1" stroke="#6692FD" stroke-linecap="round"
                                    stroke-linejoin="round" />
                            </svg>
                        </div>
                    </div>
                    <div class="bottom-text loading-bottom" v-else>
                        <span class="left-text">
                            正在分析您的竞争力...
                        </span>
                    </div>
                </div>

                <div class="introduction">
                    <div class="bubble">
                        <div class="bubble-text">
                            {{ bubbleSelecetedText }}
                        </div>
                    </div>
                    <div class="tail"></div>
                </div>

                <img :src="robotImagePath" alt="" class="robot">

                <div class="analyzed-text">
                    <div class="text">
                        <template v-if="loading">
                            <span class="analyzing-text">
                                正在分析中<span class="dot">.</span><span class="dot">.</span><span class="dot">.</span>
                            </span>
                        </template>
                        <template v-else>
                            <div v-for="(item, index) in analyzedTextList" :key="index" v-html="item"></div>
                        </template>
                    </div>
                    <!-- <div class="promote">
                        <span>查看报告</span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 12 12" fill="none">
                            <path d="M3 10.5L9 6L3 1.5" stroke="#6692FD" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                    </div> -->
                </div>

                <div class="note">
                    <span class="note-text">
                        *当前内容由AI智能算法生成
                    </span>
                </div>

                <div class="middle-bar">
                    <div class="left">
                        <span class="title">
                            智能投递
                        </span>
                        <span class="describe">
                            智能推荐与您匹配度高的岗位
                        </span>
                    </div>
                    <div class="right">
                        <span class="refresh" @click="handleRefresh">
                            刷新
                        </span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="17" viewBox="0 0 16 17" fill="none"
                            :class="{ 'refresh-rotate': isRefreshing }" @click="handleRefresh">
                            <path
                                d="M3.75737 12.7426C4.84313 13.8284 6.34313 14.5 8 14.5C11.3137 14.5 14 11.8137 14 8.5C14 5.1863 11.3137 2.5 8 2.5C6.34313 2.5 4.84313 3.17157 3.75737 4.25737C3.20469 4.81003 2 6.16667 2 6.16667"
                                stroke="#5B96FF" stroke-linecap="round" stroke-linejoin="round" />
                            <path d="M2 3.5V6.16667H4.66667" stroke="#5B96FF" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                    </div>
                </div>

                <div class="job-list">
                    <!-- 加载中或加载失败都显示骨架屏 -->
                    <template v-if="jobListLoading || jobListError">
                        <div v-for="i in 3" :key="`skeleton-${i}`"
                            :class="['job-item-skeleton', { 'skeleton-static': jobListError }]">
                            <div class="skeleton-top">
                                <div class="skeleton-title">
                                    <div class="skeleton-title-text"></div>
                                    <div class="skeleton-priority"></div>
                                </div>
                                <div class="skeleton-salary"></div>
                            </div>
                            <div class="skeleton-labels">
                                <div class="skeleton-label"></div>
                                <div class="skeleton-label"></div>
                                <div class="skeleton-label"></div>
                            </div>
                            <div class="skeleton-describe">
                                <div class="skeleton-company"></div>
                                <div class="skeleton-type"></div>
                                <div class="skeleton-size"></div>
                            </div>
                            <div class="skeleton-middle">
                                <div class="skeleton-profile"></div>
                                <div class="skeleton-company-name"></div>
                            </div>
                            <div class="skeleton-bottom">
                                <div class="skeleton-time"></div>
                                <div class="skeleton-location"></div>
                            </div>
                            <div class="skeleton-match-degree"></div>
                        </div>
                    </template>

                    <!-- 真实岗位列表 -->
                    <template v-else>
                        <JobItem v-for="(matchJobItem, index) in matchJobItems" :key="matchJobItem.postId"
                            :isSelected="selectedIndex.has(matchJobItem.postId)" :jobItem=matchJobItem
                            @select="clickJobItem(matchJobItem.postId)" />
                    </template>
                </div>
            </div>
        </transition>

    </div>

    <div class="bottom-bar" v-if="showBottom">
        <div class="left-select">

            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="25" viewBox="0 0 24 25" fill="none"
                v-if="!ifChooseSelectAll" @click="selecAll">
                <circle cx="12" cy="12.5" r="9" stroke="#90BBFF" stroke-width="2" />
            </svg>

            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="25" viewBox="0 0 24 25" fill="none" v-else
                @click="selecAll">
                <circle cx="12" cy="12.5" r="9" stroke="#90BBFF" stroke-width="2" />
                <circle cx="12" cy="12.5" r="5" fill="#4177FC" />
            </svg>

            <span class="select-all-text">
                全选
            </span>
        </div>

        <button class="delever-button" @click="handleSubmitClick">
            一键投递
        </button>
    </div>

    <transition name="dialog">
        <!-- 遮罩层 -->
        <div v-if="showSuccessDialog" class="overlay" @click="closeSuccessDialog"></div>
    </transition>

    <transition name="dialog">
        <!-- 对话框 -->
        <div v-if="showSuccessDialog" class="successDialog">
            <img src="../../assets/ai-images/success-dialog-svg.svg" alt="">
            <div class="confirm-button-layout">
                <button class="confirm-button" @click="closeSuccessDialog">确认</button>
            </div>
        </div>
    </transition>

</template>

<style scoped>
* {
    font-family: PingFangSC;
}

.container {
    position: relative;
    /* 或 absolute */
    width: 100vw;
    min-height: 100vh;
    padding-bottom: 60px;
    /* 用 min-height 保证内容撑开 */
    background-image: url('../../assets/ai-images/background.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    overflow-y: auto;
    /* 自动滚动 */
}

.top-bar {
    display: flex;
    width: 375px;
    height: 34px;
    padding: 0px 16px;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;
}

.point-container {
    margin-left: 16px;
    display: flex;
    width: 110px;
    height: 78px;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    flex-shrink: 0;

    .point-text {
        align-self: stretch;

        .number {
            color: #333;
            font-size: 48px;
            font-style: normal;
            font-weight: 600;
            line-height: 120%;
        }

        .loading-number {
            display: inline-flex;
            align-items: center;
            height: 48px;
            margin-right: 5px;

            .loading-dots {
                display: flex;
                gap: 4px;
                align-items: center;

                span {
                    width: 8px;
                    height: 8px;
                    border-radius: 50%;
                    background: #6692FD;
                    animation: loading-dots 1.4s infinite ease-in-out;

                    &:nth-child(1) {
                        animation-delay: -0.32s;
                    }

                    &:nth-child(2) {
                        animation-delay: -0.16s;
                    }

                    &:nth-child(3) {
                        animation-delay: 0s;
                    }
                }
            }
        }

        .word {
            display: inline-flex;
            align-items: end;
            justify-content: end;
            height: 100%;
            color: #333;
            font-size: 16px;
            font-style: normal;
            font-weight: 600;
            line-height: 120%;
        }
    }

    .bottom-text {
        display: flex;
        padding: 4px 8px;
        justify-content: center;
        align-items: center;
        gap: 8px;
        white-space: nowrap;
        /* 不换行 */
        border-radius: 12px;
        background: rgba(255, 255, 255, 0.50);
        margin-top: 3px;

        .left-text {
            color: #6692FD;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
            /* 12px */
        }

        .right-text {
            display: flex;
            align-items: baseline;

            .text {
                color: rgba(65, 119, 252, 0.80);
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }
        }

        &.loading-bottom {
            justify-content: center;

            .left-text {
                color: #999;
            }
        }
    }
}

.introduction {
    position: absolute;
    top: 28px;
    left: 135px;

    .bubble {
        display: inline-block;
        width: 112px;
        padding: 6px;
        background: white;
        border-radius: 8px 8px 0px 8px;
        box-shadow: 0 0 2px rgba(0, 0, 0, 0.05);

        .bubble-text {
            font-family: PingFangSC;
            font-size: 10px;
            font-style: normal;
            font-weight: 400;
            line-height: 150%;

            background: linear-gradient(94deg, #C757FB -17.24%, #306AFC 47.87%, #6FD7FF 106.26%);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
    }

    .tail {
        position: absolute;
        right: -9px;
        bottom: 0px;
        width: 0;
        height: 0;
        border-left: 10px solid white;
        /* 右向尾巴的主体 */
        border-top: 10px solid transparent;
        box-shadow: 0 0 2px rgba(0, 0, 0, 0.05);
    }
}

.robot {
    width: 108px;
    height: 116px;
    flex-shrink: 0;
    aspect-ratio: 27/29;

    position: absolute;
    top: 34px;
    left: 251px;

    z-index: 0;
}

.analyzed-text {
    position: relative;
    margin: 10px auto 0px auto;
    display: flex;
    width: 343px;
    min-height: 60px;
    padding: 12px 12px 12px 12px;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    gap: 6px;

    /* 外边框 */
    border-radius: 16px;
    background: #FFF;
    box-shadow: 0px 4px 36px 0px rgba(200, 226, 254, 0.60);

    .text {
        color: #333;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 150%;


    }

    .promote {
        position: absolute;
        right: 12px;
        bottom: 4.5px;

        display: flex;
        align-items: baseline;

        color: rgba(65, 119, 252, 0.80);
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
    }
}

:deep(.highlight) {
    color: var(--primary-Color, #4177FC);
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 150%;
}

.note {
    display: flex;
    flex-direction: row-reverse;
    align-items: center;
    width: 343px;
    margin: 4px auto 0px auto;

    .note-text {
        color: rgba(0, 0, 0, 0.30);
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
        /* 12px */
    }
}

.middle-bar {
    display: flex;
    width: 343px;
    margin: 16px auto 0px auto;
    justify-content: space-between;
    align-items: center;

    .left {
        display: flex;
        align-items: baseline;
        gap: 4px;

        .title {
            color: #333;
            font-size: 16px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
            /* 19.2px */
        }

        .describe {
            color: rgba(0, 0, 0, 0.30);
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
            /* 12px */
        }
    }

    .right {
        display: flex;
        align-items: center;
        gap: 4px;

        color: #5B96FF;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
        /* 14.4px */
    }
}

.job-list {
    display: flex;
    width: 343px;
    margin: 12px auto 0px auto;
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
}

.bottom-bar {
    position: fixed;
    bottom: 0px;
    display: flex;
    width: 375px;
    height: 49px;
    padding: 0px 16px;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;
    background-color: #FFF;

    .left-select {
        display: flex;
        width: 56px;
        flex-shrink: 0;

        justify-content: space-between;
        align-items: center;

        .selector {
            width: 24px;
            height: 24px;
            flex-shrink: 0;
            aspect-ratio: 1/1;
        }

        .select-all-text {
            color: #333;
            font-size: 14px;
            font-style: normal;
            font-weight: 400;
            line-height: normal;
            text-align: center;
            white-space: nowrap;
        }
    }

    .delever-button {
        display: flex;
        height: 32px;
        padding: 8px 16px;
        justify-content: center;
        align-items: center;
        gap: 10px;

        border-radius: 46px;
        border: none;
        background: linear-gradient(96deg, #70D9FF -33.39%, #306AFC 53.24%, #C657FA 149.98%);

        color: #FFF;
        font-size: 14px;
        font-style: normal;
        font-weight: 500;
        line-height: normal;

    }
}

.ai-help-animation {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

.fade-out-enter-active,
.fade-out-leave-active {
    transition: opacity 0.5s ease;
}

.fade-out-enter-from,
.fade-out-leave-to {
    opacity: 0;
}

.fade-out-enter-to,
.fade-out-leave-from {
    opacity: 1;
}

.fade-in-enter-active {
    transition: opacity 0.5s ease;
}

.fade-in-enter-from {
    opacity: 0;
}

.fade-in-enter-to {
    opacity: 1;
}

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

.successDialog {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    padding: 20px;
    z-index: 1001;
    border-radius: 8px;

    img {
        width: 280px;
    }

    .confirm-button-layout {
        position: absolute;
        left: 50%;
        top: 73%;
        transform: translate(-50%, -50%);
    }

    .confirm-button {
        width: 143px;
        height: 36px;
        border: none;
        border-radius: 25px;
        background: var(--primary-Color, #4177FC);
        padding: 8px 16px;

        color: #FFF;
        font-size: 16px;
        font-style: normal;
        font-weight: 500;
        line-height: 120%;
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

.refresh-rotate {
    animation: rotate-refresh 0.5s linear;
}

@keyframes rotate-refresh {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(-360deg);
    }
}

.analyzing-text {
    color: #6692FD;
    font-size: 14px;
    letter-spacing: 1px;
}

.dot {
    animation: blink 1.4s infinite both;
}

.dot:nth-child(2) {
    animation-delay: 0.2s;
}

.dot:nth-child(3) {
    animation-delay: 0.4s;
}

@keyframes blink {

    0%,
    80%,
    100% {
        opacity: 0.2;
    }

    40% {
        opacity: 1;
    }
}
</style>
<style>
@keyframes loading-dots {

    0%,
    80%,
    100% {
        transform: scale(0.8);
        opacity: 0.5;
    }

    40% {
        transform: scale(1);
        opacity: 1;
    }
}

/* 骨架屏样式 */
.job-item-skeleton {
    width: 343px;
    padding: 12px;
    border-radius: 8px;
    background: #FFF;
    position: relative;
    animation: skeleton-pulse 1.5s ease-in-out infinite;
}

.skeleton-top {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 4px;
}

.skeleton-title {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
}

.skeleton-title-text {
    width: 120px;
    height: 22px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-priority {
    width: 24px;
    height: 16px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-salary {
    width: 80px;
    height: 20px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-labels {
    display: flex;
    gap: 8px;
    margin-bottom: 4px;
}

.skeleton-label {
    width: 50px;
    height: 16px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-describe {
    display: flex;
    gap: 8px;
    margin-bottom: 10px;
}

.skeleton-company,
.skeleton-type,
.skeleton-size {
    width: 40px;
    height: 14px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 3px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-middle {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
}

.skeleton-profile {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-company-name {
    width: 100px;
    height: 16px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 4px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.skeleton-time {
    width: 60px;
    height: 14px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 3px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-location {
    width: 80px;
    height: 14px;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    border-radius: 3px;
    animation: skeleton-shimmer 1.5s infinite;
}

.skeleton-match-degree {
    position: absolute;
    top: 45px;
    right: 12px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    animation: skeleton-shimmer 1.5s infinite;
}

@keyframes skeleton-shimmer {
    0% {
        background-position: -200% 0;
    }

    100% {
        background-position: 200% 0;
    }
}

@keyframes skeleton-pulse {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.8;
    }
}

.skeleton-static {
    animation: none !important;
    /* 你也可以把背景色调淡一点，表示是静态骨架 */
}
</style>