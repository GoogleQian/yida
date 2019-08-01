<template>
  <div class="dev-admin">
    <crud
      :tableData="dataList"
      :options="options"
      :tableColumn="tableColumn"
      :tableFunc="tableFunc"
      :pageParams="pageParams"
      operaWidth="300"
      @pageChange="pageChange"
      @add="add"
      @del="del"
      @view="view"
      @connectLog="connectLog"
      @edit="edit"
      @_import="_import"
      @_export="_export"
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
        <el-form-item label="设备号" prop="devCode">
          <el-input v-model="dialogForm.devCode" :disabled="dialog.title=== '修改'"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
          <el-button @click="dialog.visible=false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 弹框显示连接记录 -->
    <el-dialog title="连接记录" :visible.sync="dialogFormVisible">
      <connect-log :row="selectRow" ref="connectLog"></connect-log>
    </el-dialog>

    <!-- 导入导出 -->
    <el-dialog title="设备导入" :visible.sync="importDialog" width="600px">
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
  devList,
  devDel,
  devMulDel,
  mulBindStatus,
  add,
  edit
} from "@/service/devbind";
import {
  uploadDev,
  download_ter_template_dev,
  _exportDev
} from "@/service/upload";
import { downloadFile } from "@/common/js/blobdownload";
import ConnectLog from "@/views/emqclient/emqClient";
import { tableFunc, tableColumn, queryItem } from "./data";
export default {
  components: { crud, ConnectLog, MyUpload },
  data() {
    return {
      dialogFormVisible: false,
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
      dialogForm: { devCode: "" },
      rules: {
        // devCode: {}
      },
      selectRow: {},
      importDialog: false,
      importDialogForm: null,
      options:[
        {
          value:1,
          label:"在线"
        },
        {
          value:0,
          label:"离线"
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
      devList({
        ...this.pageParams,
        ...this.queryParams,
        ...this.baseParams
      }).then(
        res => {
          this.loading = false;
          if (res.ret === 0) {
            this.pageParams.total = res.datas.itemCounts;
            this.dataList = res.datas.list;
          }
        },
        err => {
          if (err.response.status == 401) {
            return;
          }
          this.loading = false;
          this.$message({
            message: "获取列表失败",
            type: "error"
          });
        }
      );
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
      this.dialogForm = { devCode: "" };
    },
    edit(row) {
      this.dialog = {
        title: "修改",
        visible: true
      };
      this.dialogForm = { ...row };
    },
    connectLog(row) {
      this.selectRow = row;
      this.dialogFormVisible = true;
      setTimeout(() => {
        this.$refs.connectLog.getList();
      }, 100);
    },
    del(row) {
      this.$confirm(
        `你确定删除“<span style="font-weight:700">${
          row.devCode
        }</span>”设备信息吗?`,
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
          devDel({ id: row.id }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.getList();
            } else {
              this.$message({
                type: "error",
                message: res.msg
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
    mulDel(list) {
      this.$confirm(`你确定删除这些设备信息吗?`, "删除设备", {
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
        `你确定对${row.devCode}${
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
            message: "已取消"
          });
        });
    },
    qrCode(row) {
      QRCode.toDataURL(row.devCode).then(res => {
        this.$confirm(`<img src="${res}" width="100%"/>`, `${row.devCode}`, {
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
              devId: this.dialogForm.id
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
    _import() {
      this.importDialog = true;
    },
    importconfirm(formData) {
      uploadDev(formData).then(res => {
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
      if (multipleSelection.length > 0) {
        ids = multipleSelection.map(item => item.id);
        _exportDev({ ids: [...ids] }).then(res => {
          downloadFile(res, "设备列表");
        });
      } else {
        this.$message({
          message: "请至少选择一项",
          type: "warning"
        });
      }
    },
    downloadTemp() {
      download_ter_template_dev().then(res => {
        downloadFile(res, "设备模板");
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
