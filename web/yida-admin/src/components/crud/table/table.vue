<template>
  <div class="table">
    <div class="funcBar">
      <el-button type="success" v-if="tableFunc.includes('add')" @click="add">添加</el-button>
      <el-button type="danger" v-if="tableFunc.includes('mulDel')" @click="mulDel">删除</el-button>
      <el-button v-if="tableFunc.includes('import')" @click="_import">导入</el-button>
      <el-button v-if="tableFunc.includes('export')" @click="_export">导出</el-button>
      <el-button v-if="tableFunc.includes('generate')" @click="generate">生成滤芯</el-button>
    </div>
    <el-table
      border
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" v-if="tableFunc.includes('select')"></el-table-column>

      <!-- <el-table-column v-for="item in tableColumn" :key="item.label" :label="item.label" :width="item.width" :prop="item.key" :resizable="false" :formatter="item.formatter">
      </el-table-column>-->
      <el-table-column
        v-for="item in tableColumn"
        :key="item.label"
        :label="item.label"
        :width="item.width"
        :resizable="false"
      >
        <template slot-scope="scope">
          <!-- 不需要formatter -->
          <span v-if="!item.formatter">{{scope.row[item.key]}}</span>
          <!-- 需要formatter -->
          <span v-if="item.formatter" v-html="item.formatter(scope.row, scope.row[item.key])"></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" :width="operaWidth">
        <template slot-scope="scope">
          <el-button v-if="tableFunc.includes('view')" type="text" @click="view(scope.row)">查看</el-button>
          <el-button v-if="tableFunc.includes('qrCode')" type="text" @click="qrCode(scope.row)">二维码</el-button>
          <el-button v-if="tableFunc.includes('unbind')" type="text" @click="unbind(scope.row)">解绑</el-button>
          <el-button
            v-if="tableFunc.includes('singleStatus')"
            type="text"
            @click="singleStatus(scope.row)"
          >{{scope.row.enableMultipleBind === 1 ? '关闭' : '开启'}}重绑</el-button>
          <el-button
            v-if="tableFunc.includes('onSell')"
            type="text"
            @click="onSell(scope.row)"
          >{{scope.row.state === 1 ? '下': '上'}}架</el-button>
          <el-button v-if="tableFunc.includes('edit')" @click="edit(scope.row)">修改</el-button>
          <el-button v-if="tableFunc.includes('connectLog')" @click="connectLog(scope.row)">连接记录</el-button>
          <el-button v-if="tableFunc.includes('del')" type="danger" @click="del(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    tableData: {
      // 表格数据
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
    }
  },
  data() {
    return {
      multipleSelection: []
    };
  },
  methods: {
    del(row) {
      this.$emit("del", row);
    },
    unbind(row) {
      this.$emit("unbind", row);
    },
    view(row) {
      this.$emit("view", row);
    },
    mulDel() {
      if (this.multipleSelection.length < 1) {
        this.$message({
          message: "请至少选择一项",
          type: "warning"
        });
        return;
      }
      this.$emit("mulDel", this.multipleSelection);
    },
    qrCode(row) {
      this.$emit("qrCode", row);
    },
    singleStatus(row) {
      this.$emit("singleStatus", row);
    },
    add() {
      this.$emit("add");
    },
    edit(row) {
      this.$emit("edit", row);
    },
    connectLog(row) {
      this.$emit("connectLog", row);
    },
    onSell(row) {
      this.$emit("onSell", row);
    },
    _import() {
      this.$emit("_import");
    },
    _export() {
      this.$emit("_export", this.multipleSelection);
    },

    generate() {
      this.$emit("generate");
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
