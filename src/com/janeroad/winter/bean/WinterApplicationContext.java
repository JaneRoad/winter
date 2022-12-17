package com.janeroad.winter.bean;

import com.janeroad.winter.annotation.Autowired;
import com.janeroad.winter.annotation.Component;
import com.janeroad.winter.annotation.ComponentScan;
import com.janeroad.winter.annotation.Scope;
import com.janeroad.winter.enums.ScopeTypeEnum;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author janeroad
 */
public class WinterApplicationContext {

    private final Class configClass;

    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Object> singletonBeanMap = new ConcurrentHashMap<>();

    public WinterApplicationContext(Class configClass) {
        this.configClass = configClass;
        //扫描 把BeanDefinition对象存到beanDefinitionMap中
        // 如果含有ComponentScan扫描注解
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            //获取包的路径
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".", "/");
            ClassLoader classLoader = WinterApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            File file = new File(resource.getFile());
            System.out.println("当前扫描文件夹路径：" + file);

            //如果包路径是一个目录
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                //遍历目录
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    System.out.println("当前扫描文件路径：" + fileName);
                    //如果文件名是以.class结尾就是java的文件
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("/", ".");

                        try {
                            //类加载器获取类
                            Class<?> clazz = classLoader.loadClass(className);
                            //如果类包含Component注解
                            if (clazz.isAnnotationPresent(Component.class)) {
                                //获取beanName
                                Component componentAnnotation = clazz.getAnnotation(Component.class);
                                String beanName = componentAnnotation.value();
                                if ("".equals(beanName)) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                //把 beanDefinition 存在map中
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scopeAnnotation.value());
                                } else {
                                    beanDefinition.setScope(ScopeTypeEnum.SINGLETON.name());
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }

        //实例化单例Bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (ScopeTypeEnum.SINGLETON.name().equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonBeanMap.put(beanName, bean);
            }
        }
    }


    /**
     * 创建bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object beanObject = clazz.getConstructor().newInstance();

            //依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(beanObject, getBean(field.getName()));
                }
            }
            return beanObject;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据bean名称获取bean对象
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        //从map中查找beanName对应的BeanDefinition
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        } else {
            //判断如果是单例就从单例map中获取，否则创建一个bean放进单例map中，然后返回bean
            String scope = beanDefinition.getScope();
            if (ScopeTypeEnum.SINGLETON.name().equals(scope)) {
                Object bean = singletonBeanMap.get(beanName);
                if (bean == null) {
                    bean = createBean(beanName, beanDefinition);
                    singletonBeanMap.put(beanName, bean);
                }
                return bean;
                //如果是多例创建后返回bean
            } else {
                return createBean(beanName, beanDefinition);
            }
        }
    }
}
