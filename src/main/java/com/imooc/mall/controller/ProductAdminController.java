package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.request.ProductUpdateReq;
import com.imooc.mall.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class ProductAdminController {

    @Resource
    private ProductService productService;

    @PostMapping("/admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(@Valid @RequestBody ProductAddReq productAddReq){
        productService.add(productAddReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/product/update")
    @ResponseBody
    public ApiRestResponse updateProduct(@Valid @RequestBody ProductUpdateReq productUpdateReq){
        productService.update(productUpdateReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/product/delete")
    @ResponseBody
    public ApiRestResponse deleteProduct(Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }
}
