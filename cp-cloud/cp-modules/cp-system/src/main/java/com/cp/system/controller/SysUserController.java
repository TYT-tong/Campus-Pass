package com.cp.system.controller;


import com.cp.common.core.domain.R;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.core.web.controller.BaseController;
import com.cp.common.core.web.page.TableDataInfo;
import com.cp.common.security.annotation.InnerAuth;
import com.cp.common.security.annotation.RequiresPermissions;
import com.cp.system.api.domain.SysUser;
import com.cp.system.api.model.LoginUser;
import com.cp.system.service.ISysPermissionService;
import com.cp.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 用户信息
 *
 * @author cp
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPermissionService permissionService;

    public static LoginUser setVoUser(SysUser sysUser, ISysPermissionService permissionService) {
        Set<String> roles = permissionService.getRolePermission(sysUser);
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser vo = new LoginUser();
        vo.setSysUser(sysUser);
        vo.setRoles(roles);
        vo.setPermissions(permissions);
        return vo;
    }

    /**
     * 获取用户列表
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        return getDataTable(userService.selectUserPage(user));
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        return getLoginUserR(sysUser);
    }

    @InnerAuth
    @GetMapping("/phone/{phone}")
    public R<LoginUser> phone(@PathVariable("phone") String phone) {
        SysUser sysUser = userService.selectUserByPhone(phone);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户不存在");
        }
        return getLoginUserR(sysUser);
    }

    private R<LoginUser> getLoginUserR(SysUser sysUser) {

        LoginUser vo = setVoUser(sysUser, permissionService);
        return R.ok(vo);
    }

}
