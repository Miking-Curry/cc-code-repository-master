<template>
  <div class="work-exp-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <el-icon class="back-icon" @click="goBack">
        <ArrowLeft />
      </el-icon>
      <span class="page-title">工作经验</span>
      <span class="save-btn" @click="saveInfo">保存</span>
    </div>

    <!-- 表单内容 -->
    <div class="form-list">
      <!-- 工作年限 -->
      <div>
        <div class="form-item">
          <span class="item-label">工作年限<span style="color: red">*</span></span>
        </div>

        <div class="result-row" @click="showYearsPicker = true">
          <el-input v-model="form.years" placeholder="输入内容" class="no-border-input" />
          <span class="error-message" v-if="workErrorMessage">
            {{ workErrorMessage }}
          </span>
        </div>
      </div>
    </div>

    <!-- 社保证明上传 -->
    <div class="photo-section">
      <div class="section-label">社保证明
        <!-- <span style="color: red">*</span> -->
        <span class="section-tip">请上传一张公司信息</span></div>
      <div class="photo-upload-row">
        <div v-if="!socialImage" class="upload-box" @click="triggerSocialUpload">
          <input type="file" ref="socialInput" @change="handleSocialUpload" accept="image/*" style="display: none" />
          <div class="upload-placeholder">
            <el-icon>
              <Picture />
            </el-icon>
            <div>点击上传图片</div>
          </div>
        </div>
        <div v-else class="photo-preview-box">
          <img :src="socialImage" class="id-photo-preview" />
          <el-icon class="delete-icon" @click="removeSocialImage">Close</el-icon>
        </div>
      </div>
      <span class="error-message" v-if="imageErrorMessage">
        {{ imageErrorMessage }}
      </span>
    </div>
    <!-- 
    工作年限选择器 -->
    <el-dialog v-model="showYearsPicker" :show-close="false" class="years-picker-dialog" width="100%" top="auto"
      append-to-body :modal="true" :close-on-click-modal="true" :lock-scroll="false"
      :custom-class="'years-picker-dialog'">
      <!-- 插槽方式将 header 置空 -->
      <template #header></template>
      <div class="picker-list">
        <div class="picker-title">请选择工作年限</div>
        <div class="picker-option" v-for="(option, idx) in yearsOptions" :key="option" @click="selectYears(option)">{{
          option }}</div>
        <div class="picker-cancel" @click="showYearsPicker = false">取消</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, Picture } from '@element-plus/icons-vue';
import { WorkInformationResponse } from '@/utils/api/type';
import { deleteWorkInformation, getWorkInformation, saveWorkInformation } from '@/utils/api/credit';
import { showError, showSuccess } from '@/utils/toast';

const router = useRouter();
const socialInput = ref<HTMLInputElement | null>(null);

interface WorkForm {
  years: string;
  socialProof: File | string | null;
}

const form = ref<WorkForm>({
  years: '',
  socialProof: null
});
const socialImage = ref<string | null>(null);
const showYearsPicker = ref(false);
const yearsOptions = ['应届生', '3年以下', '3-5年', '5年以上'];

const workErrorMessage = ref('')
const imageErrorMessage = ref('')

function goBack() {
  router.go(-1);
}

function validateWork() {
  if (!form.value.years) {
    workErrorMessage.value = '请选择工作年限'
    return false;
  }
  return true
}

function validateImage() {
  // 允许为空，始终返回true
  return true;
}

async function saveInfo() {
  workErrorMessage.value = ''
  imageErrorMessage.value = ''
  if (validateWork() && validateImage()) {
    try {
      await deleteWorkInformation()
      // 如果没有上传图片，传空File对象
      const socialProofToSend = form.value.socialProof instanceof File ? form.value.socialProof : new File([], '');
      await saveWorkInformation(form.value.years, socialProofToSend);
      showSuccess('保存成功')
      console.log('保存成功')
      router.push("/credit");
    } catch (error) {
      showError('保存失败')
      console.error('保存失败:', error);
    }
  } else {
    showError('校验失败')
    console.log('校验失败')
  }
}

function triggerSocialUpload() {
  socialInput.value?.click();
}

function handleSocialUpload(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0];
  if (file) {
    // 验证文件类型和大小
    if (!file.type.startsWith('image/')) {
      imageErrorMessage.value = '请上传图片文件'
      return;
    }

    if (file.size > 5 * 1024 * 1024) { // 5MB限制
      imageErrorMessage.value = '图片大小不能超过5MB'
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (reader.result) {
        socialImage.value = reader.result as string;
      }
      form.value.socialProof = file; // 保存文件对象
    };
  }
}

function removeSocialImage() {
  socialImage.value = null;
  form.value.socialProof = null;
}

function selectYears(option: string) {
  form.value.years = option;
  showYearsPicker.value = false;
}

const fetchWorkInformation = async () => {
  try {
    const res: WorkInformationResponse = await getWorkInformation()
    if (res.code === 200 && res.data) {
      form.value.years = res.data.workYears
      form.value.socialProof = res.data.socialSecurityRecordPicUrl
      socialImage.value = res.data.socialSecurityRecordPicUrl
    }
  } catch (error) {
    console.error('获取身份信息失败:', error)
  } finally {
  }
}

onMounted(() => {
  fetchWorkInformation()
})
</script>

<style scoped>
/* 原有的样式保持不变 */
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

.picker-list {
  background: #fff;
  border-radius: 16px 16px 0 0;
  padding-bottom: 10px;
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
  border-top: 8px solid #f7f8fa;
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
.years-picker-dialog .el-dialog__header {
  display: none;
}

.years-picker-dialog {
  padding: 0 !important;
}

.years-picker-dialog .el-dialog__body {
  padding: 0;
  background: transparent;
}
</style>