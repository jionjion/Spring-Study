package xml.data.jdbcTemplate;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

/**
 * @author Jion
 * 数据库持久层
 *  默认开启事物
 */
public class Dao {

    /**
     * JDBC模板
     */
    private JdbcTemplate jdbcTemplate;

    public Dao(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询方法
     */
    public User find(Integer id) {
        final User user = new User();
        String sql = "select u.id, u.`name`, u.address, u.birthday from user u where id = ?";
        // 执行查询,并返回结果
        jdbcTemplate.query(sql, resultSet -> {
            // 通过字段名或者位置索引,获得属性
            Integer id1 = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            Date birthday = resultSet.getDate("birthday");
            // 对象赋值,并返回
            user.setId(id1);
            user.setName(name);
            user.setAddress(address);
            user.setBirthday(birthday);
        }, id);
        return user;
    }

    /**
     * 保存方法
     */
    public void save(User user) {
        String sql = "insert into user(id,name,address,birthday) values(?,?,?,?)";
        // 更新操作,并绑定参数列表
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getAddress(), user.getBirthday());
    }

    /**
     * 删除方法
     */
    public void delete(User user) {
        String sql = "delete from user where id = " + user.getId();
        // 执行SQL
        jdbcTemplate.execute(sql);
    }
}
