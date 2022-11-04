package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.mapper.DishMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
implements DishService{


    private DishFlavorService dishFlavorService;


    private CategoryService categoryService;

    /**
     * 新增菜品以及口味
     * @param dto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dto) {
        //先保存菜品信息
        this.save(dto);
        //获取保存后菜品id
        Long id = dto.getId();
        if(dto.getFlavors() != null || dto.getFlavors().size() > 0){
            //设置菜品以及口味对应关系并保存到口味表
            List<DishFlavor> flavorList = dto.getFlavors().stream().map(data -> {
                data.setDishId(id);
                return data;
            }).collect(Collectors.toList());
            dishFlavorService.saveBatch(flavorList);
        }
    }

    /**
     * 分页查询
     * @param page 页数
     * @param pageSize 分页大小
     * @param name 菜品名称
     * @return
     */
    @Override
    public Page<DishDto> page(int page, int pageSize, String name) {
        //分页
        Page<Dish> pageInfo = new Page<>();
        Page<DishDto> dishDtoPage = new Page<>();
        //分页条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //因为原本的分页信息不满足前端展示条件，所以这里进行处理
        lqw.eq(StringUtils.isNotBlank(name),Dish::getName,name);
        this.page(pageInfo, lqw);
        //判空
        if(pageInfo.getRecords() == null || pageInfo.getRecords().size() == 0){
            dishDtoPage.setTotal(0);
            return dishDtoPage;
        }
        //将分页参数赋值
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //转化
        List<DishDto> collect = pageInfo.getRecords().stream().map(data -> {
            DishDto to = new DishDto();
            Long categoryId = data.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if(byId != null){
                to.setCategoryName(byId.getName());
            }
            BeanUtils.copyProperties(data, to);
            return to;
        }).collect(Collectors.toList());
        //最终分页数据赋值
        dishDtoPage.setRecords(collect);
        return dishDtoPage;
    }

    /**
     * 通过菜品id获取带上口味的菜品信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto result = new DishDto();
        Dish byId = this.getById(id);
        if(byId == null){
            return null;
        }
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(id != null,DishFlavor::getDishId,id);
        List<DishFlavor> flavorList = dishFlavorService.list(lqw);
        BeanUtils.copyProperties(byId,result);
        result.setFlavors(flavorList);
        return result;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //菜品信息更新
        this.updateById(dishDto);

        //口味的更新可以选择先删除后更新
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);

        //口味列表不为空
        if(dishDto.getFlavors() != null){
            List<DishFlavor> collect = dishDto.getFlavors().stream().map(data -> {
                data.setDishId(dishDto.getId());
                return data;
            }).collect(Collectors.toList());
            dishFlavorService.saveBatch(collect);
        }
    }

    @Autowired
    public void setDishFlavorService(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}




