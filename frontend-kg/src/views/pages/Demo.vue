<template>
  <div class="graph-editor">
    <div class="graph-toolbar">
      <div class="toolbar-container">
        <div title="保存"><a-icon class="tool-icon" type="save" @click="saveWeb"/></div>
        <div title="刷新"><a-icon class="tool-icon tool-div" type="file-sync" @click="uploadGraph"/></div>
        <div><a-divider  style="margin-left: 15px;margin-right: 15px;font-size: 21px" type="vertical"/></div>
        <div title="导出图片"><a-icon class="tool-icon" type="picture" @click="exportPic"/></div>
        <div title="导出文件"><a-icon class="tool-icon tool-div" type="file-text" @click="exportFile"/></div>
        <div><a-divider  style="margin-left: 15px;margin-right: 15px;font-size: 21px" type="vertical"/></div>
        <div title="添加"><a-icon class="tool-icon" type="plus" @click="addNL"/></div>
        <div title="删除"><a-icon class="tool-icon tool-div" type="minus" @click="deleteNL"/></div>
        <div><a-divider  style="margin-left: 15px;margin-right: 15px;font-size: 21px" type="vertical"/></div>
        <div title="还原缩放"><a-icon class="tool-icon" type="home" @click="reset"/></div>
        <div title="取消过滤"><a-icon class="tool-icon tool-div" type="rollback" @click="rollback"/></div>
        <div title="搜索"><a-icon class="tool-icon" type="search" style="margin-left: 20px;" v-if="!isSearch" @click="set_isSearch"/></div>
        <div title="搜索info" v-show="isSearch">
          <a-select
            show-search
            placeholder="选择节点或者关系"
            option-filter-prop="children"
            style="width: 250px;margin-left: 20px;"
            :filter-option="filterOption"
            @change="handleChange"
        >
            <a-select-option value="disabled" disabled>
              以下为历史搜索记录：
            </a-select-option>
            <a-select-option v-for="d in this.$store.state.history_list" :key="d.kd+'e'">
              {{d.text}}
            </a-select-option>
            <a-select-option value="disable" disabled>
              以下为关系以及节点列表：
            </a-select-option>
          <a-select-option v-for="d in this.$store.state.result_list" :key="d.kd">
            {{d.text}}
          </a-select-option>
        </a-select>
        </div>
        <div><a-icon class="tool-icon" type="close-circle" style="margin-left: 10px;" v-show="isSearch" @click="closeSearch"/></div>

      </div>
        <div style="right: 30px;position: absolute ">
            <a-dropdown :trigger="['click']">
                <a class="ant-dropdown-link" @click="e => e.preventDefault()">
                    <a-icon type="ellipsis" class="tool-icon tool-div"/>
                </a>

            <a-menu slot="overlay" >
                <a-menu-item @click = "fuse()"> <a-icon type="retweet" />图谱融合</a-menu-item>

                <a-menu-item @click = "dele()"> <a-icon type="delete" />删除图谱</a-menu-item>

                <a-menu-item @click = "rename()"> <a-icon type="redo" />图谱更名</a-menu-item>
            </a-menu>
        </a-dropdown>
        </div>
    </div>

    <Graph ref="Graph" v-bind:id = "id" v-bind:ids = "ids" @handleChange="handleChange" @close_Search="closeSearch"></Graph>
    <AddNLModal @add_node="add_node" @add_link="add_link"></AddNLModal>
    <fuseGraph ref="fuseGraph"></fuseGraph>

  </div>
</template>

