<template>
  <div class="z-table">
    <div class="funcs">
      <!-- 功能按钮 -->
      <el-button
        v-for="item in funcs"
        :key="item"
        @click="func(item)"
        :type="item === '新增'?'success': (item === '删除'?'danger': 'primary')"
      >{{item}}</el-button>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        v-for="column in tableColumns"
        :key="column.label"
        :label="column.label"
        :width="column.width"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <!-- 可接收html格式文本 -->
          <div v-html="column.formatter(scope.row)" v-if="column.formatter"></div>
          <div v-else>{{scope.row[column.value]}}</div>
        </template>
      </el-table-column>
      <!-- 行内功能按钮 -->
      <el-table-column label="操作" fixed="right" v-if="lineFuncs.length > 0">
        <template slot-scope="scope">
          <!-- <el-button v-for="item in lineFuncs" v-if="item === '广告审核'" :key="item" type="text" @click="lineFunc(item, scope.row)">{{item}}</el-button> -->
          <el-button
            size="mini"
            v-for="item in lineFuncs"
            v-if="item !== '广告审核' && item !== '删除'"
            :key="item"
            type="text"
            @click="lineFunc(item, scope.row)"
          >{{item}}</el-button>
          <el-button
            size="mini"
            v-for="item in lineFuncs"
            v-if="item === '广告审核'"
            :key="item"
            type="text"
            @click="lineFunc(item, scope.row)"
          >{{scope.row.state === 1 ? '下架': '上架'}}</el-button>
          <el-button
            size="mini"
            v-for="item in lineFuncs"
            v-if="item === '删除'"
            :key="item"
            type="danger"
            @click="lineFunc(item, scope.row)"
          >{{item}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="page.currentPage"
      background
      :page-size="10"
      layout="prev, pager, next"
      :total="page.total"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  props: {
    tableData: {
      type: Array,
      default: function() {
        return [{ name: "zzq" }];
      }
    },
    tableColumns: {
      type: Array,
      default: function() {
        return [{ label: "姓名", value: "name" }];
      }
    },
    page: {
      type: Object,
      default: function() {
        return {
          currentPage: 1,
          total: 0
        };
      }
    },
    funcs: {
      type: Array,
      default: function() {
        return [];
      }
    },
    lineFuncs: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleCurrentChange(val) {
      this.$emit("handleCurrentChange", val);
    },
    // 向父组件触发功能函数
    func(opera) {
      if (opera === "新增") {
        // 新增不需要检测是否选中一项
        this.$emit("func", { opera, row: this.multipleSelection });
      } else if (opera === "删除") {
        if (!this.multipleSelection || this.multipleSelection.length === 0) {
          this.$message({
            message: "请至少选择一项进行操作",
            type: "warning"
          });
          return;
        } else {
          this.$emit("func", { opera, row: this.multipleSelection });
        }
      } else {
        // 非新增需要检测是否选中一项
        if (!this.multipleSelection || this.multipleSelection.length === 0 || this.multipleSelection.length > 1) {
          this.$message({
            message: "请选择一项进行操作",
            type: "warning"
          });
          return;
        }
        this.$emit("func", { opera, row: this.multipleSelection[0] });
      }
    },
    lineFunc(opera, row) {
      this.$emit("lineFunc", { opera, row });
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
