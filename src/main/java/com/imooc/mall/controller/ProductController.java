package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/product/detail")
    public ApiRestResponse showDetail(@RequestParam Integer id){
        Product product=productService.showDetail(id);
        return ApiRestResponse.success(product);
    }

    @PostMapping("/product/list")
    public ApiRestResponse showList(ProductListReq productListReq){
        PageInfo pageInfo=productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }
}
