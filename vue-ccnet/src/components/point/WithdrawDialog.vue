<template>
    <el-dialog v-model="showWithdraw" width="90%" :show-close="false" center class="no-blank-dialog withdrawl-dialog">

        <div class="withdrawl-amount-select">
            <div class="amount-select-text">
                <span class="withdrawal-amount-text">
                    选择提现金额
                </span>
                <span class="expiration-text">
                    有效期至2025-02-05
                </span>
            </div>
            <div class="amount-select-selector">
                <el-button class="selector-button" :type="amount === 100 ? 'primary' : 'default'"
                    @click="amount = 100">100元</el-button>
                <el-button class="selector-button" :type="amount === 200 ? 'primary' : 'default'"
                    @click="amount = 200">200元</el-button>
                <el-button class="selector-button" :type="amount === 500 ? 'primary' : 'default'"
                    @click="amount = 500">500元</el-button>
            </div>
            <div class="amount-select-customize">
                <el-input v-model="amount" placeholder="自定义提现金额" class="customize-input"
                    :class="{ 'is-error': amountError }" />
                <span v-if="amountError" class="error-message">{{ amountError }}</span>
                <div class="customize-text">
                    <span class="left">
                        提现金额为不小于100的整数
                    </span>
                    <span class="right">
                        积分兑换比例为1：100
                    </span>
                </div>
            </div>
        </div>
        <div class="choose-withdrawal-method">
            <span class="text">
                选择提现方式
            </span>
            <el-button class="selector-button" :type="withdrawType === '微信' ? 'primary' : 'default'"
                @click="withdrawType = '微信'">微信</el-button>
        </div>
        <div class="enter-withdrawal-account">
            <span class="text">
                输入提现账号
            </span>
            <el-input v-model="account" placeholder="请输入手机号码" class="customize-input"
                :class="{ 'is-error': accountError }" />
            <span v-if="accountError" class="error-message">{{ accountError }}</span>
        </div>
        <div class="withdrawal-instruction">
            <span class="top">
                提现说明
            </span>
            <div class="bottom">
                <span class="small-text">
                    1.一个账号一天只能提现一次
                </span>
                <span class="small-text">
                    2.提现后72小时内到账，若遇提现未到账的情况，请及时联系客服
                </span>
            </div>
        </div>
        <div class="submit">
            <el-button class="submit-buttom" block @click="submitWithdraw">立即提现到微信</el-button>
        </div>
    </el-dialog>

    <!-- 成功弹窗 -->
    <el-dialog v-model="showSuccessDialog" :show-close="false" center width="247px"
        class="no-blank-dialog withdrawal-success-dialog">
        <div class="top">
            <div class="title">
                <img src="../../assets/point-icons/ok.png" alt="">
                <span class="title-text">
                    提交成功，等待打款
                </span>
            </div>
            <span class="small-text">
                关注才财网公众号领取
            </span>
        </div>
        <div class="middle">
            <div class="amount-text">
                <span class="left">
                    {{ amount }}
                </span>
                <span class="right">
                    元
                </span>
            </div>
            <div class="acount-text">
                <span class="left-word">
                    收款账户
                </span>
                <span class="right-number">
                    {{ account }}
                </span>
            </div>
        </div>
        <el-button type="primary" class="bottom-buttom" @click="showSuccessDialog = false">确认</el-button>

    </el-dialog>

    <!-- 积分不足弹窗 -->
    <el-dialog v-model="showErrorDialog" :show-close="false" center class="no-blank-dialog withdrawal-fail-dialog">
        <span class="text">
            当前积分不足，暂时不能提现
        </span>
        <el-button @click="showErrorDialog = false" class="buttom">去做任务赚积分</el-button>
    </el-dialog>

</template>

<script setup lang="ts">
import { getUserPhoneNumber, withdraw } from '@/utils/api/point'
import { ApiResponse } from '@/utils/api/type'
import { showError } from '@/utils/toast'
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
    points: {
        type: [Number, null], // 允许 null
        default: 0,           // 或者给个默认值
        required: true
    }
})
const emit = defineEmits(['success'])

