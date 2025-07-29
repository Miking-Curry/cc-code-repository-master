<template>
  <div class="identity-info-page">

    <!-- 顶部栏 -->
    <div class="top-bar">
      <el-icon class="back-icon" @click="goBack">
        <ArrowLeft />
      </el-icon>
      <span class="page-title">身份信息</span>
      <span class="save-btn" @click="saveInfo">保存</span>
    </div>

    <!-- 表单内容 -->
    <div class="form-list">
      <!-- 姓名 -->
      <div>
        <div class="form-item">
          <span class="item-label">姓名<span style="color: red">*</span></span>
        </div>
        <div class="edit-input-row">
          <input type="text" class="no-border-input" v-model="form.name" placeholder="输入内容"></input>
          <span class="error-message" v-if="formNameErrorMessage">
            {{ formNameErrorMessage }}
          </span>
        </div>
      </div>
      <!-- 身份证号 -->
      <div>
        <div class="form-item">
          <span class="item-label">身份证号<span style="color: red">*</span></span>
        </div>
        <div class="edit-input-row">
          <input type="text" class="no-border-input" v-model="form.idNumber" placeholder="输入内容"></input>
          <span class="error-message" v-if="formNumberErrorMessage">
            {{ formNumberErrorMessage }}
          </span>
        </div>
      </div>
    </div>

    <!-- 身份证照片上传 -->
    <div class="photo-section">
      <div class="section-label">身份证照片<span style="color: red">*</span><span class="section-tip">（只需上传人像面照片）</span>
      </div>
      <div class="photo-upload-row">
        <div v-if="!idFrontImage" class="upload-box" @click="triggerFrontUpload">
          <input type="file" ref="frontInput" @change="handleFrontUpload" accept="image/*" style="display: none" />
          <div class="upload-placeholder">
            <el-icon>
              <Picture />
            </el-icon>
            <div>点击上传照片</div>
          </div>
        </div>
        <div v-else class="photo-preview-box">
          <img :src="idFrontImage" class="id-photo-preview" />
          <el-icon class="delete-icon" @click="removeFrontImage">Close</el-icon>
        </div>
      </div>
      <span class="error-message" v-if="formPhotoErrorMessage">
        {{ formPhotoErrorMessage }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, Picture } from '@element-plus/icons-vue';
import { IdCardInformationResponse } from '@/utils/api/type';
import { deleteIdCardInformation, getIdCardInformation, saveIdCardInformation } from '@/utils/api/credit';
import { showError, showSuccess } from '@/utils/toast';

const router = useRouter();
const frontInput = ref<HTMLInputElement | null>(null);
const form = ref<{
  name: string | null;
  idNumber: string | null;
  idFrontImage: File | string | null;
}>({
  name: '',
  idNumber: '',
  idFrontImage: null
});
const idFrontImage = ref<string | null>('');

const formNameErrorMessage = ref('')
const formNumberErrorMessage = ref('')
const formPhotoErrorMessage = ref('')

function goBack() {
  router.go(-1);
}

const fetchIdCardInformation = async () => {
  try {
    const res: IdCardInformationResponse = await getIdCardInformation()
    if (res.code === 200 && res.data) {
      form.value.idFrontImage = res.data.idCardPic || null;
      idFrontImage.value = res.data.idCardPic || null;
      form.value.idNumber = res.data.idCardNum || null;
      form.value.name = res.data.name || null;
    }
  } catch (error) {
    showError('获取身份信息失败')
    console.error('获取身份信息失败:', error)
  } finally {
  }
}

onMounted(() => {
  fetchIdCardInformation()
})

function validateName() {
  if (!form.value.name) {
    formNameErrorMessage.value = '请填写姓名'
    return false;
  }
  return true
}

function validateNumber() {
  if (!form.value.idNumber) {
    formNumberErrorMessage.value = '请填写身份证号'
    return false;
  }
  if (form.value.idNumber.length !== 18) {
    formNumberErrorMessage.value = '请输入正确的身份证号码'
    return false;
  }
  return true
}

function validateImage() {
  if (!form.value.idFrontImage) {
    formPhotoErrorMessage.value = '请上传身份证照片'
    return false;
  }
  return true
}

async function saveInfo() {
  formNameErrorMessage.value = ''
  formNumberErrorMessage.value = ''
  formPhotoErrorMessage.value = ''
  // 表单验证
  if (validateName() && validateNumber() && validateImage()) {
    try {
      if (form.value.idFrontImage instanceof File) {
        await deleteIdCardInformation()
        await saveIdCardInformation(form.value.name as string, form.value.idNumber as string, form.value.idFrontImage);
        showSuccess('保存成功')
        console.log('保存成功')
      } else {
        // 如果是字符串（URL），表示图片已存在，不重新上传
        showError('身份证照片未更新，仅保存文本信息,不做任何修改')
        console.warn('身份证照片未更新，仅保存文本信息');
      }
      router.push("/credit");
    } catch (error) {
      showError('保存失败')
      console.error('保存失败:', error);
    }
  } else {
    showError('校验错误')
    console.log('校验错误')
    return
  }


}

function triggerFrontUpload() {
  if (frontInput.value) {
    (frontInput.value as HTMLInputElement).click();
  }
}

function handleFrontUpload(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0];
  if (file) {
    // 验证文件类型和大小
    if (!file.type.startsWith('image/')) {
      formPhotoErrorMessage.value = '请上传图片文件'
      return;
    }

    if (file.size > 5 * 1024 * 1024) { // 5MB限制
      formPhotoErrorMessage.value = '图片大小不能超过5MB'
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      idFrontImage.value = reader.result as string | null;
      form.value.idFrontImage = file; // 保存文件对象
    };
  }
}

function removeFrontImage() {
  idFrontImage.value = '';
  form.value.idFrontImage = null;
}

//尚未开发
function autoRecognize() {
  if (!idFrontImage.value) {
    return;
  }

  // 这里可以添加自动识别的API调用
}
</script>

<style scoped>
.identity-info-page {
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
  padding-top: 16px;
  overflow: hidden;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 0 16px 0 16px;
  border-bottom: none;
}

.form-item:last-child {
  border-bottom: none;
}

.item-label {
  flex: 0 0 80px;
  color: #333;
  font-size: 15px;
}

.edit-input-row {
  padding: 0 16px 16px 16px;
  background: #fff;
}

.no-border-input {
  width: 100%;
  border: none;
  border-bottom: 1px solid #eee;
}

.no-border-input:focus {
  outline: none;
  border: none;
  border-bottom: 1px solid #eee;
  box-shadow: none;
}

/* .no-border-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  border-radius: 0 !important;
  border-bottom: 1px solid #eee !important;
  padding-left: 0 !important;
  padding-right: 0 !important;
}

/* 关键点：去掉 is-focus 状态下的阴影（focus 边框） */
/* .no-border-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: none !important;
}

/* input 本身也清理一下（虽然焦点边框不是它引起的） */
/* .no-border-input :deep(.el-input__inner) {
  width: 100%;
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding-left: 0 !important;
  padding-right: 0 !important;
} */

.photo-section {
  background: #fff;
  margin-top: 12px;
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

.id-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
}

.photo-preview-box {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
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
  border: 1px solid #eee;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
}

.auto-rec-btn {
  margin-top: 2px;
}

.error-message {
  color: #f56c6c;
  font-size: 13px;
}
</style>