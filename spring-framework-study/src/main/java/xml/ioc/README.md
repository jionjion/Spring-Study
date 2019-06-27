
## Spring 容器加载方式

使用文件路径加载

``` java
FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("F:/workspace/appcontext.xml")
```


使用类路径加载

``` java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml")
```

使用Web.xml,
1.加载Web前端控制器
``` xml
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
``` xml
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

### 通过构造器,配置Bean属性


### 通过P标签,配置Bean

### 配置之间的继承与依赖

### 通过 `SpEL` 表达式动态赋值


### 静态工厂方法配置Bean

### 实例工厂方法配置Bean

### 通过`FactoryBean<T>`接口配置Bean