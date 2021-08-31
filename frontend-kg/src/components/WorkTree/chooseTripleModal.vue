<template>
  <a-modal
      :visible=$store.state.chooseTripleVisible
      title="三元组选择"
      cancelText="取消"
      okText="选择"
      @cancel="cancel"
      @ok="handleSubmitChooseTriple"
  >

      <el-table
          ref="multipleTable"
          :data="transData"
          tooltip-effect="dark"
          height="250"
          style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="mainBody"
              label="mainBody"
              width="100">
          </el-table-column>
          <el-table-column
              prop="relation"
              label="relation"
              width="100">

          </el-table-column>
          <el-table-column
              prop="object"
              label="object"
              width="100">
          </el-table-column>
          <el-table-column
              prop="label"
              label="label"
              width="80">
          </el-table-column>
      </el-table>

      <br/>
      <a-form :form="form">
          <a-form-item
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
              label="name"
          >
              <a-input
                  v-decorator="[
          'name',
          { rules: [{ required: true, message: '请输入返回名称' }] },
        ]"
                  placeholder="请输入返回名称"
              />
          </a-form-item>
      </a-form>


      <a-divider/>
      <p>注:选择框内为文本提取后得到三元组信息,</p>
      <p>点击可选择三元组</p>
      <vue-alert-loading :visible="triplesLoading" title="知识图谱正在构建..."/>
  </a-modal>




</template>



<script>

import Cookies from "js-cookie";

const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8 },
};
const formTailLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8, offset: 4 },
};

import data from "../../assets/testData.json"
import {addTxtTriplesAPI, getKGProjectAPI} from "@/api/Info";
import $ from "jquery";
import {message} from "ant-design-vue";
import callUtils from "../../utils/callUtils";

export default {
    name: "chooseTripleModal",
    data(){
    return{
        formItemLayout,
        formTailLayout,
        form: this.$form.createForm(this, { name: 'dynamic_rule' }),
        checked: false,
        transData:
            [
                {
                    "id":1,
                    "mainBody":"a",
                    "relation":"belongs",
                    "object":"b",
                    "label":"可信",
                    "Mpost":"a",
                    "Spost":"aa"
                },

                {
                    "id":2,
                    "mainBody":"c",
                    "relation":"belongs",
                    "object":"b",
                    "label":"不可信",
                    "Mpost":"b",
                    "Spost":"bb"
                }
            ],
        value1:[],
        triples:{
            "options":[],
            "name":"",
            "userId":"",

        },
        mainBody:"",
        relation:"",
        object:"",
        userId: Cookies.get('userId'),
        multipleSelection:[],
        triplesLoading:false,
    }

  },
    watch:{
/*        value1(newData,oldData){
            console.log(newData.length === this.transData[0].options.length+this.transData[1].options.length)
            this.checked = newData.length===this.transData[0].options.length+this.transData[1].options.length
        }*/

    },

    methods: {
        changeData(mainBody,relation,object) {
            //console.log(mainBody,relation,object)
            this.mainBody = mainBody
            this.relation = relation
            this.object = object
            this.$store.state.mainBody = mainBody
            this.$store.state.relation = relation
            this.$store.state.object = object
            this.$store.commit("set_changeTripleVisible",false)
        },

        init(data){
            this.value1 = []
            //this.transData = require("../../assets/testData.json")
            //console.log(this.multipleSelection)
            this.transData = JSON.parse(data);
            for(let i = 0;i<this.transData.length;i++){
                if(this.transData[i].label==="可信"){
                    this.multipleSelection.push(this.transData[i])
                    //console.log(this.transData[i])
                }
            }
            let checkedId = []
            for(let item of this.multipleSelection){
                checkedId.push(item.id)
            }
            this.$nextTick(()=>{
                for(let key in this.transData){
                    if(checkedId.indexOf(this.transData[key]['id'])!==-1){
                        this.$refs.multipleTable.toggleRowSelection(this.transData[key],true)
                    }
                }
            })
        },
        cancel() {
            this.$store.commit('set_chooseTripleVisible', false);
        },
        handleSubmitChooseTriple(e) {
            this.triplesLoading = true
            e.preventDefault();
            let cnt = 0
            for(let i = 0;i<this.multipleSelection.length;i++){
                let temp = {"mainBody":this.multipleSelection[i].mainBody,"relation":this.multipleSelection[i].relation,"object":this.multipleSelection[i].object,"Mpost":this.multipleSelection[i].Mpost,"Spost":this.multipleSelection[i].Spost}
                this.triples.options.push(temp)
            }

            if(this.form.getFieldValue("name")==="" || this.form.getFieldValue("name")===undefined){
                alert("请输入返回的名称！")
            }
            else{
                this.$set(this.triples,"name",this.form.getFieldValue("name"))
                this.$set(this.triples,"userId",this.userId)
                addTxtTriplesAPI(this.triples).then(res => {
                    if (res.data.success) {
                        message.success('上传成功');
                        callUtils.$emit('getKGProject', 'Home')
                        this.$store.commit('set_chooseTripleVisible',false);
                        this.triplesLoading = true
                        /*this.triplesLoading = false*/
                    }else {
                        message.error("上传失败");
                        this.$store.commit('set_chooseTripleVisible',false);
                        this.triplesLoading = true

                    }
                })
                this.form.resetFields()
            }
        },
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        selectAll() {
            this.value1 = []
            if (this.checked) {
                for(let i = 0;i<this.transData[0].options.length;i++){
                    this.value1.push(this.transData[0].options[i].id)
                }
                for(let i = 0;i<this.transData[1].options.length;i++){
                    this.value1.push(this.transData[1].options[i].id)
                }
            } else {
                this.value1 = []
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        }
    }
}
</script>

<style >


.el-checkbox {
    text-align: right;
    width: 100%;
    padding-right: 10px;
}
.select{
}
.el-select__tags-text {
    display: inline-block;
    max-width: 70px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.el-tag__close.el-icon-close {
    top: -7px;
}




</style>