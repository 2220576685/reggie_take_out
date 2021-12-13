package com.itheima.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 11:39
 */
public interface CategoryService extends IService<Category> {
    //根据ID删除分类
    public void remove(Long id);
}
