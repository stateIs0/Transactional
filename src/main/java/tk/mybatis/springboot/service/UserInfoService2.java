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
@Service("userInfoService2")
public class UserInfoService2 {

  @Autowired
  protected UserInfoMapper userInfoMapper;
  @Autowired
  protected JdbcTemplate jdbcTemplate;

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

  @Transactional(propagation = Propagation.REQUIRED)
  public void innerTxREQUIRED() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));
    throw new RuntimeException();
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void innerTxSUPPORTS() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));
    throw new RuntimeException();
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void innerTxMANDATORY() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void innerTxREQUIRES_NEW() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));

  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void innerTxNOT_SUPPORTED() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));

  }

  @Transactional(propagation = Propagation.NEVER)
  public void innerTxNEVER() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));

  }

  @Transactional(propagation = Propagation.NESTED)
  public void innerTxNESTED() {
    userInfoMapper
        .insert(new UserInfo().setUsername("inner").setPassword("inner"));
    throw new RuntimeException();
  }




  public void close() {
    userInfoMapper.delete(new UserInfo());
  }




}
