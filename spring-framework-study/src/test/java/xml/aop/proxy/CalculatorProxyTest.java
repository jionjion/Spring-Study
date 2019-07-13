package xml.aop.proxy;

import org.junit.Test;

/**
 * @author Jion
 *  代理类的测试
 */
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