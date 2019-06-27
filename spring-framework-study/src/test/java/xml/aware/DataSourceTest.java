package xml.aware;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Jion
 *  测试通过属性文件进行加载
 */
public class DataSourceTest {

    /** 测试属性文件的读取 */
    @Test
    public void testDataSource(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aware/aware.xml");
        DataSource dataSource = ctx.getBean(DataSource.class);

        // 查看属性
        System.out.println(dataSource);
    }

}