package xml.aop.aspect;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  测试通知方法
 */
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