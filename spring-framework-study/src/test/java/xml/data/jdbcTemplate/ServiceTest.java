package xml.data.jdbcTemplate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Jion
 */
public class ServiceTest {

    private Service service;

    @Before
    public void init() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("xml/data/jdbcTemplate.xml");
        this.service = (Service) ctx.getBean("service");
    }

    @Test
    public void testFind() {
        User user = service.find(2);
        System.out.println(user);
    }

    @Test
    public void testSave() {
        User user = new User(3,"Arise","BeiJing",new Date());
        service.save(user);
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setId(2);
        service.delete(user);
    }
}