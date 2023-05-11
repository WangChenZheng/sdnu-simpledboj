import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        name: "首页",
        component: () => import("@/views/dashboard/index"),
        meta: { title: "首页", icon: "dashboard", affix: true },
      },
    ],
  },
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },
  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },
];

export const asyncRoutes = [
  {
    path: "/permission",
    component: Layout,
    redirect: "/permission/index",
    meta: { title: "权限管理", icon: "el-icon-s-order", roles: ['admin'] },
    children: [
      {
        path: "index",
        name: "角色管理",
        component: () => import("@/views/permission/role"),
        meta: { title: "角色管理", icon: "el-icon-s-order" },
      }
    ],
  },
  {
    path: "/ucenter",
    component: Layout,
    redirect: "/ucenter/list",
    meta: { title: "用户管理", icon: "el-icon-user", roles: ['admin', 'teacher'] },
    children: [
      {
        path: "list",
        name: "用户列表",
        component: () => import("@/views/ucenter/index"),
        meta: { title: "用户列表", icon: "el-icon-s-order" },
      },
      {
        path: "addUser",
        name: "添加用户",
        component: () => import("@/views/ucenter/addUser"),
        meta: { title: "添加用户", icon: "el-icon-plus" },
      },
      {
        path: "addBatchUser",
        name: "批量添加用户",
        component: () => import("@/views/ucenter/addBatchUser"),
        meta: { title: "添批量添加用户", icon: "el-icon-plus" },
      },
    ],
  },
  {
    path: "/course",
    component: Layout,
    redirect: "/course",
    meta: { title: "课程管理", icon: "el-icon-s-order" },
    children: [
      {
        path: "index",
        name: "课程学习",
        component: () => import("@/views/course/index"),
        meta: { title: "课程学习", icon: "el-icon-bell", roles: ["student"] },
      },
      {
        path: "list",
        name: "课程列表",
        component: () => import("@/views/course/list"),
        meta: { title: "课程列表", icon: "el-icon-s-order", roles: ['admin', 'teacher'] },
      },
      {
        path: "addCourse",
        name: "添加课程",
        component: () => import("@/views/course/addCourse"),
        meta: { title: "添加课程", icon: "el-icon-plus", roles: ['admin', 'teacher'] },
      },
      {
        path: "caseList",
        name: "测试案例",
        component: () => import("@/views/course/caseList"),
        meta: { title: "测试案例", icon: "el-icon-s-order", roles: ['admin', 'teacher'] },
      },
      {
        path: "addCase",
        name: "添加测试案例",
        component: () => import("@/views/course/addCase"),
        meta: { title: "添加测试案例", icon: "el-icon-plus", roles: ['admin', 'teacher'] },
      },
      {
        path: "judge",
        name: "代码评测",
        component: () => import("@/views/course/judge"),
        meta: { title: "代码评测", icon: "el-icon-circle-check", roles: ['student']  },
      },
      {
        path: "document",
        name: "文档",
        component: () => import("@/views/course/document"),
        meta: { title: "文档", icon: "el-icon-s-order", roles: ['student']  },
        hidden: true,
      },
    ],
  },
  // 404 page must be placed at the end !!!
  { path: "*", redirect: "/404", hidden: true },
];

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
