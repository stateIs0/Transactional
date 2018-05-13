package tk.mybatis.springboot.service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceTest {


  @Autowired
  DirtyUserInfoService dirtyUserInfoService;
  @Autowired
  NoRepetitionUserInfoService noRepetitionUserInfoService;
  @Autowired
  PhantomUserInfoService phantomUserInfoService;
  @Autowired
  SpringAopBug springAopBug;


  Executor threadPool = Executors.newCachedThreadPool();


  @Before
  public void before() {
    dirtyUserInfoService.insert();
    dirtyUserInfoService.close();
  }

  @After
  public void after() {
//    dirtyUserInfoService.close();
  }


  @Test
  public void 脏读测试() throws InterruptedException {
    threadPool.execute(() -> {
      try {
        dirtyUserInfoService.脏读线程1();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    threadPool.execute(() -> {
      dirtyUserInfoService.脏读线程2();
    });
    Thread.sleep(2100);
  }

  @Test
  public void 不可重复读测试() throws InterruptedException {
    threadPool.execute(() -> {
      try {
        noRepetitionUserInfoService.不可重复读线程1();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    threadPool.execute(() -> {
      noRepetitionUserInfoService.不可重复读线程2();
    });
    Thread.sleep(2100);
  }


  @Test
  public void 幻读测试() throws InterruptedException {
    threadPool.execute(() -> {
      try {
        phantomUserInfoService.幻读线程1();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    threadPool.execute(() -> {
      phantomUserInfoService.幻读线程2();
    });
    Thread.sleep(2100);
  }

  @Test
  public void Spring内部调用测试() {
    springAopBug.bug();
  }


}