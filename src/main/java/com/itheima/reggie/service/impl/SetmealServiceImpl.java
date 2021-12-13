package com.itheima.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;

import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 13:50
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 保存套餐的基本信息
     *
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }
    /**
     * 修改套餐
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }

        setmealDishService.saveBatch(setmealDishes);


    }

    /**
     * 单个id查看套餐
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {

        //查询套餐基本信息，从dish表查询
        Setmeal byId = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId, setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getDishId, byId.getId());

        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;

    }
    /**
     * 批量删除
     *
     * @param ids
     */

    public void removeWithDish(List<Long> ids) {
//        A. 查询该批次套餐中是否存在售卖中的套餐, 如果存在, 不允许删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("该菜品还在售卖中，不能删除");
        }
        //  B. 删除套餐数据
        this.removeByIds(ids);
//        C. 删除套餐关联的菜品数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getId, ids);

        setmealDishService.remove(lambdaQueryWrapper);

    }
//    public void removeWithDish(List<Long> ids){
//        LambdaQueryWrapper<Setmeal>queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.in(Setmeal::getId,ids);
//        queryWrapper.eq(Setmeal::getStatus,1);
//        int count=this.count(queryWrapper);
//
//        if (count>0){
//            throw  new CustomException("该菜品还在售卖中，不能删除");
//        }
//        this.removeByIds(ids);
//
//        LambdaQueryWrapper<SetmealDish>lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.in(SetmealDish::getId,ids);
//        setmealDishService.remove(lambdaQueryWrapper);
//
//    }



}

