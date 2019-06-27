package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  通过构造方法,获得Bean
 */
public class UserBTest {

    @Test
    public void testUserB(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");
        UserB userB = ctx.getBean(UserB.class);
        System.out.println(userB);
    }
}