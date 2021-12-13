package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/2 0002 20:37
 */
@Slf4j
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {


    /**
     * 公共字段自动填充
     * @param metaObject
     */
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        log.info("公共字段自动填充[insert]");
//        log.info(metaObject.toString());
//
//
//        metaObject.setValue("createTime", LocalDateTime.now());
//        metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("createUser",BaseContext.getCurrentId());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId());
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        log.info("公共字段自动填充[update]");
//        log.info(metaObject.toString());
//
//        metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId());
//    }

    @Override
    public void insertFill(MetaObject metaObject) {
    log.info("公共自动填充内容【insert】");
    metaObject.setValue("createTime",LocalDateTime.now());
    metaObject.setValue("updateTime",LocalDateTime.now());
    metaObject.setValue("createUser",BaseContext.getCurrentId());
    metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共自动填充内容【update】");

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
