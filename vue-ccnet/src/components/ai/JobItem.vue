<!-- JobItem.vue -->
<template>
    <div class="item" :class="{ 'is-selected': props.isSelected }" @click="handleClick">
        <div class="top">
            <div class="title">
                <div class="left">
                    <span class="title-text">
                        {{ jobItem.postName }}
                    </span>
                    <div v-for="(statusDesc, index) in jobItem.postStatusDescs" :key="index"
                        :class="getStatusClass(statusDesc)">
                        {{ statusDesc }}
                    </div>
                </div>
                <div class="right">
                    {{ jobItem.minSalary }}-{{ jobItem.maxSalary }}元
                </div>
            </div>
            <div class="labels">
                <div v-for="(tag, index) in jobItem.postTag" class="label">
                    {{ tag }}
                </div>
            </div>
            <div class="describe">
                <span class="job-type" v-if="jobItem.companyType">
                    {{ jobItem.companyType }}
                </span>
                <span class="people-number" v-if="staffSizeDisplay">
                    {{ staffSizeDisplay }}
                </span>
            </div>
        </div>

        <div class="middle">
            <div class="top-text">
                <img :src="avatarUrl" alt="" class="profile-picture" @error="handleImageError">
                <div class="company-name">
                    {{ jobItem.companyName }}
                </div>
            </div>
            <div class="bottom-text">
                <span class="release-time" v-if="relativeTime">
                    {{ relativeTime }}
                </span>
                <div class="location">
                    <span class="city" v-if="jobItem.province">
                        {{ jobItem.province }}
                    </span>
                    <span class="district" v-if="jobItem.city">
                        {{ jobItem.city }}
                    </span>
                    <span class="specific-location" v-if="jobItem.state">
                        {{ jobItem.state }}
                    </span>
                </div>
            </div>
        </div>
        <dashboard class="dashboard-positon" :percentage="jobItem.matchPercent" />
        <div class="bottom">
            <span class="view-job-detail-text" @click="jump">
                查看岗位详情
            </span>
            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="12" viewBox="0 0 10 12" fill="none">
                <path d="M2.50065 10.5837L7.08398 6.00033L2.50065 1.41699" stroke="#666666" stroke-linecap="round"
                    stroke-linejoin="round" />
            </svg>
        </div>
    </div>
</template>

<script setup lang="ts">
import { MatchJobItem } from '@/utils/api/type';
import dashboard from '../dashboard/dashboard.vue';
import { computed, ref } from 'vue';

type Props = {
    isSelected: boolean;
    jobItem: MatchJobItem;
};

const props = defineProps<Props>()

// 默认头像图片
const defaultAvatarUrl = '../../assets/ai-images/profile-picture-example.png'

// 图片加载错误状态
const imageError = ref(false)

// 计算头像URL，如果加载失败则使用默认图片
const avatarUrl = computed(() => {
    if (imageError.value || !props.jobItem.companyLogoUrl) {
        return defaultAvatarUrl
    }
    return props.jobItem.companyLogoUrl
})

// 格式化时间戳为相对时间
const formatRelativeTime = (timestamp: string): string => {
    const now = new Date()
    const targetTime = new Date(timestamp)
    const diffInMs = now.getTime() - targetTime.getTime()

    // 转换为不同的时间单位
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60))
    const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60))
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24))
    const diffInMonths = Math.floor(diffInMs / (1000 * 60 * 60 * 24 * 30))
    const diffInYears = Math.floor(diffInMs / (1000 * 60 * 60 * 24 * 365))

    if (diffInYears > 0) {
        return `${diffInYears}年前发布`
    } else if (diffInMonths > 0) {
        return `${diffInMonths}个月前发布`
    } else if (diffInDays > 0) {
        return `${diffInDays}天前发布`
    } else if (diffInHours > 0) {
        return `${diffInHours}小时前发布`
    } else if (diffInMinutes > 0) {
        return `${diffInMinutes}分钟前发布`
    } else {
        return '刚刚发布'
    }
}

// 计算相对时间
const relativeTime = computed(() => {
    if (!props.jobItem.createdAt) return ''
    return formatRelativeTime(props.jobItem.createdAt)
})

// 格式化员工规模
const formatStaffSize = (size: number): string => {
    if (size <= 50) {
        return '0-50人'
    } else if (size <= 100) {
        return '50-100人'
    } else if (size <= 500) {
        return '100-500人'
    } else if (size <= 1000) {
        return '500-1000人'
    } else if (size <= 5000) {
        return '1000-5000人'
    } else if (size <= 10000) {
        return '5000-10000人'
    } else {
        return '10000+人'
    }
}

// 计算员工规模显示
const staffSizeDisplay = computed(() => {
    if (!props.jobItem.staffSize) return ''
    return formatStaffSize(props.jobItem.staffSize)
})

