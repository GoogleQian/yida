import Layout from "@/layout/layout";
// 登录页路由
const loginRoutes = [
  {
    path: "/login",
    component: () => import("@/views/login/index")
  }
];

// 在左侧菜单中
//数据统计路由
const totalRoutes = [
  {
    path: "/total",
    component: Layout,
    redirect: "/total/admin",
    meta: { name: "数据统计", path: "/total", icon: "iconxitongzonglan" },
    childInLeft: false,
    children: [
      {
        path: "admin",
        component: () => import("@/views/deviceAll/deviceAll"),
        meta: { name: "数据统计", path: "/total", icon: "iconxitongzonglan" }
      }
    ]
  }
];

// 设备管理路由
const devRoutes = [
  {
    path: "/dev",
    component: Layout,
    redirect: "/dev/admin",
    meta: { name: "设备管理", path: "/dev", icon: "iconshebeiguanli" },
    childInLeft: true,
    children: [
      {
        path: "admin",
        component: () => import("@/views/dev-admin/dev-admin"),
        meta: { name: "净水器", path: "/dev/admin", icon: "iconjingshuiqi" }
      },
      {
        path: "filter",
        component: () => import("@/views/filter-admin/filter-admin"),
        meta: { name: "滤芯", path: "/dev/filter", icon: "iconlvxin" }
      },
      // {
      //   path: "firmware",
      //   component: () => import("@/views/firmware/firmware"),
      //   meta: {
      //     name: "固件管理",
      //     path: "/dev/firmware",
      //     icon: "icongujianguanli"
      //   }
      // },
      // {
      //   path: "level",
      //   component: () => import("@/views/level/level"),
      //   meta: {
      //     name: "升级任务",
      //     path: "/dev/level",
      //     icon: "iconshengji2"
      //   }
      // }
    ]
  }
];

// 滤芯管理路由
// const filterRoutes = [
//   {
//     path: "/filter",
//     component: Layout,
//     redirect: "/filter/admin",
//     meta: { name: "滤芯管理", path: "/filter", icon: "icon-kongqilvqingqi" },
//     childInLeft: false,
//     children: [
//       {
//         path: "admin",
//         component: () => import("@/views/filter-admin/filter-admin"),
//         meta: { name: "滤芯管理", path: "/filter", icon: "icon-kongqilvqingqi" }
//       }
//     ]
//   }
// ];

// 客户端上下线记录
const clientRoutes = [
  {
    path: "/emqclient",
    component: Layout,
    redirect: "/emqclient",
    meta: {
      name: "客户端连接记录",
      path: "/emqclient",
      icon: "icon-kongqilvqingqi"
    },
    childInLeft: false,
    children: [
      {
        path: "admin",
        component: () => import("@/views/emqclient/emqClient"),
        meta: {
          name: "客户端连接记录",
          path: "/emqclient",
          icon: "icon-kongqilvqingqi"
        }
      }
    ]
  }
];

// 用户管理
const userRoutes = [
  {
    path: "/user",
    component: Layout,
    redirect: "/user/mp",
    meta: { name: "用户管理", path: "/user", icon: "iconyonghuguanli" },
    childInLeft: true,
    children: [
      {
        path: "mp",
        component: () => import("@/views/user-admin/user-admin"),
        meta: {
          name: "微信小程序",
          path: "/user/mp",
          icon: "iconweixinxiaochengxu"
        }
      },
      {
        path: "info",
        component: () => import("@/views/userinfo/userinfo"),
        meta: {
          name: "用户信息",
          path: "/user/info",
          icon: "iconyonghuxinxi"
        }
      }
    ]
  }
];

