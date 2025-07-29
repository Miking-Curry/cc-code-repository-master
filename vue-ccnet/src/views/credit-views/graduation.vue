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
      <!-- 毕业学校 -->
      <div>
        <div class="form-item">
          <span class="item-label">毕业学校<span style="color: red">*</span></span>
        </div>

        <div class="result-row">
          <!-- 修改后的输入框，保持原有样式 -->
          <div class="school-input-container">
            <el-input v-model="form.school" placeholder="输入学校名称" class="no-border-input" @input="handleSchoolInput"
              @focus="showSuggestions = true" @blur="handleBlur" />
            <!-- 新增的下拉列表 -->
            <div class="school-dropdown" v-show="showSuggestions && filteredSchools.length > 0">
              <div v-for="school in filteredSchools" :key="school" class="dropdown-item"
                @mousedown="selectSchool(school)">
                {{ school }}
              </div>
            </div>
            <span class="error-message" v-if="schoolErrorMessage">
              {{ schoolErrorMessage }}
            </span>
          </div>
        </div>
      </div>
    </div>


    <!-- 毕业证书 -->
    <div class="photo-section">
      <div class="photo-title">
        <span class="item-label">毕业证书上传<span style="color: red">*</span></span>
      </div>
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
          <el-icon class="delete-icon" @click="removeSocialImage">
            <Close />
          </el-icon>
        </div>
      </div>
      <span class="error-message" v-if="certificateErrorMessage">
        {{ certificateErrorMessage }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowLeft, Picture, Close } from '@element-plus/icons-vue';
import { EducationInformationResponse } from '@/utils/api/type';
import { deleteEducationInformation, getEducations, saveEducationInformation } from '@/utils/api/credit';
import { showError, showSuccess } from '@/utils/toast';

const router = useRouter();
const socialInput = ref<HTMLInputElement | null>(null);

interface Form {
  school: string;
  graduationCertificate: File | string | null;
}
const form = ref<Form>({
  school: '',
  graduationCertificate: null
});
const socialImage = ref<string | null>(null);
const schoolList = ref<string[]>([]);
const filteredSchools = ref<string[]>([]);
const showSuggestions = ref(false);

const schoolErrorMessage = ref('')
const certificateErrorMessage = ref('')

// Load school data from text file
async function loadSchoolData() {
  try {
    const response = await fetch('/school.txt');
    const text = await response.text();
    schoolList.value = text.split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0);
  } catch (error) {
    console.error('加载学校数据失败:', error);
  }
}

// 处理学校输入
function handleSchoolInput() {
  if (!form.value.school) {
    filteredSchools.value = [];
    return;
  }

  const searchText = form.value.school.toLowerCase();
  filteredSchools.value = schoolList.value
    .filter(school => school.toLowerCase().includes(searchText))
    .slice(0, 5);
}

// 选择学校
function selectSchool(school: string) {
  form.value.school = school;
  showSuggestions.value = false;
}

function goBack() {
  router.go(-1);
}

function validateSchool() {
  if (!form.value.school) {
    schoolErrorMessage.value = '请填写毕业学校'
    return false;
  }
  return true
}

function validateCertificate() {
  if (!form.value.graduationCertificate) {
    certificateErrorMessage.value = '请上传毕业证书'
    return;
  }
  return true
}

async function saveInfo() {
  certificateErrorMessage.value = ''
  schoolErrorMessage.value = ''
  if (validateSchool() && validateCertificate()) {
    try {
      if (form.value.graduationCertificate instanceof File) {
        await deleteEducationInformation()
        await saveEducationInformation(form.value.school as string, form.value.graduationCertificate);
        showSuccess('保存成功')
        console.log('保存成功')
      } else {
        // 如果是字符串（URL），表示图片已存在，不重新上传
        showError('毕业证书未更新，仅保存文本信息，不上传信息')
        console.warn('毕业证书未更新，仅保存文本信息，不上传信息');
      }
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
    if (!file.type.startsWith('image/')) {
      console.log('请上传图片文件')
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      console.log('图片大小不能超过5MB')
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (reader.result) {
        socialImage.value = reader.result as string;
        form.value.graduationCertificate = file;
      }
    };
  }
}

function removeSocialImage() {
  socialImage.value = null;
  form.value.graduationCertificate = null;
}

const fetchEducationInformation = async () => {
  try {
    const res: EducationInformationResponse = await getEducations()
    if (res.code === 200 && res.data) {
      form.value.school = res.data.schoolName
      form.value.graduationCertificate = res.data.certificatePic
      socialImage.value = res.data.certificatePic
    }
  } catch (error) {
    showError('获取学历信息失败')
    console.log('获取学历信息失败:',error)
  } finally {
  }
}

// Load school data when component mounts
onMounted(() => {
  loadSchoolData();
  fetchEducationInformation();
});

// 处理失去焦点
function handleBlur() {
  setTimeout(() => {
    showSuggestions.value = false;
  }, 200);
}
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

.result-row {
  padding: 0 16px 16px 16px;
  background: #fff;
  position: relative;
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
  padding: 0 16px 24px 16px;
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

/* 下拉建议列表样式 */
.school-suggestions {
  position: absolute;
  top: calc(100% + 5px);
  left: 16px;
  right: 16px;
  max-height: 200px;
  overflow-y: auto;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 9999;
}

.suggestion-item {
  padding: 8px 16px;
  cursor: pointer;
  line-height: 1.5;
}

.suggestion-item:hover {
  background-color: #f5f7fa;
}


/* 新增的下拉列表样式 */
.school-input-container {
  position: relative;
  width: 100%;
}

.school-dropdown {
  position: fixed;
  /* 改为fixed定位，相对于视口，避免被其他元素覆盖 */
  top: calc(var(--input-top, 0) + 100%);
  /* 使用自定义属性方便调整位置 */
  left: 0;
  right: 0;
  max-height: 200px;
  overflow-y: auto;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 99999;
  /* 提高层级确保在最上层 */
  margin-top: 4px;
}

.dropdown-item {
  padding: 8px 16px;
  cursor: pointer;
  line-height: 1.5;
  color: #606266;
  font-size: 14px;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
}

/* 滚动条样式 */
.school-dropdown::-webkit-scrollbar {
  width: 6px;
}

.school-dropdown::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 3px;
}

.school-dropdown::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

.error-message {
  color: #f56c6c;
  font-size: 13px;
}
.photo-title {
  display: flex;
  align-items: center;
  padding: 18px 0px 16px 0px;
  position: relative;
  white-space: nowrap;
}
</style>