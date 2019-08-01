<template>
  <div class="edit">
    <el-form
      :model="formData"
      :rules="rules"
      ref="ruleForm"
      label-width="130px"
      class="demo-ruleForm"
    >
      <el-form-item label="待机广告名称" prop="name">
        <el-input v-model="formData.name" style="width: 315px;"></el-input>
      </el-form-item>
      <el-form-item label="Solgan" prop="title">
        <el-input v-model="formData.title" style="width: 315px;" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item prop="solgan">
        <el-input v-model="formData.solgan" type="textarea" style="width: 315px;" placeholder="正文"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="resetForm('ruleForm')" v-if="title === '编辑'">重置</el-button>
        <el-button type="primary" @click="submitTestForm('ruleForm')">广告测试</el-button>
      </el-form-item>
    </el-form>
    <!-- 广告测试的弹窗 -->
    <el-dialog title="广告测试" :visible.sync="testVisible" @close="closeDialog">
      <el-form :model="testForm" :rules="testRules" :show-message="false">
        <el-form-item :prop="value==='1'?'phone':'devCode'">
          <el-row>
            <el-col :span="10">
              <el-radio v-model="radio" label="1" @change="change">手机号码</el-radio>
              <el-input
                placeholder="请输入有效手机号"
                v-model="testForm.phone"
                autocomplete="off"
                :disabled="value!=1"
              ></el-input>
            </el-col>
            <el-col :span="10">
              <el-radio v-model="radio" label="2" @change="change">设备ID</el-radio>
              <el-input
                placeholder="请输入15位设备ID"
                v-model="testForm.devCode"
                autocomplete="off"
                :disabled="value!=2"
              ></el-input>
            </el-col>
            <el-col :span="2">
              <el-button @click="search" icon="el-icon-search" type="primary">搜索</el-button>
            </el-col>
            <el-col :span="2">
              <el-button @click="push" icon="el-icon-caret-top" type="danger">推送</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-table
            :data="testAd"
            style="width: 100%"
            :default-sort="{prop: 'date', order: 'descending'}"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="phone" label="手机号码" sortable></el-table-column>
            <el-table-column prop="devCode" label="设备ID" sortable></el-table-column>
            <el-table-column prop="online" label="在线状态" sortable>
              <template scope="scope">
                <span v-if="scope.row.online=== 0">离线</span>
                <span v-else style="color:#67C23A">在线</span>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item>
          <Page @pageChange="pageChange" :pageParams="pageParams"></Page>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { bannerAdd, bannerEdit } from "@/service/ad";
import { getDev, pushAd } from "./api";
import Page from "@/components/crud/page/page";
export default {
  data() {
    let validPhone = (rule, value, callback) => {
      let reg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
      if (!reg.test(value)) {
        callback(new Error("电话号码格式有误！请重新输入"));
      } else {
        callback();
      }
    };
    let validDev = (rule, value, callback) => {
      let reg = /^\d{15}$/;
      if (!reg.test(value)) {
        callback(new Error("设备ID格式有误！请重新输入"));
      } else {
        callback();
      }
    };
    return {
      rules: {
        name: [
          { required: true, message: "请输入待机广告名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在1-20之间", trigger: "change" }
        ],
        title: [
          { required: true, message: "请输入广告标题", trigger: "blur" },
          { min: 1, max: 33, message: "长度在1-33之间", trigger: "change" }
        ],
        solgan: [
          { required: true, message: "请输入广告内容", trigger: "blur" },
          { min: 1, max: 99, message: "长度在1-99之间", trigger: "change" }
        ]
      },
      formData: {
        solgan: "",
        name: "",
        title: ""
      },
      testVisible: false,
      testForm: {
        phone: "",
        devCode: ""
      },
      radio: "1",
      testAd: [],
      value: "1",
      testRules: {
        phone: { validator: validPhone, trigger: "blur" },
        devCode: { validator: validDev, trigger: "blur" }
      },
      pageParams: {
        page: 1,
        pageSize: 5,
        total: 0
      },
      sendData: {
        key: "",
        page: 1,
        pageSize: 5
      },
      pushData: {
        title: "",
        solgan: "",
        devCodes: []
      },
      loading: false
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
                  this.$router.push({
                    path: "/serve/ad"
                  });
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
                  this.$router.push({
                    path: "/serve/ad"
                  });
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
    submitTestForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.testVisible = true;
        } else {
          return false;
        }
      });
    },
    search() {
      this.loading = true;
      this.testAd = [];
      if (this.testForm.phone) {
        this.sendData.key = this.testForm.phone;
      } else if (this.testForm.devCode) {
        this.sendData.key = this.testForm.devCode;
      } else {
        this.$message({
          message: "手机号或设备ID不能为空",
          type: "error"
        });
        this.loading = false;
        return false;
      }
      getDev(this.sendData).then(res => {
        console.log(res);
        if (res.ret === 0) {
          this.testAd = res.datas.list;
          this.pageParams.total = res.datas.itemCounts;
          this.loading = false;
        } else {
          if (this.testForm.phone) {
            this.$message({
            message: "电话号码输入格式有误，请重新输入合法的电话号码",
            type: "error"
          });
          this.loading = false;
          } else if (this.testForm.devCode) {
            this.$message({
            message: "设备ID输入格式有误，请重新输入15位数字的设备ID",
            type: "error"
          });
          this.loading = false;
          } else {
            return false;
          }
        }
      });
    },
    change(value) {
      this.value = value;
      if (value === "1") {
        this.testForm.devCode = "";
      } else {
        this.testForm.phone = "";
      }
    },
    pageChange(page) {
      // console.log(page);
      this.sendData.page = page;
      this.search();
    },
    handleSelectionChange(row) {
      // console.log(row);
      let arr = [];
      row.forEach(v => {
        arr.push(v.devCode);
      });
      this.pushData.devCodes = arr;
    },
    push() {
      this.pushData.title = this.formData.title;
      this.pushData.solgan = this.formData.solgan;
      // console.log(this.pushData);
      pushAd(this.pushData).then(res => {
        // console.log(res);
        if (res.ret === 0) {
          this.$message({
            message: "已推送",
            type: "success"
          });
          this.testVisible = false;
        } else {
          this.$message({
            message: "推送失败",
            type: "error"
          });
        }
      });
    },
    closeDialog(){
      // console.log("close");
      this.testForm = {};
      this.testAd = [];
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
  },
  components: { Page }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
