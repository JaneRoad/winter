package com.janeroad.service;

import com.janeroad.winter.annotation.Autowired;
import com.janeroad.winter.annotation.Component;
import com.janeroad.winter.bean.BeanNameAware;

/**
 * @author janeroad
 */
@Component("userService")
public class UserService implements BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test(){
        System.out.println("userService中的orderService属性：" + orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
