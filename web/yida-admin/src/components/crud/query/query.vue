<template>
  <div class="query">
    <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-row>
        <el-col :span="20">
          <el-form-item
            v-if="item.type === 'text'"
            :label="item.label"
            v-for="item in queryItem"
            :key="item.key"
          >
            <el-input v-model="queryParams[item.key]"></el-input>
          </el-form-item>
          <el-form-item
            v-if="item.type === 'select'"
            :label="item.label"
            v-for="item in queryItem"
            :key="item.key"
          >
            <el-select
              v-model="value"
              :placeholder="item.placeholder"
              @change="selChange"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4" style="display: flex;justify-content: flex-end">
          <el-form-item>
            <el-button type="primary" @click="onSubmit">搜索</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    queryItem: {
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
    }
  },
  data() {
    return {
      queryParams: {},
      value:null
    };
  },
  methods: {
    onSubmit() {
      this.$emit("query", this.queryParams);
    },
    selChange(val){
      // console.log(1,val);
      this.value = val;
      if (val === 0 || val === 1) {
        this.queryParams.online = val;
      }else{
        this.queryParams.online = null;
      }
      if (val === 0 || val === 1) {
        this.queryParams.status = val;
      }else{
        this.queryParams.status = null;
      }

    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
