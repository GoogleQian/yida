<template>
  <div class="breadcrumb">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-for="item in breadList" :key="item.name" :to="{path: item.path}">{{item.name}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
import { breadMenu } from "./data";
export default {
  data() {
    return {};
  },
  computed: {
    breadList() {
      return breadMenu[this.$route.matched[1].path];
    }
  }
};
</script>

<style lang="less" scoped>
.el-breadcrumb {
  line-height: 48px;
  padding-left: 30px;
}
</style>
