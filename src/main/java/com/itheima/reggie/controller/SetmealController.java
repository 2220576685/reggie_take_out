package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/5 0005 21:17
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize, String name) {
//        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
//        Page<SetmealDto> dtoPage = new Page<>();
//
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(name != null, Setmeal::getName, name);
//        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
//
//        setmealService.page(pageInfo, queryWrapper);
//
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo, dtoPage, " records");
//        //创建records数据集合
//        List<Setmeal> records = pageInfo.getRecords();
//        List<SetmealDto> list = records.stream().map((imtl -> {
//
//            //创建新的集合表
//            SetmealDto setmealDto = new SetmealDto();
//
//            BeanUtils.copyProperties(imtl, setmealDto);
//            Long categoryId = imtl.getCategoryId();
//
//            Category category = categoryService.getById(categoryId);
//            if (category != null) {
//                String categoryName = category.getName();
//                setmealDto.setCategoryName(categoryName);
//            }
//            return setmealDto;
//
//        })).collect(Collectors.toList());
//        //收藏工具把records转换为list集合
//        dtoPage.setRecords(list);
//
//        return R.success(dtoPage);
//    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);


        BeanUtils.copyProperties(pageInfo, dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((impl -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(impl,setmealDto);
            Long categoryId = impl.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;

        })).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);


    }


    /**
     * 修改菜品状态码
     *
     * @param stat
     * @param ids
     * @return
     */
    @PostMapping("/status/{stat}")
    public R<String> status(@PathVariable int stat, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Setmeal> updateChainWrapper = new LambdaUpdateWrapper<>();

        updateChainWrapper.set(Setmeal::getStatus, stat);
        updateChainWrapper.in(Setmeal::getId, ids);

        setmealService.update(updateChainWrapper);
        return R.success("修改状态成功");

    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {

        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }


    /**
     * 根据id查询菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改菜品信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);

        return R.success("修改套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
