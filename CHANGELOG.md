# learn-spring

## 1. Simple bean container

### Step 1 - Basic BeanFactory

Current design:

- `BeanDefinition`: wraps a single bean object.
- `BeanFactory`: holds registered `BeanDefinition`s in a `ConcurrentHashMap`. `getBean(name)` looks up by bean name and
  unwraps the stored bean object.

Notes:

- A bean is an instance of a class that the Spring container creates, owns, and manages.
- To name a bean, Spring uses the class name and converts the first letter to lowercase.
    ```java
    BeanDefinition beanDefinition = new BeanDefinition(new HelloService());
    beanFactory.registerBeanDefinition("helloService", beanDefinition);
    ```

### Step 2 - BeanFactory creates the bean

Why:

- In step 1, the caller created the bean (`new HelloService()`) and the factory only stored it. A container should own
  object creation: the caller describes the bean, and the factory builds it.

Changes:

- `BeanDefinition` holds the bean class instead of the bean object.
- `BeanFactory` is now an interface with only `getBean`, and `DefaultListableBeanFactory` implements it. Callers depend
  only on `getBean`, so the implementation can grow (parent classes, `ApplicationContext`) without affecting them.
- `getBean` calls `createBean`, which instantiates the class via reflection (`getDeclaredConstructor().newInstance()`).
  Exceptions from the reflection are wrapped in an unchecked `BeansException`.

Differences from real Spring:

- `BeanFactory` and `DefaultListableBeanFactory` are the two ends of a longer chain:
  `DefaultSingletonBeanRegistry` → `AbstractBeanFactory` → `AbstractAutowireCapableBeanFactory` →
  `DefaultListableBeanFactory`. Every `ApplicationContext` uses a `DefaultListableBeanFactory` internally.
- Spring stores the bean class as `Object beanClass`: a class name (String) at first, resolved to a `Class` when the
  bean is created, so configuration can be read without loading classes. Here it is simplified to `Class<?>` until XML
  loading is added.

Next step:

- Every `getBean` call still creates a new instance. Step 3 will add a singleton cache to avoid that.
