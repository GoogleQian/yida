<template>
  <div class="filter-admin">
    <crud
      :tableData="dataList"
      :tableColumn="tableColumn"
      :tableFunc="tableFunc"
      :options="options"
      @qrCode="qrCode"
      :pageParams="pageParams"
      operaWidth="300"
      @pageChange="pageChange"
      @add="add"
      @del="del"
      @view="view"
      @edit="edit"
      @_import="_import"
      @_export="_export"
      :queryItem="queryItem"
      @query="query"
      :loading="loading"
      @generate="generate"
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
        <el-form-item label="滤芯编码：" prop="filterCode">
          <el-input v-model="dialogForm.filterCode" :disabled="dialog.title=== '修改'"></el-input>
        </el-form-item>
        <el-form-item label="使用状态：" prop="status">
          <el-select v-model="dialogForm.status">
            <el-option label="使用中" :value="1"></el-option>
            <el-option label="未使用" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
          <el-button @click="dialog.visible=false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 导入导出 -->
    <el-dialog title="滤芯导入" :visible.sync="importDialog" width="600px">
      <el-row style="margin-top: -20px;">
        <el-col :span="18">
          <h3 style="font-size: 16px;font-weight: 700">导入说明：</h3>
          <p>
            1.导入数据前，先下载数据模板，
            <a href="javascript:void(0)" @click="downloadTemp">点击下载模板</a>
          </p>
          <p>2.填写数据并选择上传文件</p>
          <p>3.每次导入不超过5000条数据</p>
          <p>4.请注意导入数据格式及必填项</p>
        </el-col>
        <el-col :span="6">
          <my-upload
            accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
            @_import="importconfirm"
            :fileName="'files'"
          ></my-upload>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from "qrcode";
import crud from "@/components/crud/crud";
import MyUpload from "@/components/upload/upload";
import {
  filterList,
  filterDel,
  filterAdd,
  filterEdit,
  filterGen
  //   filterView,
  //   filterImport
} from "@/service/filter";
import { upload, download_ter_template, _export } from "@/service/upload";
import { downloadFile } from "@/common/js/blobdownload";
import { tableFunc, tableColumn, queryItem } from "./data";
export default {
  components: { crud, MyUpload },
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
      genlog: {
        title: "滤芯生成",
        visible: false
      },
      dialogForm: {},
      importDialog: false,
      importDialogForm: null,
      rules: {
        // devCode: {}
      },
      options:[
        {
          value:1,
          label:"使用中"
        },
        {
          value:0,
          label:"未使用"
        }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      filterList({
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
    qrCode(row) {
      console.log(row);
      QRCode.toDataURL(row.filterCode).then(res => {
        this.$confirm(`<img src="${res}" width="100%"/>`, `${row.filterCode}`, {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          dangerouslyUseHTMLString: true
        });
      });
    },
    pageChange(page) {
      this.pageParams.page = page;
      this.getList();
    },
    add() {
      console.log("add");

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
          row.filterCode
        }</span>”滤芯信息吗?`,
        "删除设备",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          // 触发删除
          filterDel({ id: row.id }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.getList();
            } else {
              this.$message({
                message: res.msg,
                type: "error"
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
    view(row) {
      console.log(row);
      // 跳转页面
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
            filterAdd({ ...this.dialogForm }).then(res => {
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
            filterEdit({
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
    },

    generate() {
      // console.log("111");
      // this.dialog = {
      //   title: "生成",
      //   visible: true
      // };
      this.$confirm(`你确定生成滤芯编码吗?`, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true
      })
        .then(() => {
          // 触发生成
          filterGen().then(res => {
            downloadFile(res, "滤芯列表");
              this.getList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消生成"
          });
        });
    },
    _import() {
      this.importDialog = true;
    },
    importconfirm(formData) {
      upload(formData).then(res => {
        this.$message({
          message: res.ret === 0 ? `导入成功` : res.msg,
          type: res.ret === 0 ? "success" : "error"
        });
        this.getList();
        this.importDialog = false;
      });
    },
    _export(multipleSelection) {
      let ids = [];
      console.log(multipleSelection);

      if (multipleSelection.length > 0) {
        ids = multipleSelection.map(item => item.id);
        _export({ ids: [...ids] }).then(res => {
          downloadFile(res, "滤芯列表");
        });
      } else {
        this.$message({
          message: "请至少选择一项",
          type: "warning"
        });
      }
    },
    downloadTemp() {
      download_ter_template().then(res => {
        downloadFile(res, "滤芯模板");
      });
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
