<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import MissionItem from '@/components/point/MissionItem.vue'
import RecordItem from '@/components/point/RecordItem.vue'
import WithdrawDialog from '@/components/point/WithdrawDialog.vue'
import { getTaskList, getUserPoint, getUserPointRecord, getUserTodayPoint, getUserTotalPoint } from '@/utils/api/point'
import { ApiResponse, Task, PointRecord, TodayPointResponse, TotalPointResponse } from '@/utils/api/type'
import { useGoBack } from '@/utils/goBack'
import { showError } from '@/utils/toast'
import router from '@/router'

const { goBacktoUserPage } = useGoBack()

interface DisplayRecord {
    title: string
    time: string
    point: number
    statu: string
}

interface Mission {
    id: number
    title: string
    detail: string
    point: number
    completed: boolean
    icon: string
}

const showLeft = ref(true)
const showRight = computed(() => !showLeft.value)

const withdrawDialogRef = ref<InstanceType<typeof WithdrawDialog> | null>(null)
const points = ref<number>(0) // 当前积分
const todayPoints = ref<number | null>(null) // 今日获得积分
const totalPoints = ref<number | null>(null) //累计获得积分
const loading = ref(false)

const missionRef = ref<HTMLElement | null>(null);
const itemCount = ref(7); // 默认 5 个骨架条


function openWithdrawDialog() {
    if (withdrawDialogRef.value) {
        withdrawDialogRef.value.openWithdrawDialog()
    }
}

const fetchPoints = async () => {
    try {
        const res: ApiResponse<number> = await getUserPoint()
        if (res.code === 200 && res.data) {
            points.value = res.data
        }
    } catch (error) {
        showError('获取用户积分失败')
        console.error('获取用户积分失败:', error)
    }
}

const fetchTodayPoints = async () => {
    try {
        const res: TodayPointResponse = await getUserTodayPoint()
        if (res.code === 200 && res.data) {
            todayPoints.value = res.data.pointCount
        }
    } catch (error) {
        showError('获取用户今日积分失败')
        console.error('获取用户今日积分失败:', error)
    }
}

const fetchTotalPoints = async () => {
    try {
        const res: TotalPointResponse = await getUserTotalPoint()
        if (res.code === 200 && res.data) {
            totalPoints.value = res.data.pointCount
        }
    } catch (error) {
        showError('获取用户累计积分失败')
        console.error('获取用户累计积分失败:', error)
    }
}

// 获取可完成的任务
const missions = ref<Mission[]>([])
const fetchTasks = async () => {
    try {
        loading.value = true
        const res: ApiResponse<Task[]> = await getTaskList()
        if (res.code === 200 && res.data) {
            missions.value = res.data.map(task => ({
                id: task.id,
                title: task.title,
                detail: task.description,
                point: task.rewardPoint,
                completed: !task.canDo,
                icon: getTaskIcon(task.id)
            }))
        }
        if (missions.value.length === 0 || missions.value === null) {
            missions.value = []
            loading.value = false
        }
    } catch (error) {
        showError('获取任务列表失败')
        console.error('获取任务列表失败:', error)
    } finally {
        loading.value = false
    }
}

const iconMap = {
    1: 'complete',
    2: 'verify',
    3: 'browse',
    4: 'deliver',
    5: 'acept',
    6: 'share',
    7: 'wechat'
} as const

// 根据任务ID获取对应的图标
const getTaskIcon = (taskId: keyof typeof iconMap | number): string => {
    return iconMap[taskId as keyof typeof iconMap] || 'default'
}

//获取积分记录
const records = ref<DisplayRecord[]>([])
const fetchRecords = async () => {
    try {
        const res: ApiResponse<PointRecord[]> = await getUserPointRecord()
        if (res.code === 200 && res.data) {
            records.value = res.data.map(record => ({
                title: record.eventDescription,
                time: record.changeTime || '',
                point: record.changePoint,
                statu: record.changeType === '积分收入' ? 'add' : 'minus'
            })).sort((a, b) => {
                const timeA = a.time ? new Date(a.time).getTime() : 0
                const timeB = b.time ? new Date(b.time).getTime() : 0
                return timeB - timeA
            })
        }
    } catch (error) {
        showError('获取积分记录失败')
        console.error('获取积分记录失败:', error)
    }
}

function goToPointRule() {
    router.push('/point/point-rule');
}

onMounted(() => {
    fetchTasks()
    fetchPoints()
    fetchRecords()
    fetchTodayPoints()
    fetchTotalPoints()
})
</script>

