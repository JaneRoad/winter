package com.janeroad.winter.bean;

import com.janeroad.winter.annotation.Component;

/**
 * @author janeroad
 */
@Component
public class WinterBeanPostProcess implements BeanPostProcessor{
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        System.out.println("111");
    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("222");

    }
}
