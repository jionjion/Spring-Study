package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author Jion
 *  测试通过属性注入生成Bean
 */
public class UserATest {


    @Test
    public void testUserA() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");
        // 通过ID获得Bean
        UserA userA = (UserA) ctx.getBean("userA");
        userA.Info();
        System.out.println(userA);

        // 通过class类型获得Bean,如果有多个实例,会抛出异常
//        userA = ctx.getBean(UserA.class);
//        userA.Info();
//        System.out.println(userA);

        // 获得null属性
        UserA userA2 = (UserA) ctx.getBean("userA2");
        System.out.println(userA2);
    }

}