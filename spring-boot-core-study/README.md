## IOC控制反转

### 实例工厂方法配置Bean


### 一般类之间的依赖


## Bean配置


## Bean的生命周期

### 初始化方法

### 销毁方法

## 资源文件读取

### `<context:property-placeholder>` 读取属性文件
通过`<context:property-placeholder`节点,实现对属性文件的加载.


## AOP通知

### `Aspect` 实现通知




## SpringJDBC

1. 定义`DriverManagerDataSource`数据源
2. 将`JdbcTemplate`的`dataSource`属性指向数据源.
3. 配置`Dao`的依赖.


## 事物配置
通过在Xml配置中,添加对应的事物管理器.`DataSourceTransactionManager`,设置事物属性,配置切点与事物通知,对JDBC方式的事物进行控制.
