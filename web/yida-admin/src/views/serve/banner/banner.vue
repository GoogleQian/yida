<template>
  <div class="banner">
    <crud :tableData="dataList" :tableColumn="tableColumn" :tableFunc="tableFunc" :pageParams="pageParams" @pageChange="pageChange" @del="del" @view="view" @add="add" @onSell="onSell" :queryItem="queryItem" @query="query" @mulDel="mulDel" :loading="loading"></crud>
  </div>
</template>

<script>
import crud from "@/components/crud/crud";
import {
  bannerList,
  bannerAdd,
  bannerEdit,
  devBanner,
  devMulBanner,
  onSellStatus
} from "@/service/banner";
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
      bannerList({
        ...this.pageParams,
        ...this.queryParams
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
    del(row) {
      this.$confirm(
        `你确定删除<span style="font-weight:700">“${
          row.name
        }”</span>服务信息吗?`,
        "删除服务",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          // 触发删除
          devBanner({ id: row.id }).then(res => {
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
      // 跳转页面edit
      this.$router.push({ path: "/serve/banner/edit", query: { row: JSON.stringify(row) } });
    },
    mulDel(list) {
      this.$confirm(`你确定删除这些服务信息吗?`, "删除服务", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          // 触发删除
          const idList = list.map(item => item.id);
          devMulBanner({ idList }).then(res => {
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
    onSell(row) {
      this.$confirm(
        `你确定对<span style="font-weight: 700">“${row.name}”</span>${
          row.state === 1 ? "下" : "上"
        }架?`,
        `${row.state === 1 ? "下" : "上"}架`,
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          // 触发
          let state = null;
          if (row.state === 1) {
            state = 0;
          } else {
            state = 1;
          }
          onSellStatus({
            id: row.id,
            state
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
            message: "已取消上架"
          });
        });
    },
    add() {
      this.$router.push({path: '/serve/banner/edit'})
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
