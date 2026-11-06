package org.learn.spring;

public class BeanDefinition {

    private Class<?> beanClass;

    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        PropertyValues pvs = this.propertyValues;
        if (pvs == null) {
            pvs = new PropertyValues();
            this.propertyValues = pvs;
        }
        return pvs;
    }

}
