<template>
    <a-modal
        :visible=$store.state.addNLVisible
        cancelText="取消"
        @cancel="cancel"
        :footer="null"
    >
        <a-tabs default-active-key="1" @change="callchange">
            <a-tab-pane key="1" tab="添加节点">
                <a-form :form="form1" :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }" style="margin-top: 15px" @submit="handleNodeSubmit">
                    <a-form-item label="节点名称">
                        <a-input v-decorator="['name', { rules: [{ required: true, message: '请填写节点名称！' }] }]"/>
                    </a-form-item>
                    <a-form-item label="节点特征">
                        <a-input v-decorator="['flag', { rules: [{ required: true, message: '请填写节点特征！' }] }]"/>
                    </a-form-item>
                    <a-form-item
                            v-for="(k, index) in form1.getFieldValue('keys')"
                            :key="k"
                            v-bind="index === 0 ? formItemLayout : formItemLayoutWithOutLabel"
                            :label="index === 0 ? '节点属性' : ''"
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
                                v-if="form1.getFieldValue('keys').length >= 1"
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
                    <a-form-item v-bind="formItemLayoutWithOutLabel">
                        <a-button type="primary" html-type="submit">
                            Submit
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>

            <a-tab-pane key="2" tab="添加联系" force-render>
                <a-form :form="form2" :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }" style="margin-top: 15px" @submit="handleLinkSubmit">
                    <a-form-item label="根源节点">
                        <a-input
                                placeholder="请填写出节点ID"
                                v-decorator="['FromNodeId', { rules: [{ required: true,message: '请填写出节点ID！' }] }]"
                        />
                    </a-form-item>
                    <a-form-item label="目标节点">
                        <a-input
                                placeholder="请填写入节点ID"
                                v-decorator="['ToNodeId', { rules: [{ required: true,message: '请填写入节点ID！' }] }]"
                        />
                    </a-form-item>
                    <a-form-item label="联系描述">
                        <a-input
                                placeholder="请填写关系具体描述"
                                v-decorator="['description', { rules: [{ required: true,message: '请填写关系具体描述' }] }]"
                        />
                    </a-form-item>
                    <a-form-item label="联系类型">
                        <a-select
                                placeholder="选择类型"
                                v-decorator="['type', { rules: [{ required: true, message: '请选择类型' }] }]">
                            <a-select-option value="0">关系</a-select-option>
                            <a-select-option value="1">属性</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item v-bind="formItemLayoutWithOutLabel">
                        <a-button type="primary" html-type="submit">
                            Submit
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>
        </a-tabs>
    </a-modal>
</template>

<script>
    let id = -2;
    export default {
        name: "AddNLModal",
        data(){
            return{
                key:1,
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
            this.form1=this.$form.createForm(this,{name:'addNodeModal'});
            this.form1.getFieldDecorator('keys', { initialValue: [], preserve: true });
            this.form2=this.$form.createForm(this,{name:'addLinkModal'});
        },
        methods: {
            callchange(key){
                this.key = key;
            },
            cancel(){
                this.$store.commit('set_addNLVisible',false);
            },
            handleNodeSubmit(e){
                e.preventDefault();
                this.form1.validateFields((err, values) => {
                    if (!err) {
                        const { keys, names } = values;
                        var property = '';
                        for(var i in keys){
                            property = property + '"' + names[keys[i]] + '"' + ':' + '"' + names[keys[i]+1] + '",';
                        }
                        property = '{' + property.substr(0,property.length-1) + '}';
                        const param = {
                            name : values.name,
                            flagSeg : values.flag,
                            property : property
                        }
                        this.form1.resetFields()
                        this.$emit('add_node',param);
                    }
                });
            },
            handleLinkSubmit(e){
                e.preventDefault();
                this.form2.validateFields(err => {
                    if(!err) {
                        const param = {
                            sid:this.form2.getFieldValue('FromNodeId'),
                            tid:this.form2.getFieldValue('ToNodeId'),
                            name:this.form2.getFieldValue('description'),
                            flag:this.form2.getFieldValue('type')
                        }
                        this.form2.resetFields()
                        this.$emit('add_link',param);
                    }
                })
            },
            add() {
                const { form1 } = this;
                const keys = form1.getFieldValue('keys');
                const nextKeys = keys.concat(id+=2);
                form1.setFieldsValue({
                    keys: nextKeys,
                });
            },
            remove(k) {
                const { form1 } = this;
                const keys = form1.getFieldValue('keys');
                form1.setFieldsValue({
                    keys: keys.filter(key => key !== k).filter(key => key !== k-1),
                });
            },
        }
    }
</script>

<style scoped>
    .dynamic-delete-button {
        cursor: pointer;
        position: relative;
        top: 4px;
        font-size: 24px;
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