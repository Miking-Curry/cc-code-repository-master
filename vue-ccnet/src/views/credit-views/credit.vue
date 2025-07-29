<template>
    <div class="credit-score-container">

        <!--回退按钮--->
        <div class="top">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none"
                @click="goBacktoUserPage">
                <path d="M11.9993 15.3332L4.66602 7.99984L11.9993 0.666504" stroke="white" stroke-linecap="round"
                    stroke-linejoin="round" />
            </svg>
            <div class="right">
                <span class="small-text">规则</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                    <path
                        d="M6.99935 12.8332C8.61017 12.8332 10.0685 12.1802 11.1241 11.1246C12.1798 10.069 12.8327 8.61065 12.8327 6.99984C12.8327 5.38902 12.1798 3.93069 11.1241 2.87505C10.0685 1.81942 8.61017 1.1665 6.99935 1.1665C5.38853 1.1665 3.9302 1.81942 2.87456 2.87505C1.81893 3.93069 1.16602 5.38902 1.16602 6.99984C1.16602 8.61065 1.81893 10.069 2.87456 11.1246C3.9302 12.1802 5.38853 12.8332 6.99935 12.8332Z"
                        stroke="white" stroke-linejoin="round" />
                    <path
                        d="M7 8.3488V7.18213C7.9665 7.18213 8.75 6.39862 8.75 5.43213C8.75 4.46563 7.9665 3.68213 7 3.68213C6.0335 3.68213 5.25 4.46563 5.25 5.43213"
                        stroke="white" stroke-linecap="round" stroke-linejoin="round" />
                    <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M6.99935 11.5837C7.32151 11.5837 7.58268 11.3225 7.58268 11.0003C7.58268 10.6782 7.32151 10.417 6.99935 10.417C6.67719 10.417 6.41602 10.6782 6.41602 11.0003C6.41602 11.3225 6.67719 11.5837 6.99935 11.5837Z"
                        fill="white" />
                </svg>
            </div>
        </div>

        <!--欢迎页面--->
        <div class="welcome">
            <div class="top-text">
                <span>Hi！</span>
                <span>{{ username }}</span>
            </div>
            <span class="bottom">
                提高信用分可以获得HR优先查看权！
            </span>
        </div>

        <!--信用分卡片--->
        <div class="credit-card">
            <div class="credit-card-top">
                <div class="credit-card-top-left">
                    <div class="credit-card-top-left-top">
                        <span class="credit-card-top-left-top-text">
                            你当前的信用分
                        </span>
                        <span class="level">
                            {{ currentContent.rank }}
                        </span>
                    </div>
                    <span class="small-text">
                        *来自最近三个月数据
                    </span>
                </div>
                <div class="image">
                    <CreditScore :score="creditScore"></CreditScore>
                </div>
            </div>

            <div class="line"></div>

            <div class="credit-card-bottom">
                <div v-html="textList[0]"></div>
                <div v-html="textList[1]"></div>
            </div>
        </div>

        <div class="reminder">
            <div class="reminder-text">
                {{ currentContent.content }}
            </div>
        </div>

        <!-- 任务列表卡片 -->
        <div class="task-card">
            <div class="task-title-row">
                <span class="task-title">信用分任务</span>
            </div>

            <div class="task-list">
                <CreditMissionItem v-for="(item, index) in creditMission" :key="index" v-bind="item">

                </CreditMissionItem>

            </div>

        </div>
    </div>

</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import CreditMissionItem from '@/components/credit/CreditMissionItem.vue';
import { ApiResponse } from '@/utils/api/type';
import { getCreditPoints, getUsername } from '@/utils/api/credit';
import { useRouter } from 'vue-router';
import { useGoBack } from '@/utils/goBack';
import { showError } from '@/utils/toast';

const router = useRouter()

const username = ref<string | null>('')

const fetchUsername = async () => {
    try {
        const res: ApiResponse = await getUsername()
        if (res.code === 200 && res.data) {
            username.value = res.data
        }
    } catch (error) {
        // showError('获取用户名失败')
        console.error('获取用户名失败:', error)
    } finally {
    }
}

interface CreditMission {
    title: string
    point: number
    completed: boolean
    icon: string
}

const creditMission = ref<CreditMission[]>([
    {
        title: '身份信息上传',
        point: 1,
        completed: false,
        icon: 'idCard'
    },
    {
        title: '填写毕业信息',
        point: 1,
        completed: false,
        icon: 'graduate'
    },
    {
        title: '填写工作经验',
        point: 5,
        completed: false,
        icon: 'work'
    },
    {
        title: '填写离职信息',
        point: 1,
        completed: false,
        icon: 'resign'
    },
    {
        title: '技能证书上传',
        point: 2,
        completed: false,
        icon: 'skill'
    },
])


const creditScore = ref<number>(0)

const fetchCreditScore = async () => {
    try {
        const res: ApiResponse<number> = await getCreditPoints()
        if (res.code === 200 && res.data) {
            creditScore.value = res.data
        }
    } catch (error) {
        showError('获取信用分数失败')
        console.error('获取信用分数失败:', error)
    } finally {
    }
}


interface CreditContent {
    rank: string;
    content: string;
    minScore: number;
    percentage: number;
    level: string;
}

