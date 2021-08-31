<template>
  <div style="background-color: #e9eef3;">
    <el-card class="type_setting">
      <div slot="header" class="clearfix">
        <span style="font-weight: 700">排版模式</span>
        <a-icon type="align-right" style="float: right; padding: 3px 0" />
      </div>

      <div>
        <p style="font-weight: 700;font-size: 20px">模式描述</p>
          <br/>
        <p style="padding-left: 16px;padding-right: 16px;font-size: 20px">本页面为COIN系统排版模式展示页面，采用ant-design设计风格。包括:</p>
          <br/>
        <ul style="padding-left: 16px;padding-right: 16px">
          <li style="font-size: 16px">排版模式展示 <a-icon type="check-circle" /></li>
          <li style="font-size: 16px">统计数据展示 <a-icon type="line-chart" /></li>
          <li style="font-size: 16px">过滤模式展示 <a-icon type="filter" /></li>
        </ul>
        <!--p style="padding-left: 16px;padding-right: 16px">目前暂不支持添加空白画布，需要根据格式导入图谱。点击"即刻开始"开启图谱可视化！</p-->
      </div>
      <a-divider></a-divider>

      <div>
        <a-tabs default-active-key="1" @change="callback">
          <a-tab-pane key="1" tab="节点信息">
            <a-collapse >
              <a-collapse-panel key="1" header="节点信息">
                <a-collapse  v-for="(value,index) in type_data['nodes']" v-bind:key="index">

                  <div >
                    <a-button-group  class = "btns_1">
                      <router-link :to="path">
                        <el-tooltip content="过滤同类节点" placement="top">
                          <a-button v-on:click.once="filter(node_path[value[0].flagSeg])"  round icon="filter" />
                        </el-tooltip>
                      </router-link>
                      <router-link :to="path">
                        <el-tooltip content="过滤邻接节点" placement="top">
                          <a-button v-on:click.once="filter(node_path_neighbor[value[0].flagSeg])"  round icon="snippets"/>
                        </el-tooltip>
                      </router-link>
                    </a-button-group>
                  </div>
                  <a-collapse-panel   :header="value[0].flagSeg" >
                    <p v-for="(node,index) in value" v-bind:key="index">
                      ID: {{node.id}}<br/>
                      <router-link :to="path">
                        <el-tooltip content="过滤邻接节点" placement="top">
                          <a-button style="float: right" v-on:click="filter(node_path_neighbor[node.id])" round icon="snippets"/>
                        </el-tooltip>
                      </router-link>
                      <br/>名称: {{node.name}}
                      <a-divider></a-divider>
                    </p>
                  </a-collapse-panel>
                  <p></p>
                </a-collapse>
              </a-collapse-panel>
            </a-collapse>
          </a-tab-pane>
          <a-tab-pane key="2" tab="关系信息" force-render>
            <a-collapse >
              <a-collapse-panel key="1" header="关系信息">
                <a-collapse v-for="(value,index) in type_data['links']" v-bind:key="index">
                  <div >
                    <a-button-group  class = "btns_2">
                      <router-link :to="path">
                        <el-tooltip content="过滤两端节点" placement="top">
                          <a-button v-on:click.once="filter(link_path_neighbor[value[0].description])"  round icon="filter"/>
                        </el-tooltip>
                      </router-link>
                    </a-button-group>
                  </div>
                  <a-collapse-panel :header="value[0].description" >

                    <p v-for="(link,index) in value" v-bind:key="index">
                      ID: {{link.id}}<br/>
                      <router-link :to="path">
                        <el-tooltip content="过滤两端节点" placement="top">
                          <a-button style="float: right" v-on:click="filter(link_path_neighbor[link.id])" round icon="snippets"/>
                        </el-tooltip>
                      </router-link>
                      <br/>
                      描述: {{link.description}}<br/><br/>
                      指向: {{link.source}}->{{link.target}}<br/>
                      <a-divider></a-divider>
                    </p>
                  </a-collapse-panel>
                  <p></p>
                </a-collapse>
              </a-collapse-panel>
            </a-collapse>
          </a-tab-pane>

        </a-tabs>
      </div>

    </el-card>

    <a-card  style=" font-weight: 700;font-size: 20px" class="graph_1">
      <span>统计信息</span>
      <a-divider></a-divider>

      <a-row type="flex" justify="center" align="top">
        <a-col span="12">
          <a-card title="数据统计">
            <p style="fontSize: 14px;color: rgba(0, 0, 0, 0.85); marginBottom: 16px;fontWeight: 500">

            </p>
            <a-card style="fontSize: 14px" title="最多类别">
              节点 : {{water_node.data[0].name}} - {{water_node.data[0].value}} 个
              <a-divider></a-divider>
              关系 : {{water_link.data[0].name}} - {{water_link.data[0].value}} 个
            </a-card>
          </a-card>
        </a-col>


        <a-col span="10" style="float: right" >
          <rank-list title="排行" :list="rank_node"/>
        </a-col>
      </a-row>
    </a-card>


    <a-card style="font-weight: 700;font-size: 20px" class="graph_1"  >
      <span>饼状图</span>
      <a-divider></a-divider>
      <div>
        <a-tabs default-active-key="1" @change="callback">
          <a-tab-pane key="1" tab="节点信息">
            <div id="main_1" ></div>
          </a-tab-pane>
          <a-tab-pane key="2" tab="关系信息" force-render>
            <div id="main_2" ></div>
          </a-tab-pane>

        </a-tabs>
      </div>

    </a-card>


    <a-card style="font-weight: 700;font-size: 20px" class="graph_3">
      <span>词云图</span>
      <a-divider></a-divider>
      <div>
        <a-tabs default-active-key="1" @change="callback">
          <a-tab-pane key="1" tab="节点信息">
            <div style="width: 400px;height: 400px;left: 50%;top: 20%;" id="container_1"></div>
          </a-tab-pane>
          <a-tab-pane key="2" tab="关系信息" force-render>
            <div style="width: 400px;height: 400px;left: 50%;top: 20%;" id="container_2"></div>
          </a-tab-pane>

        </a-tabs>
      </div>
    </a-card>


  </div>
