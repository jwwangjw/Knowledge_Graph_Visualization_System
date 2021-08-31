<template class="container containerLoc">
    <div class="container containerLoc">
        <div class="edit-block">
            <div class="title">
                用户项目
            </div>
            <div class="upload">
                <div style="margin-top: 10px">
                    <p style="float: left;font-size: 16px;font-weight: 700;margin-top: 4px">项目: </p>
                    <input type="file" ref="readFile" style="display: none" v-on:change="getFile($event)">
                    <select v-model="selected" @change="chooseProj" name="项目">
                        <option v-for="p in projectList" :key="p.id" :value="p.id" :label="p.filename"></option>
                    </select>
                    <a-button v-if="load" type="primary" @click="loadGraph" style="margin-left: 15px;border: 0px;background-color: #42b983">加载</a-button>
                    <a-button v-else type="primary" @click="saveGraph" style="margin-left: 15px;border: 0px">保存</a-button>
                </div>
          </div>
        </div>

        <div class="edit-block">
            <div class="title">
                分类导图
            </div>
          <div class="button">
            <el-button type="primary" plain @click="set_d3">力导图模式</el-button>
            <el-button type="success" plain @click="set_type">排版模式</el-button>
          </div>
        </div>
    </div>
</template>
<script>
import {addNewFileProjectAPI, getKGProjectAPI} from "../api/Info";
import {message} from "ant-design-vue";
import $ from "jquery";

export default {
  name: "InfoBar",
  data(){
    return{
        firstload: true,
        load: true,
        file:'',
        selected: -1,
        KGonshow: -1,
        projectList:[]
    }
  },
  mounted() {
      console.log('upload');
      this.getKGProject();
  },
  methods:{
      getKGProject(){
          getKGProjectAPI().then(res => {
              this.projectList = $.parseJSON(res.data.content);
              this.projectList.push({"filename":"...导入新项目", "id":-2});

              if(!this.firstload){
                  this.selected = this.projectList[this.projectList.length - 2].id;
                  localStorage.setItem("file_id",this.selected.toString())
                  this.loadGraph();
              }else{
                  this.firstload = false;
              }
          })
      },
      getFile(event){
          this.file = event.target.files[0];
          console.log(this.file);
          event.preventDefault();
          let formData=new FormData;
          formData.append("file",this.file);
          addNewFileProjectAPI(formData).then(res => {
              if(res.data.success){
                  message.success('上传成功');
                  let emp  = Number(localStorage.getItem("file_num"))
                  localStorage.setItem("file_num",(emp+1).toString())
                  localStorage.removeItem('pos')

                  this.getKGProject();
              }else{
                  message.error("上传失败");
                  this.selected = this.KGonshow;
              }
          });
          this.file = '';
      },
      chooseProj(){
          if(this.selected == -2){
              console.log("add file")
              this.$refs.readFile.dispatchEvent(new MouseEvent("click"))
              this.selected = this.KGonshow
          }
          if(this.KGonshow == this.selected){
              this.load = false;
          }else{
              this.load = true;
          }
      },
      loadGraph(){
          this.load = false;
          console.log("load");
          localStorage.setItem("file_id",this.selected.toString())
          this.$emit('uploadgraph',this.selected);
          this.KGonshow = this.selected;
          console.log("当前图谱id:",this.KGonshow);
      },
      saveGraph(){
          console.log("save");
          this.$emit('savegraph', this.KGonshow);
      },
      set_d3(){
          console.log("切换为力导图模式");
          this.$emit('set_d3');
      },
      set_type(){
          console.log("切换为排版模式");
          this.$emit('set_type');
      }

  },
}

</script>

<style scoped>
    @import "../assets/css/all.css";
    .containerLoc{
        left: 10px;
    }
    .upload{
        margin-top: 25px;
        margin-left: 10px;
    }
    select {
        float: left;
        width: 60%;
        height: 24pt;
        line-height: 40pt;
        padding-right: 20pt;
        text-indent: 6pt;
        text-align: left;
        vertical-align: middle;
        border: 1px solid #94c1e7;
        -moz-border-radius: 6px;
        -webkit-border-radius: 6px;
        border-radius: 6px;
        font-family: SimHei;
        font-size: 15pt;
        font-weight: 500;
        color: RGBA(102, 102, 102, 0.7);
        cursor: pointer;
        outline: none;
        margin-left: 10px;
    }
</style>