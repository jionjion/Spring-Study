package xml.autoWiring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  自动装配,接口与接口实现类
 */
public class DaoTest {

    @Test
    public void testDao(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/autoWiring/autoWiring.xml");
        Dao dao = (Dao) ctx.getBean("dao");
        dao.save();
    }
}