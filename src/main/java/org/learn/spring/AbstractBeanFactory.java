package org.learn.spring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return bean;
        }
        bean = createBean(beanName, getBeanDefinition(beanName));
        addSingleton(beanName, bean);
        return bean;
    }

    protected Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

}
