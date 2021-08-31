import Vue from 'vue'
import Vuex from 'vuex'
import global from "../utils/global";
Vue.use(Vuex)

import createPersistedState from 'vuex-persistedstate'

export default new Vuex.Store({
  state: {
    tag:"null",
    result_list:[],
    history_list:[],
    filterId:'',
    graphVisible:false,
    changeNodeVisible:false,
    changeLinkVisible:false,
    addNodeVisible:false,
    addRelationVisible:false,
    addNLVisible:false,
    typesettingVisible:false,
    collapsed_home:false,
    entity:{},
    gLoad: false,
    uInfo: {},
    pageTabList:[
      // {
      // 	title:"首页",
      // 	name:"HomeDefault",
      // 	path:"/Home",
      // 	canClose:false
      // }
    ],
    actPage:0,
    isOpenRemember:true,
    username:"",
    userId:0,
    chooseTripleVisible:false,
    changeTripleVisible:false,
    mainBody:"",
    relation:"",
    object:"",
    fuseGraphVisible:false,
    graphList:[],
    loading:false,
  },
  getters: {},
  mutations: {
    set_graphVisible:function (state,data){
      state.graphVisible=data
    },
    set_changeNodeVisible:function (state,data){
      state.changeNodeVisible=data
    },
    set_changeLinkVisible:function (state,data){
      state.changeLinkVisible=data
    },
    set_addRelationVisible:function (state,data){
      state.addRelationVisible=data
    },
    set_addNLVisible:function (state,data){
      state.addNLVisible=data
    },
    set_typesettingVisible:function (state,data){
      state.typesettingVisible=data
    },
    set_filterId:function (state,data){
      state.filterId=data
    },
    set_coll:function (state,data){
      state.collapsed_home=data
    },
    set_resultList:function (state,data){
      state.result_list=data
    },
    set_historyList:function (state,data){
      state.history_list=data
    },
    InitData(state) {

    },
    changeOpenRemember(state,val){
      state.isOpenRemember=val
    },
    changeActPage(state,val){
      state.actPage=val;
    },
    delPage(state,val){
      state.pageTabList.splice(val,1)
      if(state.actPage>=val){
        state.actPage-=1;
      }
    },
    pageJump(state,obj){
      console.log("pageJump");
      let arr=state.pageTabList,needPush=true;
      if(arr.length==0) {
        state.pageTabList.unshift({
          title: "欢迎使用",
          name: "HomeDefault",
          path: "/",
          // canClose:obj.path!='/Home'
        })
      }
      const path = global.getPath(obj.path)
      for (var i = 0; i < arr.length; i++) {
        if(global.getPath(arr[i].path).path === path.path){
          needPush=false;
          state.actPage=i;
        }
      }
      if(needPush){
        state.actPage=arr.length;
        state.pageTabList.push({
          title: obj.meta.title||'无标题',
          name: obj.name,
          path: path.path,
          // canClose:obj.path!='/Home'
        })
      }
    },
    hideLoad(state){
      state.gLoad=false;
    },
    showLoad(state){
      state.gLoad=true;
    },
    updateUsername(state,data){
      state.username=data;
    },
    updateUserId(state,data){
      state.userId=data;
    },
    clearPageTabList(state,data){
      state.pageTabList=data;
    },
    set_chooseTripleVisible:function (state,data){
      state.chooseTripleVisible=data
    },
    set_changeTripleVisible:function (state,data){
      state.changeTripleVisible = data
    },
    set_mainBody:function (state,data){
      state.mainBody = data
    },
    set_relation:function (state,data){
      state.relation = data
    },
    set_object:function (state,data){
      state.object = data
    },

    set_fuseGraphVisible:function (state,data){
      state.fuseGraphVisible = data
    },
    set_graphList:function (state,data){
      state.graphList = data
    },
    set_loading:function (state,data){
      state.loading = data
    },
    set_pageList:function (state,data){
      state.pageTabList = data
    },

  },
  actions: {
    initApp(context) {
      context.commit('InitData')
    }
  },
  modules: {}
})
