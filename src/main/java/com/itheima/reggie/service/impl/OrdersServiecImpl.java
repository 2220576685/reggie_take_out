package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrdersMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/9 0009 9:36
 */
@Slf4j
@Service
public class OrdersServiecImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts==null||shoppingCarts.size()==0){
                throw new ClassCastException("购物车为空，不可以下单");

        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if (addressBook==null){
            throw new ClassCastException("地址为空，不可以下单");
        }
        //订单号
        long orderId = IdWorker.getId();

        AtomicInteger amount=new AtomicInteger(0);
       List<OrderDetail>orderDetails = shoppingCarts.stream().map((item)->{
           OrderDetail orderDetail = new OrderDetail();
           orderDetail.setOrderId(orderId);
           orderDetail.setNumber(item.getNumber());
           orderDetail.setDishFlavor(item.getDishFlavor());
           orderDetail.setDishId(item.getDishId());
           orderDetail.setSetmealId(item.getSetmealId());
           orderDetail.setName(item.getName());
           orderDetail.setImage(item.getImage());
           orderDetail.setAmount(item.getAmount());
           amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
           return orderDetail;



        }).collect(Collectors.toList());

        //组装订单数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
    //像订单表插入一条数据
        this.save(orders);

        //向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);


        //清空购物车
        shoppingCartService.remove(wrapper);
    }
}
