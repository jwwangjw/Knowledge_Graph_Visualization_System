import axios from "axios";

export {addNodeAPI,deleteNodeAPI,updateNodeAPI,addRelationAPI,deleteRelationAPI,updateRelationAPI,getKGraphAPI}

let url = '/api/kg'

/**
 * res的信息可以在本代码中进行操作，返回仅需要的数据如 res.data.content，具体在控制台中查看，降低代码复杂度
 * 大部分传递body的情况下(post)，id为核心内容
 * 前端与后端接口，出现问题需及时沟通
 */

function addNodeAPI(param) {
    //console.log(param.name)
    return axios.get(url + '/' + param.name + '/' + param.flag + '/addNode')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function deleteNodeAPI(param) {
    return axios.get(url + '/' + param.id + '/deleteNode')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

/**
 *
 * @param param 为ElementVO
 * @param text .name .flag
 * @returns {Promise<AxiosResponse<any>>}
 */
function updateNodeAPI(param) {
    return axios.get(url + '/' + param.id + '/'+param.name+'/'+param.flag+'/updateNode')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

/**
 *
 * @param param1 源节点
 * @param param2 目标节点
 * @param text .name .flag
 * @returns {Promise<AxiosResponse<any>>}
 */
function addRelationAPI(param) {
    return axios.get(url+'/'+param.sid+'/'+param.tid+'/'+param.name+'/'+param.flag+'/addRelation')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function deleteRelationAPI(param) {
    return axios.get(url+'/'+param.id+'/deleteRelation')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function updateRelationAPI(param) {
    return axios.get(url+'/'+param.id+'/'+param.description +'/'+param.type+'/updateRelation')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })

}

function getKGraphAPI(){
    return axios.get(url + '/getKG')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}