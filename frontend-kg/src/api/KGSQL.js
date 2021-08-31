import axios from "axios";

export {addNodeAPI, addLinkAPI,deleteNodeAPI,deleteLinkAPI,updateNodeAPI,updateLinkAPI,getHistoryAPI,updateHistoryAPI,addHistoryAPI}

let url = '/api/sql'

function addNodeAPI(param){
    //console.log('addNodeAPI');
    return axios.post(url+'/'+param.name+'/'+param.flag+'/'+param.property+'/addSqlNode', {id: param.id, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}

function addLinkAPI(param) {
    //console.log('addLinkAPI');
    return axios.post(url+'/'+param.sid+'/'+param.tid+'/'+param.name+'/'+param.flag+'/addSqlRelation', {id: param.id, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function deleteNodeAPI(param){
    //console.log('deleteNodeAPI');
    return axios.post(url+'/'+param.id+'/deleteSqlNode', {id: param.fileId, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function deleteLinkAPI(param){
    //console.log('deleteLinkAPI');
    return axios.post(url+'/'+param.id+'/deleteSqlRelation', {id: param.fileId, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function updateNodeAPI(param){
    //console.log('updateNodeAPI');
    return axios.post(url+'/'+param.id+'/'+param.name+'/'+param.flag+'/'+param.property+'/updateSqlNode', {id: param.fileId, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function updateLinkAPI(param){
    //console.log('updateLinkAPI');
    return axios.post(url+'/'+param.id+'/'+param.name+'/'+param.flag+'/updateSqlRelation', {id: param.fileId, json:JSON.stringify(param.json)})
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function getHistoryAPI(file_id){
    return axios.post(url+'/'+file_id+'/getSearchRecord')
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}
function updateHistoryAPI(param){
    return axios.post(url+'/'+param.fileId+'/'+param.record+'/updateSearchRecord')
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}

function addHistoryAPI(param){
    return axios.get(url+'/'+param.file_id+'/'+param.id+'/'+param.text+'/'+param.kd+'/addHistory')
        .then(res => {
            //console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}