<script>
  import Graph from "../../components/graph";
  import AddNLModal from "../../components/WorkTree/AddNLModal";
  import fuseGraph from "../../components/WorkTree/fuseGraph";
  import {message} from "ant-design-vue";
  import {getKGProjectAPI, renamePageAPI, deletePageAPI, fusionAPI} from "@/api/Info";
  import Cookies from "js-cookie";
  import callUtils from "@/utils/callUtils";
  export default{
    components: {AddNLModal, Graph,fuseGraph},
    data(){
      return{
        data:"",
        isSearch:false,
        id:Number(this.$route.params.id),
        ids:'',
        result_list:[],
        userId: Cookies.get('userId'),
        graphList:[],
        pageList:[],
          pageList2:[],
        actPageNum:0,
      }
    },
    watch: {
      $route(to,from){
        //console.log('demo watch',to);
        this.id = Number(this.$route.params.id);
        this.ids = this.$route.params.ids;
      }
    },
    mounted() {

      this.id = Number(this.$route.params.id);
      this.ids = this.$route.params.ids;
      //this.refresh();
      /*if(this.$route.params.ids != 'null'){
        this.filter(this.$route.params.ids);
      }*/
    },
    methods:{
      closeSearch(){
        this.isSearch=false
        this.$refs.Graph.closeSearch()
      },
      set_isSearch(){
        this.isSearch=true
      },
      handleChange(value) {
        let list_t;
        let param = '';
        if(isNaN(value)){
          value=Number(value.substr(0,value.length-1))
          list_t=this.$store.state.history_list
          for(var i in list_t){
            if(Number(list_t[i].kd)==value){
              param={
                id:list_t[i].id,
                text:list_t[i].text,
                kd:list_t[i].kd
              }
            }
          }
          this.$refs.Graph.handleChange(param)
        } else {
          list_t=this.$store.state.result_list
          for(var j in list_t){
            if(list_t[j].kd==value){
              param={
                id:list_t[j].id,
                text:list_t[j].text,
                kd:list_t[j].kd
              }
            }
          }
          this.$refs.Graph.handleChange(param)
        }
      },
      filterOption(input, option) {
        return (
            option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        );
      },
      filter(ids) {
        var m = [];
        var temp = this.$route.params.ids.split(",");
        for(var i in temp){
          m.push(Number(i));
        }
        this.$refs.Graph.filterNL(m);
      },
      reset() {
        this.$refs.Graph.reset();
      },
      rollback() {
        this.$refs.Graph.closeFilter();
      },
      saveWeb(){
        //console.log('save');
        this.$refs.Graph.save_Web(this.id);
      },
      uploadGraph(){
        //console.log('upload');
        this.$refs.Graph.uploadOtherGraph(this.id);
      },
      exportPic(){
        //console.log('exportPic');
        this.$refs.Graph.savePNG();
      },
      exportFile(){
        //console.log('exportFile');
        this.$refs.Graph.saveJSON();
      },
      addNL(){
        this.$store.commit('set_addNLVisible',true);
      },
      deleteNL(){
        //console.log('deleteNL');
        message.success('请在直接在图中进行操作！');
      },
      add_node(param){
        //console.log('add_node');
        this.$refs.Graph.addNode(param);
        this.$store.commit('set_addNLVisible',false);
      },
      add_link(param){
        //console.log('add_link');
        this.$refs.Graph.addLink(param);
        this.$store.commit('set_addNLVisible',false);
      },

      fuse(){
          this.$store.commit("set_loading",false)
          getKGProjectAPI(this.userId).then(res => {
              this.$store.commit("set_loading",true)
              let array = JSON.parse(res.data.content)
              for(let i = 0;i<array[0].Children.length;i++){
                  let ids = array[0].Children[i].MenuPath
                  let start = 0
                  let end = 0
                  let cnt = 0
                  for(let j = 0;j<ids.length;j++){
                      if(ids[j]==='/'){
                          cnt++
                          if(cnt===3){
                              start = j
                          }
                          if(cnt===4){
                              end = j
                          }
                      }

                  }
                  let id = ids.substring(start+1,end)

                  this.$set(this.graphList,i,{"ID":id,"name":array[0].Children[i].MenuTitle})
                  cnt = 0
              }
          })
          this.$forceUpdate()
          //console.log(this.graphList)
          this.$refs.fuseGraph.init(this.graphList)
          this.$store.commit('set_fuseGraphVisible',true);

      },
        dele(){

            let name = this.$route.params.name
            deletePageAPI(this.$route.params.id).then(res => {
                if (res.data.success) {
                    message.success('删除成功');
                    callUtils.$emit('getKGProject', 'Home')

                    for(let i = 0;i<this.$store.state.pageTabList.length;i++){
                        if(name!==this.$store.state.pageTabList[i].title){
                            this.pageList2.push(this.$store.state.pageTabList[i])
                        }
                    }
                    if(this.pageList2.length===0){
                        this.pageList2.push({"name": "HomeDefault", "path": "/","title": "欢迎使用"})
                    }
                    //console.log(this.pageList2)
                    this.$store.commit("set_pageList",this.pageList2)
                    this.$router.push({path:"/"})
                }else {
                    message.error("删除失败");
                }
            })
        },
        rename(){
            let code = prompt("请输入想要修改的名字:");
            if(code==="" || code===undefined){
                alert("要修改的名字不能为空")
            }else{
                let m = {"id":this.$route.params.id,"userId":Cookies.get('userId'),"name":code}
                let oldName = this.$route.params.name
                renamePageAPI(m).then(res => {
                    if (res.data.success) {
                        message.success('更名成功');
                        this.actPageNum = this.$store.state.actPage
                        for(let i = 0;i<this.$store.state.pageTabList.length;i++){
                            if(oldName===this.$store.state.pageTabList[i].title){
                                if(this.$store.state.pageTabList[i].name==="Demo"){
                                    this.pageList.push({"name":this.$store.state.pageTabList[i].name,"path":"/"+this.$store.state.pageTabList[i].name+"/"+m.name+"/"+m.id+"/"+"null","title":m.name})
                                }
                                else if(this.$store.state.pageTabList[i].name==="Typesetting"){
                                    this.pageList.push({"name":this.$store.state.pageTabList[i].name,"path":"/"+this.$store.state.pageTabList[i].name+"/"+m.name+"/"+m.id,"title":m.name})
                                }
                            }
                            else{
                                this.pageList.push(this.$store.state.pageTabList[i])
                            }
                        }
                        this.$router.push("/Demo"+"/"+code +"/"+m.id+"/"+"null")
                        this.$store.commit("changeActPage",this.actPageNum)
                        this.$store.commit("set_pageList",this.pageList)
                        callUtils.$emit('getKGProject', 'Home')
                    }else {
                        message.error("更名失败");
                    }
                })
                this.pageList = []
            }
        },
    }
  }
</script>

<style scoped>
  .graph-editor{
    border: 1px solid #e6f7ff;
    border-radius: 2px;
  }
  .graph-toolbar{
    display: flex;
    padding: 14px;
    background: #fafafa;
    border: 1px solid #e6f7ff;
    height: 46px;
  }
  .toolbar-container{
    display: flex;
    align-items: center;
  }
  .tool-icon{
    font-size: 18px;
  }
  .tool-div{
    margin-left: 20px;
  }
  .tool-icon:hover{
    color: black;
    cursor: pointer;

  }
</style>
