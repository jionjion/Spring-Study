package xml.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author Jion
 * 通过 ApplicationContextAware 接口获得上下文容器
 */
public class AwareApplicationContext implements ApplicationContextAware {

    // 容器
    private ApplicationContext applicationContext;

    /** 接口唯一的方法,获取上下文对象 **/
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /** 尝试读取资源文件 */
    public void readResource() throws IOException {
        // 1.通过classpath:config.txt方式获取
		Resource resource1 = applicationContext.getResource("classpath:xml/aware/config.txt");
        System.out.println(resource1.getFile().length());

        // 2.通过file:F:\\JAVA_WorkSpace\\Spring\\src\\main\\resources\\config.txt方式获取
		Resource resource2 = applicationContext.getResource("file:F:\\Spring-Study\\spring-framework-study\\src\\main\\resources\\xml\\aware\\config.txt");
        System.out.println(resource2.getFile().length());

        // 3.空缺,默认使用ApplicationContext的构造方式
        Resource resource3 = applicationContext.getResource("xml/aware/config.txt");
        System.out.println(resource3.getFile().length());
    }
}
