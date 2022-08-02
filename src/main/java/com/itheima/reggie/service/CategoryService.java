package com.itheima.reggie.service;

import com.itheima.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据id来删除分类
     * @param id
     */
    void remove(Long id);
}
