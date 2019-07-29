# Spring Framework 学习

当前Spring版本5.1.X
使用Maven搭建


## Spring 容器加载方式

使用文件路径加载

```
FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("F:/workspace/appcontext.xml")
```


使用类路径加载

```
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml")
```

使用Web.xml,
1.加载Web前端控制器
```xml
<servlet>
	<servlet-name>springDispatcherServlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<!-- 将Spring的所有XML路径写下 -->
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring/spring-*.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
```

2.加载监听类容器
```xml
<listener>
  	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<servlet>
  	<servlet-name>context</servlet-name>
  	<servlet-class>org.springframework.web.context.ContextLoaderListener</servlet-class>
  	<load-on-startup>1</load-on-startup>
</servlet>
```

## IOC控制反转
使用XML方式,配置Spring的Bean

### 通过Set方法,配置Bean属性

创建Bean
```java
public class UserA {

    private String name;

    /*** 属性必须提供Setter方法 */
    public void setName(String name) {
        this.name = name;
    }

    public void Info() {
        System.out.println("这是BeanA!");
    }

    @Override
    public String toString() {
        return "UserA{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

配置Xml
```xml
<beans>
    <!-- 配置一个 bean -->
    <!-- 通过属性注入: 通过 setter 方法注入属性值 -->
    <bean id="userA" class="xml.ioc.UserA">
        <!-- 为属性赋值 -->
        <property name="name" value="Jion"/>
    </bean>

    <bean id="userA2" class="xml.ioc.UserA">
        <!-- 若某一个 bean 的属性值不是 null, 使用时需要为其设置为 null -->
        <property name="name">
            <null/>
        </property>
    </bean>
</beans>    
```

测试方法
可以通过`ClassPathXmlApplicationContext`加载一个上下文的Xml配置,并生成Spring容器.
可以通过Bean的Id或者Bean的类型,获得容器中的实例

```java
public class UserATest {


    @Test
    public void testUserA() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");
        // 通过ID获得Bean
        UserA userA = (UserA) ctx.getBean("userA");
        userA.Info();
        System.out.println(userA);

        // 通过class类型获得Bean,如果有多个实例,会抛出异常
//        userA = ctx.getBean(UserA.class);
//        userA.Info();
//        System.out.println(userA);

        // 获得null属性
        UserA userA2 = (UserA) ctx.getBean("userA2");
        System.out.println(userA2);
    }
}
```

### 通过构造器,配置Bean属性

Bean类
要求Bean必须有一个无参数的构造方法

```java
public class UserB {

    private String name;

    /** 必须含有一个无参数的构造器 */
    public UserB(){
        super();
    }

