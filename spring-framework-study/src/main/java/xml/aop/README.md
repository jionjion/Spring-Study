
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