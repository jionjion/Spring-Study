package xml.di;

/**
 * @author Jion
 */
public class Service {

    private Dao dao;

    /** 为依赖属性生成Setter方法 */
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public void save(){
        System.out.println("这是Service层的save()方法");
        // 调用依赖方法
        dao.save();
    }
}
