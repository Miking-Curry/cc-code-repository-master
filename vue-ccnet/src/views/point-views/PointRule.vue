<script setup lang="ts">

import { useRouter } from 'vue-router'

const router = useRouter()

function goBack() {
    router.back()
}

</script>

<template>
    <div class="container">
        <div class="top">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none" @click="goBack">
                <path d="M11.9993 15.3327L4.66602 7.99935L11.9993 0.666016" stroke="white" stroke-linecap="round"
                    stroke-linejoin="round" />
            </svg>
        </div>
        <img src="../../assets/point-icons/point-rule-background.png" alt="" class="background">
        <div class="content">
            <div class="rule">
                <div class="top-text">
                    参与活动前，请用户详细阅读活动规则及相关条款。凡参与本活动，则视为用户已阅读、理解并同意活动规则全部内容。
                </div>
                <div class="middle-text">
                    <span class="title">
                        一、活动参与对象
                    </span>
                    <span class="text">
                        凡是登录手机端的用户皆可参与积分活动
                    </span>
                </div>
                <div class="below-text">
                    <span class="title">
                        二、活动规则
                    </span>
                    <div class="rule-list">
                        <span class="text">
                            1.完善个人简介加100积分
                        </span>
                        <span class="text">
                            2.完成实名认证加20积分
                        </span>
                        <span class="text">
                            3.浏览岗位5秒以上加5积分
                        </span>
                        <span class="text">
                            4.投递一个岗位加10积分
                        </span>
                        <span class="text">
                            5.接受面试邀请加10积分
                        </span>
                        <span class="text">
                            6.分享职位信息加10积分
                        </span>
                        <span class="text">
                            7.绑定微信加20积分
                        </span>
                        <span class="text">
                            8.积分抵现比例：100积分=1元，满10000积分即可进行抵现
                        </span>
                    </div>
                </div>
            </div>
            <div class="bottom-text">
                *活动最终解释权归东莞才财网络科技公司所有，活动过程中有任何问题请后台进行反馈提交，我们会在48小时内进行处理。
            </div>
        </div>
    </div>
</template>

<style scoped>
.container {
    width: 100%;
    height: 100%;
    background-color: #fff;
}

.top {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 100;

    display: flex;
    height: 34px;
    padding: 0px 16px;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;
}

.background {
    width: 100%;
}

.content {
    display: flex;
    height: 511px;
    padding: 0px 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 37px;
    align-self: stretch;
}

.rule {
    display: flex;
    height: 420px;
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
    flex-shrink: 0;
    align-self: stretch;
}

.bottom-text {
    flex: 1 0 0;
    align-self: stretch;
    color: #666;
    font-family: PingFangSC;
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 150%;
    /* 18px */
}

.top-text {
    align-self: stretch;
    color: #666;
    font-family: PingFangSC;
    font-size: 13px;
    font-style: normal;
    font-weight: 600;
    line-height: 150%;
    /* 19.5px */
}

.middle-text {
    display: flex;
    height: 49px;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
    flex-shrink: 0;
    align-self: stretch;
}

.title {
    color: #333;
    font-family: PingFangSC;
    font-size: 16px;
    font-style: normal;
    font-weight: 600;
    line-height: 150%;
    /* 24px */
    align-self: stretch;
}

.text {
    color: #666;
    font-family: PingFangSC;
    font-size: 13px;
    font-style: normal;
    font-weight: 400;
    line-height: 150%;
    /* 19.5px */
    align-self: stretch;
}

.below-text {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
    align-self: stretch;
}

.rule-list {
    display: flex;
    padding: 0px 12px;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    align-self: stretch;
}
</style>