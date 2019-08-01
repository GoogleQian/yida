<template>
  <div class="left">
    <el-menu :default-active="defaultActive" class="el-menu-vertical-demo" :collapse="isCollapse" :router="true" background-color="#2F4355" text-color="#fff" active-text-color="#fff">
      <div class="h_title">
        Etar
      </div>
      <el-menu-item v-for="route in appRoutes" :key="route.path" v-if="!route.childInLeft" :index="route.path + '/admin'">
        <i :class="'iconfont '+ route.meta.icon" style="margin-right: 5px;"></i>
        <span slot="title">{{route.meta.name}}</span>
      </el-menu-item>
      <el-submenu v-for="subRoutes in appRoutes" :key="subRoutes.path" v-if="subRoutes.childInLeft" :index="subRoutes.path">
        <template slot="title"><i :class="'iconfont ' + subRoutes.meta.icon" style="margin-right: 5px;"></i>{{subRoutes.meta.name}}</template>
        <el-menu-item v-for="subRoute in subRoutes.children" :key="subRoute.path" :index="subRoutes.path + '/' + subRoute.path">
          <i :class="'iconfont '+ subRoute.meta.icon" style="margin-right: 5px;"></i>{{subRoute.meta.name}}
        </el-menu-item>
      </el-submenu>
      <!-- 动态菜单 -->
      <template v-for="router in menus">
        <el-submenu :index="`/${router.url}`" :key="router.url" v-if="router.childType === 1">
          <template slot="title"><i :class="'iconfont ' + router.icon" style="margin-right: 5px;"></i><span class="title">{{router.name}}</span></template>
          <el-menu-item v-for="subRouter in router.list" :index="`/${router.url}/${subRouter.url}`" :key="subRouter.url">
            <i :class="'iconfont ' + subRouter.icon" style="margin-right: 5px;"></i>
            <span class="title" slot="title">{{subRouter.name}}</span>
          </el-menu-item>
        </el-submenu>
        <el-menu-item :index="'/'+router.url" :key="router.url" v-else>
          <i :class="'iconfont ' + router.icon"></i>
          <span class="title" slot="title">{{router.name}}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import { appRoutes, setRoutes } from "@/router/routes";
export default {
  created() {
    // 获取nav成功后生成menu并根据route生成funcs
    this.$store.dispatch("getNav");
    console.log(this.$route);

  },
  computed: {
    defaultActive() {
      return this.$route.matched[1].path;
    },
    isCollapse() {
      return this.$store.state.app.isCollapse;
    },
    appRoutes() {
      let _appRoutes = [...appRoutes];
      let dynamicMenus = this.$store.state.auth.menus;
      // dynamicMenus.map(item => {
      //   if(item.name === '设置'){  // 有设置
      //     _appRoutes = [...appRoutes, ...setRoutes]

      //   }
      // })
      return _appRoutes;
    },
    menus() {
      const menus = [...this.$store.state.auth.menus];
      let _menus = menus.filter(item => {
        return item.name !== '设备待机广告'
      })
      return _menus;
    }
  }
};
</script>

<style scoped lang="less">
@import "./index.less";
</style>
