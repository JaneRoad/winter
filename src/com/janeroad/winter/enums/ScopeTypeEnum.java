package com.janeroad.winter.enums;

/**
 * @author janeroad
 */

public enum ScopeTypeEnum {
    //单例
    SINGLETON,
    //每次对该bean的请求都会创建一个新的实例
    PROTOTYPE,
    //每次http请求将会有各自的bean实例，类似于prototype
    REQUEST,
    //在一个http session中，一个bean定义对应一个bean实例
    SESSION,
    //在一个全局的http session中，一个bean定义对应一个bean实例。典型情况下，仅在使用portlet context的时候有效
    GLOBAL_SESSION
}
