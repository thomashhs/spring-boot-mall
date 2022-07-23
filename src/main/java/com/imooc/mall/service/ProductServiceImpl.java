package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.request.ProductUpdateReq;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductServiceImpl implements ProductService{

    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(ProductAddReq productAddReq){
        Product product=productMapper.selectByName(productAddReq.getName());
        if(product!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        product=new Product();
        BeanUtils.copyProperties(productAddReq,product);
        int count=productMapper.insertSelective(product);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(ProductUpdateReq productUpdateReq){
        Product product=productMapper.selectByName(productUpdateReq.getName());
        if(product!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        product=new Product();
        BeanUtils.copyProperties(productUpdateReq,product);
        int count=productMapper.updateByPrimaryKeySelective(product);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Product product=productMapper.selectByPrimaryKey(id);
        if(product==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }

        int count=productMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }
}
