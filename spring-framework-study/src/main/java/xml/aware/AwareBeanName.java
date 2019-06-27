package xml.aware;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author Jion
 *  通过 BeanNameAware 获得Bean的名称
 */
public class AwareBeanName implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /** 获得该类在Bean的 */
    public String getBeanName(){
        return this.beanName;
    }
}
