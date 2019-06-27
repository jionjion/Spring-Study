package xml.di;

/**
 * @author Jion
 *  依赖
 */
public class Dao {

    private String ip;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void save(){
        System.out.println("这是Dao层的save()方法" + ip);
    }
}
