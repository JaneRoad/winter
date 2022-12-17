package com.janeroad.service;

import com.janeroad.winter.annotation.Autowired;
import com.janeroad.winter.annotation.Component;

/**
 * @author janeroad
 */
@Component("userService")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println("userService中的orderService属性：" + orderService);
    }
}