const showWithdraw = ref(false)
const amount = ref(100)
const amountError = ref('') // 金额错误信息
const withdrawType = ref('微信')
const account = ref('')
const accountError = ref('') // 账号错误信息


const showSuccessDialog = ref(false)
const showErrorDialog = ref(false)

// 验证金额是否大于100
function validateAmount() {
    if (!amount.value) {
        amountError.value = '请输入金额'
        return false;
    }
    if (amount.value < 100) {
        amountError.value = '请输入大于或等于100的金额'
        return false;
    }
    return true;
}

// 验证手机号
function validateAccount() {

    const phoneRegex = /^1[3-9]\d{9}$/
    if (!account.value) {
        accountError.value = '请输入手机号码'
        return false
    }
    if (!phoneRegex.test(account.value)) {
        accountError.value = '请输入正确的手机号码'
        return false
    }
    accountError.value = ''
    return true
}

// 提现按钮点击事件
function openWithdrawDialog() {
    showWithdraw.value = true
}

// 提交提现
async function submitWithdraw() {
    const accountRight = validateAccount();
    const amountRight = validateAmount();
    if (!accountRight || !amountRight) { //校验失败
        return
    }

    // 选择自定义金额优先
    let withdrawNeedPoints = amount.value * 100

    if (props.points !== null && props.points >= withdrawNeedPoints) {
        try {
            // 发起提现请求
            const res:ApiResponse = await withdraw(amount.value, account.value);

            // 可根据返回内容判断是否成功
            if (res.code === 200) {
                showWithdraw.value = false;
                showSuccessDialog.value = true;
                emit('success', { reducedPoints: withdrawNeedPoints });
            } else {
                // 处理提现失败
                showWithdraw.value = false;
                showErrorDialog.value = true;
                console.error('提现失败：', res.message);
            }
        } catch (error) {
            // （接口返回非 200）非200都算在异常中
            showWithdraw.value = false;
            showErrorDialog.value = true;
            console.error('请求异常：', error);
        }
    } else {
        showWithdraw.value = false;
        showErrorDialog.value = true;
    }
}

const fetchUserPhoneNumber = async () => {
    try {
        const res: ApiResponse = await getUserPhoneNumber()
        if (res.code === 200 && res.data) {
            account.value = res.data.phone
        }
    } catch (error) {
        showError('获取手机号码失败')
        console.error('获取手机号码失败:', error)
    } finally {
    }
}

defineExpose({
    openWithdrawDialog
})

onMounted(() => {
    fetchUserPhoneNumber()
})
</script>

<style lang="scss" scoped>
.header {
    display: flex;
    padding: 4px 0px;
    justify-content: center;
    align-items: center;
    gap: 4px;
    margin-bottom: 10px;

    img {
        width: 16px;
        height: 16px;
        flex-shrink: 0;
        aspect-ratio: 1/1;
    }

    .text {
        color: #A6C8FF;
        font-family: Inter;
        font-size: 12px;
        font-style: normal;
        font-weight: 500;
        line-height: 120%;
    }
}

