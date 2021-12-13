package com.itheima.reggie.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.ShoppingCart;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/7 0007 19:05
 */
@Transactional
public interface ShoppingCartService extends IService<ShoppingCart> {

}
