package tk.mybatis.springboot.service;

import java.io.IOException;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 事务传播测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropagationTest {

  @Resource(name = "userInfoService1")
  UserInfoService1 outerTx;
  @Resource(name = "userInfoService2")
  UserInfoService2 innerTx;


  @Before
  public void before() {
    outerTx.close();
    innerTx.close();
  }

  @After
  public void after() {

  }

  @Test
  public void REQUIRED() {
    outerTx.outerTxREQUIRED();
  }

  @Test
  public void SUPPORTS() {
    outerTx.outerTxSUPPORTS();
  }

  @Test
  public void MANDATORY() {
    outerTx.outerTxMANDATORY();
  }

  @Test
  public void REQUIRES_NEW() {
    outerTx.outerTxREQUIRES_NEW();
  }

  @Test
  public void NOT_SUPPORTED() {
    outerTx.outerTxNOT_SUPPORTED();
  }

  @Test
  public void NEVER() {
    outerTx.outerTxTxNEVER();
  }

  @Test
  public void NESTED() {
    outerTx.outerTxNESTED();

  }

  @Test
  public void timeOut() {
    outerTx.timeOut();

  }

  @Test
  public void readOnly() {
    outerTx.readOnly();

  }

  @Test
  public void rollbackFor() throws IOException {
    outerTx.rollbackFor();

  }

  @Test
  public void noRollbackFor() throws IOException {
    outerTx.noRollbackFor();

  }
  @Test
  public void rollbackForClassName() {
    outerTx.rollbackForClassName();
  }

  @Test
  public void noRollbackForClassName() {
    outerTx.noRollbackForClassName();
  }



}
