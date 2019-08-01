<template>
  <div class="crud">
    <Query :options="options" :queryItem="queryItem" @query="query"/>
    <Table
      :tableData="tableData"
      :tableColumn="tableColumn"
      :tableFunc="tableFunc"
      @connectLog="connectLog"
      @generate="generate"
      @del="del"
      @edit="edit"
      @unbind="unbind"
      @view="view"
      @mulDel="mulDel"
      @_import="_import"
      @_export="_export"
      @add="add"
      @onSell="onSell"
      @singleStatus="singleStatus"
      @qrCode="qrCode"
      v-loading="loading"
      :operaWidth="operaWidth"
    ></Table>
    <Page @pageChange="pageChange" :pageParams="pageParams"></Page>
  </div>
</template>

<script>
import Query from "./query/query";
import Table from "./table/table";
import Page from "./page/page";
export default {
  props: {
    tableData: {
      // 表格数据
      type: Array,
      default: function() {
        return [];
      }
    },
    //下拉选择框数据
    options: {
      type: Array,
      default: function() {
        return [];
      }
    },
    operaWidth: {
      default: "200",
      type: String
    },
    tableColumn: {
      // 表格栏目
      type: Array,
      default: function() {
        return [];
      }
    },
    tableFunc: {
      // 表格特殊功能（左侧selection、删除、添加等操作）
      type: Array,
      default: function() {
        return [];
      }
    },
    pageParams: {
      type: Object,
      default: function() {
        return {
          page: 1,
          pageSize: 10,
          total: 0
        };
      }
    },
    loading: {
      type: Boolean,
      default: false
    },
    queryItem: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  methods: {
    del(row) {
      this.$emit("del", row);
    },
    unbind(row) {
      this.$emit("unbind", row);
    },
    generate(row) {
      this.$emit("generate", row);
    },
    view(row) {
      this.$emit("view", row);
    },
    edit(row) {
      this.$emit("edit", row);
    },
    connectLog(row) {
      this.$emit("connectLog", row);
    },
    mulDel(list) {
      this.$emit("mulDel", list);
    },
    _import() {
      this.$emit("_import");
    },
    _export(multipleSelection) {
      this.$emit("_export", multipleSelection);
    },
    singleStatus(row) {
      this.$emit("singleStatus", row);
    },
    qrCode(row) {
      this.$emit("qrCode", row);
    },
    add() {
      this.$emit("add");
    },
    onSell(row) {
      this.$emit("onSell", row);
    },
    pageChange(page) {
      this.$emit("pageChange", page);
    },
    query(queryParams) {
      this.$emit("query", queryParams);
    }
  },
  components: { Table, Page, Query }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
