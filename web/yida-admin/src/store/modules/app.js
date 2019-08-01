const app = {
    state: {
        count: 1,
        isCollapse: false,  // 菜单展开合并
        breadList: [],  // 面包屑导航
    },
    mutations: {
        toogleCollapse(state) {
            state.isCollapse = !state.isCollapse;
        },
        setBreadList(state, list){
            state.breadList = [...list]
        }
    }
}

export default app