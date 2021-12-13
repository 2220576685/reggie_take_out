package com.itheima.reggie.controller;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.BaseStream;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/7 0007 19:08
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加到购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //  A. 获取当前登录用户，为购物车对象赋值
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        //  B. 根据当前登录用户ID 及 本次添加的菜品ID/套餐ID，查询购物车数据是否存在
        if (dishId != null) {
            //分类是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //分类是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //查询购物车数据是否存在
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //        C. 如果已经存在，就在原来数量基础上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);

        } else {
            //  D. 如果不存在，则添加到购物车，数量默认就是1
            shoppingCart.setNumber(1);
            //创建时间为当前时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            //调用save方法，将shoppingCart添加进入主页
            shoppingCartService.save(shoppingCart);
            //将购物车赋值给cartServiceOne对象
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);


    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }


    /**
     * 清除购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> delete() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清除购物车成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> jian(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //        C. 如果已经存在，就在原来数量基础上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);

        } else {
            return R.error("购物车为空，请先添加数据");


        }
        return R.success(cartServiceOne);

    }
}






