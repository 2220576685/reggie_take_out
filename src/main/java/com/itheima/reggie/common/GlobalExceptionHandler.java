package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.xml.SqlXmlFeatureNotImplementedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/2 0002 11:31
 */
//@Slf4j
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public R<String>exceptionHandler(SQLIntegrityConstraintViolationException ex){
//
//
//        return R.error("用户已存在");
//    }
//}
@Slf4j
@ControllerAdvice(annotations = {RestController.class,Controller.class})
@ResponseBody
public class GlobalExceptionHandler{
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String>exceptionHandler(SQLIntegrityConstraintViolationException ex){

        return R.error("用户已存在");
    }
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}