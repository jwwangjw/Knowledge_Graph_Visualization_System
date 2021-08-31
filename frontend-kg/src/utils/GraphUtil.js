import {message} from "ant-design-vue";
import {getHistoryAPI} from "../api/KGSQL";
import store from '../store/index';

export {setLinkNumber, getLinkId, judgeType, filterNodesXY, filterLinksXY,getSearchContentList,getSearchHistoryList}



function getSearchHistoryList(file_id){
    getHistoryAPI(file_id).then(res => {
        if(res.data.success){
            let res_temp = JSON.parse(res.data.content)
            let lit=[]
            for (var item in res_temp){
                let param_temp={
                    id:res_temp[item].id,
                    text:res_temp[item].text,
                    kd:res_temp[item].kd
                }
                lit.push(param_temp)
            }
            store.commit('set_historyList',lit)
        } else {
            message.error(res.data.message);
            store.state.history_list=[]
        }
    })
}

function getSearchContentList(file_id,neo4jGraph){
    let res_temp = neo4jGraph.nodes
    let temp_n = []
    let index = 0
    res_temp.forEach(d => {
        temp_n.push({
            text: "<ID>" + d.id + " - " + d.name,
            id:d.id,
            kd:index++
        })
        temp_n.push({
            text: "<描述>" + d.name + " - #" + d.id,
            id:d.id,
            kd:index++
        })
        temp_n.push({
            text: "<特征>" + d.flagSeg + " - #" + d.id,
            id:d.id,
            kd:index++
        })
        if(d.property!=""){
            const properties = JSON.parse(d.property)
            for(var key in properties){
                temp_n.push({
                    text: "<"+key+">" + properties[key] + " - #" + d.id,
                    id:d.id,
                    kd:index++
                })
            }
        }
    })
    let res_temp1 = neo4jGraph.links
    res_temp1.forEach(d=>{
        temp_n.push({
            text: "<ID>" + d.id + " - " + d.name,
            id:d.id,
            kd:index++
        })
        temp_n.push({
            text: "<描述>" + d.description + " - " + d.name,
            id:d.id,
            kd:index++
        })
    })
    store.commit("set_resultList",temp_n)
}

function setLinkNumber(group,type){
    //console.log('set')
    if(group.length==0) return;
    //对该分组内的关系按照方向进行分类，此处根据连接的实体ASCII值大小分成两部分
    var linksA = [], linksB = [];
    for(var i = 0;i<group.length;i++){
        var link = group[i];
        if(link.source < link.target){
            linksA.push(link);
        }else{
            linksB.push(link);
        }
    }
    //确定关系最大编号。为了使得连接两个实体的关系曲线呈现对称，根据关系数量奇偶性进行平分。
    //特殊情况：当关系都是连接到同一个实体时，不平分
    var maxLinkNumber = 0;
    if(type=='self'){
        maxLinkNumber = group.length;
    }else{
        maxLinkNumber = group.length%2==0?group.length/2:(group.length+1)/2;
    }
    //如果两个方向的关系数量一样多，直接分别设置编号即可
    if(linksA.length==linksB.length){
        var startLinkNumber0 = 1;
        for(var a=0;a<linksA.length;a++){
            linksA[a].linknum = startLinkNumber0++;
        }
        startLinkNumber0 = 1;
        for(var b=0;b<linksB.length;b++){
            linksB[b].linknum = startLinkNumber0++;
        }
    }else{
        //当两个方向的关系数量不对等时，先对数量少的那组关系从最大编号值进行逆序编号，然后在对另一组数量多的关系从编号1一直编号到最大编号，再对剩余关系进行负编号
        //如果抛开负号，可以发现，最终所有关系的编号序列一定是对称的（对称是为了保证后续绘图时曲线的弯曲程度也是对称的）
        var biggerLinks,smallerLinks;
        if(linksA.length>linksB.length){
            biggerLinks = linksA;
            smallerLinks = linksB;
        }else{
            biggerLinks = linksB;
            smallerLinks = linksA;
        }

        var startLinkNumber1 = maxLinkNumber;
        for(var c=0;c<smallerLinks.length;c++){
            smallerLinks[c].linknum = startLinkNumber1--;
        }
        var tmpNumber = startLinkNumber1;

        startLinkNumber1 = 1;
        var p = 0;
        while(startLinkNumber1<=maxLinkNumber){
            biggerLinks[p++].linknum = startLinkNumber1++;
        }
        //开始负编号
        startLinkNumber1 = 0-tmpNumber;
        for(var d=p;d<biggerLinks.length;d++){
            biggerLinks[d].linknum = startLinkNumber1++;
        }
    }
}

function getLinkId(d){
    if(typeof (d.source) === 'object'){
        return d.source.id+"-"+d.description+"-"+d.target.id
    }
    else{
        return d.source+"-"+d.description+"-"+d.target
    }
}
function judgeType(d){
    if((d.type) == 1){
        return "3,3"
    } else {
        return "0,0"
    }
}
function filterNodesXY(arr){
    console.log("filterNodesXY")
    const temp = arr.map(item => {
        console.log(item)
        let obj = new Object()
        obj.flagSeg = item.flagSeg
        obj.name = item.name
        obj.id = item.id
        return obj
    });
    return temp
}
function filterLinksXY(arr){
    const temp = arr.map(item => {
        let obj = new Object()
        obj.description = item.description
        obj.id = item.id
        obj.type = item.type
        obj.source = item.source.name
        obj.target = item.target.name
        return obj
    })
    return temp
}