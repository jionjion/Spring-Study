
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
