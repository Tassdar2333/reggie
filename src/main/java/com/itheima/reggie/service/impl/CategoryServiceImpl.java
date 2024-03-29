package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要判断是否关联菜品或套餐
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();

        dishWrapper.eq(Dish::getCategoryId,id);

        int count1 = dishService.count(dishWrapper);

        //查询当前分类是否关联菜品，如果有则抛出异常
        if(count1 > 0){

            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");

        }

        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();

        setmealWrapper.eq(Setmeal::getCategoryId,id);

        int count2 = setmealService.count(setmealWrapper);

        //查询当前分类是否关联套餐，如果有则抛出异常
        if(count2 > 0){

            //已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");

        }

        //正常删除分类
        super.removeById(id);

    }
}




