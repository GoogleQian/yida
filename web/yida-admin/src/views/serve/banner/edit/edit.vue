<template>
  <div class="edit">
    <el-form
      :model="formData"
      :rules="rules"
      ref="ruleForm"
      label-width="140px"
      class="demo-ruleForm"
    >
      <el-form-item label="BANNER广告名称" prop="name">
        <el-input v-model="formData.name" style="width: 315px;"></el-input>
      </el-form-item>
      <el-form-item label="图片" prop="imageUrl">
        <el-upload
          class="avatar-uploader"
          action="/mng/yida/file/image"
          :headers="headers"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="formData.imageUrl" :src="formData.imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="URL" prop="adUrl">
        <el-input v-model="formData.adUrl" style="width: 315px;" placeholder="www.example.com"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="resetForm('ruleForm')" v-if="title === '编辑'">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { bannerAdd, bannerEdit } from "@/service/banner";
export default {
  data() {
    return {
      rules: {
        name: [
          { required: true, message: "请输入BANNER广告名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在1-20之间", trigger: "blur" }
        ],
        imageUrl: [{ required: true, message: "请选择图片", trigger: "blur" }],
        adUrl: [{ required: true, message: "请输入URL", trigger: "blur" }]
      },
      formData: { name: null, imageUrl: null, adUrl: null }
    };
  },
  created() {
    this.setFormData();
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.title === "添加") {
            bannerAdd({ ...this.formData }).then(res => {
              if (res.ret === 0) {
                this.$message({
                  message: `${this.title}成功`,
                  type: "success"
                });
                setTimeout(() => {
                  this.$router.push({ path: "/serve/banner" });
                }, 1000);
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          } else if (this.title === "编辑") {
            bannerEdit({ ...this.formData }).then(res => {
              if (res.ret === 0) {
                this.$message({
                  message: `${this.title}成功`,
                  type: "success"
                });
                setTimeout(() => {
                  this.$router.push({ path: "/serve/banner" });
                }, 1000);
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    setFormData() {
      if (this.$route.query.row) {
        this.formData = JSON.parse(this.$route.query.row);
      } else {
        this.formData = { name: null, imageUrl: null, adUrl: null };
      }
    },
    handleAvatarSuccess(res, file) {
      if (res.ret === 0) {
        this.formData.imageUrl = res.datas;
      } else {
        this.$message({
          message: res.msg,
          type: "error"
        });
      }
    },
    beforeAvatarUpload(file) {
      // 图片格式校验
      // const isJPG = file.type === "image/jpeg";
      // const isLt2M = file.size / 1024 / 1024 < 2;
      //   if (!isJPG) {
      //     this.$message.error("上传头像图片只能是 JPG 格式!");
      //   }
      // if (!isLt2M) {
      // this.$message.error("上传头像图片大小不能超过 2MB!");
      // }
      // return isJPG && isLt2M;
    }
  },
  computed: {
    title() {
      return this.$route.query.row ? "编辑" : "添加";
    },
    headers() {
      return {
        token: localStorage.getItem("token")
      };
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
