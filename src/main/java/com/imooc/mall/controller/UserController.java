package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/test/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id){
        return userService.selectByUserId(id);
    }

    @GetMapping("/test2")
    @ResponseBody
    public String test(){
        return "hello world";
    }

    @GetMapping("/register")
    @ResponseBody
    public ApiRestResponse register(String userName,String password){
        if(userName==null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if(password==null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        if(password.length()<8){
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
    }
}
