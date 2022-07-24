package com.imooc.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.ProductAddReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.request.ProductUpdateReq;
import com.imooc.mall.model.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryService categoryService;

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

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
        productMapper.batchUpdateSellStatus(ids,sellStatus);
    }

    @Override
    public PageInfo<Product> selectList(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectList();
        PageInfo pageInfo=new PageInfo(productList);
        return pageInfo;
    }

    @Override
    public Product showDetail(Integer id){
        Product product=productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        ProductListQuery productListQuery=new ProductListQuery();
        if(!StringUtils.isEmpty(productListReq.getKeyword())){
            String keyword=new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }
        System.out.println("productListReq:"+productListReq);
        if(productListReq.getCategoryId()!=null){
            List<CategoryVO> categoryVOList=categoryService.listForCustomer(productListReq.getCategoryId());
            List<Integer> catogoryIds=new ArrayList<>();
            catogoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList,catogoryIds);
            productListQuery.setCategoryIds(catogoryIds);
        }
        PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        List<Product> productList=productMapper.selectListForCustomer(productListQuery);
        System.out.println("productListQuery:"+productListQuery);
        PageInfo pageInfo=new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList,List<Integer> catogoryIds){
        for(int i=0;i<categoryVOList.size();i++){
            CategoryVO categoryVO=categoryVOList.get(i);
            if(categoryVO!=null){
                catogoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(),catogoryIds);
            }

        }

    }
}
