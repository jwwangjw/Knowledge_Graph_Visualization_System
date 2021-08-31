package com.example.KnowledgeGraph.bllmpl.User;

import java.util.regex.*;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.KnowledgeGraph.data.readMapper;
import com.example.KnowledgeGraph.bl.User.userService;


@Service
public class userServiceImpl implements userService{
    @Autowired
    readMapper readMapper;

    @Autowired
    userServiceImpl userService;

    @Override
    public ResponseVO login(String name,String password){
        //System.out.println(name);
        //System.out.println(password);
        String temp = readMapper.getPassword(name);   // 从数据库中查找用户名相应的密码
        //System.out.println(temp);
        if(temp == null){
            return ResponseVO.buildFailure("用户名不存在！");
        }
        if(!temp.equals(password)){
            return ResponseVO.buildFailure("密码错误！");
        }
        int uid = readMapper.getUserId(name);

        return ResponseVO.buildSuccess(uid);
    }

    @Override
    public ResponseVO register(String phone,String name,String password){
        String temp = readMapper.getPassword(name);

        if(phone.length()!=11 || !isNum(phone)){
            return ResponseVO.buildFailure("手机号必须由11位数字组成！");
        }
        if(name.length()>8 || name.length()<4 || !isLetterDigit(name)){
            return ResponseVO.buildFailure("用户名必须由4-8位数字和字母组成！");
        }
        if(temp!=null){
            return ResponseVO.buildFailure("用户名已存在！");
        }
        if(password.length()>16 || password.length()<6 || !isLetterDigit(password)){
            return ResponseVO.buildFailure("密码必须为6-16位数字和字母组成！");
        }

        readMapper.register(phone,name,password);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO updateUser(String phone,String name,String password){
        String temp = readMapper.getPWD(phone,name);
        //System.out.println(temp);
        if(temp==null){
            return ResponseVO.buildFailure("不存在手机号和用户名为输入内容的账户！");
        }else{
            if(password.length()>16 || password.length()<6 || !isLetterDigit(password)){
                return ResponseVO.buildFailure("新密码必须为6-16位数字和字母组成！");
            }
            else {
                readMapper.updateAccount(name, password);
            }
        }
        return ResponseVO.buildSuccess(true);
    }

   /**
    * 判断字符串是否为纯数字
    * @param str 手机号
    * @return boolean
    * */
    public boolean isNum(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否为数字+字母
     * @param str 用户名/密码
     * @return boolean
     * */

    public static boolean isLetterDigit(String str) {
        String regex = "^[A-Za-z0-9]+$";
        return str.matches(regex);
    }

}
