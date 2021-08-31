 <template>
  <div>
  <div class="block_node">
  <el-collapse>
    <div v-for="(value,index) in type_data['nodes']" v-bind:key="index">
      <el-collapse-item :title="value[0].flagSeg" name=key>
        <div v-for="(node,index) in value" v-bind:key="index">
          <el-collapse-item title="节点信息">
            <div>名字: {{node.name}}</div>
            <div>id: {{node.id}}</div>
          </el-collapse-item>
        </div>
      </el-collapse-item>
    </div>
  </el-collapse>
  </div>



<!--<div>
  <div v-for="(value,index) in type_data['nodes']" v-bind:key="index">
    <div>{{value[0].name}}</div>
  </div>
</div>-->



  <div class="block_link">
    <el-collapse>
      <div v-for="(value,index) in type_data['links']" v-bind:key="index">
        <el-collapse-item :title="value[0].type" name=key>
          <div v-for="(node,index) in value" v-bind:key="index">
            <el-collapse-item title="关系信息">
              <div>描述: {{node.description}}</div>
              <div>id: {{node.id}}</div>
              <div>指向: {{node.source}}->{{node.target}}</div>
            </el-collapse-item>
          </div>
        </el-collapse-item>
      </div>
    </el-collapse>
  </div>
</div>
</template>


<script>
    import {getSqlJsonAPI} from "@/api/Info";

    export default {
        name: "typesetting",
        data(){
          return{
            activeNames: ['2'],

            type_data:{
              "nodes":[],
              "links":[],
            },
            temp:{
              "nodes":[],
              "links":[],
            }
          }
        },
        mounted() {
          let file_id = Number(localStorage.getItem('file_id'))
          getSqlJsonAPI(file_id).then(res => {
            this.temp = JSON.parse(res.data.content)
            this.trans(this.temp)
          })
         


        },

        methods:{
          handleChange(val) {
            console.log(val);
          },

          trans(temp){
            let tmp = {}
            let num = 0
            for(let i = 0;i<temp['nodes'].length;i++){
              if(tmp[temp["nodes"][i].flagSeg]===undefined){
                tmp[temp["nodes"][i].flagSeg] = num
                num = num + 1
              }
            }

            for(let i = 0;i<temp["nodes"].length;i++){
              if(this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]]===undefined){
                this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]] = []
                this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]][0]= temp["nodes"][i]
              }
              else{
                let cnt =  this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]].length
                this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]][cnt] = temp["nodes"][i]
              }
            }
            for(let i = 0;i<temp["links"].length;i++){
              if(this.type_data["links"][temp["links"][i].type]===undefined){
                this.type_data["links"][temp["links"][i].type] = []
                this.type_data["links"][temp["links"][i].type][0]= temp["links"][i]
              }
              else{
                let cnt =  this.type_data["links"][temp["links"][i].type].length
                this.type_data["links"][temp["links"][i].type][cnt] = temp["links"][i]
              }
            }
            console.log("排版模式:",this.type_data)
            this.$forceUpdate();
          },
        }
    }
</script>

<style scoped>

.block_node{
  position: absolute;
  left: 400px;
  height: 300px;
  width: 150px;

}

.block_link{
  position: absolute;
  right: 400px;
  height: 300px;
  width: 150px;

}

</style>