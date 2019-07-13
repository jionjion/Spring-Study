package xml.bean;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  测试Bean的作用范围
 */
public class BeanScopeTest {

    @Test
    public void testBeanScope(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/bean/bean.xml");
        BeanScope beanScope1 = (BeanScope) ctx.getBean("beanScope");
        BeanScope beanScope2 = (BeanScope) ctx.getBean("beanScope");

        System.out.println(beanScope1.hashCode());
        System.out.println(beanScope2.hashCode());
    }
}