package xml.di;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  测试依赖
 */
public class ServiceTest {

    /** 测试依赖 */
    @Test
    public void testService(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/di/di.xml");

        // 通过ref
        Service service = (Service) ctx.getBean("service");
        service.save();

        Service service2 = (Service) ctx.getBean("service2");
        service2.save();
    }
}