    /** 通过构造器为属性赋值 */
    public UserB(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserB{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

Xml配置
```xml
<beans>
    <!-- 通过构造器注入属性值 -->
    <bean id="userB" class="xml.ioc.UserB">
        <!-- 要求: 在 bean 中必须有对应的构造器.  -->
        <constructor-arg value="Jion"/>
    </bean>
</beans>    
```

测试方法
```java
public class UserBTest {

    @Test
    public void testUserB(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");
        UserB userB = ctx.getBean(UserB.class);
        System.out.println(userB);
    }
}
```

### 更多配置Bean的方式
Bean类
```java
public class UserC {

    private String name;

    private String address;

    private Integer age;

    /** 必须含有一个无参数的构造器 */
    public UserC(){
        super();
    }

    /** 通过构造器为属性赋值 */
    public UserC(String name) {
        this.name = name;
    }

    public UserC(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public UserC(String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserC{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}

```

Xml配置
使用`index`属性为构造器注入的属性指定参数位置
使用`<value>`标签配置属性值
```xml
<beans>
    <!-- 若一个 bean 有多个构造器,可以根据 index 和 value 进行更加精确的定位. -->
    <bean id="userC" class="xml.ioc.UserC">
        <constructor-arg value="Jion" index="0"/>
        <constructor-arg value="上海" index="1"/>
        <constructor-arg value="25" type="java.lang.Integer"/>
    </bean>

    <!-- 对于有特殊字符的值注入注入 -->
    <bean id="userC2" class="xml.ioc.UserC">
        <constructor-arg value="Jion"/>
        <constructor-arg>
            <value><![CDATA[<上海 QvQ>]]></value>
        </constructor-arg>
    </bean>
</beans>    
```

测试方法
```java
public class UserCTest {

    @Test
    public void testUserC(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        UserC userC = (UserC) ctx.getBean("userC");
        System.out.println(userC.hashCode());
        System.out.println(userC);


        UserC userC2 = (UserC) ctx.getBean("userC2");
        System.out.println(userC2.hashCode());
        System.out.println(userC2);

    }
}
```

### P标签配置Bean,配置间的继承与依赖,`SpEL`表达式动态赋值

Bean类
```java
public class UserD {

    private String name;

    private String address;

    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "UserC{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
```

Xml配置
使用`P:属性`为Bean配置
各个Bean配置之间通过`parent`属性进行继承.继承的子配置中可以重写父配置的信息
通过`#{}`调用已经配置好的Bean的属性,并可以进行方法调用或者运算
```xml
<beans>
    <!-- p标签的引用 -->
    <bean id="userD-name" class="java.lang.String">
        <constructor-arg>
            <value><![CDATA[<JionJion>]]></value>
        </constructor-arg>
    </bean>
    <!--  p:name 直接为属性赋值 p:age-ref 为属性引用对象  -->
    <bean id="userD" class="xml.ioc.UserD" p:name-ref="userD-name" p:address="HeNan" p:age="25" />
    
    <!-- bean 的配置,使用 parent 来完成继承,可以重写属性; depends-on 表示对象间依赖 -->
    <bean id="userD2" parent="userD" p:age="24" depends-on="userD"/>

    <!-- 使用SpEL表达式,属性须提供Getter方法,或者为公有 -->
    <bean id="userD3" class="xml.ioc.UserD">
        <property name="name" value="#{userD.name}"/>
        <property name="address" value="#{userD.address.toUpperCase()}"/>
        <property name="age" value="#{userD.age + 10}"/>
    </bean>    
</beans>    
```

测试方法
```java
public class UserDTest {


    @Test
    public void testUserD(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过P标签完成注入
        UserD userD = (UserD) ctx.getBean("userD");
        System.out.println(userD);

        // 通过Bean配置间继承完成注入
        UserD userD2 = (UserD) ctx.getBean("userD2");
        System.out.println(userD2);

        // 通过SpEL表达式注入属性
        UserD userD3 = (UserD) ctx.getBean("userD3");
        System.out.println(userD3);
    }
}
```

### 静态工厂方法配置Bean
Bean类
```java
public class UserE {
    private String name;

    private String address;

    private Integer age;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UserE() {
        super();
    }

    public UserE(String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserE{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
```

创建静态工厂类,并提供创建实例的方法
```java
public class StaticUserFactory {

    /** 对外提供静态方法,返回一个Bean */
    public static UserE getUserInstance(String name){
        return new UserE(name,"河南",24);
    }
}
```

配置Xml,指定静态工厂类,`factory-method`属性指向工厂提供的静态方法
```xml
<beans>
    <!-- 使用静态工厂方法,配置一个Bean -->
    <!-- 在 class 中指定静态工厂方法的全类名, 在 factory-method 中指定静态工厂方法的方法名 -->
    <bean id="userE" class="xml.ioc.StaticUserFactory" factory-method="getUserInstance">
        <!-- 可以通过 constructor-arg 子节点为静态工厂方法指定参数 -->
        <constructor-arg value="Jion"/>
    </bean>
</beans>
```

测试方法
```java
public class StaticUserFactoryTest {

    public void testStaticUserFactory(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过静态工厂方法获得Bean
        UserE userE = (UserE) ctx.getBean("userE");
        System.out.println(userE);
    }
}
```

### 实例工厂方法配置Bean
实例工厂类
```java
public class InstanceUserFactory {

    /** 对外提供实例方法,返回一个Bean */
    public UserE getUserEInstance(String name){
        return new UserE(name,"河南",24);
    }
}
```

配置Xml
首先创建实例工厂类的bean,再创建所生产的类的bean.
```xml
<beans>
    <!-- 2. 实例工厂方法: 先需要创建工厂对象, 再调用工厂的非静态方法返回实例(了解) -->
    <bean id="instanceUserFactory" class="xml.ioc.InstanceUserFactory"/>

    <!-- ②. 有实例工厂方法来创建 bean 实例 -->
    <!-- factory-bean 指向工厂 bean, factory-method 指定工厂方法(了解) -->
    <bean id="userE2" factory-bean="instanceUserFactory" factory-method="getUserEInstance">
        <!-- 通过 constructor-arg 执行调用工厂方法需要传入的参数 -->
        <constructor-arg value="Jion"/>
    </bean>
</beans>    
```

测试方法
```java
public class StaticUserFactoryTest {

    public void testStaticUserFactory(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过静态工厂方法获得Bean
        UserE userE = (UserE) ctx.getBean("userE");
        System.out.println(userE);
    }
}
```

### 通过`FactoryBean<T>`接口配置Bean

工厂Bean类
通过`FactoryBean<T>`接口,并实现其中的`getObject`返回一个Bean实例,`getObjectType`返回实例的类型,以及`isSingleton`是否单例模式.完成Bean的配置

```java
public class UserFactoryBean implements FactoryBean<UserE> {

    /** 创建一个Bean */
    @Override
    public UserE getObject() throws Exception {
        return new UserE("Jion","ShangHai",20);
    }

    /**
     *  这个Bean的类类型
     */
    @Override
    public Class<?> getObjectType() {
        return UserE.class;
    }

    /**
     *  这个Bean是否为单例模式
     */
    @Override
    public boolean isSingleton() {
        // 返回单例Bean
        return true;
    }
}
```

Xml配置
```xml
<beans>
    <!-- 配置通过 FactoryBean 的方式来创建 bean 的实例 -->
    <bean id="userE3" class="xml.ioc.UserFactoryBean"/>
</beans>
```

测试方法
```java
public class UserFactoryBeanTest {

    @Test
    public void testUserFactoryBeanTest(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过 FactoryBean 接口配置的Bean
        UserE userE = (UserE) ctx.getBean("userE3");
        System.out.println(userE);
    }
}
```
## DI依赖注入
是对控制反转的一种实现,负责创建对象并维护对象之间的关系.
在启动Spring容器加载Bean配置的时候,完成的对变量赋值的行为

### 一般类之间的依赖

在类与类之间存在依赖关系,即一个类需要调用另一个类的方法.
被依赖的类
```java
public class Dao {

    private String ip;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void save(){
        System.out.println("这是Dao层的save()方法" + ip);
    }
}
```

存在依赖的类
```java
public class Service {

    private Dao dao;

    /** 为依赖属性生成Setter方法 */
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public void save(){
        System.out.println("这是Service层的save()方法");
        // 调用依赖方法
        dao.save();
    }
}
```

#### 通过`ref`引入依赖

Xml配置
通过`ref`属性完成依赖之间的引用
```xml
<beans>
    <!-- 配置依赖 -->
    <bean id="dao" class="xml.di.Dao">
        <property name="ip" value="127.0.0.1"/>
    </bean>
    <!-- 引用依赖 -->
    <bean id="service" class="xml.di.Service">
        <!-- 通过 ref 属性值指定当前属性指向哪一个 bean! -->
        <property name="dao" ref="dao"/>
    </bean>
</beans>
```

#### 通过内部类引入依赖
类与上面相同
Xml配置
通过在`<property>`节点中,添加`<bean>`标签,配置内部类.完成依赖的引入
```xml
<beans>
    <!-- 声明使用内部 bean -->
    <bean id="service2" class="xml.di.Service">
        <property name="dao">
            <!-- 内部 bean, 类似于匿名内部类对象. 不能被外部的 bean 来引用, 也没有必要设置 id 属性 -->
            <bean class="xml.di.Dao">
                <property name="ip" value="127.0.0.1"/>
            </bean>
        </property>
    </bean>
</beans>
```

### 拥有集合框架的依赖

当类中的集合框架属性,存在需要注入的类时.需要对应的标签配置

存在依赖的集合框架
Bean的`services`属性,依赖`List`中的类
```java
public class Collection {

    private String uri;

    private List<Service> services;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "uri='" + uri + '\'' +
                ", services=" + services +
                '}';
    }
}
```
#### 通过`<List>`标签
可以通过`<list>`标签为`List`集合框架的依赖进行注入
```xml
<beans>
    <bean id="collection" class="xml.di.Collection">
        <property name="uri" value="127.0.0.1"/>
        <property name="services">
            <!-- 使用 list 元素来装配集合属性 -->
            <list>
                <ref bean="service"/>
                <ref bean="service2"/>
            </list>
        </property>
    </bean>
</beans>
```

也可以先声明`<List>`,再做引入
```xml
<beans>
    <!-- 声明集合类型的 bean -->
    <util:list id="services">
        <ref bean="service"/>
        <ref bean="service2"/>
    </util:list>
    <!-- 引入集合类型的 bean -->
    <bean id="collection2" class="xml.di.Collection">
        <property name="uri" value="127.0.0.1"/>
        <!-- 引用外部声明的 list -->
        <property name="services" ref="services"/>
    </bean>
</beans>
```

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
## 自动装配

- XML配置
首先在XML的根节点`<beans>`开启`default-autowire`属性,选择对应的自动装配型`constructor(构造器);byName(名字);byType(类型);"default"(默认);no(禁止)`
也可以在各自`<bean>`节点各自维护

| 方式        | 说明                                                                                               |
| ----------- | -------------------------------------------------------------------------------------------------- |
| No          | 不做任何操作                                                                                       |
| byName      | 根据属性名自动装配                                                                                 |
| byType      | 如果容器中存在与指定属性相同的Bean则装配;如果存在多个,则抛出异常;如果不存在则隐式装配失败,不做异常 |
| Constructor | 使用构造参数装配                                                                                   |
如果使用依据名称或者类型自动装配,则不能再声明构造器方式,否则会抛出异常.
如果使用构造器方式自动装配,则使用私有属性后set/get方法并不会改变注入方式

### 接口与实现类之间的自动装配

定义接口
```java
public interface Dao {

    public void save();
}
```

定义实现类
```java
public class DaoImpl implements Dao {

    @Override
    public void save() {
        System.out.println(UserDao);
    }
}
```

配置Xml
```xml
<beans>
    <!-- 接口与接口类之间指定实现装配 -->
    <bean id="dao" class="xml.autoWiring.DaoImpl"/>
</beans>
```

测试方法
```java
public class DaoTest {

    @Test
    public void testDao(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/autoWiring/autoWiring.xml");
        Dao dao = (Dao) ctx.getBean("dao");
        dao.save();
    }
}
```


### 类与类之间的依赖注入

#### 通过构造器注入
1. 私有注入对象的属性
2. 在构造器中传入对象属性对象


#### 通过属性Set方法注入
1. 私有注入对象的属性
2. 提供对象的set方法,并在其中添加

#### 示例
创建需要依赖的Bean
其中依赖`Service`类.分别通过私有属性并提供Set方法和构造器中引入`Service`类,两种预处理方式准备实现依赖注入.
```java
public class Collection {

    Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public Collection() {
        super();
    }

    public Collection(Service service) {
        this.service = service;
    }

    public void save(){
        service.save();
        System.out.println("Collection 层保存方法...");
    }
}
```

Xml配置
在根节点中禁止自动注入,分别为每一个Bean配置注入方式
`autowire`方式分别为`constructor`构造器,`byName`Bean的名字,`byType`Bean的类型
```xml
<beans default-autowire="no">
    <!-- 依赖Bean -->
    <bean id="service" class="xml.autoWiring.Service"/>

    <!-- 通过构造器自动注入 -->
    <bean id="collection" class="xml.autoWiring.Collection" autowire="constructor"/>

    <!-- 通过Setter属性自动注入 -->
    <bean id="collection2" class="xml.autoWiring.Collection" autowire="byName"/>

    <!-- 通过类型自动注入 -->
    <bean id="collection3" class="xml.autoWiring.Collection" autowire="byType"/>
</beans>
```

测试方法
```java
public class CollectionTest {
    
    @Test
    public void testCollection(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/autoWiring/autoWiring.xml");

        // 通过构造器自动注入
        Collection collection = (Collection) ctx.getBean("collection");
        collection.save();

        // 通过Setter属性自动注入
        Collection collection2 = (Collection) ctx.getBean("collection2");
        collection2.save();

        // 通过类型自动注入
        Collection collection3 = (Collection) ctx.getBean("collection3");
        collection3.save();
    }
}
```
##
应用资源,读取配置信息,应用容器等..

### `<context:property-placeholder>` 读取属性文件
通过`<context:property-placeholder`节点,实现对属性文件的加载.

创建资源文件
```properties
jdbc.username=root
password=root
url=127.0.0.1
```

创建读取的Bean
```java
public class DataSource {

    private String username;

    private String password;

    private String url;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
```

配置Xml
```xml
<beans xmlns:context="http://www.springframework.org/schema/context">
    <!-- 资源文件位置 -->
    <context:property-placeholder file-encoding="UTF-8" location="classpath:xml/aware/config.properties"/>

    <bean id="dataSource" class="xml.aware.DataSource">
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${password}"/>
        <property name="url" value="${url}"/>
    </bean>
</beans>
```

测试方法
```java
public class DataSourceTest {

    /** 测试属性文件的读取 */
    @Test
    public void testDataSource(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aware/aware.xml");
        DataSource dataSource = ctx.getBean(DataSource.class);
        // 查看属性
        System.out.println(dataSource);
    }
}
```

### `ApplicationContextAware` 接口
`ApplicationContextAware`接口的`setApplicationContext`方法可以获得容器上下文.通过容器去动态加载配置文件,文件系统,类文件等..

Bean
在Bean中实现`ApplicationContextAware`接口的`setApplicationContext`方法,获得当前容器对象,并尝试动态加载资源文件
```java
public class AwareApplicationContext implements ApplicationContextAware {

    // 容器
    private ApplicationContext applicationContext;

    /** 接口唯一的方法,获取上下文对象 **/
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /** 尝试读取资源文件 */
    public void readResource() throws IOException {
        // 1.通过classpath:config.txt方式获取
		Resource resource1 = applicationContext.getResource("classpath:xml/aware/config.txt");
        System.out.println(resource1.getFile().length());

        // 2.通过file:F:\\JAVA_WorkSpace\\Spring\\src\\main\\resources\\config.txt方式获取
		Resource resource2 = applicationContext.getResource("file:F:\\Spring-Study\\spring-framework-study\\src\\main\\resources\\xml\\aware\\config.txt");
        System.out.println(resource2.getFile().length());

        // 3.空缺,默认使用ApplicationContext的构造方式
        Resource resource3 = applicationContext.getResource("xml/aware/config.txt");
        System.out.println(resource3.getFile().length());
    }
}
```

配置Xml
```xml
<beans>
    <!-- 通过aware接口,获得容器上下文 -->
    <bean id="applicationContext" class="xml.aware.AwareApplicationContext"/>
</beans>
```


### `BeanNameAware` 接口
通过`BeanNameAware`接口的`setBeanName`方法可以获得Bean的名称

Bean
```java
public class AwareBeanName implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /** 获得该类在Bean的 */
    public String getBeanName(){
        return this.beanName;
    }
}
```

Xml配置
```xml
<beans>
    <!-- 通过aware接口,获得容器中自己Bean的名字 -->
    <bean id="awareBeanName" class="xml.aware.AwareBeanName"/>
</beans>
```

测试方法
```java
public class AwareBeanNameTest {

    @Test
    public void testGetBeanName(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aware/aware.xml");
        AwareBeanName awareBeanName = ctx.getBean(AwareBeanName.class);
        System.out.println(awareBeanName.getBeanName());
    }
}
```
## 代理模式
JDK的代理模式.通过代理接口的实现类,在方法执行前后或者发生异常时进行某些操作.

定义接口
```java
public interface Calculator {

    /** 计算除法 */
    public int div(int a, int b);
}
```

定义实现类
```java
public class CalculatorImpl implements Calculator {

    /** 实现除法 */
    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
```

定义代理方法
通过`InvocationHandler`接口的方法,在接口方法`invoke`中实现代理方法.并通过`Proxy.newProxyInstance`返回一个代理类.
```java
public class CalculatorProxy {

    /** 要代理的对象 */
    private Calculator target;

    /** 通过构造器传入代理实现类 */
    public CalculatorProxy(Calculator target) {
        super();
        this.target = target;
    }

    /** 返回代理对象,代理对象中实现了要代理的方法 */
    public Calculator getLoggingProxy(){
        // 代理后对象
        Calculator proxy = null;

        // 类加载器
        ClassLoader loader = target.getClass().getClassLoader();
        // 接口
        Class [] interfaces = new Class[]{Calculator.class};
        // 代理方法
        InvocationHandler handler = new InvocationHandler() {
            /**
             * proxy: 代理对象。 一般不使用该对象
             * method: 正在被调用的方法
             * args: 调用方法传入的参数
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 方法
                String methodName = method.getName();

                // 调用目标方法
                Object result = null;

                try {
                    // 前置通知
                    System.out.println("[前置通知:] 方法 " + methodName + " 开始执行,参数为: " + Arrays.asList(args));
                    result = method.invoke(target, args);
                    // 返回通知, 运行后执行,可以获得返回值
                    System.out.println("[返回通知:] 方法 " + methodName + " 正常结束执行,结果为: " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 异常通知, 可以访问到方法出现的异常
                    System.out.println("[异常通知:] 方法 " + methodName + " 结束执行,出现异常: " + e.getCause());
                }

                // 后置通知. 因为方法可以能会出异常, 所以访问不到方法的返回值
                System.out.println("[后置通知] 方法 " + methodName + " 执行结束,结果为: " + result);

                // 返回执行结果
                return result;
            }
        };

        /**
         * loader: 代理对象使用的类加载器。
         * interfaces: 指定代理对象的类型. 即代理代理对象中可以有哪些方法.
         * handler: 当具体调用代理对象的方法时, 应该如何进行响应, 实际上就是调用 InvocationHandler 的 invoke 方法
         */
        proxy = (Calculator) Proxy.newProxyInstance(loader, interfaces, handler);

        return proxy;
    }
}
```

测试方法
在测试方法中通过代理类调用接口实现类的方法,并在方法执行前后进行一些操作.
```java
public class CalculatorProxyTest {
    /** 测试代理类的除法 */
    @Test
    public void testDiv(){
        // 要代理的
        Calculator tager = new CalculatorImpl() ;
        // 代理类
        CalculatorProxy calculatorProxy = new CalculatorProxy(tager);
        // 被代理后的对象
        Calculator proxy = calculatorProxy.getLoggingProxy();
        // 执行对象的方法
        int result = proxy.div(1,0);
        System.out.println("结果:" + result);
    }
}
```
## AOP通知

### `Aspect` 实现通知

定义接口
```java
public interface Calculator {

    /** 计算除法 */
    public int div(int a, int b);
}
```

定义实现类
```java
public class CalculatorImpl implements Calculator {

    /** 实现除法 */
    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
```

定义切面类及方法.
配合Xml定义,可以获得方法的执行参数,返回结果,抛出异常等信息.
```java
public class LoggingAspect {

    /** 前置通知,方法执行前调用 */
    public void beforeMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object [] args = joinPoint.getArgs();

        System.out.println("[前置通知:] 方法 " + methodName + " 开始执行,参数为: " + Arrays.asList(args));
    }

    /** 返回通知,方法顺利执行后调用 */
    public void afterReturning(JoinPoint joinPoint, Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[返回通知:] 方法 " + methodName + " 正常结束执行,结果为: " + result);
    }

    /** 后置通知,无论方法是否正常执行后调用 */
    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[后置通知] 方法 " + methodName + " 执行结束." );
    }

    /** 异常通知,方法发生异常时调用 */
    public void afterThrowing(JoinPoint joinPoint, Exception e){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[异常通知:] 方法 " + methodName + " 结束执行,出现异常: " + e.getLocalizedMessage());
    }

    /** 环绕通知 */
    public Object aroundMethod(ProceedingJoinPoint joinPoint){

        Object result = null;
        String methodName = joinPoint.getSignature().getName();

        try {
            // 前置通知
            System.out.println("[环绕前置通知:] 方法 " + methodName + " 开始执行,参数为: " + Arrays.asList(joinPoint.getArgs()));
            // 执行目标方法
            result = joinPoint.proceed();
            // 返回通知
            System.out.println("[环绕返回通知:] 方法 " + methodName + " 正常结束执行,结果为: " + result);
        } catch (Throwable e) {
            // 异常通知
            System.out.println("[环绕异常通知:] 方法 " + methodName + " 结束执行,出现异常: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        // 后置通知
        System.out.println("[环绕后置通知] 方法 " + methodName + " 执行结束." );
        // 返回值
        return result;
    }
}
```

Xml配置切面.
首先配置被代理的类`CalculatorImpl`和切面类`LoggingAspect`
随后配置`<aop>`,定义切点表达式`<aop:pointcut>`,指定关注点的方法,
在`<aop:aspect>`的`ref`属性中指定切面类,并在节点内定义各种通知及对应参数
```xml
<beans xmlns:aop="http://www.springframework.org/schema/aop">

    <!-- 使用 Aspect 完成AOP编程 -->

    <!-- 配置 bean, 被代理类 -->
    <bean id="calculator" class="xml.aop.aspect.CalculatorImpl"/>

    <!-- 配置切面的 bean, 定义了各种切面方法 -->
    <bean id="loggingAspect" class="xml.aop.aspect.LoggingAspect"/>

    <!-- 配置 AOP -->
    <aop:config>

        <!-- 切点表达式, 执行 xml.aop.aspect.Calculator 类的任意方法,(参数列表为 int, int),任意返回值的方法时 -->
        <aop:pointcut id="pointcut" expression="execution(* xml.aop.aspect.Calculator.*(int, int))"/>

        <!-- 配置切面及切面的执行顺序,各种通知及通知的返回值  -->
        <aop:aspect ref="loggingAspect" order="1">
            <!-- 也可以在内部定义切点表达式 -->
            <!-- <aop:pointcut id="" expression=""/> -->
            <!-- 前置通知 -->
            <aop:before method="beforeMethod" pointcut-ref="pointcut"/>
            <!-- 返回通知 -->
            <aop:after-returning method="afterReturning" pointcut-ref="pointcut" returning="result"/>
            <!-- 后置通知 -->
            <aop:after method="afterMethod" pointcut-ref="pointcut"/>
            <!-- 异常通知 -->
            <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut" throwing="e"/>
            <!-- 环绕通知 -->
            <aop:around method="aroundMethod" pointcut-ref="pointcut"/>
        </aop:aspect>
    </aop:config>
</beans>
```

测试方法
```java
public class LoggingAspectTest {
    @Test
    public void testAspect(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aop/aspect.xml");
        // 获得被代理类
        Calculator calculator = (Calculator) ctx.getBean("calculator");
        // 执行方法,查看通知
        calculator.div(10,2);
    }
}
```

### `API` 实现各种通知
首先各种实现了对应接口的实现类,随后通过Xml将其与切点或者代理类相关联,完成切面通知.

定义接口
```java
public interface Calculator {

    /** 计算除法 */
    public int div(int a, int b);
}
```

定义实现类
```java
public class CalculatorImpl implements Calculator {

    /** 实现除法 */
    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
```

定义各种通知接口实现类

前置通知,通过实现`MethodBeforeAdvice`接口的`before`方法
```java
public class CalculatorMethodBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

        System.out.println("[返回通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                + " 正常结束执行,参数为:" + Arrays.asList(args) );

    }
}
```

返回通知,通过实现`AfterReturningAdvice`接口的`afterReturning`方法
```java
public class CalculatorAfterReturningAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

        System.out.println("[返回通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                                 + " 正常结束执行,参数为:" + Arrays.asList(args) + "结果为: " + returnValue);
    }
}
```

异常通知,实现`ThrowsAdvice`接口的`afterThrowing`方法
```java
public class CalculatorThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        System.out.println("[异常通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                + " 执行异常,参数为:" + Arrays.asList(args) + "异常名称" + ex.getCause());
    }
}
```
 
#### `API`实现类结合切点织入

1. 配置各种通知接口的实现类的Bean
2. 定义切点的Bean,通过`NameMatchMethodPointcut`类生成根据方法名匹配的切点.指定任意多个方法名匹配表达式.这里直接指定`div`方法
3. 配置目标类的Bean
4. 织入,将通知与某切点进行织入,生成一个切面通知
5. 配置`ProxyFactoryBean`代理工厂类的Bean,在`target`属性中指定代理实现类的接口,`interceptorNames`中指定切面通知的或者实现通知接口的类.将目标类与通知关联.

```xml
<beans>
    <!-- 通知,创建实现接口的通知类 -->
    <bean id="beforeAdvice" class="xml.aop.api.CalculatorMethodBeforeAdvice"/>
    <bean id="returningAdvice" class="xml.aop.api.CalculatorAfterReturningAdvice"/>
    <bean id="throwsAdvice" class="xml.aop.api.CalculatorThrowsAdvice"/>

    <!-- 切点,方法匹配 -->
    <bean id="methodPointcut" class="org.springframework.aop.support.NameMatchMethodPointcut">
        <!-- 匹配的方法名,这里 * 标示全部方法 -->
<!--        <property name="ClassFilter" value="TRUE"/>-->
        <property name="mappedNames">
            <list>
                <value>div</value>
            </list>
        </property>
    </bean>

    <!-- 目标类 -->
    <bean id="calculator" class="xml.aop.api.CalculatorImpl"/>

    <!-- 织入,将某通知与切点进行织入,一次只能一个 -->
    <bean id="defaultAdvisor" class="org.springframework.aop.support.DefaultPointcutAdvisor">
        <property name="advice" ref="beforeAdvice" />
        <property name="pointcut" ref="methodPointcut" />
    </bean>

    <!-- 通过Spring的ProxyFactoryBean代理工厂获得一个代理类,在代理类中可以配置多个织入点或者实现通知接口的类 -->
    <bean id="calculatorProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 要代理对象 -->
        <property name="target" ref="calculator"/>
        <!-- 执行通知的Bean的ID -->
        <property name="interceptorNames">
            <list>
                <value>defaultAdvisor</value>
                <value>beforeAdvice</value>
                <value>returningAdvice</value>
                <value>throwsAdvice</value>
            </list>
        </property>
    </bean>
</beans>
```

#### `API`实现类通过`ProxyFactoryBean`方式织入

1. 定义各种通知接口的实现类的Bean
2. 定义目标类的Bean
3. 通过`ProxyFactoryBean`类的`target`属性指定要代理的目标,`interceptorNames`属性指定通知方法.完成目标类与通知的关联.

```xml
<beans>

    <!-- 通知,创建实现接口的通知类 -->
    <bean id="beforeAdvice" class="xml.aop.api.CalculatorMethodBeforeAdvice"/>
    <bean id="returningAdvice" class="xml.aop.api.CalculatorAfterReturningAdvice"/>
    <bean id="throwsAdvice" class="xml.aop.api.CalculatorThrowsAdvice"/>

    <!-- 目标类 -->
    <bean id="calculator" class="xml.aop.api.CalculatorImpl"/>

    <bean id="calculatorProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 指定代理的接口 -->
        <property name="proxyInterfaces" value="xml.aop.api.Calculator" />
        <!-- 要代理的类 -->
        <property name="target" ref="calculator"/>
        <!-- 通知 -->
        <property name="interceptorNames">
            <list>
                <value>beforeAdvice</value>
                <value>returningAdvice</value>
                <value>throwsAdvice</value>
            </list>
        </property>
    </bean>
</beans>
```

测试方法
```java
public class CalculatorTest {

    /** 通过指定切点和对应通知的方式进行切面编程 */
    @Test
    public void testDefaultAdvisor(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aop/api-advice.xml");
        Calculator calculatorProxy = (Calculator) ctx.getBean("calculatorProxy");
        int result = calculatorProxy.div(10,2);
        System.out.println("结果 " + result);
    }

    /** 通过代理工厂配合通知进行切面编程 */
    @Test
    public void testProxyFactoryBean(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aop/api-proxy.xml");
        Calculator calculator = (Calculator) ctx.getBean("calculatorProxy");
        int result = calculator.div(10,2);
        System.out.println("结果 " + result);
    }
}
```
## SpringJDBC
通过SpringJDBC完成SQL的执行

创建实体类
```java
public class User {

    private Integer id;

    private String name;

    private String address;

    private Date birthday;

    public User() {

    }

    public User(Integer id, String name, String address, Date birthday) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
```

定义数据库持久化对象
其中用到`org.springframework.jdbc.core.JdbcTemplate`,需要在配置文件Xml中注入.
```java
public class Dao {

    /**
     * JDBC模板
     */
    private JdbcTemplate jdbcTemplate;

    public Dao(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询方法
     */
    public User find(Integer id) {
        final User user = new User();
        String sql = "select u.id, u.`name`, u.address, u.birthday from user u where id = ?";
        // 执行查询,并返回结果
        jdbcTemplate.query(sql, resultSet -> {
            // 通过字段名或者位置索引,获得属性
            Integer id1 = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            Date birthday = resultSet.getDate("birthday");
            // 对象赋值,并返回
            user.setId(id1);
            user.setName(name);
            user.setAddress(address);
            user.setBirthday(birthday);
        }, id);
        return user;
    }

    /**
     * 保存方法
     */
    public void save(User user) {
        String sql = "insert into user(id,name,address,birthday) values(?,?,?,?)";
        // 更新操作,并绑定参数列表
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getAddress(), user.getBirthday());
    }

    /**
     * 删除方法
     */
    public void delete(User user) {
        String sql = "delete from user where id = " + user.getId();
        // 执行SQL
        jdbcTemplate.execute(sql);
    }
}
```

Xml配置

1. 定义`DriverManagerDataSource`数据源
2. 将`JdbcTemplate`的`dataSource`属性指向数据源.
3. 配置`Dao`的依赖.

```xml
<beans>

    <!-- 使用 JDBCTemplate 进行数据库操作 -->

    <!-- 数据源 -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="username" value="root" />
        <property name="password" value="123456" />
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/spring?serverTimezone=UTC" />
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
    </bean>

    <!-- JDBCTemplate 模板 -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- Dao 持久化层 -->
    <bean id="dao" class="xml.data.jdbcTemplate.Dao">
        <constructor-arg name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
</beans>
```

## 事物配置
通过在Xml配置中,添加对应的事物管理器.`DataSourceTransactionManager`,设置事物属性,配置切点与事物通知,对JDBC方式的事物进行控制.

完整配置
其中`<tx:advice>`属性标签`<tx:attributes>`中,通过`<tx:method>`对不同的方法匹配表达式,进行不同的事物控制,传播行为与隔离级别.
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:tx="http://www.springframework.org/schema/tx" xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 使用 JDBCTemplate 进行数据库操作 -->

    <!-- 数据源 -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="username" value="root" />
        <property name="password" value="123456" />
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/spring?serverTimezone=UTC" />
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
    </bean>


    <!-- JDBCTemplate 模板 -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- Dao 持久化层 -->
    <bean id="dao" class="xml.data.jdbcTemplate.Dao">
        <constructor-arg name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>

    UserService
    <bean id="service" class="xml.data.jdbcTemplate.Service">
        <property name="dao" ref="dao"/>
    </bean>

    <!-- 事物配置 -->
    <!-- 1. 配置事务管理器,各种 XXXXTransactionManager -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 2. 配置事务属性,默认使用当前唯一的事物管理器 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!-- 根据方法名指定事务的属性
                    name            方法名,或者方法签名
                    isolation       事物隔离级别
                    propagation     事物传播行为
                    timeout         执行超时时间
             -->
            <tx:method name="save*" propagation="REQUIRES_NEW"/>
            <tx:method name="find*" read-only="true" timeout="5"/>
            <tx:method name="delete*" propagation="REQUIRED" />
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!-- 3. 配置事务切入点, 以及把事务切入点和事务属性关联起来 -->
    <aop:config>
        UserService
        <aop:pointcut id="txPointCut" expression="execution(* xml.data.jdbcTemplate.Service.*.*(..))"/>
        <!-- 通知,将切点与事物管理器关联 -->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointCut"/>
    </aop:config>
</beans>
```
