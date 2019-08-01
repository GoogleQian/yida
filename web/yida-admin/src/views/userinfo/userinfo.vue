<template>
  <div class="dev-admin">
    <crud
      :tableData="dataList"
      :tableColumn="tableColumn"
      :tableFunc="tableFunc"
      :pageParams="pageParams"
      operaWidth="300"
      @pageChange="pageChange"
      @add="add"
      @del="del"
      @view="view"
      @edit="edit"
      @singleStatus="singleStatus"
      @qrCode="qrCode"
      :queryItem="queryItem"
      @query="query"
      @mulDel="mulDel"
      :loading="loading"
    ></crud>

    <!-- 新增/修改弹窗 -->
    <el-dialog :title="dialog.title" :visible.sync="dialog.visible">
      <el-form
        :model="dialogForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="姓名：" prop="realName">
          <el-input v-model="dialogForm.realName"></el-input>
        </el-form-item>
        <el-form-item label="手机：" prop="phone">
          <el-input v-model="dialogForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="出生日期：" prop="birthday">
          <el-date-picker
            v-model="dialogForm.birthday"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期时间"
            :picker-options="pickerOptions"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="性别：" prop="sex">
          <el-radio-group v-model.number="dialogForm.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
            <el-radio :label="2">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址：" prop="address">
          <el-input v-model="dialogForm.address"></el-input>
        </el-form-item>
        <el-form-item label="职业：" prop="profession">
          <el-input v-model="dialogForm.profession"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
          <el-button @click="dialog.visible=false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from "qrcode";
import crud from "@/components/crud/crud";
import { devList, devDel, devMulDel, add, edit } from "./api";
import { tableFunc, tableColumn, queryItem, pickerOptions } from "./data";
export default {
  components: { crud },
  data() {
    return {
      pageParams: {
        page: 1,
        pageSize: 10,
        total: 0
      },
      queryParams: {},
      queryItem,
      dataList: [],
      tableFunc,
      tableColumn,
      loading: false,
      dialog: {
        title: "新增",
        visible: false
      },
      dialogForm: {},
      rules: {
        // devCode: {}
      },
      pickerOptions
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      devList({
        ...this.pageParams,
        ...this.queryParams,
        ...this.baseParams
      }).then(res => {
        this.loading = false;
        if (res.ret === 0) {
          this.pageParams.total = res.datas.itemCounts;
          this.dataList = res.datas.list;
        }
      });
    },
    pageChange(page) {
      this.pageParams.page = page;
      this.getList();
    },
    add() {
      this.dialog = {
        title: "新增",
        visible: true
      };
      this.dialogForm = {};
    },
    edit(row) {
      this.dialog = {
        title: "修改",
        visible: true
      };
      this.dialogForm = { ...row };
    },
    del(row) {
      this.$confirm(
        `你确定删除“<span style="font-weight:700">${
          row.realName
        }</span>”用户信息吗?`,
        "删除用户",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          // 触发删除
          devDel({ id: row.id }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    view(row) {
      console.log(row);
      // 跳转页面
    },
    mulDel(list) {
      this.$confirm(`你确定删除这些用户信息吗?`, "删除用户", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          // 触发删除
          const idList = list.map(item => item.id);
          devMulDel({ idList }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    singleStatus(row) {
      this.$confirm(
        `你确定对${row.id}${
          row.enableMultipleBind === 1 ? "关闭" : "开启"
        }重复绑定?`,
        "强制重绑",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          // 触发
          let enableMultipleBind = null;
          if (row.enableMultipleBind === 1) {
            enableMultipleBind = 0;
          } else {
            enableMultipleBind = 1;
          }
          mulBindStatus({
            id: row.id,
            enableMultipleBind
          }).then(res => {
            if (res.ret === 0) {
              this.getList();
              this.$message({
                message: "修改成功",
                type: "success"
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    qrCode(row) {
      QRCode.toDataURL(row.id).then(res => {
        this.$confirm(`<img src="${res}" width="100%"/>`, `${row.id}`, {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          dangerouslyUseHTMLString: true
        });
      });
    },
    query(queryParams) {
      this.pageParams.page = 1;
      this.queryParams = { ...queryParams };
      this.getList();
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.dialog.title === "新增") {
            add({ ...this.dialogForm }).then(res => {
              if (res.ret === 0) {
                this.$message({
                  message: "新增成功",
                  type: "success"
                });
                this.dialog.visible = false;
                this.getList();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          } else {
            edit({
              data: { ...this.dialogForm },
              id: this.dialogForm.id
            }).then(res => {
              if (res.ret === 0) {
                this.$message({
                  message: "修改成功",
                  type: "success"
                });
                this.dialog.visible = false;
                this.getList();
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
    }
  },
  computed: {
    baseParams() {
      return { userId: this.$route.query.userId };
    }
  }
};
</script>

<style scoped lang="less">
</style>
