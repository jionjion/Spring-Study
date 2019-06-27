package xml.ioc;

/**
 * @author Jion
 *  配置创建一个Bean,交由容器管理
 */
public class UserC {

    private String name;

    private String address;

    private Integer age;

    /** 必须含有一个无参数的构造器 */
    public UserC(){
        super();
    }

    /** 通过构造器为属性赋值 */
    public UserC(String name) {
        this.name = name;
    }

    public UserC(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public UserC(String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserC{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
