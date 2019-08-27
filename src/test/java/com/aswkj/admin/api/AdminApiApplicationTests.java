package com.aswkj.admin.api;

import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.entity.UserRole;
import com.aswkj.admin.api.module.pms.service.IRoleService;
import com.aswkj.admin.api.module.pms.service.IUserRoleService;
import com.aswkj.admin.api.module.pms.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApiApplicationTests {

  @Autowired
  IUserService userService;

  @Autowired
  IRoleService roleService;

  @Autowired
  IUserRoleService userRoleService;

//  @Test
//  public void addAdminUser() {
//    userService.registerUser(new User()
//            .setUsername("hzb")
//            .setCipher("123")
//            .setName("黄先生")
//            .setGender(GenderEnum.MALE)
//    );
//  }
//
//  @Test
//  public void addRole() {
//    roleService.save(new Role().setCnName("员工").setName("ROLE_EMPLOYEE"));
//  }
//

  @Test
  public void addUserRole() {
    userRoleService.save(new UserRole().setUserId("1160076551038189569").setRoleId("1160077148147707906"));
    userRoleService.save(new UserRole().setUserId("1160076551038189569").setRoleId("1160079145311682561"));
    userRoleService.save(new UserRole().setUserId("1160083990349357058").setRoleId("1160079145311682561"));
  }


  @Test
  public void updateUser() {
//    User user = new User();
//    user.setMobile("14899995555");
//    userService.update(user, new UpdateWrapper<User>().eq("username", "test4"));
//    userService.updateById(new User().setId(1160888333667483649L).setLastLoginTime(LocalDateTime.now()));
    User user = new User();
    user.setDescription("嘻嘻嘻");
    user.setMobile("119");
    user.setId("1163348493124018178");
//    userService.lambdaUpdate().eq(User::getUsername, "test00").update(user);
    userService.updateById(user);
  }

  @Test
  public void transactionTest() {
  }

}
