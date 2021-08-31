<template>
  <a-modal
      :visible=$store.state.addRelationVisible
      title="添加关系"
      cancelText="取消"
      okText="确定"
      @cancel="cancel"
      @ok="handleSubmitAddRelation"
  >
    <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
      <a-form-item label="FromNodeId" v-bind="formItemLayout">
        <a-input
            placeholder="请填写出节点ID"
            v-decorator="['FromNodeId', { rules: [{ required: true,message: '请填写出节点ID' }] }]"
        />
      </a-form-item>
      <a-form-item label="ToNodeId" v-bind="formItemLayout">
        <a-input
            placeholder="请填写指向节点ID"
            v-decorator="['ToNodeId', { rules: [{ required: true,message: '请填写指向节点ID' }] }]"
        />
      </a-form-item>
      <a-form-item label="关系描述" v-bind="formItemLayout">
        <a-input
            placeholder="请填写关系具体描述"
            v-decorator="['description', { rules: [{ required: true,message: '请填写关系具体描述' }] }]"
        />
      </a-form-item>
      <a-form-item label="关系类型" v-bind="formItemLayout">
        <a-select
            placeholder="选择类型"
            v-decorator="['type', { rules: [{ required: true, message: '请选择类型' }] }]">
          <a-select-option value="0">关系</a-select-option>
          <a-select-option value="1">属性</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {addRelationAPI} from "../../api/KG";
import {message} from "ant-design-vue";

export default {
  name: "AddRelationModal",
  data() {
    return {
      formItemLayout: {
        labelCol: {
          xs: { span: 12 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
    }
  },
  beforeCreate() {
    this.form=this.$form.createForm(this,{name:'addRelationModal'});
  },
  methods: {
    cancel() {
      this.$store.commit('set_addRelationVisible',false)
    },
    handleSubmitAddRelation(e){
      e.preventDefault();
      this.form.validateFieldsAndScroll((err,values)=>{
        if(!err){
          const data={
            sid:this.form.getFieldValue('FromNodeId'),
            tid:this.form.getFieldValue('ToNodeId'),
            name:this.form.getFieldValue('description'),
            flag:this.form.getFieldValue('type')
          }
          addRelationAPI(data).then(res => {
            if(res.data.success){
              message.success("添加成功")
              this.$store.commit('set_addRelationVisible',false)
              this.$emit('confirmupdate')
            }else{
              message.error(res.data.message)
            }
          })
          this.form.resetFields()
        }
      });
    },
  }
}
</script>

<style scoped>

</style>