package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/9 0009 9:36
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
