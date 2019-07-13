package xml.ioc;

/**
 * @author Jion
 *  静态工厂方法,获得一个Bean
 */
public class StaticUserFactory {

    /** 对外提供静态方法,返回一个Bean */
    public static UserE getUserInstance(String name){
        return new UserE(name,"河南",24);
    }
}