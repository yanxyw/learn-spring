package org.learn.spring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        try {
            return beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean '" + beanName + "' failed", e);
        }
    }

}
