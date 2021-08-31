<template class="container containerLoc">
  <div class="container  containerLoc">

    <div class="edit-block" v-show="modify">
      <div class="title">
        详细信息
      </div>
      <div class="information">
        <a-form>
          <a-form-item class="form-item" label="ID" :label-col="{span:3}" :wrapper-col="{ span: 8, offset: 1  }">{{entity.id}}</a-form-item>
          <a-form-item class="form-item" label="类型" :label-col="{span:3}" :wrapper-col="{ span: 8, offset: 1  }">
            <a-button label="节点" style="background-color: lightgreen;border: 0px" size="small" v-if="entity.type==0">节点</a-button>
            <a-button label="联系" style="background-color: lightsalmon;border: 0px" size="small" v-else>联系</a-button>
          </a-form-item>
          <a-form-item
              class="form-item"
              style="word-break: break-word"
              label="内容" :label-col="{span:3}" :wrapper-col="{ span: 15, offset: 1  }">
            <a-input
                id="input1"
                :value="entity.name"
                v-if="modify"></a-input>
            <span v-else>{{entity.name}}</span>
          </a-form-item>
          <a-form-item
              class="form-item"
              style="word-break: break-word"
              label="特征" :label-col="{span:3}" :wrapper-col="{ span: 15, offset: 1  }">
            <a-input
                id="input2"
                :value="entity.flag"
                v-if="modify"></a-input>
            <span v-else>{{entity.flag}}</span>
          </a-form-item>

          <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="modify">
            <a-button type="primary" @click="saveModify" style="margin-left: 5px;border: 0px">
              保存
            </a-button>
            <a-button type="default" style="margin-left: 30px" @click="cancelModify">
              取消
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>

    <div class="edit-block">
      <div class="title">
        工具箱
      </div>

      <div class="toolbox">
        <div title="添加节点" class="tool-div tool-point" @click="addNode">
          <img src="../assets/icons/an.svg" class="tool">
        </div>
        <div title="添加关系" class="tool-div tool-point" @click="addRelation">
          <img src="../assets/icons/ar.svg" class="tool">
        </div>
        <div title="查找" class="tool-div tool-point">
          <img src="../assets/icons/search.svg" class="tool">
        </div>
        <div title="导出图片" class="tool-div tool-point" @click = "export_to_png">
          <img src="../assets/icons/exportfile.svg" class="tool"></div>
        <div title="导出文件" class="tool-div tool-point" @click = "export_to_json">
          <img src="../assets/icons/exportimg.svg" class="tool"></div>
        <div title="保存页面" class="tool-div tool-point" @click = "export_to_web">
          <img src="../assets/icons/toolbox.svg" class="tool"></div>
      </div>
    </div>
  </div>
</template>
<script>
import {infoTestAPI} from "../api/Info";
import {updateNodeAPI, updateRelationAPI, deleteNodeAPI, deleteRelationAPI} from "../api/KG"
import {message} from "ant-design-vue";
import graph from "@/components/graph";
import Home from "@/views/Home";

export default {
  data(){
    return{
      modify: false,
      entity: {
        "type": 0,//节点0，关系1
        "id": -1,
        "name":' ',
        "flag":' '//为联系时，关系0,属性1
      }
    }
  },
  name: "Home",
  methods:{
    addNode() {
      this.$store.commit('set_addNodeVisible',true)
    },
    addRelation(){
      this.$store.commit('set_addRelationVisible',true)
    },
    export_to_png(){
      this.$emit('savePNG')
    },
    export_to_json(){
      this.$emit('saveJSON')
    },
    export_to_web(){
      this.$emit('saveWEB')
    },
    modifyInfo(){
      //console.log('mmp')
      this.modify = true
    },
    saveModify(){
      if(this.entity.id <0){
        alert('错误的ID！')
        return
      }
      const name = document.getElementById('input1').value
      const flag = document.getElementById('input2').value
      //console.log(name,flag)
      if(name == ''){
        alert('内容不能为空！')
        return
      }
      if(flag == ''){
        alert('特征不能为空！')
        return
      }
      if(this.entity.type==1 && (flag!='属性'&&flag!='关系')){
        alert('联系的特征只能为 属性 或 关系 ！')
        return
      }
      var res
      if(this.entity.type == 0){
        const param = {
          'id' : this.entity.id,
          'name' : name,
          'flag' : flag
        }
        updateNodeAPI(param).then(res => {
          if(res.data.success){
            this.entity.name = name
            this.entity.flag = flag
            this.$emit('updategraph')
          }else{
            message.error(res.data.message)
          }
          this.modify = false
        })
      }else{
        const type = flag=='属性'?1:0
        const param = {
          'id' : this.entity.id,
          'description' : name,
          'type' : type
        }
        updateRelationAPI(param).then(res => {
          if(res.data.success){
            this.entity.name = name
            this.entity.flag = flag
            this.$emit('updategraph')
          }else{
            message.error(res.data.message)
          }
          this.modify = false
        })
      }
    },

    cancelModify(){
      this.modify = false
    },

    setEntityInfo(param){
      //console.log(true)
      this.entity.id = param.id
      this.entity.type = param.type
      this.entity.name = param.name
      if(this.entity.type == 0){
        this.entity.flag = param.flag
      }else{
        this.entity.flag = param.flag=='0'?'关系':'属性'
      }
    },
  }
}
</script>

<style scoped>
  @import "../assets/css/all.css";
  .containerLoc{
    right: 10px;
  }
  .toolbox {
    height: 100px;
    border-radius: 25px;
    background-color: white;
    cursor: auto;
  }
  .tool-div {
    height: 60px;
    width: 60px;
    margin-left: 40px;
    margin-top: 20px;
    float: left;
  }
  .tool-point {
    cursor: pointer;
  }
  .tool {
    height: 60px;
    width: 60px;
  }
  .form-item {
    margin-left: 20px;
    margin-bottom: 10px;
    font-weight: 700;
  }
</style>