package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order/create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo=orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("/order/detail")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO=orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("/order/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo=orderService.listForCustomer(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("/order/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/order/qrcode")
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress=orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @PostMapping("/order/pay")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }


}
