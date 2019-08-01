<template>
  <div class="user-admin">
    <crud :tableData="dataList" :tableColumn="tableColumn" :tableFunc="tableFunc" :pageParams="pageParams" @pageChange="pageChange" @del="del" @view="view" :queryItem="queryItem" @query="query" @mulDel="mulDel" :loading="loading"></crud>
  </div>
</template>

<script>
import crud from "@/components/crud/crud";
import { devList, userDel, userMulDel } from "@/service/user";
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
      loading: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      devList({ ...this.pageParams, ...this.queryParams }).then(res => {
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
    del(row) {
      this.$confirm(
        `你确定删除“<span style="font-weight:700">${
          row.nickName
        }</span>”账号信息吗?`,
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
          userDel({ id: row.id }).then(res => {
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
      // 跳转页面
      this.$router.push({ path: "/user/devbind", query: { userId: row.id } });
    },
    mulDel(list) {
      this.$confirm(`你确定删除这些账号信息吗?`, "删除用户", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          // 触发删除
          const idList = list.map(item => item.id);
          userMulDel({ idList }).then(res => {
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
    query(queryParams) {
      this.pageParams.page = 1;
      this.queryParams = { ...queryParams };
      this.getList();
    }
  }
};
</script>

<style scoped lang="less">
</style>
