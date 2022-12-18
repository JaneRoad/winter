package com.janeroad.winter.bean;

/**
 * @author janeroad
 */
public interface BeanPostProcessor {


    Object postProcessBeforeInitialization(String beanName, Object bean);

    Object postProcessAfterInitialization(String beanName, Object bean);

}
