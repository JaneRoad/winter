package com.janeroad.winter.bean;

/**
 * @author janeroad
 */
public interface InitializingBean {

    /**
     * 当所有bean属性都已设置时，进行配置和最终初始化。
     */
    void afterPropertiesSet();
}
