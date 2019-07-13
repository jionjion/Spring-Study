package xml.ioc;

/**
 * @author Jion
 *  实例工厂方法,获得一个Bean
 */
public class InstanceUserFactory {

    /** 对外提供实例方法,返回一个Bean */
    public UserE getUserEInstance(String name){
        return new UserE(name,"河南",24);
    }
}