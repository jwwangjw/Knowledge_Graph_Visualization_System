<template>
  <a-modal
      :visible=$store.state.changeNodeVisible
      title="修改节点信息"
      cancelText="取消"
      okText="确定"
      @cancel="cancel"
      @ok="handleSubmitchangeNode"
  >
    <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
      <a-form-item label="节点名称">
        <a-input
            :value="entity.name"
            v-model="entity.name"
        />
      </a-form-item>
      <a-form-item label="节点特征" v-bind="formItemLayout">
        <a-input
            :value="entity.flagSeg"
            v-model="entity.flagSeg"
        />
      </a-form-item>
      <a-form-item
          label="节点属性"
          v-bind="formItemLayout"
      >
        <a-input-group style="width:87.5%;margin-right: 8px" compact v-show="property_list.length>0" v-for="item in property_list" :key="item.key" :val="item.val">
          <a-input :value="item.key" style="width: 40%" v-model="item.key"></a-input>
          <a-input :value="item.val" style="width: 55%" v-model="item.val"></a-input>
          <a-icon
              v-if="property_list.length >= 1"
              class="dynamic-delete-button"
              type="minus-circle-o"
              style="width: 5%"
              @click="remove_new(item.key)"
          />
        </a-input-group>
      </a-form-item>
      <a-form-item
          v-for="(k, index) in form.getFieldValue('keys')"
          :key="k"
          v-bind="index === 0 ? formItemLayout : formItemLayoutWithOutLabel"
          :label="index === 0 ? '新增属性' : ''"
          :required="true"
      >
        <a-input-group style="width:87.5%;margin-right: 8px" compact>
          <a-input v-decorator="[`names[${k}]`,
                                {
                                    validateTrigger: ['change', 'blur'],
                                    rules: [{
                                        required: true,
                                        message: 'Please input passenger\'s name or delete this field.',
                                        },],
                                 },]" style="width: 40%" placeholder="属性名称"></a-input>
          <a-input v-decorator="[`names[${k+1}]`,
                                {
                                    validateTrigger: ['change', 'blur'],
                                    rules: [{
                                        required: true,
                                        message: 'Please input passenger\'s name or delete this field.',
                                        },],
                                 },]" style="width: 60%" placeholder="属性内容"></a-input>
        </a-input-group>
        <a-icon
            v-if="form.getFieldValue('keys').length >= 1"
            class="dynamic-delete-button"
            type="minus-circle-o"
            @click="() => remove(k)"
        />
      </a-form-item>
      <a-form-item v-bind="formItemLayoutWithOutLabel">
        <a-button type="dashed" style="width: 87.5%" @click="add">
          <a-icon type="plus" /> 添加属性
        </a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {addRelationAPI} from "../../api/KG";
import {message} from "ant-design-vue";
import callUtils from "../../utils/callUtils";
let id = -2;
export default {
  name: "changeNodeModal",
  data() {
    return {
      entity: {
        "type": 0,//节点0，关系1
        "id": -1,
        "name":' ',
        "flag":' ',//为联系时，关系0,属性1
        "property":''
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
    this.form=this.$form.createForm(this,{name:'addNodeModal'});
    this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
  },
  methods: {
    setEntity(param){
      this.property_list=[]
      this.entity=param
      let temp_json=JSON.parse(this.entity.property)
      let propertys=temp_json
      for(var key in propertys){
        let temp={
          key:key,
          val:propertys[key]
        }
        this.property_list.push(temp)
      }
    },
    remove_new(item){
      for(var i in this.property_list){
        if(this.property_list[i].key==item){
          this.property_list.splice(i,1)
        }
      }

    },
    cancel() {
      this.$store.commit('set_changeNodeVisible',false)
    },
    handleSubmitchangeNode(e){
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const { keys, names } = values;
          //console.log('Received values of form: ', values);
          var property = '';
          for(var i in keys){
            property = property + '"' + names[keys[i]] + '"' + ':' + '"' + names[keys[i]+1] + '",';
          }
          //console.log(this.property_list)
          for(var item in this.property_list){
            property = property + '"' + this.property_list[item].key + '"' + ':' + '"' + this.property_list[item].val + '",';
          }
          property = '{' + property.substr(0,property.length-1) + '}';
          const param = {
            id:this.entity.id,
            name : this.entity.name,
            flagSeg : this.entity.flagSeg,
            property : property
          }
          //console.log(param)
          this.form.resetFields()
          this.$emit("updateNode",param)
        }
      });
    },
    add() {
      const { form } = this;
      const keys = form.getFieldValue('keys');
      const nextKeys = keys.concat(id+=2);
      form.setFieldsValue({
        keys: nextKeys,
      });
    },
    remove(k) {
      const { form } = this;
      const keys = form.getFieldValue('keys');
      form.setFieldsValue({
        keys: keys.filter(key => key !== k).filter(key => key !== k-1),
      });
      //console.log(form.getFieldValue('keys'))
    },
  }
}
</script>

<style scoped>
  .dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 15px;
  margin-left: 2px;
  color: #999;
  transition: all 0.3s;
  }
  .dynamic-delete-button:hover {
  color: #777;
  }
  .dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
  }

</style>