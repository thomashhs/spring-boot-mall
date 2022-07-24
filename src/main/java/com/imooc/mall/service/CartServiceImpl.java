package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.vo.CartVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<CartVO> list(Integer userId){
        List<CartVO> cartVOList=cartMapper.selectList(userId);
        for(CartVO cartVO:cartVOList){
            cartVO.setTotalPrice(cartVO.getPrice()*cartVO.getQuantity());
        }
        return cartVOList;
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart==null){
            cart=new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(1);
            cartMapper.insertSelective(cart);
        }else{
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(1);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return list(userId);
    }

    private void validProduct(Integer productId, Integer count) {
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null || product.getStatus().equals(0)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        if(product.getStock()<count){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }


    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }else{
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(1);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId){
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }else{
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected){
        Cart cart=cartMapper.selectByUserIdAndProductId(userId,productId);
        System.out.println("userId:"+userId);
        System.out.println("productId:"+productId);
        System.out.println("selected:"+selected);
        System.out.println("cart:"+cart);
        if(cart==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }else{
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return list(userId);
    }

    @Override
    public List<CartVO> selectAll(Integer userId,Integer selected){
        cartMapper.selectOrNot(userId,null,selected);
        return list(userId);
    }

}
