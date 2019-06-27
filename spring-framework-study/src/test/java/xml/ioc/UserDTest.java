package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 */
public class UserDTest {


    @Test
    public void testUserD(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过P标签完成注入
        UserD userD = (UserD) ctx.getBean("userD");
        System.out.println(userD);

        // 通过Bean配置间继承完成注入
        UserD userD2 = (UserD) ctx.getBean("userD2");
        System.out.println(userD2);

        // 通过SpEL表达式注入属性
        UserD userD3 = (UserD) ctx.getBean("userD3");
        System.out.println(userD3);
    }
}