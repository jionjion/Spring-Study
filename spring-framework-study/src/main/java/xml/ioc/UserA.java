package xml.ioc;

/**
 * @author Jion
 *  配置创建一个Bean,交由容器管理
 */
public class UserA {

    private String name;

    /*** 属性必须提供Setter方法 */
    public void setName(String name) {
        this.name = name;
    }

    public void Info(){
        System.out.println("这是BeanA!");
    }

    @Override
    public String toString() {
        return "UserA{" +
                "name='" + name + '\'' +
                '}';
    }
}
