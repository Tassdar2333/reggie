package com.itheima.reggie.service;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface EmployeeService extends IService<Employee> {
    R<Employee> login(Employee employee);
}
