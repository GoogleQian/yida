import Vue from 'vue'
import App from './App.vue'
import router from './router/'
import store from './store/index'
import Element from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'

import "@/common/css/index.less"

Vue.use(Element, { size: 'mini' });

Vue.config.productionTip = false
new Vue({
  router,
  store,
  el: '#app',
  render: h => h(App)
});