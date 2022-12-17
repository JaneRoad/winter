package com.janeroad.service;

import com.janeroad.winter.annotation.Autowired;
import com.janeroad.winter.annotation.Component;
import com.janeroad.winter.bean.BeanNameAware;
import com.janeroad.winter.bean.BeanPostProcessor;
import com.janeroad.winter.bean.InitializingBean;

/**
 * @author janeroad
 */
@Component("userService")
public class UserService implements BeanNameAware, InitializingBean, BeanPostProcessor {

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

    @Override
    public void afterPropertiesSet() {
        System.out.println("进入afterPropertiesSet()");
    }

    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        if (bean instanceof UserService){
            System.out.println("111");
        }
    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        if (bean instanceof UserService){
            System.out.println("222");
        }
    }
}
