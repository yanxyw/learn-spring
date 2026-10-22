import org.junit.jupiter.api.Test;
import org.learn.spring.BeanDefinition;
import org.learn.spring.DefaultListableBeanFactory;

public class BeanFactoryTest {

    @Test
    void testRegisterBeanDefinition() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);

        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.hello();
    }

}
