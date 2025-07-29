<template>
  <div class="work-exp-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <el-icon class="back-icon" @click="goBack">
        <ArrowLeft />
      </el-icon>
      <span class="page-title">毕业信息</span>
      <span class="save-btn" @click="saveInfo">保存</span>
    </div>

    <!-- 表单内容 -->
    <div class="form-list">
      <!--入职时间-->
      <div>
        <div class="form-item">
          <span class="item-label">入职时间<span style="color: red">*</span></span>
        </div>
        <div class="result-row" @click="showStartDatePicker = true">
          <el-input v-model="form.joinAt" placeholder="请选择入职时间" class="no-border-input" readonly
            @click="showStartDatePicker = true" />
          <span class="error-message" v-if="enterDateErrorMessage">
            {{ enterDateErrorMessage }}
          </span>
        </div>
      </div>

      <!-- 离职时间 -->
      <div>
        <div class="form-item">
          <span class="item-label">离职时间<span style="color: red">*</span></span>
        </div>
        <div class="result-row" @click="showEndDatePicker = true">
          <el-input v-model="form.resignAt" placeholder="请选择离职时间" class="no-border-input" readonly
            @click="showEndDatePicker = true" />
          <span class="error-message" v-if="leaveDateErrorMessage">
            {{ leaveDateErrorMessage }}
          </span>
        </div>
      </div>

      <!-- 离职原因 -->
      <div>
        <div class="form-item">
          <span class="item-label">离职原因<span style="color: red">*</span></span>
        </div>
        <div class="result-row">
          <el-input v-model="form.resignReason" placeholder="输入内容" class="no-border-input" />
          <span class="error-message" v-if="leaveReasonErrorMessage">
            {{ leaveReasonErrorMessage }}
          </span>
        </div>
      </div>

      <!-- 证明人姓名 -->
      <div>
        <div class="form-item">
          <span class="item-label">证明人姓名
            <!-- <span style="color: red">*</span> -->
          </span>
          <span class="item-tip">请填写证明人姓名</span>
        </div>

        <div class="result-row">
          <el-input v-model="form.witnessName" placeholder="输入内容" class="no-border-input" />
          <span class="error-message" v-if="witnessNameErrorMessage">
            {{ witnessNameErrorMessage }}
          </span>
        </div>
      </div>

      <!-- 证明人电话 -->
      <div>
        <div class="form-item">
          <span class="item-label">证明人电话
            <!-- <span style="color: red">*</span> -->
          </span>
          <span class="item-tip">请填写证明人电话</span>
        </div>
        <div class="result-row">
          <el-input v-model="form.witnessPhone" placeholder="输入内容" class="no-border-input" />
          <span class="error-message" v-if="witnessPhoneErrorMessage">
            {{ witnessPhoneErrorMessage }}
          </span>
        </div>
      </div>
    </div>

    <!-- 入职日期选择器 -->
    <el-dialog v-model="showStartDatePicker" :show-close="false" width="100%" top="auto" append-to-body :modal="true"
      :close-on-click-modal="true" :lock-scroll="false" class="date-picker-dialog">
      <template #header></template>
      <div class="picker-list">
        <el-date-picker v-model="form.joinAt" type="month" placeholder="选择日期" format="YYYY年MM月" value-format="YYYY年MM月"
          @change="showStartDatePicker = false"></el-date-picker>
        <div class="picker-cancel" @click="showStartDatePicker = false">取消</div>
      </div>
    </el-dialog>

    <!-- 离职日期选择器 -->
    <el-dialog v-model="showEndDatePicker" :show-close="false" width="100%" top="auto" append-to-body :modal="true"
      :close-on-click-modal="true" :lock-scroll="false" class="date-picker-dialog">
      <template #header></template>
      <div class="picker-list">
        <el-date-picker v-model="form.resignAt" type="month" placeholder="选择日期" format="YYYY年MM月"
          value-format="YYYY年MM月" @change="showEndDatePicker = false"></el-date-picker>
        <div class="picker-cancel" @click="showEndDatePicker = false">取消</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';
import { ElDatePicker } from 'element-plus';
import { deleteResignInformation, getResignInformation, saveResignInformation } from '@/utils/api/credit';
import { ResignInformationResponse } from '@/utils/api/type';
import { showError, showSuccess } from '@/utils/toast';

const router = useRouter();
interface ResignForm {
  joinAt: string;
  resignAt: string;
  resignReason: string;
  witnessName: string;
  witnessPhone: string;
}
const form = ref<ResignForm>({
  joinAt: '',
  resignAt: '',
  resignReason: '',
  witnessName: '',
  witnessPhone: ''
});
const showEndDatePicker = ref<boolean>(false);
const showStartDatePicker = ref<boolean>(false);

function goBack() {
  router.go(-1);
}

const enterDateErrorMessage = ref<string | null>('')
const leaveDateErrorMessage = ref<string | null>('')
const leaveReasonErrorMessage = ref<string | null>('')
const witnessNameErrorMessage = ref<string | null>('')
const witnessPhoneErrorMessage = ref<string | null>('')

