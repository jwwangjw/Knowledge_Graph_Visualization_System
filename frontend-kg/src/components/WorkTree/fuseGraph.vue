<template>
    <a-modal
            :visible=$store.state.fuseGraphVisible
            title="知识图谱融合"
            cancelText="取消"
            okText="融合"
            @cancel="cancel"
            @ok="handleSubmitFuseGraph"
    >
        <el-table
                ref="multipleTable"
                :data="excelData"
                height="300"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column
                    prop="ID"
                    label="ID"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="姓名"
                    width="200">
            </el-table-column>
        </el-table>

        <br/><br/><br/>

        <a-form :form="form">
            <a-form-item
                    :label-col="formItemLayout.labelCol"
                    :wrapper-col="formItemLayout.wrapperCol"
                    label="文件名"
            >
                <a-input
                        v-decorator="[
          '文件名',
          { rules: [{ required: true, message: '请输入生成文件名称' }] },
        ]"
                        placeholder="请输入生成文件名称"
                />
            </a-form-item>
        </a-form>
        <vue-alert-loading :visible="loading_visible" title="知识图谱正在构建..."/>
    </a-modal>
</template>

<script>
    import VueLoading from 'vue-simple-loading';
    import Cookies from "js-cookie";
    import {fusionAPI} from "../../api/Info";
    import {message} from "ant-design-vue";
    import callUtils from "@/utils/callUtils";

    const formItemLayout = {
        labelCol: { span: 4 },
        wrapperCol: { span: 8 },
    };
    const formTailLayout = {
        labelCol: { span: 4 },
        wrapperCol: { span: 8, offset: 4 },
    };

    export default {
        name: "fuseGraph",
        data() {
            return {
                formItemLayout,
                formTailLayout,
                form: this.$form.createForm(this, { name: 'dynamic_rule' }),
                multipleSelection: [],
                excelData: this.$store.state.graphList,
                ids:[],
                userId:Cookies.get('userId'),
                loading_visible:false,
            }

        },
        methods: {
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },

            init(data){
                this.excelData = data
            },

            cancel() {
                this.$store.commit('set_fuseGraphVisible', false);
            },
            handleSubmitFuseGraph(){
                if(this.form.getFieldValue("文件名")==="" || this.form.getFieldValue("文件名")===undefined){
                    alert("请输入新融合的文件名！")
                }
                else {
                    for(let i = 0;i<this.multipleSelection.length;i++){
                        this.$set(this.ids,i,this.multipleSelection[i].ID)
                    }

                    let param = {
                        userId:this.userId,
                        ids:this.ids,
                        name:this.form.getFieldValue("文件名")
                    }
                    //console.log(param)
                    this.loading_visible=true
                    fusionAPI(param).then(res => {
                        //console.log(res)
                        if (res.data.success) {
                            message.success('上传成功');
                            callUtils.$emit('getKGProject', 'Home');
                            this.loading_visible=false
                            this.$store.commit('set_fuseGraphVisible', false);
                        }else {
                            message.error("上传失败");
                        }
                    })
                    this.form.resetFields()
                }
            },
        }
    }
</script>

<style scoped>

</style>