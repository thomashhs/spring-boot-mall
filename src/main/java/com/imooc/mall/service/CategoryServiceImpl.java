package com.imooc.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.CategoryAddReq;
import com.imooc.mall.model.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public void add(CategoryAddReq categoryAddReq) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryAddReq,category);
        Category categoryOld=categoryMapper.selectByName(categoryAddReq.getName());
        if(categoryOld!=null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count=categoryMapper.insertSelective(category);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Category categoryUpdate){
        if(categoryUpdate.getName()!=null){
            Category categoryOld=categoryMapper.selectByName(categoryUpdate.getName());
            if(categoryOld!=null && !categoryUpdate.getId().equals(categoryOld.getId())){
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count=categoryMapper.updateByPrimaryKeySelective(categoryUpdate);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Category categoryOld=categoryMapper.selectByPrimaryKey(id);
        if(categoryOld==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        int count=categoryMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categoryList=categoryMapper.selectList();
        PageInfo pageInfo=new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    public List<CategoryVO> listForCustomer(Integer parentId){
        List<CategoryVO> categoryVOList=new ArrayList<>();
        recursiveFindCategories(categoryVOList,parentId);
        return categoryVOList;
    }

    public void recursiveFindCategories(List<CategoryVO> categoryVOList,Integer parentId){
        List<Category> categoryList=categoryMapper.selectByParentId(parentId);
        if(!CollectionUtils.isEmpty(categoryList)){
            for(int i=0;i<categoryList.size();i++){
                CategoryVO categoryVO=new CategoryVO();
                Category category=categoryList.get(i);
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOList.add(categoryVO);
                recursiveFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }


}
