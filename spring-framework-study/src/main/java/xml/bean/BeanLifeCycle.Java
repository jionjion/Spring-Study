package xml.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jion
 *  测试Bean的生命周期
 */
public class BeanLifeCycle implements InitializingBean, DisposableBean {

    /** 通过 InitializingBean 接口实现初始化方法 */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现 InitializingBean 接口的创建方法");
    }

    /** 通过 DisposableBean 接口实现销毁方法 */
    @Override
    public void destroy() throws Exception {
        System.out.println("实现 DisposableBean 接口的销毁方法");
    }

    /** 自定义全局的初始化方法,在Xml的Beans根节点配置 */
    public void defautInit() {
        System.out.println("自定义Bean的全局初始化方法.");
    }

    /** 自定义全局的销毁方法,在Xml的Beans根节点配置 */
    public void defaultDestroy() {
        System.out.println("自定义Bean的全局销毁方法.");
    }

    /** 自定义Bean的初始化方法,在Xml的Bean节点配置 */
    public void start() {
        System.out.println("自定义Bean开始方法.");
    }

    /** 自定义Bean的销毁方法,在Xml的Bean节点配置 */
    public void stop() {
        System.out.println("自定义Bean结束方法.");
    }
}
