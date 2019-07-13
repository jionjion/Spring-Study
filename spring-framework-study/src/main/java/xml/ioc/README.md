
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