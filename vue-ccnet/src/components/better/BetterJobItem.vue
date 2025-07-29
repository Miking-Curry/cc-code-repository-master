<template>
    <div class="job-card">
        <div class="top">
            <div class="title">
                <div class="left">
                    <span class="title-text">
                        仓管文员
                    </span>
                    <!-- <div v-for="(statusDesc, index) in jobItem.postStatusDescs" :key="index"
                        :class="getStatusClass(statusDesc)">
                        {{ statusDesc }}
                    </div> -->
                </div>
                <span class="right">
                    3000-5000元
                </span>
            </div>

            <div class="tags">
                <!-- v-if 循环 -->
                <span class="tag">
                    经验不限
                </span>
            </div>
            <div class="desc">
                <!-- v-if 循环 -->
                <span class="text">
                    东莞市沃佳贸易有限公司
                </span>
            </div>
        </div>

        <div class="middle">
            <div class="company">
                <img src="" alt="" class="avatar">
                <span class="human-resources-company-name">
                    成樑人力资源有限公司
                </span>
            </div>
            <div class="bottom">
                <span class="publish-time">
                    13天前发布
                </span>
                <div class="place">
                    <!-- v-if 循环 -->
                    <span class="place-text">
                        东莞
                    </span>
                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 10 10" fill="none">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                            d="M5.00081 4.5595L2.42032 1.979L1.97998 2.41934L4.56047 4.99984L1.97998 7.58033L2.42032 8.02067L5.00081 5.44018L7.58131 8.02067L8.02165 7.58033L5.44115 4.99984L8.02165 2.41934L7.58131 1.979L5.00081 4.5595Z"
                            fill="#999999" />
                    </svg>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { BetterJobItem } from '@/utils/api/type';

type Props = {
    jobItem: BetterJobItem;
};

// const props = defineProps<Props>()

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
.job-card {
    display: flex;
    padding: 12px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;

    border-radius: 8px;
    background: #FFF;

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
            }

            .right {
                color: var(--primary-Color, #4177FC);
                font-family: DIN;
                font-size: 18px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }


        }

        .tags {
            display: flex;
            align-items: center;
            gap: 8px;

            .tag {
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
            }
        }

        .desc {
            display: flex;
            align-items: center;
            gap: 8px;

            .text {
                color: #666;
                font-size: 14px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }
        }
    }

    .middle {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
        align-self: stretch;

        .company {
            display: flex;
            width: 198px;
            align-items: center;
            gap: 4px;

            .avatar {
                width: 16px;
                height: 16px;
                flex-shrink: 0;
                aspect-ratio: 1/1;

                border-radius: 16px;
                background: url(<path-to-image>) lightgray 50% / cover no-repeat;
            }

            .human-resources-company-name {
                color: #333;
                font-family: PingFangSC;
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
                /* 14.4px */

                display: flex;
                align-items: flex-start;
                gap: 4px;
                flex-shrink: 0;
            }
        }

        .bottom {
            display: flex;
            justify-content: space-between;
            align-items: center;
            align-self: stretch;

            .publish-time {
                color: #999;
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }

            .place {
                display: flex;
                align-items: center;
                gap: 4px;

                .place-text {
                    color: #999;
                    font-size: 12px;
                    font-style: normal;
                    font-weight: 400;
                    line-height: 120%;
                }
            }
        }
    }
}

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
</style>