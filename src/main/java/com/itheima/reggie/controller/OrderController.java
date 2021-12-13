package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.OrdersService;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/9 0009 9:42
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;

    /**
     * 提交订单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        ordersService.submit(orders);
        return R.success("下单成功");

    }

    /**
     * 查看用户订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        ordersService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);

    }

    /**
     * 后端订单详情
     *
     * @param page
     * @param pageSize
     * @param number
     * @return
     */

    @GetMapping("/page")
    public R<Page<Orders>> userPage(int page, int pageSize, String number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {


        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.gt(beginTime != null, Orders::getCheckoutTime, beginTime)
                .lt(endTime != null, Orders::getCheckoutTime, endTime);

        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        ordersService.page(pageInfo, queryWrapper);
        Page<User> userPage = new Page<>();
        BeanUtils.copyProperties(userPage, pageInfo, "records");

        List<Orders> records = pageInfo.getRecords();

        List<Orders> list = records.stream().map((item) -> {

            Orders orders = new Orders();
            BeanUtils.copyProperties(item, orders);
            //  获取订单表中用户id
            Long userId = item.getUserId();

            //  根据订单用户id，将用户表和订单表关联
            User byId = userService.getById(userId);
            //将用户表name，传给订单表中

            String name = byId.getName();
            orders.setUserName(name);

            return orders;

        }).collect(Collectors.toList());
        pageInfo.setRecords(list);


        return R.success(pageInfo);
    }

    /**
     * 派送状态修改
     *
     * @param status
     * @param number
     * @return
     */
    @PutMapping
    public R<String> update(Integer status, String number) {
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Orders::getStatus, status);
        wrapper.in(Orders::getNumber, number);

        ordersService.update(wrapper);
        return R.success("派送成功");

    }
}
