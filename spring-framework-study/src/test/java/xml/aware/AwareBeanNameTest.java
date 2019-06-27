package xml.aware;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jion
 *  通过 BeanNameAware 接口获得Bean的名称
 */
public class AwareBeanNameTest {

    @Test
    public void testGetBeanName(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/aware/aware.xml");
        AwareBeanName awareBeanName = ctx.getBean(AwareBeanName.class);
        System.out.println(awareBeanName.getBeanName());
    }
}