// 将配置改成数组 + minScore，更灵活可拓展
const CREDIT_CONTENT_LIST: CreditContent[] = [
    { minScore: 0, rank: '较差', content: '你尚未完成任何信用分任务，建议从基础任务开始。', percentage: 0, level: '比较低的水平' },
    { minScore: 1, rank: '较差', content: '你的当前信用分处于较差水平，在投递简历时处于劣势，尽快完成信用分任务提升竞争力。', percentage: 30, level: '比较低的水平' },
    { minScore: 3, rank: '一般', content: '你的当前信用分处于一般水平，在投递简历时竞争力一般，请尽快完成信用分任务提升竞争力。', percentage: 50, level: '中等水平' },
    { minScore: 6, rank: '良好', content: '你的当前信用分良好，在投递简历时HR有很大几率优先查看您的简历，建议继续完成剩下的任务。', percentage: 80, level: '中上水平' },
    { minScore: 10, rank: '优秀', content: '你的当前信用分处于优秀水平，在投递简历时HR有很大几率优先查看您的简历，继续保持！', percentage: 99, level: '最高水平' }
]

// 根据当前分数动态计算等级信息
const currentContent = computed(() => {
    const score = creditScore.value
    // 找到满足条件的最大 minScore 的配置项
    return [...CREDIT_CONTENT_LIST].reverse().find(item => score >= item.minScore) || CREDIT_CONTENT_LIST[0]
})

// 展示用的动态文本
const textList = computed(() => [
    `你的信用分超过<span class='credit-highlight'>${currentContent.value.percentage}%</span>的竞聘者`,
    `你在同岗位的竞争力中属于<span class='credit-highlight'>${currentContent.value.level}</span>`
])

const { goBacktoUserPage } = useGoBack()

onMounted(() => {
    fetchCreditScore()
    fetchUsername()
})
</script>

<style scoped>
* {
    margin: 0;
}


/* 整体容器 */
.credit-score-container {
    background: linear-gradient(180deg, #4177FC -6.08%, rgba(65, 119, 252, 0) 370px),
        #F8F8F8;
    min-height: 100vh;
}


.top {
    display: flex;
    width: 375px;
    height: 34px;
    padding: 0px 16px;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;

    .right {
        display: flex;
        height: 34px;
        align-items: center;
        gap: 4px;

        .small-text {
            color: #FFF;
            text-align: center;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }
}

.welcome {
    display: flex;
    width: 154px;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;

    margin-left: 34px;

    .top-text {
        display: flex;
        align-items: center;

        color: #FFF;
        text-align: center;
        font-size: 16px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
        /* 19.2px */

    }

    .bottom {
        color: rgba(255, 255, 255, 0.60);
        text-align: center;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
        white-space: nowrap;
    }
}

.credit-card {
    width: 343px;
    display: flex;
    padding: 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    align-self: stretch;
    margin: 8px auto 0 auto;

    border-radius: 8px;
    background: #FFF;

    .credit-card-top {
        display: flex;
        gap: 49px;
        align-self: stretch;

        .credit-card-top-left {
            display: flex;
            width: 86px;
            flex-direction: column;
            align-items: flex-start;
            gap: 32px;

            .credit-card-top-left-top {
                display: flex;
                flex-direction: column;
                align-items: flex-start;
                gap: 8px;
                align-self: stretch;
                white-space: nowrap;

                .credit-card-top-left-top-text {
                    align-self: stretch;
                    color: #666;
                    text-align: center;
                    font-size: 12px;
                    font-style: normal;
                    font-weight: 400;
                    line-height: 120%;
                }

                .level {
                    color: #333;
                    text-align: center;
                    font-size: 20px;
                    font-style: normal;
                    font-weight: 500;
                    line-height: 120%;
                }
            }

            .small-text {
                color: #999;
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                white-space: nowrap;
                line-height: 120%;
            }
        }

        .image {
            width: 176px;
            height: 88px;
            overflow: visible;
        }
    }

    .line {
        width: 311px;
        height: 0px;
        border: 1px solid #E6E6E6;
    }

    .credit-card-bottom {
        color: #666;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
    }
}

:deep(.credit-highlight) {
    color: #333;
    font-size: 12px;
    font-style: normal;
    font-weight: 500;
    line-height: 120%;
}

.reminder {
    width: 343px;
    display: flex;
    padding: 12px 16px;
    margin: 16px auto 0px auto;
    align-items: center;
    align-self: stretch;

    border-radius: 8px;
    background: #FFF;

    .reminder-text {
        color: #333;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 150%;
    }
}


canvas {
    display: block;
}


.task-card {
    display: flex;
    margin: 16px auto 0px auto;
    padding-top: 16px;
    flex-direction: column;
    align-items: flex-start;
    align-self: stretch;

    border-radius: 8px;
    background: #FFF;

    width: 343px;
}

.task-title-row {
    display: flex;
    padding: 0px 16px;
    align-items: center;
    gap: 10px;
    align-self: stretch;
}

.task-title {
    color: #333;
    font-size: 16px;
    font-style: normal;
    font-weight: 500;
    line-height: 120%;
}

.task-list {
    display: flex;
    flex-direction: column;
}

.task-item {
    display: flex;
    align-items: center;
    border-radius: 10px;
    padding: 12px 12px;

}
</style>