<template>
  <a-layout class="graph">
    <a-layout-content class="graph-container" @contextmenu.prevent=""></a-layout-content>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <div class="tooltip"></div>
    <div class="progress" v-show="modify1">
      <a-progress type="dashboard" :percent="percent1/2" :width="50" :format="() => percent1+'%'"/>
      <a-button-group>
        <a-button icon="minus-circle" @click="decline1" size="small" shape="circle"></a-button>
        <a-button icon="plus-circle" @click="increase1" size="small" shape="circle"/>
        <a-button icon="close-circle" @click="close_p1" size="small" shape="circle"/>
      </a-button-group>
    </div>
    <div class="progress1" v-show="modify2">
      <a-progress type="dashboard" :percent="percent2/2" :width="50" :format="() => percent2+'%'"/>
      <a-button-group>
        <a-button icon="minus-circle" @click="decline2" size="small" shape="circle"></a-button>
        <a-button icon="plus-circle" @click="increase2" size="small" shape="circle"/>
        <a-button icon="close-circle" @click="close_p2" size="small" shape="circle"/>
      </a-button-group>
    </div>
    <div class="progress2" v-show="modify3">
      <a-progress type="dashboard" :percent="percent3/2" :width="50" :format="() => percent3+'%'"/>
      <a-button-group>
        <a-button icon="minus-circle" @click="decline3" size="small" shape="circle"></a-button>
        <a-button icon="plus-circle" @click="increase3" size="small" shape="circle"/>
        <a-button icon="close-circle" @click="close_p3" size="small" shape="circle"/>
      </a-button-group>
    </div>
    <changeNodeModal ref="changeNodeModal" @setEntity="setEntity" @updateNode="updateNode"></changeNodeModal>
    <changeLinkModal ref="changeLinkModal" @setLink="setLink" @updateLink="updateLink"></changeLinkModal>
  </a-layout>
</template>
<script>
import * as d3 from 'd3'
import $ from 'jquery'
import {saveAs} from 'file-saver'
import {message} from "ant-design-vue";
import {saveSvgAsPng} from 'save-svg-as-png'
import {saveFileJsonAPI, getSqlJsonAPI,getKGraphAPI} from "@/api/Info"
import {addNodeAPI,addLinkAPI,deleteNodeAPI,deleteLinkAPI,updateNodeAPI,updateLinkAPI,getHistoryAPI,updateHistoryAPI,addHistoryAPI} from "../api/KGSQL";
import {setLinkNumber, getLinkId, judgeType, filterNodesXY, filterLinksXY} from "@/utils/GraphUtil.js";
import callUtils from "../utils/callUtils";
import changeNodeModal from "@/components/WorkTree/changeNodeModal";
import changeLinkModal from "@/components/WorkTree/changeLinkModal";
import {div} from "zrender";
import {getSearchContentList, getSearchHistoryList} from "../utils/GraphUtil";

