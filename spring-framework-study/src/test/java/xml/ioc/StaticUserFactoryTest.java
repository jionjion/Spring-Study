package xml.ioc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Jion
 */
public class StaticUserFactoryTest {

    public void testStaticUserFactory(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过静态工厂方法获得Bean
        UserE userE = (UserE) ctx.getBean("userE");
        System.out.println(userE);
    }
}