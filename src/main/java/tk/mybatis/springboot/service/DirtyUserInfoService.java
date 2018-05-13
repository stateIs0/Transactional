package tk.mybatis.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.springboot.model.UserInfo;

/**
 * 自己修改设置隔离级别
 */
@Service
public class DirtyUserInfoService extends UserInfoService1 {

  @Transactional(isolation = Isolation.DEFAULT)
  public void 脏读线程1() throws InterruptedException {
    // 1. 事务 A 读取数据
    UserInfo u1 = jdbcTemplate.queryForObject("select * from user_info",
        (resultSet, i) -> new UserInfo().setUsername(resultSet.getString(2)));
    System.out.println(u1.getUsername());
    Thread.sleep(1000);
    // 3. 事务 A 再次读取数据
    UserInfo u2 = jdbcTemplate.queryForObject("select * from user_info",
        (resultSet, i) -> new UserInfo().setUsername(resultSet.getString(2)));
    System.out.println(u2.getUsername());
    System.err.println("是否脏读： " + !u1.getUsername().equals(u2.getUsername()));
  }

  @Transactional(isolation = Isolation.DEFAULT)
// READ_UNCOMMITTED  READ_COMMITTED REPEATABLE_READ SERIALIZABLE
  public void 脏读线程2() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    UserInfo u = jdbcTemplate.queryForObject("select * from user_info",
        (resultSet, i) -> new UserInfo().setUsername(resultSet.getString(2))
            .setId(resultSet.getInt(1)));
    // 2. 事务 B 修改数据不提交
    u.setUsername("updateData");
    userInfoMapper.updateByPrimaryKey(u);
    try {
      // 不提交
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