export default {
  name: 'Home',
  components: {changeNodeModal,changeLinkModal},
  props: {
    id: {
      type: Number,
      required: true
    },
    ids: {
      type: String,
      required: true
    }
  },
  watch: {
    id(val,oldval) {
      this.save_Web(oldval);
      this.uploadOtherGraph(val);
    },
    ids(val,oldval) {
      if(val!='null'){
        this.filterNL(val);
      }
    }
  },
  data () {
    return {
      links_temp:[],
      history_list:[],
      node_temp:'',
      nodes_new:[],
      svgArea:'',
      zoomed:'',
      transform:{},
      d:'',
      zoom_id:0,
      collapsed_home:false,
      percent1:100,
      percent2:100,
      percent3:100,
      save:false,
      modify1:false,
      modify2:false,
      modify3:false,
      showDes:true,
      tmpEvent: undefined,  // 右击事件时暂存指定的事件
      tmpDatum: undefined,
      collapsed: false,
      r:35,
      // 视图宽高
      height:window.innerHeight,
      width:window.innerWidth,
      nodes: [],
      bigIsOn:false,
      links: [],
      nodesText: [],
      flagList:[],
      web_data:{
        "nodes":[],
        "links":[],
      },
      count:0,
      temp_store:[],
      simulation: null,
      colorList:[ // 20种颜色
        "#1f77b4", "#ff7f0e",
        "#2ca02c", "#d62728",
        "#9467bd", "#8c564b",
        "#e377c2", "#7f7f7f",
        "#bcbd22", "#17becf",
        "#aec7e8", "#ffbb78",
        "#98df8a", "#ff9896",
        "#c5b0d5", "#c49c94",
        "#f7b6d2", "#c7c7c7",
        "#dbdb8d", "#9edae5"
      ],
      testGraph:null,
      neo4jGraph:{
        "nodes": [],
        "links": []
      },
    }
  },
  mounted(){
    /*this.getGraph().then(res => {
      this.initGraph(this.neo4jGraph)
    })*/
    /*if(localStorage.getItem('file_num')==null) {
      window.localStorage.setItem('file_num', "1")
      window.localStorage.setItem('file_id', "1")

    } else {
      let file_id = Number(localStorage.getItem('file_id'))
      this.getGraphProject(file_id)
    }

    this.getGraph().then(res => {
      this.initGraph(res)
    })*/
    this.getGraphProject(Number(this.id)).then(res => {
      this.initGraph(this.neo4jGraph)
    })
    callUtils.$on('getCol',(data)=>{
      this.collapsed_home=data.val
    })
  },

  methods:{

    setneo4jGraph(res){
      this.neo4jGraph = $.parseJSON(res.data.content)
      //console.log("neo4j:")
      //console.log(this.neo4jGraph)
    },

    async getGraph(){
      const res = await getKGraphAPI()
      if(res){
        this.neo4jGraph = $.parseJSON(res.data.content)
        return this.neo4jGraph
      }else{
        alert("连接失败！")
      }
    },
    async getGraphProject(file_id){
      const res = await getSqlJsonAPI(file_id)
      if(res) {
        this.setneo4jGraph(res)
        return this.neo4jGraph
      }else{
        alert("连接失败！")
      }
    },
    initGraph(data){

      getSearchHistoryList(this.id);
      getSearchContentList(this.id, this.neo4jGraph);

      let _this = this
      const links = data.links
      const nodes = data.nodes

      let linkGroup = {}
      let linkMap = {}

      for(let i=0;i<links.length;i++){
        let keyi = links[i].source<links[i].target?links[i].source+':'+links[i].target:links[i].target+':'+links[i].source;

        // eslint-disable-next-line no-prototype-builtins
        if(!linkMap.hasOwnProperty(keyi)){
          linkMap[keyi] = 0;
        }
        linkMap[keyi]+=1;
        // eslint-disable-next-line no-prototype-builtins
        if(!linkGroup.hasOwnProperty(keyi)){
          linkGroup[keyi] = [];
        }
        linkGroup[keyi].push(links[i]);
      }
      for(let j=0; j<links.length; j++){
        let keyj = links[j].source<links[j].target?links[j].source+':'+links[j].target:links[j].target+':'+links[j].source;
        links[j].size = linkMap[keyj];
        //同一组的关系进行编号
        let group = linkGroup[keyj];
        let keyPair = keyj.split(':');
        let type = 'noself'//标示该组关系是指向两个不同实体还是同一个实体
        if(keyPair[0] == keyPair[1]){
          type = 'self';
        }
        //给节点分配编号
        setLinkNumber(group,type)
      }
      // 设置缩放
      const zoomed = d3.zoom().on("zoom", (event) =>{
        g.attr("transform", event.transform)
      }).scaleExtent([0.5,20])  // 缩放上下限
      _this.zoomed=zoomed
      const tooltip=d3.select(".tooltip")
      const progress=d3.select(".progress")
      const progress1=d3.select(".progress1")
      const progress2=d3.select(".progress2")

      const svgArea = d3.select(".graph-container")
          .append("svg")
          .attr("id", "graph-svg")
          .attr("viewBox", [-_this.width/2, -900/2, _this.width, 900])
          .call(zoomed)
          .attr('fill', 'white')

      svgArea.append("marker")  // 添加箭头
          .attr("id", "marker")
          .attr("orient", "auto")
          .attr("stroke-width",1.5)
          .attr("markerUnits", "strokeWidth") //设置为strokeWidth箭头会随着线的粗细发生变化
          .attr("markerUnits", "userSpaceOnUse")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", 70) //箭头坐标
          .attr("refY", 0)
          .attr("markerWidth", 6)
          .attr("markerHeight", 6)
          .append("path")
          .attr("d", "M 0 -5 L 10 0 L 0 5")
          .attr('fill', '#000')
          .attr("stroke-opacity", 1)
      _this.svgArea=svgArea

      _this.simulation = d3.forceSimulation(nodes)
          .force("link", d3.forceLink(links).id(d => d.name).distance(350))  // .distance(int)设置连线长度
          .force("collide", d3.forceCollide().radius(()=>60))  // 设置碰撞半径防止重叠
          .force("charge", d3.forceManyBody()/*.strength(-10)*/)  // .strength(int)负值为斥力，正值为吸引力
          .alpha(0.3)
          //.force("x", d3.forceX())  // 中心引力
          //.force("y", d3.forceY())
          //.force('center', d3.forceCenter(_this.width / 2, _this.height / 2))
          .on("tick", _this.tick);
      //console.log(_this.simulation)

      _this.dragger = _this.drag(_this.simulation)

      const g = svgArea.append("g")

      _this.links = g.append("g")
          .attr("class", "link")
          .attr("marker-end", "url(#marker)")
          .selectAll("path")
          .data(links)
          .join("path")
          .style("stroke-dasharray", judgeType)// 判断是否为虚线
          .attr('text-anchor', "middle")
          .attr("id", getLinkId)
          .attr("cursor", "pointer")
          .attr("stroke","#999")
          .attr("stroke-width",2.5)
          .attr("stroke-opacity", 1)
          .on("click",function (d) {
            const link = d.target.__data__
            const param = {
              'type':1,
              'id': link.id,
              'name': link.description,
              'flag': link.type
            }
            _this.$emit('setentity', param)
          })
          .on("contextmenu",function (d){
            progress2.style("left", (event.pageX) + "px")
                .style("top", (event.pageY - 28) + "px");
            const link = d.target.__data__
            const param = {
              'id': link.id,
              'name': link.description,
              'flag': link.type
            }
            //console.log(param)
            _this.d=link
            _this.$contextmenu({
              items: [
                {
                  label: "设置关系长度",
                  icon:"fa fa-close",
                  onClick: () => {
                    _this.modify3=true
                    _this.links_temp=links
                  }},{
                  label: "删除关系",
                  icon:"fa fa-close",
                  onClick: () => {
                    _this.deleteLink(param);
                  },
                  divided: true,
                }, {
                  label: "修改关系信息",
                  divided: true,
                  icon: "fa fa-gavel" ,
                  onClick: () => {
                    _this.changeLink(param);
                  }
                }, {
                  label: "隐藏/显示标签",
                  divided: true,
                  icon:"fa fa-wrench",
                  onClick:()=>{
                    if(_this.showDes){
                      let d1=_this.neo4jGraph.links.length
                      let temp_store=_this.temp_store
                      d3.selectAll('text').each(function (d,i){
                        if(i<d1){
                          temp_store.push(d.description)
                          d3.select(this).text("")
                        }
                      })
                      _this.showDes=false
                      _this.temp_store=temp_store
                    }else{
                      let d1=_this.neo4jGraph.links.length
                      let temp_store=_this.temp_store
                      //console.log(d3.selectAll('text'))
                      d3.selectAll('text').each(function (d,i){
                        if(i<d1){
                          d3.select(this)
                              .append("textPath")
                              .text("")
                              .style("font-size", "14px")
                              .attr('xlink:href', d => "#"+d.source.id+"-"+d.description+"-"+d.target.id)
                              .attr("fill", "#000")
                              .attr("startOffset", "47.5%")
                              .text("np").text(d=>{return d.description})
                        }
                      })

                      _this.showDes=true
                      _this.temp_store=[]
                    }
                  }
                },],
              event,
              //x: event.clientX,
              //y: event.clientY,
              customClass: "class-a",
              zIndex: 3,
              minWidth: 230
            });
            return false;
          })
          .on("mouseenter",function (d){
            d.target.style.stroke = '#6ac6ff'
            d.target.style.strokeWidth = 10
            const link=d.target.__data__
            let temp
            if(link.flag==1){
              temp="属性"
            }else {
              temp="关系"
            }
            tooltip.transition()
                .duration(100)
                .style("opacity",.9);
            tooltip.html("<font>"+"  ID : "+"</font>"+"<font color='#feff0a'>"+link.id+"  "+"</font>"+"</br>"+"  内容 : "+"<font color='#feff0a'>"+link.description+"  "+"</font>"+"<br>"+"  类型 : "+"<font color='#feff0a'>"+"关系"+"  "+"</font>"+"<br>"+"  特征 : "+"<font color='#feff0a'>"+temp+"  "+"</font>")
            if(!_this.collapsed_home){
              tooltip.style("left", (event.pageX-260) + "px")
                  .style("top", (event.pageY - 28) + "px");
            }else{
              tooltip.style("left", (event.pageX) + "px")
                  .style("top", (event.pageY - 28) + "px")}
          })
          .on("mouseleave", e => {
            e.target.style.stroke = '#999'
            e.target.style.strokeWidth = 2.5
            tooltip.transition()
                .duration(200)
                .style("opacity", 0);
          });

      _this.linksText = g.append("g")
          .attr("class", "link-type")
          .selectAll("linktext")
          .data(links)
          .enter()
          .append("text")
          .append("textPath")
          .style("font-size", "14px")
          .attr('xlink:href', d => "#"+d.source.id+"-"+d.description+"-"+d.target.id)
          .attr("fill", "#000")
          .attr("startOffset", "47.5%")
          .text(d => {
            let description = d.description;
            if (description.length > 5) return description.slice(0, 5) + "...";
            return d.description;
          })

      _this.nodes = g.append("g")
          .attr("class", "node")
          .selectAll("circle")
          .data(nodes)
          .join("circle")
          .attr("r", _this.$data.r)  // 半径
          .attr("fill", _this.color)
          .attr("cursor", "pointer")
          .attr("id", d => d.id)
          .on("dblclick", _this.queryProp) //双击事件获得属性
          .on("contextmenu",function(d){
            progress.style("left", (event.pageX) + "px")
                .style("top", (event.pageY - 28) + "px");
            progress1.style("left", (event.pageX) + "px")
                .style("top", (event.pageY - 28) + "px");
            event.preventDefault();
            const node = d.target.__data__
            const param = {
              'type':0,
              'id': node.id,
              'name':node.name,
              'flag':node.flagSeg
            }
            _this.$contextmenu({
              items: [
                {
                  label: "删除节点",
                  icon:"fa fa-close",
                  divided: true,
                  onClick: () => {
                    _this.deleteNode(param);
                  }
                },
                {
                  label: "修改节点信息",
                  icon: "fa fa-gavel" ,
                  onClick: () => {
                    _this.changeNode(node);
                  }
                }, {
                  label: "调整节点",
                  icon:"fa fa-wrench",
                  minWidth: 0,
                  children: [{ label: "调整节点大小" ,
                    onClick:()=>{
                      _this.$data.modify1=true
                      _this.$data.d=d.target
                      _this.nodes_new.push(d.target.__data__)
                    }
                  }, { label: "调整文字大小",
                    onClick:()=>{
                      _this.$data.modify2=true
                      _this.$data.d=d.target
                    }},
                    { label: "调整节点形状",
                    onClick:()=>{
                      _this.$data.d=d.target
                    }}]
                },],
              event,
              //x: event.clientX,
              //y: event.clientY,
              customClass: "class-a",
              zIndex: 3,
              minWidth: 230
            });
            return false;
          })
          .on("mouseenter",function (d){
            if(_this.nodes_new.indexOf(d.target.__data__)<0){
              d3.select(this).attr('r',35+6)
              const node=d.target.__data__
              tooltip.transition()
                  .duration(100)
                  .style("opacity",.9);
              if(node.property.length>2){
              let properties=JSON.parse(node.property);
              let temp_str=''
              for(var key in properties){
                temp_str=temp_str+"<font>"+"  "+key+" : "+"</font>"+"<font color='#feff0a'>"+properties[key]+"</font>"+"<br>"
              }
              tooltip.html("<font>"+"  ID : "+"</font>"+"<font color='#feff0a'>"+node.id+"  "+"</font>"+"</br>"+"  内容 : "+"<font color='#feff0a'>"+node.name+"  "+"</font>"+"<br>"+"  类型 : "+"<font color='#feff0a'>"+"节点"+"  "+"</font>"+"<br>"+"  特征 : "+"<font color='#feff0a'>"+node.flagSeg+"  "+"</font>"+"<br>"+temp_str)}else{
                tooltip.html("<font>"+"  ID : "+"</font>"+"<font color='#feff0a'>"+node.id+"  "+"</font>"+"</br>"+"  内容 : "+"<font color='#feff0a'>"+node.name+"  "+"</font>"+"<br>"+"  类型 : "+"<font color='#feff0a'>"+"节点"+"  "+"</font>"+"<br>"+"  特征 : "+"<font color='#feff0a'>"+node.flagSeg+"  "+"</font>"+"<br>")
              }
              if(!_this.collapsed_home){
                    tooltip.style("left", (event.pageX-260) + "px")
                           .style("top", (event.pageY - 28) + "px");
              }else{
                  tooltip.style("left", (event.pageX) + "px")
                  .style("top", (event.pageY - 28) + "px")}
            }else{
            d3.select(this).attr('r',_this.$data.r+6)
            const node=d.target.__data__
            tooltip.transition()
                   .duration(100)
                   .style("opacity",.9);
            tooltip.html("<font>"+"  ID : "+"</font>"+"<font color='#feff0a'>"+node.id+"  "+"</font>"+"</br>"+"  内容 : "+"<font color='#feff0a'>"+node.name+"  "+"</font>"+"<br>"+"  类型 : "+"<font color='#feff0a'>"+"节点"+"  "+"</font>"+"<br>"+"  特征 : "+"<font color='#feff0a'>"+node.flagSeg+"  "+"</font>")
                .style("left", (event.pageX-300) + "px")
                .style("top", (event.pageY) + "px");
          }})
          .on("mouseleave",function (d){
            if(_this.nodes_new.indexOf(d.target.__data__)<0){
              d3.select(this).attr('r',35)
              tooltip.transition()
                  .duration(200)
                  .style("opacity", 0);
            }else{
              d3.select(this).attr('r',_this.$data.r)
              tooltip.transition()
                  .duration(200)
                  .style("opacity", 0);
            }}
          )
          .call(_this.dragger);


      _this.nodesText = g.append("g")
          .attr("class","node-text")
          .selectAll("text")
          .data(nodes)
          .join("text")
          .attr('text-anchor', "middle")
          //.attr("dx",-27)
          .attr("dy",5.5)
          .style("font-size", "14px")
          .text(d => {
            let name = d.name;
            if (name.length > 4) return name.slice(0, 4) + "...";
            return d.name;
          })
      if(_this.$store.state.filterId.length>0){_this.filter_new(_this.$store.state.filterId)}
    },
    reset(){
      this.svgArea.transition()
          .duration(750)
          .call(this.zoomed.transform, d3.zoomIdentity);

    },
    refreshGraph(data){ // 更新图谱
      //console.log("graph refreshGraph")
      let _this = this;
      const links = data.links;
      const nodes = data.nodes;

      _this.links = _this.links
          .attr("class", "link")
          .data(links, getLinkId)
          .join("path")
          .attr("marker-end", "url(#marker)")
          .merge(_this.links)
          .style("stroke-dasharray", judgeType)
          .attr("stroke-width",2.5)
          .attr("stroke-opacity", 1)
          .on("click",function (d) {
            const link = d.target.__data__
            const param = {
              'type':1,
              'id': link.id,
              'name': link.description,
              'flag': link.type
            }
            this.$emit('setentity', param)
          })
          .on("mouseenter", e => {
            e.target.style.stroke = '#6ac6ff'
            e.target.style.strokeWidth = 10
          })
          .on("mouseleave", e => {
            e.target.style.stroke = '#999'
            e.target.style.strokeWidth = 5
          });

      _this.linksText
          .data(links,getLinkId)
          .join(
              enter =>
                  enter.append('text').append('textPath')
                      .attr('xlink:href', d => "#"+ getLinkId(d))
                      .attr("startOffset", "47.5%")
                      .text(d => {
                        let description = d.description;
                        if (description.length > 5) return description.slice(0, 5) + "...";
                        return d.description;
                      }),
              update =>
                  update
                      .attr('xlink:href', d => "#"+ getLinkId(d))
                      .text(d => {
                        let description = d.description;
                        if (description.length > 5) return description.slice(0, 5) + "...";
                        return d.description;
                      }),
              exit => exit.remove()
          )

      _this.nodes = _this.nodes
          .attr("class", "node")
          .data(nodes)
          .join(
              enter => enter.append("circle").attr("r", 30)
                  .call(enter => enter.transition().attr("r", 30))
                  .call(_this.dragger),
              update => update,
              exit => exit.remove()
          )
          .attr("r", 30)  // 半径
          .attr("fill", _this.color)
          .attr("cursor", "pointer")
          .attr("id", d => d.id)
          .merge(_this.nodes)
          .on("click", function(d){
            const node = d.target.__data__
            const param = {
              'type':0,
              'id': node.id,
              'name':node.name,
              'flag':node.flagSeg
            }
            _this.$emit('setentity', param)
          })
          .on("dblclick", _this.queryProp)
      //.call(_this.dragger)

      _this.nodesText = _this.nodesText
          .attr("class", "node-text")
          .data(nodes)
          .join("text")
          .attr("dx",0)
          .attr("dy",5.5)
          .merge(_this.nodesText)
          .text(d => {
            let name = d.name;
            if (name.length > 4) return name.slice(0, 4) + "...";
            return d.name;
          })

      _this.simulation.nodes(nodes)
      _this.simulation.force("link").links(links);
      _this.simulation.alpha(0.5).restart();
    },
    increase1() {
      let percent1 = this.percent1 + 10;
      this.percent1 = percent1;
      let d=this.$data.d
      this.$data.r=35*this.percent1/100
      d3.select(d).attr('r',this.$data.r)
    },
    decline1() {
      let percent1 = this.percent1 - 10;
      if (percent1 < 0) {
        percent1 = 0;
      }
      this.percent1 = percent1;
      let d=this.$data.d
      this.$data.r=35*this.percent1/100
      d3.select(d).attr('r',this.$data.r)
    },
    increase2() {
      let percent = this.percent2 + 10;
      this.percent2 = percent;
      let temp1=this.percent2*12/100
      let d1=this.$data.d.__data__
      d3.selectAll("text").each(function (d){
        let temp=d.name
        if(temp==d1.name){
          d3.select(this).style("font-size",temp1+"px")
        }
      })
    },
    color(d){ // 根据值选择颜色
      if(this.flagList.indexOf(d.flagSeg)===-1){
        this.flagList.push(d.flagSeg)
      }
      return this.colorList[this.flagList.indexOf(d.flagSeg)]
    },
    decline2() {
      let percent2 = this.percent2 - 10;
      if (percent2 < 0) {
        percent2 = 0;
      }
      this.percent2 = percent2;
      let temp1=this.percent2*12/100
      let d1=this.$data.d.__data__
      d3.selectAll("text").each(function (d){
        let temp=d.name
        if(temp==d1.name){
          d3.select(this).style("font-size",temp1+"px")
        }
      })},
    increase3() {
      let percent = this.percent3 + 10;
      this.percent3 = percent;
      let temp1=this.percent3*350/100
      let link=this.d
      let that=this
      d3.selectAll('path').each(function (d){
        if(d!=undefined){
          if(d.id==link.id){
            let temp=d
            that.simulation.force("link",d3.forceLink(that.links_temp).id(d=>d.name).distance(d=>{
              if(d.id==temp.id){
                return temp1
              }else{
                return 350
              }
            }))
          }
        }
      })
    },
    decline3() {
      let percent3 = this.percent3 - 10;
      if (percent3 < 0) {
        percent3 = 0;
      }
      this.percent3 = percent3;
      let temp1=this.percent3*350/100
      let that=this
      let link=this.d
      d3.selectAll('path').each(function (d){
        if(d!=undefined){
          if(d.id==link.id){
            let temp=d
            that.simulation.force("link",d3.forceLink(that.links_temp).id(d=>d.name).distance(d=>{
              if(d.id==temp.id){
                return temp1
              }else{
                return 350
              }
            }))
          }
        }
      })


    },
    close_p3(){
      this.modify3=false
    },
    drag(simulation){
      // node拖动操作
      //const _this = this
      function dragstarted(event) {

        if (!event.active) simulation.alphaTarget(0.3).restart();
        if(localStorage.getItem('pos')==null){
          //console.log('')
        }else{
          let pos_data = JSON.parse(localStorage.getItem('pos'))
          window.localStorage.removeItem('pos')
        }
        event.subject.fx = event.subject.x;
        event.subject.fy = event.subject.y;
      }

      function dragged(event) {
        event.subject.fx = event.x;
        event.subject.fy = event.y;
      }

      function dragended(event) {
        if (!event.active) simulation.alphaTarget(0);//.restart();
        //event.subject.fx = event.x;
        //event.subject.fy = event.y;
        event.subject.fx = null;
        event.subject.fy = null;
        //_this.save_Web("")

      }
      return d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended);
    },
    queryProp(d) {},
    tick() {
      this.links.attr("d", function (d) {

        //如果连接线连接的是同一个实体，则对path属性进行调整，绘制的圆弧属于长圆弧，同时对终点坐标进行微调，避免因坐标一致导致弧无法绘制
        if (d.target == d.source) {
          dr = 35 / d.linknum;
          return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 1,1 " + d.target.x + "," + (d.target.y + 1);
        } else if (d.size % 2 != 0 && d.linknum == 1) {//如果两个节点之间的连接线数量为奇数条，则设置编号为1的连接线为直线，其他连接线会均分在两边
          return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
        }
        //根据连接线编号值来动态确定该条椭圆弧线的长半轴和短半轴，当两者一致时绘制的是圆弧
        // 注意A属性后面的参数，前两个为长半轴和短半轴，第三个默认为0，第四个表示弧度大于180度则为1，小于则为0，这在绘制连接到相同节点的连接线时用到；第五个参数，0表示正角，1表示负角，即用来控制弧形凹凸的方向。本文正是结合编号的正负情况来控制该条连接线的凹凸方向，从而达到连接线对称的效果
        var curve = 1.5;
        var homogeneous = 1.2;
        var dx = d.target.x - d.source.x,
                dy = d.target.y - d.source.y,
                dr = Math.sqrt(dx * dx + dy * dy) * (d.linknum + homogeneous) / (curve * homogeneous);
        //当节点编号为负数时，对弧形进行反向凹凸，达到对称效果
        if (d.linknum < 0) {
          dr = Math.sqrt(dx * dx + dy * dy) * (-1 * d.linknum + homogeneous) / (curve * homogeneous);
          return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,0 " + d.target.x + "," + d.target.y;
        }
        return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
      });

      this.nodes.attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
      });

      this.nodesText.attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
      });
    },
    close_p1(){
      this.$data.modify1=false
      this.percent1=100
    },
    close_p2(){
      this.$data.modify2=false
      this.percent2=100
    },
    changeNode(param){
      this.$store.commit("set_changeNodeVisible",true)
      this.setEntity(param)

    },
    setEntity(param){
      this.$refs.changeNodeModal.setEntity(param)
    },
    updateNode(param){
      const graphdata = this.getWeb(this.id);
      const temp = {
        id:param.id,
        name : param.name,
        flag : param.flagSeg,
        property : param.property,
        fileId : graphdata.id,
        json : graphdata.json
      }
      updateNodeAPI(temp).then(res => {
        if(res.data.success){
          this.setneo4jGraph(res);
          this.uploadOtherGraph(this.id);
        } else {
          message.error(res.data.message)
        }
      })
      this.$store.commit('set_changeNodeVisible',false)
    },
    changeLink(param){
      this.$store.commit("set_changeLinkVisible",true)
      this.setLink(param)
    },
    setLink(param){
      this.$refs.changeLinkModal.setLink(param)
    },
    updateLink(param){
      const graphdata = this.getWeb(this.id);
      const temp = {
        id:param.id,
        name : param.name,
        flag : param.flag,
        fileId : graphdata.id,
        json : graphdata.json
      }
      updateLinkAPI(temp).then(res => {
        if(res.data.success){
          this.setneo4jGraph(res);
          this.uploadOtherGraph(this.id);
        } else {
          message.error(res.data.message)
        }
      })
      this.$store.commit('set_changeLinkVisible',false)
    },
    save_JSON(){
      let data = {}

      data["nodes"] = filterNodesXY(this.neo4jGraph.nodes)
      data["links"] = filterLinksXY(this.neo4jGraph.links)
      let test =  document.getElementById("graph-svg")
      let pos = test.lastChild.childNodes[2].firstChild
      for(let i=0;i<data["nodes"].length;i++){

        data["nodes"][i].transform = pos.getAttribute("transform")
        let temp = data["nodes"][i].transform.substring(data["nodes"][i].transform.indexOf("(")+1,data["nodes"][i].transform.indexOf(")"))
        let position = temp.split(",")
        data["nodes"][i].x = position[0]
        data["nodes"][i].y = position[1]
        pos = pos.nextSibling
      }

      let str = JSON.stringify(data, null, 2)
      let file = new File([str], "graph.json", { type: 'application/json; charset=utf=8;' })
      saveAs(file)

      //this.nodes = data["nodes"]
    },
    getWeb(file_id){
      //console.log('graph_saveweb')
      let graphdata = {};
      graphdata["nodes"] = this.neo4jGraph["nodes"]
      graphdata["links"] = filterLinksXY(this.neo4jGraph["links"])
      const temp = {
        'id': file_id,
        'json': graphdata,
      };
      return temp;
    },
    save_PNG(){
      saveSvgAsPng(document.getElementById("graph-svg"), "知识图谱.png", {
        left: -this.width/2, top: -900/2, encoderOptions: 1, scale: 2
      })
    },
    save_Web(file_id){
      const temp = this.getWeb(file_id);
      saveFileJsonAPI(temp).then(res => {
        if(res.data.success){
          message.success('保存成功');
        }else{
          message.error(res.data.message);
        }
      });
    },

    saveJSON(){
      this.save_JSON()
    },
    savePNG(){
      this.save_PNG()
    },
    saveWEB(file_id){
      this.save_Web(file_id)
    },
    uploadOtherGraph(file_id){
      this.getGraphProject(this.id).then(res => {
        this.flagList = [];
        $("#graph-svg").remove();
        this.initGraph(res);
      })
    },
    addNode(param){
      const graphdata = this.getWeb(this.id);
      const temp = {
        name : param.name,
        flag : param.flagSeg,
        property : param.property,
        id : graphdata.id,
        json : graphdata.json
      }
      addNodeAPI(temp).then(res => {
        if(res.data.success){
          this.setneo4jGraph(res);
          this.uploadOtherGraph(this.id);
        } else {
          message.error(res.data.message)
        }
      })
    },
    addLink(param){
      const graphdata = this.getWeb(this.id);
      const temp = {
        tid : param.tid,
        sid : param.sid,
        name : param.name,
        flag : param.flag,
        id : graphdata.id,
        json : graphdata.json
      }
      addLinkAPI(temp).then(res => {
        if(res.data.success){
          this.setneo4jGraph(res);
          this.uploadOtherGraph(this.id);
        } else {
          message.error(res.data.message);
        }
      })
    },
    deleteNode(param) {
      if(param.id <0){
        alert('错误的ID！')
        return
      }
      const graphdata = this.getWeb(this.id);
      var that = this;
      that.$confirm({
        title: '是否确认删除?',
        content: h => <div style="color:red;">删除后不可恢复</div>,
        onOk() {
          const temp = {
            id:param.id,
            fileId:graphdata.id,
            json : graphdata.json
          }
          deleteNodeAPI(temp).then(res => {
            if(res.data.success){
              that.setneo4jGraph(res);
              that.uploadOtherGraph(that.id);
              message.success("删除成功")
            } else {
              message.error(res.data.message)
            }
          })
        },
        onCancel() {
        },
        class: 'test',
      });

    },
    deleteLink(param) {
      //console.log(param)
      if(param.id <0){
        alert('错误的ID！')
        return
      }
      const graphdata = this.getWeb(this.id);
      var that = this;
      that.$confirm({
        title: '是否确认删除?',
        content: h => <div style="color:red;">删除后不可恢复</div>,
        onOk() {
          const temp = {
            id:param.id,
            fileId:graphdata.id,
            json : graphdata.json
          }
          deleteLinkAPI(temp).then(res => {
            if(res.data.success){
              that.setneo4jGraph(res);
              that.uploadOtherGraph(that.id);
              message.success("删除成功")
            } else {
              message.error(res.data.message)
            }
          })
        },
        onCancel() {
        },
        class: 'test',
      });

    },
    handleChange(param){
      this.filter_new([Number(param.id)]);
      const temp = {
        file_id:this.id,
        id:param.id,
        text:param.text,
        kd: param.kd
      }
      addHistoryAPI(temp).then(res => {
        if(res.data.success){
          getSearchHistoryList(this.id);
        } else {
          message.error(res.data.message);
        }
      })
    },
    closeSearch(){
      const that=this
      if(that.node_temp.id<500){
        d3.selectAll('circle').each(function(d){
          if(d.id==that.node_temp.id){
            d3.select(this).attr('opacity',"1")
            that.node_temp=d
          }
        })
      }
      this.closeFilter();
    },
    filterNL(ids){
      var temp = this.ids.split(",");
      let filterId=[]
      for(var i in temp){
        filterId.push(Number(temp[i]));
      }
      this.$store.commit('set_filterId',filterId)
    },
    filter_new(filterId){
      this.closeFilter()
      let m_t=false
      for(var i in filterId){
        if(filterId[i]>500){
          m_t=true
        }
      }
      d3.selectAll('circle').each(function (d) {
        if(filterId.indexOf(d.id)<0){
          d3.select(this).attr('opacity','0.3')
        }
      })
      d3.selectAll('path').each(function (d){
        if(d!=undefined){
          if(m_t){
            if(filterId.indexOf(d.id)<0){
              d3.select(this).attr('opacity','0.3')
            }
          }
          if((filterId.indexOf(d.source.id)<0 || filterId.indexOf(d.target.id)<0)&&filterId.indexOf(d.id)<0){
            d3.select(this).attr('opacity','0.3')
          }else{
            console.log(d)
          }
        }
      })
    },
    closeFilter(){
      d3.selectAll('circle').each(function (d) {
        d3.select(this).attr('opacity','1')
      })
      d3.selectAll('path').each(function (d){
        d3.select(this).attr('opacity','1')
      })
    },
    refresh(){
      this.$emit('refresh')
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
a {
  color: #42b983;
}
.graph {
  width: 100%;
  height: 100%;/*如果可以，则将其改为100%，源模板改为100%后无法正常显示图谱*/
  cursor: move;
}
.graph-container {
  background-color: white;
  line-height: 160px;
  overflow: visible;
}
div.tooltip {
  position: absolute;
  text-align: justify;
  padding: 2px;
  font: 12px sans-serif;
  background: lightsteelblue;
  border: 5px;
  border-radius: 8px;
  pointer-events: none;
  opacity: 0;
  white-space:pre-wrap;
  line-height: 2em;
}
div.progress{
  position: absolute;
}
div.progress1{
  position: absolute;
}
div.progress2{
  position: absolute;
}
</style>
