package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.CategoryAddReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    void add(CategoryAddReq categoryAddReq);

    void update(Category categoryUpdate);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listForCustomer(Integer parentId);
}
