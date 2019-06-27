package xml.autoWiring;

/**
 * @author Jion
 */
public class DaoImpl implements Dao {

    @Override
    public void save() {
        System.out.println("Dao 实现类完成了保存....");
    }
}
