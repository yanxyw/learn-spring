package org.learn.spring;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;

}
