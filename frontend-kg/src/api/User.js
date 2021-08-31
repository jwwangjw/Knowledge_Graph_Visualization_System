import axios from "axios";

export{loginAPI,registerAPI,updateUserAPI}

let url = '/api/user'

/**
 * res的信息可以在本代码中进行操作，返回仅需要的数据如 res.data.content，具体在控制台中查看，降低代码复杂度
 * 大部分传递body的情况下(post)，id为核心内容
 * 前端与后端接口，出现问题需及时沟通
 */

function loginAPI(param){
    return axios.post(url + '/' + 'login' + '/' + param.username + '/' + param.password)
        .then(res => {

            return res
        }).catch(error => {
            console.log(error)
        })
}

function registerAPI(param){
    return axios.post(url + '/' + 'register' + '/' + param.phone + '/' + param.username + '/' + param.password)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}

function updateUserAPI(param){
    return axios.post(url + '/' + 'updateUser' + '/' + param.phone + '/' + param.username + '/' + param.password)
        .then(res => {
            return res
        }).catch(error => {
            console.log(error)
        })
}


