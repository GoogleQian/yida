<template>
  <div class="container">
    <web-view :url="url"></web-view>
  </div>
</template>

<script>
import store from "@/store/index";
const dataArr = [];
export default {
  computed: {
    data() {},
    url() {
      return store.state.url;
    }
  },
  onLoad() {
    Object.assign(this.$data, this.$options.data());
    // fetch some data
    dataArr.push({ ...this.$data });
  },
  onUnload() {
    dataArr.pop();
    const dataNum = dataArr.length;
    if (!dataNum) return;
    Object.assign(this.$data, dataArr[dataNum - 1]);
  }
};
</script>

<style>
</style>
