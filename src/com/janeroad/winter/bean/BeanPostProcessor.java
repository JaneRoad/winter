package com.janeroad.winter.bean;

/**
 * @author janeroad
 */
public interface BeanPostProcessor {


    void postProcessBeforeInitialization(String beanName, Object bean);

    void postProcessAfterInitialization(String beanName, Object bean);

}
