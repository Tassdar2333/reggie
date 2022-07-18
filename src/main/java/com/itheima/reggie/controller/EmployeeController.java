package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Auther wangchengyang
 * @Date 2022/6/4 14:27
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        R<Employee> result = employeeService.login(employee);

        request.getSession().setAttribute("employee",result.getData().getId());

        return result;
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){

        log.info("新增员工，员工信息{}",employee.toString());

        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        Long uid = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(uid);
//
//        employee.setUpdateUser(uid);

        employeeService.save(employee);
        return R.success("新增员工成功");

    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){

        log.info("page = {},pageSize = {},name={}",page,pageSize,name);

        //构建分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加一个排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);

    }

    /**
     * 根据id来修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

//        Long empId = (Long)request.getSession().getAttribute("employee");
//
//        log.info(employee.toString());
//
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");

    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){

        log.info("根据id查询员工信息");

        Employee employee = employeeService.getById(id);

        if(employee != null){
            return R.success(employee);
        }

        return R.error("未查询到该员工");
    }



    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
