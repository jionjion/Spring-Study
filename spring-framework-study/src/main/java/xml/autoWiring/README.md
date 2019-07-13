
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
        System.out.println("Dao 实现类完成了保存....");
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
