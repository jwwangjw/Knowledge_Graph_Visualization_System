package com.example.KnowledgeGraph.bl.User;

import com.example.KnowledgeGraph.vo.ResponseVO;

public interface userService {

   /**
    * 用户登录判断
    *
    * @param name 用户名
    * @param password 密码
    * @return ResponseVO
    * */

    ResponseVO login(String name,String password);

    /**
     * 用户注册
     * @param phone 电话
     * @param name  用户名
     * @param password 密码
     * @return ResponseVO
     * */

    ResponseVO register(String phone,String name,String password);

    /**
     * 修改用户信息
     * @param phone 电话
     * @param name  用户名
     * @param password 密码
     * @return ResponseVO
     * */

    ResponseVO updateUser(String phone,String name,String password);


}
