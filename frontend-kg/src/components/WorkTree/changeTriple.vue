<template>
    <a-modal
        :visible=$store.state.changeTripleVisible
        title="修改信息"
        cancelText="取消"
        okText="确定"
        @cancel="cancel"
        @ok="check"
    >
    <a-form :form="form">
        <a-form-item
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
            label="mainBody"
        >
            <a-input
                v-decorator="[
          'mainBody',
          { rules: [{ required: true, message: 'Please input mainBody' }] },
        ]"
                placeholder="Please input mainBody"
            />
        </a-form-item>
        <a-form-item
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
            label="relation"
        >
            <a-input
                v-decorator="[
          'relation',
          { rules: [{ required: true, message: 'Please input relation' }] },
        ]"
                placeholder="Please input relation"
            />
        </a-form-item>
        <a-form-item
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
            label="object"
        >
            <a-input
                v-decorator="[
          'object',
          { rules: [{ required: true, message: 'Please input object' }] },
        ]"
                placeholder="Please input object"
            />
        </a-form-item>

    </a-form>
        <br/><br/><br/><br/><br/>
    </a-modal>
</template>

<script>
const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8 },
};
const formTailLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 8, offset: 4 },
};
export default {
    name:"changeTriple",
    data() {
        return {
            checkNick: false,
            formItemLayout,
            formTailLayout,
            form: this.$form.createForm(this, { name: 'dynamic_rule' }),
        };
    },
    methods: {
        cancel(){
            this.$store.commit('set_changeTripleVisible',false);
        },

        check() {
            this.$store.state.mainBody =  this.form.getFieldValue('mainBody')
            this.$store.state.relation =  this.form.getFieldValue('relation')
            this.$store.state.object =  this.form.getFieldValue('object')

            this.form.validateFields(err => {
                if (!err) {
                    console.info('success');
                }
            });
            this.$store.commit('set_changeTripleVisible',false);
        },
        handleChange(e) {
            this.checkNick = e.target.checked;
            this.$nextTick(() => {
                this.form.validateFields(['nickname'], { force: true });
            });
        },
    },
};
</script>