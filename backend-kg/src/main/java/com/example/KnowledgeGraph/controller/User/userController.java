package com.example.KnowledgeGraph.controller.User;

import com.example.KnowledgeGraph.bl.User.userService;
import com.example.KnowledgeGraph.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")

public class userController {
    private final static String ACCOUNT_INFO_ERROR = "用户名或密码错误";
    private final static String USERID_NOT_EXIST = "用户名不存在";

    @Autowired
    userService userService;

    @PostMapping("/login/{name}/{password}")
    public ResponseVO login(@PathVariable String name, @PathVariable String password) {

        return userService.login(name,password);

    }

    @PostMapping("/register/{phone}/{name}/{password}")
    public ResponseVO register(@PathVariable String phone,@PathVariable String name, @PathVariable String password) {
        return userService.register(phone,name,password);
    }

    @PostMapping("/updateUser/{phone}/{name}/{password}")
    public ResponseVO updateUser(@PathVariable String phone,@PathVariable String name, @PathVariable String password) {
        return userService.updateUser(phone,name,password);
    }

}
