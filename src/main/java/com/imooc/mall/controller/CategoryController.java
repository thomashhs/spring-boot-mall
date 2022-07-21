package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.CategoryAddReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CategoryController {

    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody CategoryAddReq req) throws ImoocMallException {
        System.out.println("req:"+req);
        User user=(User)session.getAttribute("imooc_mall_user");
        if(user==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        if (userService.checkAdmin(user)) {
            categoryService.add(req);
            return ApiRestResponse.success();
        }else{
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_ADMIN);
        }

    }
}
