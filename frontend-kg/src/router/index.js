import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/pages/Login'
import Home from '../views/Home.vue'
import store from '../store/index'
import global from "../utils/global";
import EditBar from "../components/EditBar";
import InfoBar from "../components/InfoBar";
import graph from "../components/graph";
import Cookies from 'js-cookie'
Vue.use(VueRouter)


const router = new VueRouter({
    mode: 'history',
    base: '/Web/',
    routes: [{
      path: '/login',

      name: 'Login',
      meta: {title: '登录'},//requireAuth: false
      component: (resolve) => require(['@/views/pages/Login.vue'], resolve),
    },{
        path: '/register',

        name: 'Register',
        meta: {title: '注册'},//requireAuth: false
        component: (resolve) => require(['@/views/pages/Register.vue'], resolve),
    }, {
        path: '/resetPWD',

        name: 'ResetPWD',
        meta: {title: '重置密码'},//requireAuth: false
        component: (resolve) => require(['@/views/pages/ResetPWD.vue'], resolve),
    },{
        path: '/',
        name: 'Home',
        meta: {title: '主布局'},
        component: (resolve) => require(['@/views/Home.vue'], resolve),
        children: [
            {path: '/',name: 'HomeDefault',meta: {title: '欢迎使用',keepAlive: true},component: (resolve) => require(['@/views/pages/HomeDefault'], resolve),},
            /*{path: '/Demo/:name/:id',name: 'Demo',meta: {title: 'Demo',keepAlive: true},component: (resolve) => require(['@/views/pages/Demo'], resolve),},*/
            {path: '/Typesetting/:name/:id',name: 'Typesetting',meta: {title: 'Typesetting',keepAlive: true},component: (resolve) => require(['@/views/pages/Typesetting'], resolve),},
            {path: '/',name: 'HomeDefault',meta: {title: '欢迎使用',keepAlive: true},component: (resolve) => require(['@/views/pages/HomeDefault'], resolve),},
            {path: '/Demo/:name/:id/:ids',name: 'Demo',meta: {title: 'Demo',keepAlive: true},component: (resolve) => require(['@/views/pages/Demo'], resolve),},
            // 四种过滤情况
        ]
    }]
})

router.beforeEach((to, from, next) => {
    let judge = document.cookie.indexOf('userId')
    if (to.name==='Login')  {
        if(judge!==-1){
            next({
                path: '/'
            })
        }else {
            next()
        }
    }else if (to.meta.title) {
        if(judge===-1){
            if(to.name!=='Register' && to.name!=='ResetPWD'){
                next({
                    path: '/login'
                })
            }else {
                next()
            }
        }else {
            to.meta.title = to.params.name || '欢迎使用';
            document.title = to.meta.title;
            store.commit('pageJump', to)
            next()
        }
/*        }*/
    }else {
        //通知一下
        console.error('stop to errorPage')
        global.toast('页面路径无效')
    }
});

export default router
