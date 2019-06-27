package xml.aware;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author Jion
 *  测试通过 ApplicationContextAware 接口获得资源文件信息
 */
public class AwareApplicationContextTest {

    @Test
    public void testReadResource() throws IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aware/aware.xml");
        AwareApplicationContext applicationContext = ctx.getBean(AwareApplicationContext.class);
        applicationContext.readResource();
    }
}