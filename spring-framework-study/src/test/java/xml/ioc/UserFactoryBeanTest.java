package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 * 通过 FactoryBean 接口配置Bean
 */
public class UserFactoryBeanTest {

    @Test
    public void testUserFactoryBeanTest(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过 FactoryBean 接口配置的Bean
        UserE userE = (UserE) ctx.getBean("userE3");
        System.out.println(userE);
    }

}