package xml.autoWiring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 */
public class CollectionTest {


    @Test
    public void testCollection(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/autoWiring/autoWiring.xml");

        // 通过构造器自动注入
        Collection collection = (Collection) ctx.getBean("collection");
        collection.save();

        // 通过Setter属性自动注入
        Collection collection2 = (Collection) ctx.getBean("collection2");
        collection2.save();

        // 通过类型自动注入
        Collection collection3 = (Collection) ctx.getBean("collection3");
        collection3.save();
    }
}