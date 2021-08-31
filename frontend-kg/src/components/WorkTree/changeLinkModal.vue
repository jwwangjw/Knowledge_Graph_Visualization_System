<template>
  <a-modal
      :visible=$store.state.changeLinkVisible
      title="修改关系信息"
      cancelText="取消"
      okText="确定"
      @cancel="cancel"
      @ok="handleSubmitchangeLink"
  >
    <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
      <a-form-item label="关系名称">
        <a-input
            :value="entity.name"
            v-model="entity.name"
        />
      </a-form-item>
      <a-form-item label="关系类别" v-bind="formItemLayout">
        <a-select
            :defaultValue="entity.flag"
            placeholder="请选择关系类别"
            style="width: 100%"
            @change="handleChange"
        >
          <a-select-option value="关系">
            关系
          </a-select-option>
          <a-select-option value="属性">
            属性
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {addRelationAPI} from "../../api/KG";
import {message} from "ant-design-vue";
let id = -2;
export default {
  name: "changeLinkModal",
  data() {
    return {
      entity: {
        "type": 0,//节点0，关系1
        "id": -1,
        "name":' ',
        "flag":' ',//为联系时，关系0,属性1
      },
      property_list:[],
      formItemLayout: {
        labelCol: {
          xs: { span: 5 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 17 },
          sm: { span: 17 },
        },
      },
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 17, offset: 0 },
          sm: { span: 17, offset: 5 },
        },
      },
    }
  },
  beforeCreate() {
    this.form=this.$form.createForm(this,{name:'changeLinkModal'});
  },
  methods: {
    handleChange(value) {
      this.entity.flag=value
    },
    setLink(param){
      this.entity=param
      if(this.entity.flag==0){
        this.entity.flag="关系"
      }else{
        this.entity.flag="属性"
      }
    },
    cancel() {
      this.$store.commit('set_changeLinkVisible',false)
    },
    handleSubmitchangeLink(e){
      if(this.entity.flag=="关系"){
        this.entity.flag=0
      }else{
        this.entity.flag=1
      }
      const param = {
        id:this.entity.id,
        name : this.entity.name,
        flag: this.entity.flag
      }
      this.form.resetFields()
      this.$emit("updateLink",param)
    }
  }
}
</script>

<style scoped>

</style>