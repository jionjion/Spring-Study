package xml.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author Jion
 *  通过代理实现接口方法的代理操作
 */
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
