package com.cp.auth.config;

import com.cp.common.core.domain.R;
import com.cp.system.api.domain.SysUser;
import com.cp.system.api.model.LoginUser;
import com.cp.system.api.rpc.UserRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tyt15
 */
@Configuration
public class UserRpcConfig {

  @Configuration
  @Profile("dev")
  static class DevUserRpcMock {
    @Bean
    public UserRpcService userRpcService() {
      return new UserRpcService() {
        @Override
        public R<LoginUser> getUserInfo(String username, String source) {
          return R.ok(build(username));
        }

        @Override
        public R<LoginUser> getUserByPhone(String phone, String source) {
          return R.ok(build(phone));
        }

        @Override
        public R<Boolean> recordUserLogin(SysUser sysUser, String source) {
          return R.ok(Boolean.TRUE);
        }

        @Override
        public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
          return R.ok(Boolean.TRUE);
        }

        private LoginUser build(String principal) {
          SysUser sysUser = new SysUser();
          sysUser.setUserId(1L);
          sysUser.setUserName(principal);
          LoginUser vo = new LoginUser();
          vo.setSysUser(sysUser);
          Set<String> roles = new HashSet<>();
          roles.add("admin");
          vo.setRoles(roles);
          Set<String> perms = new HashSet<>();
          perms.add("*:*:*");
          vo.setPermissions(perms);
          return vo;
        }
      };
    }
  }

  @Configuration
  @Profile("!dev")
  static class ProdUserRpc {
    @DubboReference(version = "1.0.0", check = false, timeout = 5000, retries = 0)
    private UserRpcService ref;

    @Bean
    public UserRpcService userRpcService() {
      return ref;
    }
  }
}