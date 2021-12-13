package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 13:47
 */
public interface DishService extends IService<Dish> {
  public void saveWithFlavor(DishDto dishDto);

  DishDto getByIdWithFlavor(Long id);
  public void updateWithFlavor(DishDto dishDto);
}
