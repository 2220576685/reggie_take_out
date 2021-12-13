package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 13:47
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    void updateWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);
}
