<template>

  <a-layout class="pageAll">
    <a-layout-sider
            breakpoint="sm"
            collapsed-width="0"
            v-model="collapsed"
            :trigger="null"
            collapsible>
      <!-- theme="light" -->
      <div>
        <img src="@/assets/coining.png" style="margin-left: 10px;width: 20%;float: left"/>
        <div style="margin-top: 10px;color: white;font-weight: 500;font-size: 30px">COINING</div>
      </div>
      <a-menu style="margin-top: 12px" theme="dark" mode="inline" :open-keys="openNavList"  @openChange="onOpenNav">
        <a-sub-menu :key="nav.MenuID" v-for="(nav) in NavBarData">
          <span slot="title"><a-icon :type="nav.Icon" />{{nav.MenuTitle}}</span>
          <a-menu-item :key="nChild.MenuID" v-for="(nChild) in nav.Children">
            <router-link :to="nChild.MenuPath">{{nChild.MenuTitle}}</router-link>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout >

      <a-layout-header class="pageHead">
        <a-icon :type="collapsed ? 'menu-unfold' : 'menu-fold'"
                class="triggerIcon headTool" @click="() => (collapsed = !collapsed)" />

        <a-switch class="openMethod headTool" checked-children="Tab" un-checked-children="Cover" :checked="isOpenRemember"  @change="changeOpenType" />
        <div style="right: 50px;position: absolute ">

          <a-dropdown-button>
            {{this.username}}
            <a-menu slot="overlay" >

                <a-menu-item @click = "goToLogin"> <a-icon type="logout" />点击登出</a-menu-item>

                <a-menu-item @click = "goToRegister"> <a-icon type="login" />账户注册</a-menu-item>

            </a-menu>
            <a-icon slot="icon" type="user" />
          </a-dropdown-button>
        </div>
      </a-layout-header>
      <a-layout-content class="pageCont">
        <a-spin  tip="Loading..." :spinning="loading" >

          <a-tabs v-show='isOpenRemember' size="small" class="pgTab"
                  :activeKey="actPage"
                  hide-add type="editable-card"
                  @tabClick="changePage"  @edit="onDelPage" style="padding-bottom:16px">
            <a-tab-pane v-for="(pg,pgInd) in pageTabList" :key="pgInd" :tab="pg.title" :closable="pgInd!=0">

            </a-tab-pane>
          </a-tabs>

          <router-view class="moduleAll" :class="{hasTab:isOpenRemember}" />
        </a-spin>
      </a-layout-content>
      <a-layout-footer style="textAlign: center">
        Use Ant Design ©2021 Created by COINING67
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script>
  import {mapState} from 'vuex';
  import {getKGProjectAPI} from "../api/Info";
  import {message} from "ant-design-vue";
  import $ from "jquery";
  import callUtils from "../utils/callUtils";
  import Cookies from 'js-cookie'

  export default {
    name: 'Home',
    computed: {
      ...mapState([
        'isOpenRemember',
        'pageTabList',
        'actPage',
      ]),
    },
    data () {
      return {
        UpdateGraph: true,
        graph_visible: true,
        typesetting_visible:false,

        loading:false,
        collapsed: false,
        openNavList:[],
        NavBarData:[],
        username:"",
        userId:Cookies.get('userId'),
      }
    },
    watch:{
      collapsed(val,oldval){
        callUtils.$emit('getCol',{val});
      }
    },
    mounted() {
      callUtils.$on('getKGProject',(Home)=>{
        this.getKGProject();
      });
      let data=this.collapsed
      callUtils.$emit('getCol',{data});
      //console.log('upload page!')
      this.getKGProject()
      this.username = Cookies.get('storeUsername')
    },
    methods: {
      async getKGProject() {
        this.loading = true;
        getKGProjectAPI(this.userId).then(res => {

          this.NavBarData = $.parseJSON(res.data.content);
          if(!res){
            message.error('加载项目目录出错！');
          }
          this.loading = false;
        })
      },

      changeOpenType(e){
        this.$store.commit('changeOpenRemember',e)
      },
      changePage(e){
        if(e!=this.actPage){
          this.$store.commit('changeActPage',e)
          this.$router.push({path:this.pageTabList[e].path})
        }

      },
      onDelPage(e){
        let arr=this.pageTabList,len=arr.length;
        if(e==len-1&&this.actPage==e){//删的是最后一页
          this.$router.push({path:this.pageTabList[len-2].path})
        }
        this.$store.commit('delPage',e)
      },
      onOpenNav(e){
        let endKey=e.pop();
        this.openNavList=endKey?[endKey]:[];
      },
      goToLogin(){
        this.$store.commit('clearPageTabList',[])
        sessionStorage.clear()
        Cookies.remove('storeUsername')
        Cookies.remove('userId')

        this.$router.push({path:'/login'});
      },

      goToRegister(){
        this.$store.commit('clearPageTabList',[])
        sessionStorage.clear()
        Cookies.remove('storeUsername')
        Cookies.remove('userId')
        this.$router.push({path:'/register'});
      },

    }
  }
</script>

<style lang="scss" scoped>
/*.home {
  height: 100%;
}
.button{
  position:fixed;
  top:30px;
  right:300px;
}*/
.pageAll{
  width: 100vw;
  height:100vh;
  .Navlogo{
    display: flex;
    align-items: center;

  }
  .pageHead{
    background-color: white;
    height: 56px;
    padding: 0 ;
    display: flex;
    align-items: center;
    .headTool{
      margin: 0 15px;
    }
    .triggerIcon {
      font-size: 18px;
      line-height: 100%;
      cursor: pointer;
      color: #001529;
      transition: color 0.3s;
    }
    // .openMethod{
    // }
  }
  .pageCont{
    overflow: hidden;
    padding-top: 18px;
    padding-left: 24px;
    padding-right: 24px;
    padding-bottom: 18px;
    height: 100%;
    .pgTab/deep/{
      .ant-tabs-bar{
        margin: 0;
      }
      /deep/.ant-tabs-card-bar,.ant-tabs-tab{
        height: 26px;
        line-height: 26px;
        font-size: 12px;
      }
    }
    .moduleAll{
      width: 100%;
      background-color: #ffffff;
      height: calc(100vh - 64px - 20px);
      overflow-y: auto;
      &.hasTab{

        height: calc(100vh - 64px - 20px - 26px);
      }
    }
  }

}
</style>