.withdrawl-amount-select {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .amount-select-text {
        display: flex;
        align-items: baseline;
        gap: 4px;
        align-self: stretch;

        .withdrawal-amount-text {
            color: #333;
            font-size: 16px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
        }

        .expiration-text {
            color: #999;
            font-size: 10px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }

    .amount-select-selector {
        display: flex;
        align-items: center;
        gap: 14px;
        align-self: stretch;

        .selector-button {
            display: flex;
            width: 105px;
            height: 54px;
            padding: 10px;
            justify-content: center;
            align-items: center;
            gap: 10px;
            //字体
            font-size: 20px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }

    .amount-select-customize {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
        align-self: stretch;

        .customize-input {
            display: flex;
            align-items: center;
            gap: 10px;
            align-self: stretch;
        }

        .customize-text {
            display: flex;
            justify-content: space-between;
            align-items: center;
            align-self: stretch;

            span {
                color: #999;
                font-size: 10px;
                font-style: normal;
                font-weight: 400;
                line-height: 120%;
            }
        }
    }
}

.choose-withdrawal-method {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    padding: 10px 0 10px 0;

    .text {
        align-self: stretch;
        color: #333;
        font-size: 16px;
        font-style: normal;
        font-weight: 500;
        line-height: 120%;
        /* 19.2px */
    }

    .selector-button {
        display: flex;
        width: 106px;
        height: 54px;
        padding: 10px;
        justify-content: center;
        align-items: center;
        gap: 10px;
        //字体
        font-size: 20px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;
    }
}

.enter-withdrawal-account {
    display: flex;
    padding: 10px 0 10px 0;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .text {
        align-self: stretch;
        //字体
        color: #333;
        font-size: 16px;
        font-style: normal;
        font-weight: 500;
        line-height: 120%;
    }

    .customize-input {
        display: flex;
        align-items: center;
        gap: 10px;
        align-self: stretch;


    }

}

.withdrawal-instruction {
    display: flex;
    padding: 6px 0 10px 0;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;

    .top {
        color: #999;
        //字体
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 120%;


    }

    .bottom {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
        align-self: stretch;

        .small-text {
            align-self: stretch;
            //字体
            color: #999;
            font-size: 10px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
            /* 12px */
        }
    }
}

.submit {
    display: flex;
    padding: 10px 0 10px 0;
    flex-direction: column;
    align-items: flex-start;

    .submit-buttom {
        display: flex;
        height: 58px;
        padding: 8px 16px;
        justify-content: center;
        align-items: center;
        gap: 10px;
        align-self: stretch;
        border-radius: 40px;
        background: var(--primary-Color, #4177FC);
        //字体
        color: #FFF;
        font-size: 16px;
        font-style: normal;
        font-weight: 500;
        line-height: 120%;
        /* 19.2px */
    }
}

.top {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 4px;

    .title {
        display: flex;
        align-items: center;
        gap: 4px;

        img {
            width: 24px;
            height: 24px;
            aspect-ratio: 1/1;
        }

        .title-text {
            color: #333;
            font-family: Inter;
            font-size: 14px;
            font-style: normal;
            font-weight: 400;
            line-height: normal;
        }

    }

    .small-text {
        color: #666;
        font-size: 10px;
        font-style: normal;
        font-weight: 400;
        line-height: normal;
    }
}

.middle {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    align-self: stretch;

    .amount-text {
        align-self: stretch;
        display: flex;
        justify-content: center;

        .left {
            color: #333;
            text-align: center;
            font-size: 48px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
        }

        .right {
            color: #333;
            font-size: 14px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
            display: flex;
            align-items: flex-end;
            padding-bottom: 7px;
            margin-left: 2px;
        }
    }

    .acount-text {
        display: flex;
        align-items: center;
        gap: 16px;
        align-self: stretch;

        .left-word {
            color: #999;
            font-family: Inter;
            font-size: 14px;
            font-style: normal;
            font-weight: 400;
            line-height: normal;
        }

        .right-number {
            color: #333;
            text-align: right;
            font-size: 14px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%;
        }
    }


}

.bottom-buttom {
    display: flex;
    height: 36px;
    padding: 8px 16px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    border-radius: 25px;
    background: var(--primary-Color, #4177FC);
}

.error-message {
    color: #f56c6c;
    font-size: 12px;
    line-height: 1;
    padding-top: 4px;
    padding-bottom: 4px;
}
</style>

<style lang="scss">
.no-blank-dialog {
    .el-dialog__header {
        display: none;
    }
}

.withdrawl-dialog {
    border-radius: 16px;

    .is-error .el-input__wrapper {
        border-color: #f56c6c !important;
        box-shadow: 0 0 2px #f56c6c;
    }
}

.withdrawal-success-dialog {

    border-radius: 16px;
    padding: 32px;
    margin: 25vh auto;

    .el-dialog__body {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 16px;
    }
}

.withdrawal-fail-dialog {

    width: 247px;
    border-radius: 16px;
    padding: 32px;
    margin: 25vh auto;

    .el-dialog__body {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 32px;

        .text {
            color: #333;
            font-size: 14px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
        }

        .buttom {
            width: 143px;
            height: 36px;
            border-radius: 25px;
            background: var(--primary-Color, #4177FC);
            //字体
            color: #FFF;
            font-size: 16px;
            font-style: normal;
            font-weight: 500;
            line-height: 120%;
        }
    }
}
</style>