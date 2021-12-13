package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 11:47
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新建分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加菜品成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize){
//        //分页构造器
//        Page<Category> pageInfo = new Page<>(page,pageSize);
//        //条件构造器
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        //添加排序条件，根据sort进行排序
//        queryWrapper.orderByAsc(Category::getSort);
//
//        //分页查询
//        categoryService.page(pageInfo,queryWrapper);
//        return R.success(pageInfo);
//    }
    @GetMapping("/page")
    public R<Page>page(int page,int pageSize){
        Page<Category>pageInfo =new Page<>(page,pageSize);

        LambdaQueryWrapper<Category>queryWrapper =new LambdaQueryWrapper<>();
        //进行根据顺序排序
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
   @DeleteMapping
    public R<String> delete( Long ids){
        log.info("删除分类，id为：{}",ids);

        //categoryService.removeById(id);
        categoryService.remove(ids);

        return R.success("分类信息删除成功");
    }

    /**
     * 修改
     * @param category
     * @return
     */

    @PutMapping
    public R<String>update(@RequestBody Category category){
        categoryService.updateById(category);

        return R.success("修改菜品成功");

    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType()) ;

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);

    }
}
