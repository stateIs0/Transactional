/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.springboot.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.springboot.mapper.UserInfoMapper;
import tk.mybatis.springboot.model.UserInfo;

/**
 * @author liuzh
 * @since 2016-01-31 21:42
 */
@Service("userInfoService1")
public class UserInfoService1 {

  @Autowired
  protected UserInfoMapper userInfoMapper;
  @Autowired
  protected JdbcTemplate jdbcTemplate;
  @Autowired
  UserInfoService2 innerTx;

  public List<UserInfo> getAllList(UserInfo UserInfo) {
    return userInfoMapper.selectAll();
  }

  public UserInfo getAll() {
    return jdbcTemplate.queryForObject("select * from user_info", new RowMapper<UserInfo>() {
      @Override
      public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserInfo().setUsername(resultSet.getString(2));
      }
    });
  }

  public void insert() {
    userInfoMapper.insert(new UserInfo().setUsername("test1").setPassword("test1pwd"));
  }

  //  @Transactional
  public void outerTxREQUIRED() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxREQUIRED();

  }

  //  @Transactional
  public void outerTxSUPPORTS() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxSUPPORTS();

  }

  @Transactional
  public void outerTxMANDATORY() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxMANDATORY();
    throw new RuntimeException();
  }

  @Transactional
  public void outerTxREQUIRES_NEW() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxREQUIRES_NEW();
    throw new RuntimeException();
  }

//  @Transactional
  public void outerTxNOT_SUPPORTED() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxNOT_SUPPORTED();
    throw new RuntimeException();
  }

  public void outerTxTxNEVER() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxNEVER();
    throw new RuntimeException();
  }


  @Transactional
  public void outerTxNESTED() {
    userInfoMapper.insert(new UserInfo().setUsername("outer").setPassword("outer"));
    innerTx.innerTxNESTED();

  }

  @Transactional(timeout = 3, propagation = Propagation.NESTED)
  public void timeOut() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    userInfoMapper
        .insert(new UserInfo().setUsername("timeout").setPassword("timeout"));
  }

  @Transactional(readOnly = true)
  public void readOnly() {
    userInfoMapper
        .insert(new UserInfo().setUsername("timeout").setPassword("timeout"));
  }

  @Transactional(rollbackFor = {CxsException.class})
  public void rollbackFor() {
    userInfoMapper
        .insert(new UserInfo().setUsername("rollbackFor").setPassword("rollbackFor"));
    throw new CxsException();
  }

  @Transactional(noRollbackFor = {RuntimeException.class})
  public void noRollbackFor() {
    userInfoMapper
        .insert(new UserInfo().setUsername("rollbackFor").setPassword("rollbackFor"));
    throw new CxsException();
  }

  @Transactional(rollbackForClassName = {"CxsException"})
  public void rollbackForClassName() {
    userInfoMapper
        .insert(new UserInfo().setUsername("rollbackFor").setPassword("rollbackFor"));
    throw new CxsException();
  }

  @Transactional(noRollbackForClassName = {"CxsException"})
  public void noRollbackForClassName() {
    userInfoMapper
        .insert(new UserInfo().setUsername("rollbackFor").setPassword("rollbackFor"));
    throw new CxsException();
  }



  public void close() {
    userInfoMapper.delete(new UserInfo());
  }


}
