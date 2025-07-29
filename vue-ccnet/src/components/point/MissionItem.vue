<!-- MissionItem.vue -->
<template>
    <div class="item">
        <div class="left">
            <img :src="icon" alt="" />
            <div class="text">
                <span class="title">{{ title }}</span>
                <span class="detail">{{ detail }}</span>
            </div>
            <div class="point-add">
                <img src="../../assets/point-icons/small-coin.png" alt="" />
                <span class="point-add-text">+{{ point }}积分</span>
            </div>
        </div>
        <div class="right">
            <button :class="[completed ? 'complete' : 'uncomplete']" @click="jump" :disabled="props.completed">
                {{ completed ? '已完成' : '去完成' }}
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import browseIcon from '@/assets/point-icons/browse-icon.png'
import aceptIcon from '@/assets/point-icons/accept-interview-icon.png'
import completeIcon from '@/assets/point-icons/complete-icon.png'
import deliverIcon from '@/assets/point-icons/deliver-icon.png'
import shareIcon from '@/assets/point-icons/share-icon.png'
import wechatIcon from '@/assets/point-icons/wechat.png'
import realNameIcon from '@/assets/point-icons/real-name.png'
import { useUserStore } from '@/stores/user'

import { useRouter } from 'vue-router'
import { showUnderDevelopDialog } from '@/utils/globalDialog'

const router = useRouter()

const props = defineProps<{
    title: string
    detail: string
    point: number
    completed: boolean
    icon: string
}>()

// 根据 iconName 动态选择图片
const iconMap: Record<string, string> = {
    browse: browseIcon,
    deliver: deliverIcon,
    acept: aceptIcon,
    share: shareIcon,
    complete: completeIcon,
    verify: realNameIcon,
    wechat: wechatIcon
}

const icon = iconMap[props.icon]

const userStore = useUserStore()
const token = userStore.token

function jump() {
    switch (props.icon) {
        case "complete":
            const url = `http://112.74.33.58:48110/#/content/resume` + `?token=` + token;
            window.location.href = url; // 直接跳转
            break;
        case "verify":
            router.push({ path: '/credit/identity'})
            break;
        case "browse":
            window.location.href = `http://112.74.33.58:48110/#/jobs`; // 直接跳转
            break;
        case "deliver":
            window.location.href = `http://112.74.33.58:48110/#/jobs`; // 直接跳转
            break;
        case "share":
            window.location.href = `http://112.74.33.58:48110/#/jobs`; // 直接跳转
            break;
        case "acept":
            window.location.href = `http://112.74.33.58:48110/#/messages`; // 直接跳转
            showUnderDevelopDialog()
            break;
        case "wechat":
            showUnderDevelopDialog()
            break;
        default:
            console.log("跳转不到任何地址");
    }
}
</script>

<style scoped lang="scss">
.item {
    display: flex;
    align-items: center;
    width: 375px;
    height: 60px;

    .left {
        width: 251px;
        height: 40px;
        margin-left: 16px;
        display: inline-flex;
        align-items: center;

        img {
            width: 40px;
            height: 40px;
        }

        .text {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            width: 131px;
            height: 33px;
            margin-left: 8px;

            .title {
                font-size: 14px;
                height: 17px;
                font-weight: 500;
            }

            .detail {
                height: 12px;
                font-size: 10px;
                color: #999999;
            }
        }

        .point-add {
            height: 14px;
            width: 76px;
            display: inline-flex;
            align-items: center;

            img {
                width: 12px;
                height: 12px;
            }

            .point-add-text {
                font-size: 12px;
                color: #FF751F;
                margin-left: 4px;
            }
        }
    }

    .right {
        display: inline-flex;
        margin-left: 12px;
    }
}

.uncomplete {
    width: 80px;
    height: 32px;
    background: #4177FC;
    color: #fff;
    border-radius: 18px 18px 18px 18px;
    border: none;
    font-size: 14px;
}

.complete {
    color: #fff;
    border-radius: 18px 18px 18px 18px;
    border: none;
    width: 80px;
    height: 32px;
    background: #DAE7FF;
    font-size: 14px;
}
</style>