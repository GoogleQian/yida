<template>
    <div class="page">
        <el-pagination @current-change="handleCurrentChange" :current-page.sync="pageParams.page" :page-size="pageParams.pageSize" layout="prev, pager, next, jumper" :total="pageParams.total">{{pageParams.total}}
        </el-pagination>
    </div>
</template>

<script>
export default {
  props: {
    pageParams: {
      type: Object,
      default: function() {
        return {
          page: 1,
          pageSize: 10,
          total: 0
        };
      }
    }
  },
  methods: {
    // handleSizeChange(val) {
    //   this.$emit("pageSizeChange", val);
    // },
    handleCurrentChange(val) {
      this.$emit("pageChange", val);
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
