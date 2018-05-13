package tk.mybatis.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.springboot.model.UserInfo;

/**
 * WARN：关于 REPEATABLE_READ，按照定义，如果事务的隔离级别为可重复读，那么应该是可以发生幻读的情况的，但是 MySQL 下有一些不一样：
 *    * All consistent reads within the same transaction read the snapshot established by the first read.
 *    * 就是说如果在 MySQL 中，你如果将事务级别设置成可重复读，那么在同一个事务中，后续的读就直接读取第一次读得结果了，不会出现幻读的情况。
 */
@Service
public class PhantomUserInfoService extends UserInfoService1 {

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void 幻读线程1() throws InterruptedException {
    // 1. 事务 A 读取数据
    int c1 = jdbcTemplate
        .queryForObject("select count(*) from user_info", (resultSet, i) -> (resultSet.getInt(1)));
    System.out.println("c1------> " + c1);
    Thread.sleep(1000);
    // 3. 事务 A 再次读取数据
    int c2 = jdbcTemplate
        .queryForObject("select count(*) from user_info", (resultSet, i) -> (resultSet.getInt(1)));
    System.out.println("c2-------> " + c2);
    System.err.println("是否幻读： " + (c2 != c1));
  }


  @Transactional(isolation = Isolation.REPEATABLE_READ)
// READ_UNCOMMITTED  READ_COMMITTED REPEATABLE_READ SERIALIZABLE
  public void 幻读线程2() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // 2. 事务 B 写入数据
    userInfoMapper.insertSelective(new UserInfo().setUsername("phantom read test"));
  }
}
