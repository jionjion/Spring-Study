package xml.data.jdbcTemplate;

/**
 * @author Jion
 * 业务服务层
 */
public class Service {

    private Dao dao;

    public User find(Integer id) {
       return dao.find(id);
    }

    public void save(User user) {
        dao.save(user);
    }

    public void delete(User user) {
        dao.delete(user);
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
