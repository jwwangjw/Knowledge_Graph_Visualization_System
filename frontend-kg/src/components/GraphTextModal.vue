<template>
  <a-modal
    title="当前知识图谱文本（JSON格式）"
    :visible="graphTextModalVisible"
    cancelText="取消"
    okText="确定"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <div style="margin-bottom:10px;">
      <label class="label">实体</label>
      <a-textarea
        :default-value="nodetext"
        :auto-size="{ minRows:5, maxRows: 8 }"
        style="min-width: 300px;"
      />
    </div>
    <div style="margin-bottom:10px;">
      <label class="label">关系</label>
      <a-textarea
        :default-value="linktext"
        :auto-size="{ minRows:5, maxRows: 8 }"
        style="min-width: 300px;"
      />
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'GraphTextModal',
  props: {
    nodetext:{
      type: String,
    },
    linktext:{
      type: String
    }
  },
  data(){
    return {
      graphTextModalVisible: false,
    }
  },

  methods: {
    handleOk(){
      setTimeout(() => this.graphTextModalVisible = false, 100)
    },

    handleCancel(){
      this.graphTextModalVisible = false
    },
  }
}
</script>

<style scoped>
.label {
  display: block;
  font-weight: 600;
  letter-spacing: .5px;
  margin-bottom: 5px;
}
</style>
