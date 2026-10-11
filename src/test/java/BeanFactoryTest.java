import org.junit.jupiter.api.Test;
import org.learn.spring.BeanDefinition;
import org.learn.spring.BeanFactory;

public class BeanFactoryTest {

    @Test
    void testRegisterBeanDefinition() {
        BeanFactory beanFactory = new BeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(new HelloService());

        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.hello();
    }

}
