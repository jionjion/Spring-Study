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