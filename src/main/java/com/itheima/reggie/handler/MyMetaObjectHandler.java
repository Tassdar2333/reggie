package com.itheima.reggie.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.reggie.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-plus自动填充配置
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");

        metaObject.setValue("updateTime",LocalDateTime.now());

        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
