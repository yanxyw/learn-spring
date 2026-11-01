package org.learn.spring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {

    private final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    @Test
    void testRegisterBeanDefinition() {
        beanFactory.registerBeanDefinition("helloService", new BeanDefinition(HelloService.class));

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        assertEquals("Hello!", helloService.hello());
    }

    @Test
    void testGetBeanReturnsSameInstance() {
        beanFactory.registerBeanDefinition("helloService", new BeanDefinition(HelloService.class));

        assertSame(beanFactory.getBean("helloService"), beanFactory.getBean("helloService"));
    }

    @Test
    void testGetBeanWithUnknownNameThrows() {
        assertThrows(BeansException.class, () -> beanFactory.getBean("unknown"));
    }

}
