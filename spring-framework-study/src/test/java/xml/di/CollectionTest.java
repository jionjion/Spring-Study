package xml.di;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 */
public class CollectionTest {

    @Test
    public void testCollection(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/di/di.xml");

        // 通过<List>标签
        Collection collection = (Collection) ctx.getBean("collection");
        System.out.println(collection);

        // 通过集合类型的Bean
        Collection collection2 = (Collection) ctx.getBean("collection");
        System.out.println(collection2);

    }
}