package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Jion
 *  通过构造方法的不同,获得不同的Bean
 */
public class UserCTest {


    @Test
    public void testUserC(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        UserC userC = (UserC) ctx.getBean("userC");
        System.out.println(userC.hashCode());
        System.out.println(userC);


        UserC userC2 = (UserC) ctx.getBean("userC2");
        System.out.println(userC2.hashCode());
        System.out.println(userC2);

    }
}