async function saveInfo() {
  enterDateErrorMessage.value = ''
  leaveDateErrorMessage.value = ''
  leaveReasonErrorMessage.value = ''
  witnessNameErrorMessage.value = ''
  witnessPhoneErrorMessage.value = ''
  // 表单验证
  if (!form.value.joinAt) {
    enterDateErrorMessage.value = '请选择入职时间'
    return;
  }

  if (!form.value.resignAt) {
    leaveDateErrorMessage.value = '请选择离职时间'
    return;
  }

  // 新增：入职日期不能晚于离职日期
  // 只比较年月，格式为 'YYYY年MM月'
  const join = form.value.joinAt.replace('年', '-').replace('月', '');
  const resign = form.value.resignAt.replace('年', '-').replace('月', '');
  if (join > resign) {
    enterDateErrorMessage.value = '入职日期不能晚于离职日期';
    leaveDateErrorMessage.value = '入职日期不能晚于离职日期';
    return;
  }

  if (!form.value.resignReason) {
    leaveReasonErrorMessage.value = '请填写离职原因'
    return;
  }

  // if (!form.value.witnessName) {
  //   witnessNameErrorMessage.value = '请填写证明人姓名'
  //   return;
  // }

  // if (!form.value.witnessPhone) {
  //   witnessPhoneErrorMessage.value = '请填写证明人电话'
  //   return;
  // }

  try {
    await deleteResignInformation()
    await saveResignInformation(form.value.joinAt, form.value.resignAt, form.value.resignReason, form.value.witnessName, form.value.witnessPhone)
    showSuccess('保存成功')
    console.log('保存成功');
    router.push("/credit");
  } catch (error) {
    showError('保存失败')
    console.error('保存失败:', error);
  }
}

const fetchResignInformation = async () => {
  try {
    const res: ResignInformationResponse = await getResignInformation()
    if (res.code === 200 && res.data) {
      form.value.joinAt = res.data.joinAt
      form.value.resignAt = res.data.resignAt
      form.value.resignReason = res.data.resignReason
      form.value.witnessName = res.data.witnessName
      form.value.witnessPhone = res.data.witnessPhone
    }
  } catch (error) {
    showError('获取离职信息失败')
    console.error('获取离职信息失败:', error)
  } finally {
  }
}

onMounted(() => {
  fetchResignInformation()
})

</script>

<style scoped>
.work-exp-page {
  background: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 30px;
}

.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 16px;
  position: sticky;
  top: 0;
  z-index: 10;
}

.back-icon {
  font-size: 22px;
  color: #333;
  cursor: pointer;
}

.page-title {
  font-size: 17px;
  font-weight: 500;
  color: #222;
}

.save-btn {
  color: #409EFF;
  font-size: 15px;
  cursor: pointer;
}

.form-list {
  background: #fff;
  margin-top: 12px;
  border-radius: 8px;
  overflow: hidden;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 18px 16px;
  position: relative;
}

.form-item:last-child {
  border-bottom: none;
}

.item-label {
  flex: 0 0 90px;
  color: #333;
  font-size: 15px;
}

.item-tip {
  color: #bbb;
  font-size: 12px;
  margin-left: 8px;
}

.result-row {
  padding: 0 16px 16px 16px;
  background: #fff;
}

.result-value {
  color: #222;
  font-size: 15px;

}

.result-row .el-input {
  width: 100%;
}

.no-border-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  border-radius: 0 !important;
  border-bottom: 1px solid #eee !important;
  padding-left: 0 !important;
  padding-right: 0 !important;
}

.no-border-input :deep(.el-input__inner) {
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding-left: 0 !important;
  padding-right: 0 !important;
}

.photo-section {
  background: #fff;
  margin-top: 12px;
  border-radius: 8px;
  padding: 18px 16px 24px 16px;
}

.section-label {
  font-size: 15px;
  color: #333;
  margin-bottom: 10px;
}

.section-tip {
  color: #bbb;
  font-size: 12px;
  margin-left: 6px;
}

.photo-upload-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.upload-box {
  width: 120px;
  height: 76px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafbfc;
  cursor: pointer;
  position: relative;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #bbb;
  font-size: 13px;
}

.upload-placeholder .el-icon {
  font-size: 28px;
  margin-bottom: 4px;
}

.photo-preview-box {
  position: relative;
  display: inline-block;
}

.id-photo-preview {
  width: 120px;
  height: 76px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #eee;
}

.delete-icon {
  position: absolute;
  top: -10px;
  right: -10px;
  font-size: 20px;
  color: #f56c6c;
  background: #fff;
  border-radius: 50%;
  cursor: pointer;
  border: 1px solid #eee;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
}

/**** years picker ****/
.years-picker-dialog .el-dialog__header {
  display: none;
}

.years-picker-dialog .el-dialog__body {
  padding: 0;
  background: transparent;
}

.picker-list {
  background: #fff;
  border-radius: 16px 16px 0 0;
  padding: 10px;
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
}

.picker-title {
  text-align: center;
  color: #409EFF;
  font-size: 16px;
  padding: 16px 0 8px 0;
  font-weight: 500;
}

.picker-option {
  text-align: center;
  font-size: 16px;
  color: #222;
  padding: 14px 0;
  border-top: 1px solid #f0f0f0;
  cursor: pointer;
}

.picker-option:first-of-type {
  border-top: none;
}

.picker-cancel {
  text-align: center;
  color: #409EFF;
  font-size: 16px;
  padding: 14px 0 10px 0;
  cursor: pointer;
  border-radius: 0 0 16px 16px;
  background: #fff;
}

.error-message {
  color: #f56c6c;
  font-size: 13px;
}
</style>

<style>
/* 针对 el-dialog 的全局样式，因为 append-to-body 会将其移出组件作用域 */
.date-picker-dialog .el-dialog__header {
  display: none;
}

.date-picker-dialog {
  padding: 0 !important;
}

.date-picker-dialog .el-dialog__body {
  padding: 0;
  background: transparent;
}

.date-picker-dialog .el-date-editor.el-input {
  width: 100%;
}

.date-picker-dialog .el-date-editor.el-input .el-input__wrapper {
  width: 100%;
}

.resign-date-picker-style {
  background-color: #fff;
}
</style>