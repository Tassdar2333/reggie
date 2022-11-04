package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.DishFlavor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 *
 */
public interface DishService extends IService<Dish> {
    /**
     * 保存菜品（带上了口味的）
     * @param dto
     */
    void saveWithFlavor(DishDto dto);

    Page<DishDto> page(int page,int pageSize,String name);

    DishDto getByIdWithFlavor(Long id);

    /**
     * 根据id修改菜品内容
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}
