# learn-spring

## 1. Simple bean container

### Step 1 - Basic BeanFactory

Current design:
- `BeanDefinition`: wraps a single bean object.
- `BeanFactory`: holds registered `BeanDefinition`s in a `ConcurrentHashMap`. `getBean(name)` looks up by bean name and unwraps the stored bean object.

Notes:
- A bean is an instance of a class that the Spring container creates, owns, and manages.
- To name a bean, Spring uses the class name and converts the first letter to lowercase.
    ```java
    BeanDefinition beanDefinition = new BeanDefinition(new HelloService());
    beanFactory.registerBeanDefinition("helloService", beanDefinition);
    ```
