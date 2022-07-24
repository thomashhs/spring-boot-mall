package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.CategoryAddReq;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.request.ProductUpdateReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

public interface ProductService {

    void add(ProductAddReq productAddReq);

    void update(ProductUpdateReq productUpdateReq);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo<Product> selectList(Integer pageNum, Integer pageSize);

    Product showDetail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
