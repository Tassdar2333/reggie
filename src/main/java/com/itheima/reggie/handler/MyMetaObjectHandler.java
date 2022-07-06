package com.itheima.reggie.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
        this.strictInsertFill(metaObject, "create_time", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        // 或者
        this.strictInsertFill(metaObject, "create_time", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "create_time", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
        // 或者
        this.strictUpdateFill(metaObject, "update_time", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "update_time", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }
}
