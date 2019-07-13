
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
