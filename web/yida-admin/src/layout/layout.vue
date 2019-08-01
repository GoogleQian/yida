<template>
  <div class="appwrapper">
    <el-container>
      <el-aside style="max-width: 201px;">
        <left-bar class="leftBar"></left-bar>
      </el-aside>
      <el-container>
        <el-header style="height: 44px;">
          <header-bar class="header"></header-bar>
        </el-header>
        <bread-crumb></bread-crumb>
        <el-main>
          <app-main class="app-main"></app-main>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import HeaderBar from "@/layout/header/header.vue";
import LeftBar from "@/layout/left/left.vue";
import AppMain from "@/layout/main/main.vue";
import BreadCrumb from "@/layout/breadcrumb/breadcrumb.vue";
export default {
  components: {
    HeaderBar,
    LeftBar,
    AppMain,
    BreadCrumb
  }
};
</script>

<style scoped lang="less">
.appwrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
  min-width: 1200px;
  .el-header {
    background: #2a3b4c;
    padding-right: 0;
  }
  .el-main{
    background: #f0f2f5;
    height: 100%;
  }
}
</style>