<template>
    <div class="head">
        <!-- <TopBar title="活动中心" /> -->
        <!-- <img src="../assets/point-icons/proportion.png" alt="" class="proportion"> -->

        <div class="rule-tap">
            <van-icon name="arrow-left" size="16px" color="#fff" style="margin-left: 16px;" @click="goBacktoUserPage" />
            <div class="rule" @click="goToPointRule">
                <span>规则</span>
                <img src="../../assets/point-icons/help.png" alt="">
            </div>
        </div>
        <div class="point">
            <div class="point-detail">
                <span class="point-text">任务积分</span>
                <span class="point-number" v-if="points != null">
                    {{ points }}
                    <img src="../../assets/point-icons/proportion.png" alt="" class="proportion">
                </span>
                <el-skeleton style="width: 100%;height: 50px;display: flex;align-items: center;" animated v-else>
                    <template #template>
                        <el-skeleton-item variant="p" style="width: 50%;height: 30px;" />
                    </template>
                </el-skeleton>
                <span class="today-and-total-point">
                    今日积分：{{ todayPoints }}|累计积分：{{ totalPoints }}
                </span>
            </div>
            <div class="point-button">
                <button class="withdrawal-icon" @click="openWithdrawDialog">
                    立即提现
                </button>
            </div>
        </div>
        <img src="../../assets/point-icons/coin.png" alt="" class="coin">
    </div>
    <img v-if="showLeft" src="../../assets/point-icons/left@2x.png" alt="" class="switch-tap">
    <img v-else-if="showRight" src="../../assets/point-icons/right@2x.png" alt="" class="switch-tap">
    <div class="middle">
        <div class="middle-tap">
            <span class="left-tap tap" @click="showLeft = true" :class="{ active: showLeft }">
                日常任务
            </span>
            <span class="right-tap tap" @click="showLeft = false" :class="{ active: showRight }">
                积分记录
            </span>
        </div>
        <div class="mission-list" v-if="showLeft" ref="missionRef">
            <!-- 加载中 -->
            <el-skeleton style="width: 100%" animated v-if="loading || missions.length === 0">
                <template #template>
                    <div style="padding: 20px">
                        <el-skeleton-item variant="p" style="width: 50%" />
                        <el-skeleton-item variant="text" style="width: 100%;margin-top: 25px" v-for="i in itemCount"
                            :key="i" />
                    </div>
                </template>
            </el-skeleton>
            <MissionItem v-for="(mission, index) in missions" :key="index" v-bind="mission" v-else />
        </div>
        <div class="record-list" v-else-if="showRight">
            <!-- 加载中 -->
            <el-skeleton style="width: 100%" animated v-if="loading">
                <template #template>
                    <div style="padding: 20px">
                        <el-skeleton-item variant="p" style="width: 50%" />
                        <el-skeleton-item variant="text" style="width: 100%;margin-top: 25px" v-for="i in itemCount"
                            :key="i" />
                    </div>
                </template>
            </el-skeleton>
            <RecordItem v-for="(record, index) in records" :key="index" v-bind="record" />
            <div class="noRecord" v-if="records.length === 0">
                你尚未一次积分记录！
            </div>
        </div>

        <div class="footer" v-if="showLeft">
            <span class="footer-text">
                *每日日常任务，当天凌晨12：01刷新
            </span>
        </div>
        <div class="footer" v-else-if="showRight">
            <span class="footer-text">
                *积分记录，实时刷新
            </span>
        </div>
    </div>

    <WithdrawDialog ref="withdrawDialogRef" :points="points" @success="points -= $event.reducedPoints" />
</template>

<style scoped>
.blank {
    height: 88px;
    width: 375px;
}

.head {
    width: 375px;
    height: 226px;
    background: linear-gradient(to top, rgb(255, 255, 255), #5c8dfd);
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    position: relative;
}


.coin {
    height: 158px;
    width: 167px;
    float: right;
    position: absolute;
    top: 34px;
    left: 208px;
    z-index: 0;
}

.rule-tap {
    display: flex;
    width: 375px;
    height: 34px;
    justify-content: space-between;
    align-items: center;

    .rule {
        margin-right: 16px;
        display: flex;
        justify-content: center;
        align-items: center;

        span {
            font-size: 12px;
            color: #fff;
            margin-right: 4px;
            line-height: 34px;
            text-align: center;
        }

        img {
            width: 11.67px;
            height: 11.67px;
        }
    }
}

.point {
    display: inline-flex;
    flex-direction: column;
    height: 123px;
    width: 140px;
    margin-top: 8px;
    margin-left: 16px;
    color: #fff;

    .point-detail {
        display: flex;
        flex-direction: column;
        height: 76px;
        width: 140px;

        .point-text {
            height: 14px;
            width: 140px;
            font-size: 16px;
            font-weight: 500;
            display: flex;
            align-items: center;
        }

        .point-number {
            height: 50px;
            font-size: 40px;
            line-height: 120%;
            text-align: left;
            display: inline-flex;
            align-items: center;
            font-weight: 500;
            width: fit-content;
            position: relative;

            .proportion {
                position: absolute;
                width: 70px;
                height: 18.75px;
                right: -75px;
                top: 5px;
            }
        }

        .today-and-total-point {
            width: 140px;
            height: 12px;
            font-size: 12px;
            color: rgba(255, 255, 255, 0.6);
            white-space: nowrap;
        }
    }

    .point-button {
        margin-top: 8px;

        .withdrawal-icon {
            height: 39px;
            width: 104px;
            border-radius: 20px;
            color: #333333;
            border: none;
        }
    }
}

.switch-tap {
    position: absolute;
    top: 180px;
    width: 375px;
    height: 487px;
}

.middle {
    background-color: transparent;
    width: 375px;
    display: flex;
    position: absolute;
    flex-direction: column;
    top: 180px;
    min-height: calc(100vh - 180px - 8px);
    padding-bottom: 8px;
}

.middle-tap {
    height: 46px;
    width: 375px;
    background-color: transparent;

    .tap {
        display: inline-block;
        width: 187px;
        height: 46px;
        line-height: 46px;
        text-align: center;
        font-size: 16px;
        color: #999999;

    }

    .active {
        color: #000000;
    }
}

.mission-list {
    margin-top: 10px;
    width: 375px;
    display: flex;
    flex-direction: column;
    flex: 1;
}

.record-list {
    margin-top: 10px;
    width: 375px;
    display: flex;
    flex-direction: column;
    flex: 1;
}

.footer {
    height: 30px;
    width: 375px;
    display: flex;
    align-items: center;
    margin-top: auto;

    .footer-text {
        color: #999999;
        font-size: 12px;
        font-weight: 400;
        margin-left: 10px;
    }
}

.noRecord {
    height: 300px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

}
</style>