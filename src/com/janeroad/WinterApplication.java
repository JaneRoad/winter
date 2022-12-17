package com.janeroad;

import com.janeroad.winter.config.AppConfig;
import com.janeroad.winter.bean.WinterApplicationContext;

/**
 * @author janeroad
 */
public class WinterApplication {
    public static void main(String[] args) {
        WinterApplicationContext context = new WinterApplicationContext(AppConfig.class);
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));

    }
}
