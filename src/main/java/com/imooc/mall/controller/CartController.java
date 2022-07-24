package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("/cart/list")
    public ApiRestResponse list(){
        List<CartVO> cartVOList=cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/cart/add")
    public ApiRestResponse add(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOList=cartService.add(UserFilter.currentUser.getId(),productId,count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/cart/update")
    public ApiRestResponse update(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOList=cartService.update(UserFilter.currentUser.getId(),productId,count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/cart/delete")
    public ApiRestResponse delete(@RequestParam Integer productId){
        List<CartVO> cartVOList=cartService.delete(UserFilter.currentUser.getId(),productId);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/cart/select")
    public ApiRestResponse select(@RequestParam Integer productId,@RequestParam Integer selected){
        List<CartVO> cartVOList=cartService.selectOrNot(UserFilter.currentUser.getId(),productId,selected);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/cart/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected){
        List<CartVO> cartVOList=cartService.selectAll(UserFilter.currentUser.getId(),selected);
        return ApiRestResponse.success(cartVOList);
    }
}
