package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.request.ProductUpdateReq;
import com.imooc.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {


    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    List<CartVO> update(Integer userId, Integer productId, Integer count);

    List<CartVO> delete(Integer userId, Integer productId);

    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected);

    List<CartVO> selectAll(Integer userId, Integer selected);
}
