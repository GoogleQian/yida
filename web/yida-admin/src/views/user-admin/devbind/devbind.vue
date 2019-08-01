<template>
  <div class="user-admin">
    <crud
      :tableData="dataList"
      :tableColumn="tableColumn"
      :tableFunc="tableFunc"
      :pageParams="pageParams"
      @pageChange="pageChange"
      @unbind="unbind"
      @view="view"
      :queryItem="queryItem"
      @query="query"
      @mulDel="mulDel"
      @del="del"
      :loading="loading"
    ></crud>
  </div>
</template>

<script>
import crud from "@/components/crud/crud";
import { devList, devDel, devMulDel, devUnbind } from "@/service/devbind";
import { tableFunc, tableColumn, queryItem } from "./data";
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
        title: '新增',
        visible: false
      }
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
        title: '新增',
        visible: true
      }
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
    unbind(row) {
      this.$confirm(
        `你确定解绑“<span style="font-weight:700">${
          row.devCode
        }</span>”设备信息吗?`,
        "解绑设备",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          // 触发解绑
          devUnbind({ id: row.id }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "解绑成功"
              });
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消解绑"
          });
        });
    },
    view(row) {
      // 跳转页面
    },
    mulDel(list) {
      this.$confirm(`你确定解绑这些设备信息吗?`, "解绑设备", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          // 触发解绑
          const idList = list.map(item => item.id);
          devMulDel({ idList }).then(res => {
            if (res.ret === 0) {
              this.$message({
                type: "success",
                message: "解绑成功"
              });
              this.getList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消解绑"
          });
        });
    },
    query(queryParams) {
      this.pageParams.page = 1;
      this.queryParams = { ...queryParams };
      this.getList();
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
