package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.CategoryAddReq;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

public interface ProductService {

    void add(ProductAddReq productAddReq);
}
