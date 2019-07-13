package xml.aop.api;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Jion
 *  使用API的方式,配置切面编程
 */
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