// 增值服务管理
const serveRoutes = [
  {
    path: "/serve",
    component: Layout,
    redirect: "/serve/banner",
    meta: { name: "增值服务管理", path: "/serve", icon: "iconzengzhifuwu" },
    childInLeft: true,
    children: [
      {
        path: "banner",
        component: () => import("@/views/serve/banner/banner"),
        meta: {
          name: "小程序banner",
          path: "/serve/banner",
          icon: "iconbanner"
        }
      },
      {
        path: "goods",
        component: () => import("@/views/serve/goods/goods"),
        meta: {
          name: "小程序滤芯购买",
          path: "/serve/goods",
          icon: "icongoumai"
        }
      },
      {
        path: "ad",
        component: () => import("@/views/serve/ad/ad"),
        meta: {
          name: "设备待机广告",
          path: "/serve/ad",
          icon: "iconguanggao"
        }
      },
      {
        path: "ad-reply",
        component: () => import("@/views/serve/ad-reply/ad-reply"),
        meta: {
          name: "广告应答",
          path: "/log/ad-reply",
          icon: "iconyingda"
        }
      }
    ]
  }
];

// 固件升级
// const firemwareRoutes = [
//   {
//     path: "/firmware",
//     component: Layout,
//     redirect: "/firmware/firmware",
//     meta: { name: "固件升级", path: "/firmware", icon: "icon-gujianshengji" },
//     childInLeft: false,
//     children: [
//       {
//         path: "firmware",
//         component: () => import("@/views/firmware/firmware"),
//         meta: { name: "固件升级", path: "/firmware", icon: "icon-gujianshengji" }
//       }
//     ]
//   }
// ];
// 日志管理
const logRoutes = [
  {
    path: "/log",
    component: Layout,
    redirect: "/log/loginLog",
    meta: { name: "日志管理", path: "/log", icon: "iconrizhiguanli" },
    childInLeft: true,
    children: [
      {
        path: "loginLog",
        component: () => import("@/views/log/loginLog/loginLog"),
        meta: {
          name: "登录日志",
          path: "/log/loginLog",
          icon: "icondenglu-copy"
        }
      },
      {
        path: "operateLog",
        component: () => import("@/views/log/operateLog/operateLog"),
        meta: {
          name: "操作日志",
          path: "/log/operateLog",
          icon: "iconcaozuorizhi"
        }
      },
      {
        path: "errorLog",
        component: () => import("@/views/log/errorLog/errorLog"),
        meta: {
          name: "异常日志",
          path: "/log/errorLog",
          icon: "iconunusual"
        }
      }
    ]
  }
];
// 设置
export const setRoutes = [
         {
           path: "/system",
           component: Layout,
           redirect: "/system/account",
           meta: { name: "设置", path: "/system", icon: "iconziyuan" },
           childInLeft: true,
           children: [
             {
               path: "account",
               component: () => import("@/views/system/account/account"),
               meta: {
                 name: "账号管理",
                 path: "/system/account",
                 icon: "iconzhanghaoguanli"
               }
             },
             {
               path: "role",
               component: () => import("@/views/system/role/role"),
               meta: {
                 name: "角色管理",
                 path: "/system/role",
                 icon: "icontubiaozhizuomoban-copy"
               }
             }
           ]
         }
       ];

// 不在左侧菜单中
const otherRoutes = [
  {
    path: "/",
    redirect: "/total",
    component: Layout
  },
  {
    path: "/user",
    component: Layout,
    children: [
      {
        path: "/user/devbind",
        component: () => import("@/views/user-admin/devbind/devbind")
      }
    ]
  },
  {
    path: "/serve",
    component: Layout,
    children: [
      {
        path: "banner/edit",
        component: () => import("@/views/serve/banner/edit/edit"),
        meta: { name: "banner修改" }
      },
      {
        path: "goods/edit",
        component: () => import("@/views/serve/goods/edit/edit"),
        meta: { name: "商品修改" }
      },
      {
        path: "ad/edit",
        component: () => import("@/views/serve/ad/edit/edit"),
        meta: { name: "广告修改" }
      }
    ]
  },
  {
    path: "/dev",
    component: Layout,
    children: [
      {
        path: "level/desc",
        component: () => import("@/views/level/desc/desc"),
        meta: { name: "升级详情" }
      }
    ]
  }
];

export const appRoutes = [
  ...totalRoutes,
  ...userRoutes,
  ...devRoutes,
  ...serveRoutes,
  ...logRoutes
];
const routes = [...loginRoutes, ...appRoutes, ...otherRoutes, ...setRoutes];
export default routes;
