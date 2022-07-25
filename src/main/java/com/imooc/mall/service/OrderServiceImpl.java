package com.imooc.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderItemVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.util.OrderCodeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Resource
    private CartService cartService;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){
        //拿到用户ID
        Integer userId=UserFilter.currentUser.getId();

        //如果购物车已勾选的为空，报错
        List<CartVO> cartVOList=cartService.list(userId);
        List<CartVO> cartVOListTemp=new ArrayList<>();
        for(CartVO cartVO:cartVOList){
            if(cartVO.getSelected().equals(1)){
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList=cartVOListTemp;
        if(CollectionUtils.isEmpty(cartVOList)){
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }

        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);

        //把购物车对象转为订单item对象
        List<OrderItem> orderItemList=CartVOListToOrderItemList(cartVOList);

        //扣库存
        for(OrderItem orderItem:orderItemList){
            Product product=productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock=product.getStock()-orderItem.getQuantity();
            if(stock<0){
                throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }

        //把购物车中的已勾选商品删除
        for(CartVO cartVO:cartVOList){
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }

        //生成订单
        Order order=new Order();

        //生成订单号，有独立的规则
        order.setOrderNo(OrderCodeFactory.getOrderCode(userId.longValue()));
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(10);
        order.setPostage(0);
        order.setPaymentType(1);

        //插入到Order表
        orderMapper.insertSelective(order);
        //循环保存每个商品到order_item表
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return order.getOrderNo();
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice=0;
        for(OrderItem orderItem:orderItemList){
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }


    private void validSaleStatusAndStock(List<CartVO> cartVOList) {

        for(CartVO cartVO:cartVOList){

            Product product=productMapper.selectByPrimaryKey(cartVO.getProductId());
            if(product==null || product.getStatus().equals(0)){
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
            }
            if(product.getStock()<cartVO.getQuantity()){
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }

        }

    }


    private List<OrderItem> CartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList=new ArrayList<>();
        for(CartVO cartVO:cartVOList){
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    @Override
    public OrderVO detail(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        Integer userId=UserFilter.currentUser.getId();
        if(userId!=order.getUserId()){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }

        OrderVO orderVO=getOrderVO(order);
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        User user=UserFilter.currentUser;
        List<Order> orderList=orderMapper.selectByUserId(user.getId());
        List<OrderVO> orderVOList=getOrderVOList(orderList);
        PageInfo pageInfo=new PageInfo(orderVOList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void cancel(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        Integer userId=UserFilter.currentUser.getId();
        if(userId!=order.getUserId()){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    private List<OrderVO> getOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList=new ArrayList<>();
        for(Order order:orderList){
            OrderVO orderVO=new OrderVO();
            orderVO=getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        List<OrderItemVO> orderItemVOList=new ArrayList<>();
        List<OrderItem> orderItemList= orderItemMapper.selectByOrderNo(order.getOrderNo());
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO=new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(order.getOrderStatus()).getValue());
        return orderVO;
    }

}
