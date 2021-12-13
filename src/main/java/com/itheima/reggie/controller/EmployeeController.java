package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/11/30 0030 12:31
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1.将页面提交的密码password进行md5加密处理, 得到加密后的字符串
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库中员工数据信息

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到, 则返回登录失败结果
        if (emp == null) {
            return R.error("用户不存在");
        }

        //4.密码比对，如果不一致, 则返回登录失败结果

        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("用户已被禁用");
        }
        //6.登录成功，将员工id存入Session, 并返回登录成功结果

        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * 添加员工
     *
     * @param request
     * @param employee
     * @return
     */


    @PostMapping
    public  R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //将密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));
//
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

    //    Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("添加成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //1 构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //2 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //3 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //4 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //5 执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
//    public R<Page>page(int page,int pageSize, String name){
//        //进行分页构造
//       Page pageInfo =new Page(page,pageSize);
//
//       //条件分页
//        LambdaQueryWrapper<Employee>queryWrapper=new LambdaQueryWrapper<>();
//
//        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
//
//        //排序
//        queryWrapper.orderByDesc(Employee::getUpdateTime);
//        employeeService.page(pageInfo,queryWrapper);
//
//        return R.success(pageInfo);
//
//
//    }


    /**
     * 修改用户信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping

    public R<String>update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("用户信息修改成功");

    }

    /**
     *根据id查询
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<Employee >getById(@PathVariable Long id){
        log.info("根据Id查询用户信息");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("所输入的用户id不存在");

    }





}
