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
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());

        queryWrapper.orderByAsc(Category::getSort);

        queryWrapper.orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);

    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize){

        Page<Category> pageInfo = new Page<>(page,pageSize);

        //构造条件过滤
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        //排序
        wrapper.orderByAsc(Category::getSort);

        //构建分页对象
        categoryService.page(pageInfo,wrapper);

        return R.success(pageInfo);

    }

    @PostMapping
    public R<String> save(@RequestBody Category category){

        log.info("category:{}",category);

        categoryService.save(category);

        return R.success("新增分类成功");
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        log.info("删除分类，id为:{}",ids);

        //categoryService.removeById(id);
        categoryService.remove(ids);

        return R.success("删除分类成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息");

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
