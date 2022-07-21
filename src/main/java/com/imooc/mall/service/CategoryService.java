package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.CategoryAddReq;

public interface CategoryService {

    void add(CategoryAddReq categoryAddReq);
}
