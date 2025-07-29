<template>
  <div class="work-exp-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <el-icon class="back-icon" @click="goBack">
        <ArrowLeft />
      </el-icon>
      <span class="page-title">技能证书</span>
      <span class="save-btn" @click="saveInfo">保存</span>
    </div>

    <!-- 表单内容 -->
    <div class="form-list">
      <!-- 资格证书 -->
      <div>
        <div class="form-item">
          <span class="item-label">资格证书<span style="color: red">*</span></span>
        </div>
        <div class="result-row">
          <el-input v-model="form.certificateName" placeholder="输入资格证书名称" class="no-border-input" />
          <span class="error-message" v-if="certificateNameErrorMessage">
            {{ certificateNameErrorMessage }}
          </span>
        </div>
      </div>
    </div>

    <!-- 证书照片 -->
    <div class="photo-section">
      <div class="photo-title">
        <span class="item-label">技能证书上传<span style="color: red">*</span></span>
      </div>
      <div class="photo-upload-row">
        <div v-if="!certificateImage" class="upload-box" @click="triggerCertificateUpload">
          <input type="file" ref="certificateInput" @change="handleCertificateUpload" accept="image/*"
            style="display: none" />
          <div class="upload-placeholder">
            <el-icon>
              <Picture />
            </el-icon>
            <div>点击上传证书图片</div>
          </div>
        </div>
        <div v-else class="photo-preview-box">
          <img :src="certificateImage" class="id-photo-preview" />
          <el-icon class="delete-icon" @click="removeCertificateImage">Close</el-icon>
        </div>
      </div>
      <span class="error-message" v-if="certificateImageErrorMessage">
        {{ certificateImageErrorMessage }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, Picture } from '@element-plus/icons-vue';
import { CertificateInformationResponse } from '@/utils/api/type';
import { getCertificateInformation, saveCertificateInformation } from '@/utils/api/credit';
import { showError, showSuccess } from '@/utils/toast';

const router = useRouter();
const certificateInput = ref<HTMLInputElement | null>(null);
interface CertificateForm {
  certificateName: string;
  certificateFile: File | string | null;
}
const form = ref<CertificateForm>({
  certificateName: '',
  certificateFile: null
});
const certificateImage = ref<string | null>(null);

const certificateNameErrorMessage = ref('')
const certificateImageErrorMessage = ref('')

function goBack() {
  router.go(-1);
}

async function saveInfo() {
  certificateImageErrorMessage.value = ''
  certificateNameErrorMessage.value = ''

  // 表单验证
  if (!form.value.certificateName) {
    certificateNameErrorMessage.value = '请填写资格证书名称'
    return;
  }

  if (!form.value.certificateFile) {
    certificateImageErrorMessage.value = '请上传证书图片'
    return;
  }

  try {
    if (form.value.certificateFile instanceof File) {
      await saveCertificateInformation(form.value.certificateName as string, form.value.certificateFile as File);
      showSuccess('保存成功')
      console.log('保存成功')
    } else {
      // 如果是字符串（URL），表示图片已存在，不重新上传
      showError('证书未更新，仅保存文本信息，不保存信息')
      console.warn('证书未更新，仅保存文本信息，不保存信息');
    }
    router.push("/credit");
  } catch (error) {
    console.error('保存失败:', error);
  }
}

function triggerCertificateUpload() {
  certificateInput.value?.click();
}

function handleCertificateUpload(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0];
  if (file) {
    certificateImageErrorMessage.value = ''
    // 验证文件类型和大小
    if (!file.type.startsWith('image/')) {
      certificateImageErrorMessage.value = '请上传图片文件'
      return;
    }

    if (file.size > 5 * 1024 * 1024) { // 5MB限制
      certificateImageErrorMessage.value = '图片大小不能超过5MB'
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (reader.result) {
        certificateImage.value = reader.result as string;
      }
      form.value.certificateFile = file;
    };
  }
}

function removeCertificateImage() {
  certificateImage.value = null;
  form.value.certificateFile = null;
}

const fetchCertificateInformation = async () => {
  try {
    const res: CertificateInformationResponse = await getCertificateInformation()
    if (res.code === 200 && res.data && res.data.length > 0) {
      form.value.certificateName = res.data[0].certificateName
      form.value.certificateFile = res.data[0].certificatePic
      certificateImage.value = res.data[0].certificatePic
    }
  } catch (error) {
    showError('获取证书信息失败')
    console.error('获取证书信息失败:', error)
  } finally {
  }
}

onMounted(() => {
  fetchCertificateInformation()
})
</script>

<style scoped>
/* 保持原有的样式不变 */
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

.item-label {
  flex: 0 0 90px;
  color: #333;
  font-size: 15px;
}

.result-row {
  padding: 0 16px 16px 16px;
  background: #fff;
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

.photo-section {
  background: #fff;
  margin-top: 12px;
  border-radius: 8px;
  padding: 18px 16px 24px 16px;
}

.photo-upload-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.upload-box {
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
  width: 120px;
  height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #bbb;
  font-size: 13px;
  white-space: nowrap;

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

.photo-title {
  padding-bottom: 18px
}

.error-message {
  color: #f56c6c;
  font-size: 13px;
}
</style>