</template>


<script>
import {getSqlJsonAPI} from "@/api/Info";
import {WordCloud,Pie } from '@antv/g2plot';
import RankList from "../../components/Charts/RankList";
/*import ChartCard from "./Charts/ChartCard";*/

export default {
  name: "typesetting",
  components: {
    RankList,
    /*    ChartCard*/
  },
  watch :{
    $route(to,from){
      //console.log('typesetting watch: ',to);
      this.id = Number(this.$route.params.id);
      this.refresh();
    }
  },
  data(){
    return{
      id:0,
      path:'',
      activeNames: ['2'],
      type_data:{
        "nodes":[],
        "links":[],
      },
      temp:{
        "nodes":[],
        "links":[],
      },
      miniArea_node:{
        "data":[],
        "container":'Histogram_node',
        "width":300,
        "height":300
      },
      water_node:{
        "data":[{}],
      },
      miniArea_link:{
        "data":[],
        "container":'Histogram_link',
        "width":300,
        "height":300
      },
      water_link:{
        "data":[{}],
      },
      rank_node: [],
      rank_link: [],
      node_path:[],
      node_path_neighbor:[],
      link_path:[],
      link_path_neighbor:[],
    }
  },
  mounted() {
    this.id = Number(this.$route.params.id);
    this.init_type()
  },

  methods: {
    async refresh(){
      Object.assign(this.$data, this.$options.data())
      this.id = Number(this.$route.params.id);
      await this.init_type()
    },
    callback(key) {
      console.log(key);
    },
    filter(data){
      this.path = '/Demo/' + this.$route.params.name + '/' + this.$route.params.id + '/' + data ;
    },

    async init_type(){
      await getSqlJsonAPI(this.id).then(res => {
        this.temp = JSON.parse(res.data.content)
        this.trans(this.temp)
        this.add_node(this.temp)
        this.add_link(this.temp)
      })
      /*      this.draw_miniArea_node()
            this.draw_miniArea_link()*/

      this.getPie_1(this.miniArea_node.data)
      this.getPie_2(this.miniArea_link.data)
      this.getWater_1(this.miniArea_node.data)
      this.getWater_2(this.miniArea_link.data)
      this.rank_no()
      this.change_node_path()
      this.change_link_path()

    },
/*    draw_miniArea_node(){
      const data = this.miniArea_node.data;
      const chart = new G2.Chart({
        container: this.miniArea_node.container,
        width: this.miniArea_node.width,
        height: this.miniArea_node.height
      });
      chart.source(data);
      chart.interval().position('name*value').color("name")
      chart.render();
    },

    draw_miniArea_link(){
      const data = this.miniArea_link.data;

      const chart = new G2.Chart({
        container: this.miniArea_link.container,
        width: this.miniArea_link.width,
        height: this.miniArea_link.height
      });
      chart.source(data);
      chart.interval().position('name*value').color("name")
      chart.render();
    },*/

    handleChange(val) {
      console.log(val);
    },
    trans(temp) {
      let tmp = {}
      let num = 0
      for (let i = 0; i < temp['nodes'].length; i++) {
        if (tmp[temp["nodes"][i].flagSeg] === undefined) {
          tmp[temp["nodes"][i].flagSeg] = num
          num = num + 1
        }
      }
      for (let i = 0; i < temp["nodes"].length; i++) {
        if (this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]] === undefined) {
          this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]] = []
          this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]][0] = temp["nodes"][i]
        } else {
          let cnt = this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]].length
          this.type_data["nodes"][tmp[temp["nodes"][i].flagSeg]][cnt] = temp["nodes"][i]
        }
      }
      tmp = {}
      num = 0
      for (let i = 0; i < temp['links'].length; i++) {
        if (tmp[temp["links"][i].description] === undefined) {
          tmp[temp["links"][i].description] = num
          num = num + 1
        }
      }
      for (let i = 0; i < temp["links"].length; i++) {
        if (this.type_data["links"][tmp[temp["links"][i].description]] === undefined) {
          this.type_data["links"][tmp[temp["links"][i].description]] = []
          this.type_data["links"][tmp[temp["links"][i].description]][0] = temp["links"][i]
        } else {
          let cnt = this.type_data["links"][tmp[temp["links"][i].description]].length
          this.type_data["links"][tmp[temp["links"][i].description]][cnt] = temp["links"][i]
        }
      }
      this.$forceUpdate();

    },
    add_node(data){
      let tmp = {}
      let num = 0
      for (let i = 0; i < data['nodes'].length; i++) {
        if (tmp[data["nodes"][i].flagSeg] === undefined) {
          tmp[data["nodes"][i].flagSeg] = num
          num = num + 1
        }
      }
      for (let i = 0; i < data['nodes'].length; i++) {
        if (this.miniArea_node["data"][tmp[data["nodes"][i].flagSeg]] === undefined) {
          this.$set(this.miniArea_node["data"], tmp[data["nodes"][i].flagSeg], {"name":data["nodes"][i].flagSeg,"value":1})
        }else{
          this.$set(this.miniArea_node["data"][tmp[data["nodes"][i].flagSeg]], "value", this.miniArea_node["data"][tmp[data["nodes"][i].flagSeg]].value+1);
        }
      }
      let max = 0
      for (let i = 0; i < this.miniArea_node["data"].length; i++) {
        if(this.miniArea_node["data"][i].value>=max){
          max = this.miniArea_node["data"][i].value
          this.$set(this.water_node["data"],0,{"name":data["nodes"][i].flagSeg,"value":(max)})
        }
      }
    },
    add_link(data){
      let tmp = {}
      let num = 0
      for (let i = 0; i < data['links'].length; i++) {
        if (tmp[data["links"][i].description] === undefined) {
          tmp[data["links"][i].description] = num
          num = num + 1
        }
      }
      for (let i = 0; i < data['links'].length; i++) {
        if (this.miniArea_link["data"][tmp[data["links"][i].description]] === undefined) {
          this.$set(this.miniArea_link["data"], tmp[data["links"][i].description], {"name":data["links"][i].description,"value":1})
        }else{
          this.$set(this.miniArea_link["data"][tmp[data["links"][i].description]], "value", this.miniArea_link["data"][tmp[data["links"][i].description]].value+1);
        }
      }
      //console.log(this.miniArea_link.data)
      let max = 0
      for (let i = 0; i < this.miniArea_link["data"].length; i++) {
        if(this.miniArea_link["data"][i].value>=max){
          max = this.miniArea_link["data"][i].value
          this.$set(this.water_link["data"],0,{"name":this.miniArea_link["data"][i].name,"value":(max)})
        }
      }
    },
    getPie_1(data) {
      if(document.getElementById("main_1").hasChildNodes()){
        document.getElementById("main_1").removeChild(document.getElementById("main_1").childNodes[0])
      }
      let piePlot = new Pie('main_1', {
        appendPadding: 10,
        data,
        angleField: 'value',
        colorField: 'name',
        radius: 1,
        innerRadius: 0.64,
        meta: {
          value: {
            formatter: (v) => `${v} 个`,
          },
        },
        label: {
          type: 'inner',
          offset: '-50%',
          autoRotate: false,
          style: { textAlign: 'center' },
          formatter: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
        },
        statistic: {
          title: {
            offsetY: -8,
          },
          content: {
            offsetY: -4,
          },
        },
        // 添加 中心统计文本 交互
        interactions: [
          { type: 'element-selected' },
          { type: 'element-active' },
          {
            type: 'pie-statistic-active',
            cfg: {
              start: [
                { trigger: 'element:mouseenter', action: 'pie-statistic:change' },
                { trigger: 'legend-item:mouseenter', action: 'pie-statistic:change' },
              ],
              end: [
                { trigger: 'element:mouseleave', action: 'pie-statistic:reset' },
                { trigger: 'legend-item:mouseleave', action: 'pie-statistic:reset' },
              ],
            },
          },
        ],
      });
      piePlot.render()
    },
    getPie_2(data) {
      if(document.getElementById("main_2").hasChildNodes()){
        document.getElementById("main_2").removeChild(document.getElementById("main_2").childNodes[0])
      }
      const piePlot = new Pie('main_2', {
        appendPadding: 10,
        data,
        angleField: 'value',
        colorField: 'name',
        radius: 1,
        innerRadius: 0.64,
        meta: {
          value: {
            formatter: (v) => `${v} 个`,
          },
        },
        label: {
          type: 'inner',
          offset: '-50%',
          autoRotate: false,
          style: { textAlign: 'center' },
          formatter: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
        },
        statistic: {
          title: {
            offsetY: -8,
          },
          content: {
            offsetY: -4,
          },
        },
        // 添加 中心统计文本 交互
        interactions: [
          { type: 'element-selected' },
          { type: 'element-active' },
          {
            type: 'pie-statistic-active',
            cfg: {
              start: [
                { trigger: 'element:mouseenter', action: 'pie-statistic:change' },
                { trigger: 'legend-item:mouseenter', action: 'pie-statistic:change' },
              ],
              end: [
                { trigger: 'element:mouseleave', action: 'pie-statistic:reset' },
                { trigger: 'legend-item:mouseleave', action: 'pie-statistic:reset' },
              ],
            },
          },
        ],
      });
      piePlot.render();
    },
    getWater_1(data) {
      //console.log(data)
      if(document.getElementById("container_1").hasChildNodes()){
        document.getElementById("container_1").removeChild(document.getElementById("container_1").childNodes[0])
      }
      const wordCloud = new WordCloud('container_1', {
        data,
        wordField: 'name',
        weightField: 'value',
        colorField: 'name',
        wordStyle: {
          fontFamily: 'Verdana',
          fontSize: [32, 60],
          rotation: 0,
        },
        // 返回值设置成一个 [0, 1) 区间内的值，
        // 可以让每次渲染的位置相同（前提是每次的宽高一致）。
        random: () => 0.5,
      });
      wordCloud.render();
    },
    getWater_2(data) {
      //console.log(data)
      if(document.getElementById("container_2").hasChildNodes()){
        document.getElementById("container_2").removeChild(document.getElementById("container_2").childNodes[0])
      }
      const wordCloud = new WordCloud('container_2', {
        data,
        wordField: 'name',
        weightField: 'value',
        colorField: 'name',
        wordStyle: {
          fontFamily: 'Verdana',
          fontSize: [32, 80],
          rotation: 0,
        },
        // 返回值设置成一个 [0, 1) 区间内的值，
        // 可以让每次渲染的位置相同（前提是每次的宽高一致）。
        random: () => 0.5,
      });
      wordCloud.render();
    },

    rank_no(){
      let len=this.miniArea_node.data.length;
      for(let i = 0;i<len;i++){
        this.$set(this.rank_node,i, {"name":this.miniArea_node.data[i].name,"total":this.miniArea_node.data[i].value})
      }
      while(len>1){
        for(let j=0;j<len-1;j++){
          if(this.rank_node[j].total<this.rank_node[j+1].total){
              [this.rank_node[j],this.rank_node[j+1]]= [this.rank_node[j+1],this.rank_node[j]]
          }
        }
        len--;
      }
      len=this.miniArea_link.data.length;
      for(let i = 0;i<len;i++){
        this.$set(this.rank_link,i, {"name":this.miniArea_link.data[i].name,"total":this.miniArea_link.data[i].value})
      }
      while(len>1){
        for(let j=0;j<len-1;j++){
          if(this.rank_link[j].total<this.rank_link[j+1].total){
            [this.rank_link[j],this.rank_link[j+1]]= [this.rank_link[j+1],this.rank_link[j]]
          }
        }
        len--;
      }
      //console.log(this.rank_node,this.rank_link)
    },
    change_node_path(){
      let tmp = this.temp
      let shuzu = []
      for(let i = 0;i<this.temp.nodes.length;i++){
        if(this.node_path[this.temp.nodes[i].flagSeg]===undefined){
          this.$set(this.node_path,this.temp.nodes[i].flagSeg,''+this.temp.nodes[i].id)
        }
        else{
          this.node_path[this.temp.nodes[i].flagSeg]=this.node_path[this.temp.nodes[i].flagSeg]+','+this.temp.nodes[i].id
        }
      }

      for(let i = 0;i<this.temp.nodes.length;i++){
        this.$set(this.node_path_neighbor,0,'')
        this.$set(this.node_path_neighbor,this.temp.nodes[i].id,''+this.temp.nodes[i].id)
        for(let j = 0;j<this.temp.links.length;j++){
          if(getId(this.temp.links[j].source) === this.temp.nodes[i].id){
            this.node_path_neighbor[this.temp.nodes[i].id] = this.node_path_neighbor[this.temp.nodes[i].id]+','+getId(this.temp.links[j].target)
          }
          if(getId(this.temp.links[j].target) === this.temp.nodes[i].id){
            this.node_path_neighbor[this.temp.nodes[i].id] = this.node_path_neighbor[this.temp.nodes[i].id]+','+getId(this.temp.links[j].source)
          }
        }
      }
      for(let i = 1;i<this.node_path_neighbor.length;i++){
        this.node_path_neighbor[i] = [...new Set(this.node_path_neighbor[i].split(','))].sort().toString()
      }
      for(let i = 0;i<this.temp.nodes.length;i++){
        if(this.node_path_neighbor[this.temp.nodes.flagSeg]===undefined){
          this.$set(this.node_path_neighbor,this.temp.nodes[i].flagSeg,[this.temp.nodes[i].id.toString()])
        }
      }
      for(let i = 0;i<this.temp.nodes.length;i++){
        shuzu = this.node_path_neighbor[this.temp.nodes[i].id].split(',')

        this.node_path_neighbor[this.temp.nodes[i].flagSeg] = this.node_path_neighbor[this.temp.nodes[i].flagSeg].concat(shuzu)
      }
      for(let i = 0;i<this.miniArea_node.data.length;i++){
        this.node_path_neighbor[this.miniArea_node.data[i].name] = [...new Set(this.node_path_neighbor[this.miniArea_node.data[i].name])].sort().toString()
      }

      for(let i = 0;i<this.type_data.nodes.length ;i++){
        this.node_path_neighbor[this.type_data.nodes[i][0].flagSeg] = this.get_all_id(this.node_path_neighbor[this.type_data.nodes[i][0].flagSeg])
        this.node_path[this.type_data.nodes[i][0].flagSeg] = this.get_all_id(this.node_path[this.type_data.nodes[i][0].flagSeg])
      }
      for(let i = 1;i<this.temp.nodes.length+1;i++){
        this.node_path_neighbor[i] = this.get_all_id(this.node_path_neighbor[i])
      }

      function getId(name){
        for(let i = 0;i<tmp.nodes.length;i++){
          if(tmp.nodes[i].name===name){
            return tmp.nodes[i].id
          }
        }
      }
    },

    change_link_path(){
      let tmp = this.temp
      let shuzu = []
      for(let i = 0;i<this.temp.links.length;i++){
        if(this.link_path[this.temp.links[i].description]===undefined){
          this.$set(this.link_path,this.temp.links[i].description,''+this.temp.links[i].id)
        }
        else{
          this.link_path[this.temp.links[i].description]=this.link_path[this.temp.links[i].description]+','+this.temp.links[i].id
        }
      }

      for(let i = 0;i<this.type_data.links.length;i++){
        this.$set(this.link_path_neighbor,this.type_data.links[i][0].id,this.get_link_id(this.type_data.links[i][0].id))
      }

      for(let i = 0;i<this.temp.links.length;i++){
        this.$set(this.link_path_neighbor,this.temp.links[i].description,this.get_link_id(this.link_path[this.temp.links[i].description]))
      }
    },

    get_all_id(data){
      let shuzu
      if(data.search(",")===-1){
          shuzu = [data]
      }else {
        shuzu = data.split(',')
      }

      let guanxi = []
      let tmp = this.temp.links
      shuzu=shuzu.map(item => {
        return +item;
      });

      for(let i = 0;i<this.temp.links.length;i++){
        if(shuzu.includes(this.getId(tmp[i].source)) && shuzu.includes(this.getId(tmp[i].target))){
          guanxi = guanxi.concat(tmp[i].id)
        }
      }
      shuzu = shuzu.concat(guanxi)
      return [...new Set(shuzu)].toString()
    },

    get_link_id(data){
      let shuzu = []
      let guanxi
      if(data.toString().search(",")===-1){
        guanxi = [data]
      }else {
        guanxi = data.split(',')
      }

      guanxi=guanxi.map(item => {
        return +item;
      });
      for(let i = 0;i<guanxi.length;i++){
        for(let j = 0;j<this.temp.links.length;j++){
          if(this.temp.links[j].id === guanxi[i]){
            shuzu = shuzu.concat([this.getId(this.temp.links[j].source),this.getId(this.temp.links[j].target)])
          }
        }
      }
      shuzu = [...new Set(shuzu)]
      return shuzu.concat(guanxi).toString()
    },

    getId(name){
      for(let i = 0;i<this.temp.nodes.length;i++){
        if(this.temp.nodes[i].name===name){
          return this.temp.nodes[i].id
        }

      }
    }

  }
}
</script>

<style lang="scss" scoped>

@import "../../assets/css/all.css";

.clearfix{
  font-size: 20px;
}
.graph_1{
  margin-top: 20px;
  float: right;
  margin-left: 24px;
  width: 60%;
}
.graph_2{
  position: relative;
  top:100px;
  left: 10px;
  width:350px
}
.graph_3{
  margin-top: 20px;
  float: right;
  margin-left: 24px;
  width: 60%;
  height: 500px;
}
.type_setting{
  position: relative;
  top:20px;
  left: 20px;
  float: left;
  width: 35%
}
.btns_1{
/*  display: flex;
  justify-content: flex-end;*/
  z-index: 10;
  float: right;
  top:5px
}
.btns_2{
  /*  display: flex;
    justify-content: flex-end;*/
  z-index: 10;
  float: right;
  right: 13px;
  top:5px
}
</style>

