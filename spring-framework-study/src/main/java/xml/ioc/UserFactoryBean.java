package xml.ioc;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Jion
 *  通过实现 FactoryBean 完成Bean的工厂方式配置
 */
public class UserFactoryBean implements FactoryBean<UserE> {

    /** 创建一个Bean */
    @Override
    public UserE getObject() throws Exception {
        return new UserE("Jion","ShangHai",20);
    }

    /**
     *  这个Bean的类类型
     */
    @Override
    public Class<?> getObjectType() {
        return UserE.class;
    }

    /**
     *  这个Bean是否为单例模式
     */
    @Override
    public boolean isSingleton() {
        // 返回单例Bean
        return true;
    }
}
