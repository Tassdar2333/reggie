package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
implements EmployeeService{

    @Override
    public R<Employee> login(Employee employee) {

        //获取页面传递过来的密码
        String password = employee.getPassword();

        //密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //构建查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        //从数据库获取用户对象
        Employee emp = this.getOne(queryWrapper);

        if(emp == null){
            return R.error("用户不存在");
        }

        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }

        if(emp.getStatus() == 0){
            return R.error("该用户已被禁用");
        }

        return R.success(emp);

    }
}




