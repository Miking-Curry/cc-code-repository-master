<!-- MissionItem.vue -->
<template>
    <div class="item">
        <div class="left">
            <div class="icon-title">
                <div class="icon">
                    <img :src="icon" alt="">
                </div>
                <span class="title">
                    {{title}}
                </span>
            </div>
            <div class="add-text">
                +{{point}}分
            </div>
        </div>
        <button class="done-button" v-if="completed">
            已完成
        </button>
        <button class="go-button" @click="goToTargetPage" v-else>
            去完成
        </button>
    </div>
</template>

<script setup lang="ts">
import idCardIcon from '@/assets/credit-icons/idcard.png'
import graduationIcon from '@/assets/credit-icons/graduate.png'
import workIcon from '@/assets/credit-icons/work.png'
import resignIcon from '@/assets/credit-icons/resign.png'
import skillIcon from '@/assets/credit-icons/skill.png'

// 添加跳转路由
import { useRouter } from 'vue-router'

const router = useRouter()

function goToTargetPage() {
    if(props.icon === 'idCard'){
        router.push('/credit/identity')
    } else if (props.icon === 'graduate'){
        router.push('/credit/graduation')
    } else if (props.icon === 'work') {
        router.push('/credit/work')
    } else if (props.icon === 'resign') {
        router.push('/credit/resign')
    } else if (props.icon === 'skill') {
        router.push('/credit/skill')
    }
}

const props = defineProps<{
    title: string
    point: number
    completed: boolean
    icon: string
}>()

// 根据 iconName 动态选择图片
const iconMap: Record<string, string> = {
    idCard: idCardIcon,
    graduate: graduationIcon,
    work: workIcon,
    resign: resignIcon,
    skill: skillIcon
}

const icon = iconMap[props.icon]    
</script>

<style scoped lang="scss">
.item {
    display: flex;
    width: 343px;
    padding: 10px 16px;
    align-items: center;
    gap: 12px;
    align-self: stretch;

    .left {
        display: flex;
        align-items: center;
        gap: 4px;
        flex: 1 0 0;

        .icon-title {
            display: flex;
            align-items: center;

            .icon {
                width: 40px;
                height: 40px;
            }

            .title {
                color: #333;
                font-size: 14px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }
        }

        .add-text {
            display: flex;
            width: 76px;
            align-items: center;
            gap: 4px;

            color: #FF751F;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }

    .done-button {
        display: flex;
        height: 32px;
        padding: 6px 16px;
        justify-content: center;
        align-items: center;
        gap: 10px;

        border: none;
        border-radius: 18px;
        background: var(--butn-background-Color, #DAE7FF);
        color: #fff
    }

    .go-button {
        display: flex;
        width: 80px;
        height: 32px;
        padding: 6px 16px;
        justify-content: center;
        align-items: center;
        gap: 10px;

        border: none;
        color: #fff ;
        border-radius: 18px;
        background: var(--primary-Color, #4177FC);
    }
}
</style>