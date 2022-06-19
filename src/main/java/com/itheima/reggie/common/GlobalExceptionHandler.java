package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice(basePackages = {"com.itheima.reggie.controller"})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        String error = ex.getMessage();

        if(StringUtils.contains(error,"Duplicate entry")){
            return R.error(error.split(" ")[2] + "已存在");
        }

        return R.error("未知错误");
    }

}
