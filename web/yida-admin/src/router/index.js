import Vue from 'vue';

import VueRouter from 'vue-router'
import routes from './routes'
import store from '@/store/index'
// import store from '@/store/index'
Vue.use(VueRouter)

const router = new VueRouter({
    routes
})
router.beforeEach((to, from, next) => {
    const breadTrueList = [{}]
    store.commit('setBreadList', breadTrueList);
    next();
})
export default router
