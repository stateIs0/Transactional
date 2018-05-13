package tk.mybatis.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.springboot.model.UserInfo;

@Service
public class SpringAopBug extends UserInfoService1 {


  public void bug() {
    userInfoMapper.insertSelective(new UserInfo().setUsername("bug"));
    inner();
  }

  @Transactional
  public void inner() {
    userInfoMapper.insertSelective(new UserInfo().setUsername("inner operation"));
    throw new RuntimeException();
  }

}
