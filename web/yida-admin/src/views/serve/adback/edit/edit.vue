<template>
  <div class="edit">
    <el-form
      :model="formData"
      :rules="rules"
      ref="ruleForm"
      label-width="130px"
      class="demo-ruleForm"
    >
      <el-form-item
        label="待机广告名称"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          style="width: 315px;"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="Solgan"
        prop="title"
      >
        <el-input
          v-model="formData.title"
          style="width: 315px;"
          placeholder="标题"
        ></el-input>
      </el-form-item>
      <el-form-item prop="solgan">
        <el-input
          v-model="formData.solgan"
          type="textarea"
          style="width: 315px;"
          placeholder="正文"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="danger"
          @click="submitForm('ruleForm')"
        >保存</el-button>
        <el-button
          @click="resetForm('ruleForm')"
          v-if="title === '编辑'"
        >重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { bannerAdd, bannerEdit } from "@/service/ad";
export default {
  data() {
    return {
      rules: {
        name: [
          { required: true, message: "请输入待机广告名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在1-20之间", trigger: "change" }
        ],
        title: [{ required: true, message: "请输入广告标题", trigger: "blur" }],
        solgan: [
          { required: true, message: "请输入广告内容", trigger: "blur" },
          { min: 1, max: 48, message: "长度在1-48之间", trigger: "change" }
        ]
      },
      formData: {
        solgan: "",
        name: ""
      }
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
                  this.$router.go(-1);
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
                  this.$router.go(-1);
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
        this.formData = { solgan: "", name: "" };
      }
    }
  },
  computed: {
    title() {
      return this.$route.query.row ? "编辑" : "添加";
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
