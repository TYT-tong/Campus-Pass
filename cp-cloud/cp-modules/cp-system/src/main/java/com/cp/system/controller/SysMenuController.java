package com.cp.system.controller;

import com.cp.common.core.web.controller.BaseController;
import com.cp.common.core.web.domain.AjaxResult;
import com.cp.common.security.annotation.RequiresPermissions;
import com.cp.common.security.utils.SecurityUtils;
import com.cp.system.api.model.LoginUser;
import com.cp.system.domain.SysMenu;
import com.cp.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Collections;

/**
 * @author tyt15
 */ // cp-system/src/main/java/com/cp/system/controller/SysMenuController.java
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return success(menus);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();

        LoginUser user = SecurityUtils.getLoginUser();
        String userType = user.getSysUser().getUserType();
        if ("99".equals(userType)) {
            return success(Collections.emptyList());
        }
        try {

            List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
            return success(menuService.buildMenus(menus));
        } catch (Exception ex) {
            return success(Collections.emptyList());
        }
    }
}