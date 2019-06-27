package xml.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Jion
 *  实现 BeanPostProcessor 方法,在每一个 Bean 初始化前后做修改
 */
public class BeanPostProcessorImpl implements BeanPostProcessor {


    /** 在Bean初始化前执行,各种初始化的最先执行 */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 仅针对某些Bean作拦截 BeanLifeCycle
        if ("beanLifeCycle".equals(beanName)){
            System.out.println("Bean beanLifeCycle 初始化前执行");
        }
        return bean;
    }

    /** 在Bean初始化之后执行,各种初始化的最后执行 */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 仅针对某些Bean作拦截 BeanLifeCycle
        if ("beanLifeCycle".equals(beanName)){
            System.out.println("Bean beanLifeCycle 初始化后执行");
        }
        return bean;
    }
}
