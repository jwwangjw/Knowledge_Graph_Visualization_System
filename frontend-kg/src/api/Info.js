import axios from "axios";

export {infoTestAPI, getKGraphAPI, addNewFileProjectAPI, getKGProjectAPI,saveFileJsonAPI,getSqlJsonAPI,addNewTxtProjectAPI,addTxtTriplesAPI,fusionAPI,deletePageAPI,renamePageAPI}

let url = '/api/info'

/**
 * res的信息可以在本代码中进行操作，返回仅需要的数据如 res.data.content，具体在控制台中查看，降低代码复杂度
 * 大部分传递body的情况下(post)，id为核心内容
 * 前端与后端接口，出现问题需及时沟通
 */

function infoTestAPI(text){
    return axios.get(url + '/' + text + '/getInput')
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

function addNewFileProjectAPI(param,userId) {
    return axios.post(url+'/'+ userId +'/fileParse',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function getKGProjectAPI(userId) {

    return axios.get(url+'/'+ userId +'/getKgFile')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function saveFileJsonAPI(param) {
    //console.log("api:", param)
    //console.log(JSON.stringify(param.json))
    return axios.post(url+'/saveFileJson',{id:param.id, json:JSON.stringify(param.json)})
        .then(res => {
            console.log(res)
            return res
        }).catch(error => {
            console.log(error)
        })
}

function getSqlJsonAPI(param) {
    //console.log('get json',param)
    return axios.get(url+'/'+param+'/getSqlJson')
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function addNewTxtProjectAPI(param,userId) {
    return axios.post(url+'/'+ userId +'/fileTxtParse',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function addTxtTriplesAPI(param) {
    //console.log(param)
    return axios.post(url+'/txtTriples',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function fusionAPI(param){
    //console.log(param)
    return axios.post(url+'/fusion',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })

}

function deletePageAPI(param){
    //console.log(param)
    return axios.post(url+'/'+param+'/deletePage',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })

}

function renamePageAPI(param){
    //console.log(param)
    return axios.post(url+'/renamePage',param)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })

}
