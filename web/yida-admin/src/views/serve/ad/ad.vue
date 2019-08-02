<template>
  <div class="water_status top-border">
    <!-- 搜索 -->
    <z-search :searchItems="searchItems" @search="search"></z-search>
    <!-- 功能+table -->
    <z-table
      :tableData="tableData"
      :tableColumns="tableColumns"
      :page="page"
      :funcs="funcs"
      :lineFuncs="lineFuncs"
      @lineFunc="lineFunc"
      @func="func"
      @handleCurrentChange="handleCurrentChange"
      v-loading="tableLoading"
    ></z-table>
    <!-- 新增/编辑dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form :model="dialogData" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <!--不生成创建时间字段-->
        <el-form-item label="订单号:" prop="orderNo">
          <el-input v-model="dialogData.orderNo"></el-input>
        </el-form-item>
        <el-form-item label="脉冲通道:" prop="passageNo">
          <el-input v-model="dialogData.passageNo"></el-input>
        </el-form-item>
        <el-form-item label=":" prop="couponUsageId">
          <el-input v-model="dialogData.couponUsageId"></el-input>
        </el-form-item>
        <el-form-item label="脉冲数:" prop="pulse">
          <el-input v-model="dialogData.pulse"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')" :loading="btnLoading">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 审核弹窗 -->
    <el-dialog :visible.sync="adVisible" width="30%">
      <div slot="title">
        <i class="el-icon-warning" style="color: #E6A23C;"></i> &nbsp;提示
      </div>
      <p>{{adTitle}}</p>
      <span slot="footer" class="dialog-footer">
        <el-button type="success" @click="confirmVerify(1)">审核通过</el-button>
        <el-button type="danger" @click="confirmVerify(0)">审核拒绝</el-button>
        <el-button @click="adVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import ZTable from "@/components/z-table/index";
import ZSearch from "@/components/z-search/index";
import { getList, del, add, edit, mulDel, setAdState, verify } from "./api.js";
import { tableColumns, searchItems, funcs, lineFuncs } from "./data";
import * as util from "@/common/js/util";

export default {
  data() {
    return {
      tableData: [],
      page: {
        total: 0,
        currentPage: 1
      },
      tableColumns,
      searchItems,
      tableLoading: false,
      btnLoading: false,
      searchForm: {},
      dialogTitle: "新增",
      dialogVisible: false,
      labelWidth: "100px",
      dialogData: {
        schemalist: []
      },
      allDev: [],
      adTitle: "审核通过后，该条广告可用于下发到设备。确定要审核通过么？",
      adVisible: false,
      row: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.tableLoading = true;
      const params = { page: this.page.currentPage, ...this.searchForm };
      console.log(params);
      getList(params).then(res => {
        this.tableLoading = false;
        this.tableData = res.datas.list;
        this.page.total = res.datas.itemCounts;
      });
    },
    search(searchForm) {
      this.page.currentPage = 1;
      this.searchForm = searchForm;

      this.getList();
    },
    func({ opera, row }) {
      switch (opera) {
        case "修改":
          this.edit(row);
          break;
        case "新增":
          this.add();
          break;
        case "删除":
          this.mulDel(row);
          break;
        case "审核":
          this.verify(row);
          break;
        case "查看":
          this.view(row);
          break;
      }
    },
    lineFunc({ opera, row }) {
      switch (opera) {
        case "查看":
          this.view(row);
          break;
        case "删除":
          this.del(row);
          break;
        case "广告审核":
          this.setAdState(row);
          break;
      }
    },
    add() {
      // this.dialogTitle = "新增";
      // this.dialogData = {};
      // this.dialogVisible = true;
      // 新增跳转至新增页
      this.$router.push({ path: "/serve/ad/edit" });
    },
    confirmVerify(val) {
      let state = null;
      let adId = this.row.id;
      if (val) {
        state = 2;
      } else {
        state = 3;
      }
      verify({ state, adId }).then(res => {
        this.$message({
          message: res.ret === 0 ? "设置成功" : res.msg,
          type: res.ret === 0 ? "success" : "error"
        });
        this.getList();
        this.adVisible = false;
      });
    },
    verify(row) {
      console.log(row);
      this.row = row;
      // 判断若为
      if (row.state === 1) {
        this.$message({
          message: "上架中广告无法修改",
          type: "warning"
        });
        return;
      } else if (row.state === 0) {
        // this.adTitle = "";
      }
      this.adVisible = true;
    },
    setAdState(row) {
      // 只有是未上架的才可以上架，只有是
      console.log(row.state);
      if (row.state === 2 || row.state === 3) {
        this.$message({
          message: "该条广告未通过审核无法上架",
          type: "warning"
        });
        return;
      }
      let adId = row.id;
      let title = "确认上架该广告？"; // 标题
      let state = 0; // 设置状态
      switch (row.state) {
        case 0:
          title = "确认上架该广告？";
          state = 1;
          break;
        case 1:
          title = "确认下架该广告？";
          state = 0;
          break;
      }
      this.$confirm(`${title}`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          setAdState({ state, adId }).then(res => {
            this.$message({
              message: res.ret === 0 ? "设置成功" : res.msg,
              type: res.ret === 0 ? "success" : "error"
            });
            this.getList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    view(row) {
      console.log(row);
      this.$router.push({
        path: "/serve/ad/edit",
        query: { row: JSON.stringify(row) }
      });
      // 跳转页面
    },
    edit(row) {
      this.dialogTitle = "编辑";
      this.dialogData = { ...row };
      this.dialogVisible = true;
    },
    mulDel(rows) {
      if (rows !== undefined) {
        this.$confirm("此操作将永久删除所选数据, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let idList = rows.map(item => item.id);
            mulDel({ idList }).then(res => {
              this.$message({
                message: res.ret === 0 ? "删除成功" : res.msg,
                type: res.ret === 0 ? "success" : "error"
              });
              this.getList();
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    del(row) {
      row = row.id;
      this.$confirm("此操作将永久删除所选数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          del({ id: row }).then(res => {
            this.$message({
              message: res.ret === 0 ? "删除成功" : res.msg,
              type: res.ret === 0 ? "success" : "error"
            });
            this.getList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.getList();
    },
    submitForm(formName) {
      //不解析下面代码

      this.$refs[formName].validate(valid => {
        if (valid) {
          const params = this.dialogData;
          if (this.dialogTitle === "编辑") {
            edit({
              id: this.dialogData.id,
              data: { ...this.dialogData }
            }).then(res => {
              if (res.ret == 0) {
                this.$message({
                  message: "修改成功",
                  type: "success"
                });
                this.dialogVisible = false;
                this.getList();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          } else if (this.dialogTitle === "新增") {
            add(params).then(res => {
              if (res.ret == 0) {
                this.$message({
                  message: "新增成功",
                  type: "success"
                });
                this.dialogVisible = false;
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
    funcs() {
      let _funcs = [];
      // 根据menus判断是否显示审核按钮
      let menus = this.$store.state.auth.menus;
      menus.map(item => {
        if (item.name === "设备待机广告") {
          item.list.map(subItem => {
            _funcs.push(subItem.name);
          });
        }
      });
      return _funcs;
    },
    lineFuncs() {
      return lineFuncs;
    }
  },
  components: { ZTable, ZSearch }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
