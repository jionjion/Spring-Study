package xml.ioc;

/**
 * @author Jion
 *  配置创建一个Bean,交由容器管理
 */
public class UserB {

    private String name;

    /** 必须含有一个无参数的构造器 */
    public UserB(){
        super();
    }

    /** 通过构造器为属性赋值 */
    public UserB(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserB{" +
                "name='" + name + '\'' +
                '}';
    }
}