const emit = defineEmits<{
    (e: 'select'): void
}>()

function handleClick() {
    emit('select')
}

function jump() {
    // 根据岗位id跳转到指定的url
    // http://112.74.33.58:48110/#/content/postDetail?id=1669
    const url = `http://112.74.33.58:48110/#/content/postDetail?id=${props.jobItem.postId}`
    window.location.href = url
}

function handleImageError() {
    // 图片加载失败时，设置为使用默认图片
    imageError.value = true
}

function getStatusClass(statusDesc: string) {
    switch (statusDesc) {
        case '急':
            return 'impatient-label'
        case '新':
            return 'new-label'
        case '荐':
            return 'recommend-label'
        case '优':
            return 'high-quality-label'
        case '普':
            return 'ordinary-label'
        default:
            return ''
    }
}
</script>

<style scoped lang="scss">
.item {
    // 布局
    width: 343px;
    display: flex;
    padding: 12px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;

    // 边框
    border-radius: 8px;
    background: #FFF;

    position: relative;

    .dashboard-positon {
        position: absolute;
        top: 45px;
        right: 12px
    }
}

.top {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
    align-self: stretch;

    .title {
        display: flex;
        align-items: center;
        gap: 8px;
        align-self: stretch;

        .left {
            display: flex;
            align-items: center;
            gap: 8px;
            flex: 1 0 0;

            .title-text {
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;

                overflow: hidden;
                color: #333;
                text-overflow: ellipsis;
                font-size: 18px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }

            .impatient-label {
                display: flex;
                padding: 2px 4px;
                justify-content: center;
                align-items: center;

                border-radius: 4px;
                border: 0.5px solid rgba(227, 76, 76, 0.20);
                background: #FFEBEB;

                color: #E34C4C;
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }

            .new-label {
                display: inline-flex;
                padding: 2px 4px;
                justify-content: center;
                align-items: center;

                border-radius: 4px;
                border: 0.5px solid rgba(65, 119, 252, 0.20);
                background: #EBF3FF;

                color: var(--primary-Color, #4177FC);
                font-family: PingFangSC;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }

            .recommend-label {
                display: inline-flex;
                padding: 2px 4px;
                justify-content: center;
                align-items: center;

                border-radius: 4px;
                border: 0.5px solid rgba(65, 119, 252, 0.20);
                background: #EBF3FF;

                color: var(--primary-Color, #4177FC);
                font-family: PingFangSC;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }

            .high-quality-label {
                display: inline-flex;
                padding: 2px 4px;
                justify-content: center;
                align-items: center;

                border-radius: 4px;
                border: 0.5px solid rgba(166, 192, 111, 0.20);
                background: #F8FCEF;

                color: #A6C06F;
                font-family: PingFangSC;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }

            .ordinary-label {
                display: inline-flex;
                padding: 2px 4px;
                justify-content: center;
                align-items: center;

                border-radius: 4px;
                border: 0.5px solid rgba(100, 203, 203, 0.20);
                background: #EEFDFD;

                color: #64CBCB;
                font-family: PingFangSC;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 12px */
            }

        }

        .right {
            color: var(--primary-Color, #4177FC);
            font-family: DIN;
            font-size: 16px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
            /* 16.8px */
        }
    }

    .labels {
        display: flex;
        width: 197px;
        align-items: center;
        gap: 8px;

        .label {
            display: flex;
            padding: 2px 4px;
            justify-content: center;
            align-items: center;

            border-radius: 4px;
            background: #F5F5F5;

            color: #666;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
            /* 12px */
        }
    }

    .describe {
        display: flex;
        justify-content: flex-start;
        color: #666;
        font-size: 14px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
        gap: 8px;
    }
}

.middle {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
    align-self: stretch;

    .top-text {
        display: flex;
        width: 198px;
        align-items: center;
        gap: 4px;

        .profile-picture {
            width: 16px;
            height: 16px;
            flex-shrink: 0;
            aspect-ratio: 1/1;

            border-radius: 16px;
            background: url(<path-to-image>) lightgray 50% / cover no-repeat;

        }

        .company-name {
            display: flex;
            width: 170px;
            align-items: flex-start;
            gap: 4px;
            flex-shrink: 0;
            // white-space: nowrap;

            color: #333;
            font-family: PingFangSC;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }

    .bottom-text {
        display: flex;
        justify-content: space-between;
        align-items: center;
        align-self: stretch;

        color: #999;
        font-family: PingFangSC;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;

        .location {
            display: flex;
            gap: 4px;
        }

    }

}

.is-selected {
    outline: 1.5px solid var(--primary-Color, #4177FC);
}

.bottom {
    display: flex;
    align-items: center;
    gap: 4px;
    align-self: stretch;

    .view-job-detail-text {
        color: #666;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
    }
}
</style>