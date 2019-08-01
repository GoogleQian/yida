<template>
    <div class="header">
        <div class="left">
            <p>智慧型即热净水机后台管理系统</p>
        </div>
        <div class="right" @click="quit" style="cursor: pointer;">
            <i class="iconfont icon-shezhi"></i>&nbsp;退出
        </div>
    </div>
</template>

<script>
export default {
  methods: {
    toggleCollapse() {
      this.$store.commit("toogleCollapse");
    },
    quit(){
      localStorage.setItem('token', null)
      this.$router.push({
        path: '/login'
      })
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
