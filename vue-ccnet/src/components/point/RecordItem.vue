<!-- RecordItem.vue -->
<template>
    <div class="item">
        <div class="item-detail">
            <div class="item-text">
                <span class="title">
                    {{ title }}
                </span>
                <span class="time">
                    {{ time }}
                </span>
            </div>
            <div class="add-point">
                <img src="../../assets/point-icons/small-coin.png" alt="">
                <span :class="statu === 'add' ? 'add' : 'minus'">
                    {{ (statu === "add" ? "+" : "-") + point + "积分" }}
                </span>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">

const props = defineProps<{
    title: string
    time: string
    point: number
    statu: string
}>() 
</script>

<style scoped lang="scss">
.item {
    display: flex;
    padding: 10px 16px;
    align-items: center;
    gap: 12px;
    align-self: stretch;

    .item-detail {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex: 1 0 0;

        .item-text {
            display: flex;
            width: 249px;
            flex-direction: column;
            align-items: flex-start;
            gap: 4px;

            .title {
                width: 249px;
                color: #333;
                font-size: 14px;
                font-style: normal;
                font-weight: 500;
                line-height: 120%;
            }

            .time {
                display: flex;
                align-items: flex-start;
                gap: 8px;
                color: #999;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }
        }

        .add-point {
            display: flex;
            width: 76px;
            justify-content: flex-end;
            align-items: center;
            gap: 4px;

            img {
                width: 12px;
                height: 12px;
                flex-shrink: 0;
                aspect-ratio: 1/1;
            }
        }
    }
}

.add {
    //字体
    color: #FF751F;
    font-family: PingFangSC;
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 120%;
}

.minus {
    color: #FF1F1F;
    font-family: PingFangSC;
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 120%;
    white-space: nowrap;
    /* 14.4px */
}
</style>