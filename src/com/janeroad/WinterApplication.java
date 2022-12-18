package com.janeroad;

import com.janeroad.service.UserService;
import com.janeroad.winter.bean.WinterApplicationContext;
import com.janeroad.winter.config.AppConfig;

/**
 * @author janeroad
 */
public class WinterApplication {
    public static void main(String[] args) {
        WinterApplicationContext context = new WinterApplicationContext(AppConfig.class);
//        System.out.println(context.getBean("userService"));
//        System.out.println(context.getBean("userService"));
//        System.out.println(context.getBean("userService"));
//        System.out.println(context.getBean("userService"));
//        System.out.println(context.getBean("orderService"));


        UserService userServiceImpl = (UserService) context.getBean("userService");
        userServiceImpl.test();
    }
}
