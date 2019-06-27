package xml.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  使用实例工厂方法,完成Bean的配置
 */
public class InstanceUserFactoryTest {

    @Test
    public void testInstanceUserFactory(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/ioc/ioc.xml");

        // 通过示例工厂方法获得Bean
        UserE userE = (UserE) ctx.getBean("userE2");
        System.out.println(userE);
    }

}