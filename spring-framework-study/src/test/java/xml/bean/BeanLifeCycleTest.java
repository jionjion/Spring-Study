package xml.bean;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  测试Bean的生命周期
 */
public class BeanLifeCycleTest {

    @Test
    public void testBeanLifeCycle(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/bean/bean.xml");
        BeanLifeCycle beanLifeCycle = ctx.getBean(BeanLifeCycle.class);
        System.out.println(beanLifeCycle.hashCode());
        ctx.close();
    }
}