package org.learn.spring;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {

    private final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    @Nested
    class GetBean {

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

    @Nested
    class ApplyingPropertyValues {

        @Test
        void testPropertyValuesAreApplied() {
            BeanDefinition beanDefinition = new BeanDefinition(Person.class);
            beanDefinition.getPropertyValues()
                    .add("name", "Tom")
                    .add("age", 30);

            beanFactory.registerBeanDefinition("person", beanDefinition);

            Person person = (Person) beanFactory.getBean("person");
            assertEquals("Tom", person.getName());
            assertEquals(30, person.getAge());
        }

        @Test
        void testLaterPropertyValueOverridesEarlierOne() {
            BeanDefinition beanDefinition = new BeanDefinition(Person.class);
            beanDefinition.getPropertyValues()
                    .add("name", "Tom")
                    .add("name", "Jerry");

            beanFactory.registerBeanDefinition("person", beanDefinition);

            Person person = (Person) beanFactory.getBean("person");
            assertEquals("Jerry", person.getName());
        }

        @Test
        void testBeanGetsPropertyValuesFromDefinitionAtCreationTime() {
            BeanDefinition beanDefinition = new BeanDefinition(Person.class);
            beanDefinition.getPropertyValues().add("name", "Tom");
            beanFactory.registerBeanDefinition("person", beanDefinition);

            // Change the definition after registering it, but before the bean is created
            beanDefinition.getPropertyValues().add("name", "Jerry");

            Person person = (Person) beanFactory.getBean("person");
            assertEquals("Jerry", person.getName());
        }

        @Test
        void testChangingPropertyValueArrayDoesNotChangeBeanDefinition() {
            BeanDefinition beanDefinition = new BeanDefinition(Person.class);
            beanDefinition.getPropertyValues().add("name", "Tom");

            PropertyValue[] values = beanDefinition.getPropertyValues().getPropertyValues();
            values[0] = new PropertyValue("name", "Jerry");

            assertEquals("Tom", beanDefinition.getPropertyValues().getPropertyValues()[0].getValue());
        }

    }

    @Nested
    class BeanAsPropertyValue {

        @Test
        void testBeanNameAsPropertyValueIsTreatedAsLiteralString() {
            beanFactory.registerBeanDefinition("pet", new BeanDefinition(Pet.class));

            BeanDefinition personDefinition = new BeanDefinition(Person.class);
            personDefinition.getPropertyValues().add("pet", "pet");
            beanFactory.registerBeanDefinition("person", personDefinition);

            // The String "pet" is passed to setPet(Pet) as is: argument type mismatch
            BeansException e = assertThrows(BeansException.class, () -> beanFactory.getBean("person"));
            assertInstanceOf(IllegalArgumentException.class, e.getCause());
        }

        @Test
        void testGetBeanAsPropertyValueCannotWireCircularDependency() {
            BeanDefinition personDefinition = new BeanDefinition(Person.class);
            beanFactory.registerBeanDefinition("person", personDefinition);
            BeanDefinition petDefinition = new BeanDefinition(Pet.class);
            beanFactory.registerBeanDefinition("pet", petDefinition);

            personDefinition.getPropertyValues().add("pet", beanFactory.getBean("pet"));
            petDefinition.getPropertyValues().add("owner", beanFactory.getBean("person"));

            Person person = (Person) beanFactory.getBean("person");
            Pet pet = (Pet) beanFactory.getBean("pet");
            assertSame(pet, person.getPet()); // one direction is wired
            assertNull(pet.getOwner());       // the other never can be
        }

    }

}
