<script setup lang="ts">
import BetterJobItem from '@/components/better/BetterJobItem.vue';
import { ref, onMounted, onUnmounted } from 'vue';

// 响应式数据
const isSticky = ref(false);
const searchRef = ref<HTMLElement>();
const barRef = ref<HTMLElement>();
const imageRef = ref<HTMLElement>();

// 滚动处理函数
const handleScroll = () => {
    if (!barRef.value) return;

    const barBottom = barRef.value.getBoundingClientRect().top - 52;

    const windowHeight = window.innerHeight;

    // 当图片底部超出视窗顶部时，激活吸顶效果
    isSticky.value = barBottom <= 0;
};

// 组件挂载时添加滚动监听
onMounted(() => {
    window.addEventListener('scroll', handleScroll);
    // 初始化时也检查一次
    handleScroll();
});

// 组件卸载时移除滚动监听
onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
    <div class="container">
        <!-- 吸顶时的固定容器 -->
        <div v-if="isSticky" class="sticky-container">
            <div class="search sticky-search">
                <svg xmlns="http://www.w3.org/2000/svg" class="search-icon" viewBox="0 0 16 16" fill="none">
                    <path
                        d="M6.99992 12.6668C10.1295 12.6668 12.6666 10.1298 12.6666 7.00016C12.6666 3.87056 10.1295 1.3335 6.99992 1.3335C3.87032 1.3335 1.33325 3.87056 1.33325 7.00016C1.33325 10.1298 3.87032 12.6668 6.99992 12.6668Z"
                        stroke="#999999" stroke-linejoin="round" />
                    <path d="M11.074 11.0737L13.9024 13.9022" stroke="#999999" stroke-linecap="round"
                        stroke-linejoin="round" />
                </svg>
                <input type="text" class="search-input" placeholder="搜索职位/公司">
            </div>
            <div class="bar sticky-bar">
                <div class="top-bar">
                    <div class="left">
                        <!-- v-if 循环 -->
                        <span class="selected">仓库文员</span>
                        <span class="not-selected">活动策划执行</span>
                        <span class="not-selected">会展/会展活动销售经理</span>
                        <span class="not-selected">销售</span>
                    </div>
                    <div class="right">
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
                            <path d="M6 1.93213V11.0681" stroke="#666666" stroke-linecap="round"
                                stroke-linejoin="round" />
                            <path d="M1.43213 6.5H10.5681" stroke="#666666" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                        <span class="add">
                            添加
                        </span>
                    </div>
                </div>
                <div class="middle-bar">
                    <div class="left">
                        <span class="middle-selected">推荐</span>
                        <span class="middle-not-selected">最新</span>
                    </div>
                    <div class="right">
                        <div class="all">
                            <span>
                                全部
                            </span>
                            <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 16 17" fill="none">
                                <path d="M12 6.5L8 10.5L4 6.5" stroke="#666666" stroke-linecap="round"
                                    stroke-linejoin="round" />
                            </svg>
                        </div>
                        <div class="filter">
                            <span>
                                筛选
                            </span>
                            <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 16 17" fill="none">
                                <path d="M12 6.5L8 10.5L4 6.5" stroke="#666666" stroke-linecap="round"
                                    stroke-linejoin="round" />
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 原始内容 -->
        <div class="fix-top" v-if="!isSticky">
            <div class="search ori-search" ref="searchRef">
                <svg xmlns="http://www.w3.org/2000/svg" class="search-icon" viewBox="0 0 16 16" fill="none">
                    <path
                        d="M6.99992 12.6668C10.1295 12.6668 12.6666 10.1298 12.6666 7.00016C12.6666 3.87056 10.1295 1.3335 6.99992 1.3335C3.87032 1.3335 1.33325 3.87056 1.33325 7.00016C1.33325 10.1298 3.87032 12.6668 6.99992 12.6668Z"
                        stroke="#999999" stroke-linejoin="round" />
                    <path d="M11.074 11.0737L13.9024 13.9022" stroke="#999999" stroke-linecap="round"
                        stroke-linejoin="round" />
                </svg>
                <input type="text" class="search-input" placeholder="搜索职位/公司">
            </div>
        </div>


        <img src="../../assets/better-images/01.png" alt="" class="page-image" ref="imageRef">

        <div class="bar" ref="barRef">
            <div class="top-bar">
                <div class="left">
                    <!-- v-if 循环 -->
                    <span class="selected">仓库文员</span>
                    <span class="not-selected">活动策划执行</span>
                    <span class="not-selected">会展/会展活动销售经理</span>
                    <span class="not-selected">销售</span>
                </div>
                <div class="right">
                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
                        <path d="M6 1.93213V11.0681" stroke="#666666" stroke-linecap="round" stroke-linejoin="round" />
                        <path d="M1.43213 6.5H10.5681" stroke="#666666" stroke-linecap="round"
                            stroke-linejoin="round" />
                    </svg>
                    <span class="add">
                        添加
                    </span>
                </div>
            </div>
            <div class="middle-bar">
                <div class="left">
                    <span class="middle-selected">推荐</span>
                    <span class="middle-not-selected">最新</span>
                </div>
                <div class="right">
                    <div class="all">
                        <span>
                            全部
                        </span>
                        <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 16 17" fill="none">
                            <path d="M12 6.5L8 10.5L4 6.5" stroke="#666666" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                    </div>
                    <div class="filter">
                        <span>
                            筛选
                        </span>
                        <svg xmlns="http://www.w3.org/2000/svg" class="icon" viewBox="0 0 16 17" fill="none">
                            <path d="M12 6.5L8 10.5L4 6.5" stroke="#666666" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                    </div>
                </div>
            </div>
        </div>

        <div class="job-list">
            <!-- v-for 循环 -->
            <BetterJobItem v-for="(jobItem, index) in 10"></BetterJobItem>
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
}

