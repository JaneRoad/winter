package com.janeroad.service.impl;

import com.janeroad.service.UserService;
import com.janeroad.winter.annotation.Autowired;
import com.janeroad.winter.annotation.Component;
import com.janeroad.winter.bean.BeanNameAware;
import com.janeroad.winter.bean.BeanPostProcessor;
import com.janeroad.winter.bean.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author janeroad
 */
@Component("userService")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean, BeanPostProcessor {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    private String beanName;

    @Override
    public void test(){
        System.out.println("userService中的orderService属性：" + orderServiceImpl);
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
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (bean instanceof UserServiceImpl){
            System.out.println("执行postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if (bean instanceof UserServiceImpl){
            Object instance = Proxy.newProxyInstance(UserService.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("执行切面逻辑...  ");
                    return method.invoke(bean, args);
                }
            });
            System.out.println("执行postProcessAfterInitialization");
            return instance;
        }
        return bean;
    }
}
