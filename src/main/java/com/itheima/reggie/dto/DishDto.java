package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/4 0004 10:17
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors =new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
