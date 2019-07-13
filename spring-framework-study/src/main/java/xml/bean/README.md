
## Bean配置
### `<bean>`标签的常用配置

| 标签                | 属性           | 说明                              |
| ------------------- | -------------- | --------------------------------- |
| `<bean>`            |                | Spring容器托管的类实例            |
|                     | id             | 类在容器中的唯一ID                |
|                     | class          | 类的全路径                        |
|                     | scope          | 类的作用范围                      |
|                     | autowire       | 启用构造注入的方式                |
|                     | lazy-init      | 懒加载的方式                      |
|                     | init-method    | 类初始化时,执行的方法             |
|                     | destroy-method | 类销毁时,执行的方法               |
| `<constructor-arg>` | `<bean>`子标签 | 使用构造器注入时,传入构造器的参数 |
| `<property>`        | `<bean>`子标签 | 使用属性值注入时,set私有属性的类  |
|                     | name           | 指向私有属性的Bean实例的id        |
|`<qualifier>` | `<bean>`子标签| 缩小自动装配时的查找范围|
|`<context:property-placeholder>` |   | 属性文件的加载|
|						|	location  | 属性文件的路径|


## Bean的生命周期
Bean的生命周期可以分为初始化,使用,销毁三个阶段.在初始化和销毁中可以配置执行自定义的方法.

通过接口实现的初始化/销毁方法最先执行;在`<Bean>`中配置的其次执行.
通过`<Beans>`配置的默认初始化/销毁方法在以上两者实现后,默认不执行,其执行优先级最低.


### 测试方法
Bean
```java
public class BeanScope {
    private String name;

    public BeanScope(String name) {
        this.name = name;
    }
    public BeanScope() {
        super();
    }
}
```

配置Xml
```xml
<beans>
    <!-- Bean的作用范围 -->
    <bean id="beanScope" class="xml.bean.BeanScope" scope="prototype">
        <constructor-arg name="name" value="Jion"/>
    </bean>
</beans>
```
测试方法
```java
public class BeanScopeTest {

    @Test
    public void testBeanScope(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/bean/bean.xml");
        BeanScope beanScope1 = (BeanScope) ctx.getBean("beanScope");
        BeanScope beanScope2 = (BeanScope) ctx.getBean("beanScope");

        System.out.println(beanScope1.hashCode());
        System.out.println(beanScope2.hashCode());
    }
}
```


### 初始化方法
1. 通过`InitializingBean`接口实现其`afterPropertiesSet()`方法
2. 配置`<bean>`标签下的`init-method`属性,指向类中的方法作为初始化方法
3. 在配置文件`<Beans>`节点下通过`default-init-method`属性定义默认的全局初始化方法

### 销毁方法

1.通过`DisposableBean`接口实现其`destroy()`方法
2.通过`<Bean>`标签下的`destroy-method`属性,指向类中的方法作为销毁方法
3. 在配置文件`<Beans>`节点下通过`default-destroy-method`属性定义默认的全局初始化方法


### 生命周期示例
Bean
```java
public class BeanLifeCycle implements InitializingBean, DisposableBean {

    /** 通过 InitializingBean 接口实现初始化方法 */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现 InitializingBean 接口的创建方法");
    }

    /** 通过 DisposableBean 接口实现销毁方法 */
    @Override
    public void destroy() throws Exception {
        System.out.println("实现 DisposableBean 接口的销毁方法");
    }

    /** 自定义全局的初始化方法,在Xml的Beans根节点配置 */
    public void defautInit() {
        System.out.println("自定义Bean的全局初始化方法.");
    }

    /** 自定义全局的销毁方法,在Xml的Beans根节点配置 */
    public void defaultDestroy() {
        System.out.println("自定义Bean的全局销毁方法.");
    }

    /** 自定义Bean的初始化方法,在Xml的Bean节点配置 */
    public void start() {
        System.out.println("自定义Bean开始方法.");
    }

    /** 自定义Bean的销毁方法,在Xml的Bean节点配置 */
    public void stop() {
        System.out.println("自定义Bean结束方法.");
    }
}
```

配置Xml
```xml
<beans 
    default-init-method="defaultInit"
    default-destroy-method="defaultDestroy">

    <!-- Bean的生命周期,在Bean中配置初始化方法,执行顺序在初始化/销毁接口的实现方法之后 -->
    <bean id="beanLifeCycle" class="xml.bean.BeanLifeCycle" init-method="start" destroy-method="stop"/>
</beans>
```

测试方法
```java
public class BeanLifeCycleTest {

    @Test
    public void testBeanLifeCycle(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/bean/bean.xml");
        BeanLifeCycle beanLifeCycle = ctx.getBean(BeanLifeCycle.class);
        System.out.println(beanLifeCycle.hashCode());
        ctx.close();
    }
}
```

### Bean的后置处理器接口 `BeanPostProcessor`
通过实现`BeanPostProcessor`接口的`postProcessBeforeInitialization`方法和`postProcessAfterInitialization`方法,在Bean的初始化前和后执行

Bean方法
```java
public class BeanPostProcessorImpl implements BeanPostProcessor {


    /** 在Bean初始化前执行,各种初始化的最先执行 */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        BeanLifeCycleB
        if ("beanLifeCycle".equals(beanName)){
            System.out.println("Bean beanLifeCycle 初始化前执行");
        }
        return bean;
    }

    /** 在Bean初始化之后执行,各种初始化的最后执行 */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BeanLifeCycleB
        if ("beanLifeCycle".equals(beanName)){
            System.out.println("Bean beanLifeCycle 初始化后执行");
        }
        return bean;
    }
}
```

Xml配置
```xml
<beans>
    ioc
    <bean class="xml.bean.BeanPostProcessorImpl"/>
</beans>
```