/* 吸顶容器样式 */
.sticky-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background-color: #f8f8f8;
    padding: 8px 0;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.sticky-search {
    margin: 0 16px 8px 16px;
    background: #FFF;
    border-radius: 24px;
    padding: 8px 12px;
    height: 36px;
    display: flex;
    align-items: center;
    gap: 4px;
}

.sticky-bar {
    padding: 0 16px 12px 16px;
    background: #f8f8f8;
}

.search {
    display: flex;
    height: 36px;
    padding: 8px 12px;
    margin: 0px 16px 8px 16px;
    align-items: center;
    gap: 4px;
    border-radius: 24px;
    background: #FFF;

    .search-icon {
        width: 16px;
        height: 16px;
    }

    .search-input {
        flex: 1;
        border: none;
        outline: none;
    }

    .search-input::placeholder {
        font-size: 14px;
    }
}

.page-image {
    width: 343px;
    height: 140px;
    aspect-ratio: 49/20;
    border-radius: 8px;
    margin: 58px auto 0px auto;
    object-fit: cover;
    object-position: center 10%;
}

.bar {
    display: flex;
    padding: 12px 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;

    .top-bar {
        display: flex;
        align-items: center;
        gap: 8px;
        align-self: stretch;

        .left {
            display: flex;
            align-items: center;
            gap: 16px;
            flex: 1 0 0;
            overflow-x: scroll;
            overflow-y: hidden;
            white-space: nowrap;

            .selected {
                color: #333;
                font-size: 16px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }

            .not-selected {
                color: #666;
                font-size: 14px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }
        }

        .right {
            display: flex;
            align-items: center;
            gap: 4px;

            .add {
                color: #666;
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }
        }
    }

    .middle-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        align-self: stretch;

        .left {
            display: flex;
            align-items: baseline;
            gap: 8px;

            .middle-selected {
                color: #333;
                font-size: 14px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }

            .middle-not-selected {
                color: #666;
                font-size: 14px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }
        }

        .right {
            display: flex;
            align-items: center;
            gap: 8px;

            color: #666;
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;

            .all {
                display: flex;
                align-items: center;
            }

            .filter {
                display: flex;
                align-items: center;
            }

            .icon {
                width: 16px;
                height: 17px;
            }
        }
    }
}

.fix-top {
    background-color: #f8f8f8;
    width: 375px;
    position: fixed;
    padding-top: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.ori-search {}

.job-list {
    display: flex;
    padding: 0px 16px;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    align-self: stretch;
}
</style>