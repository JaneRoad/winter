package com.janeroad.winter.bean;

import com.janeroad.winter.annotation.Component;
import com.janeroad.winter.annotation.ComponentScan;
import com.janeroad.winter.annotation.Scope;
import com.janeroad.winter.enums.ScopeTypeEnum;

import java.beans.Introspector;
import java.io.File;
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
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".", "/");
            ClassLoader classLoader = WinterApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            File file = new File(resource.getFile());
            System.out.println(file);


            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    System.out.println(fileName);
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("/", ".");

                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Component.class)) {
                                Component componentAnnotation = clazz.getAnnotation(Component.class);
                                String beanName = componentAnnotation.value();
                                if ("".equals(beanName)){
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)){
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }else {
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



    public Object createBean(String beanName, BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getType();
        try {
            Object beanObject = clazz.getConstructor().newInstance();
            return beanObject;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据bean名称获取bean对象
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null){
            throw new NullPointerException();
        }else {
            String scope = beanDefinition.getScope();
            if (ScopeTypeEnum.SINGLETON.name().equals(scope)){
                Object bean = singletonBeanMap.get(beanName);
                if (bean == null){
                    bean = createBean(beanName, beanDefinition);
                    singletonBeanMap.put(beanName, bean);
                }
                return bean;
            }else {
                return createBean(beanName, beanDefinition);
            }
        }
    }
}
