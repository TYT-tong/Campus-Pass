package com.cp.auth.controller;

import com.alibaba.fastjson2.JSONObject;
import com.cp.auth.form.LoginBody;
import com.cp.auth.form.RegisterBody;
import com.cp.auth.service.SysLoginService;
import com.cp.common.core.domain.R;
import com.cp.common.core.utils.JwtUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.security.auth.AuthUtil;
import com.cp.common.security.service.TokenService;
import com.cp.common.security.utils.SecurityUtils;
import com.cp.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.cp.common.core.utils.serialization.TToK.parseTokenToObject;

/**
 * token 控制
 *
 * @author cp
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        LoginUser loginUser = parseTokenToObject(token,LoginUser.class);
        // 刷新令牌有效期
        tokenService.refreshToken(loginUser);
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }
}
