package com.cp.system.controller;


import com.cp.common.core.domain.R;
import com.cp.common.core.web.controller.BaseController;
import com.cp.common.security.annotation.RequiresPermissions;
import com.cp.system.service.ISysRoleService;
import com.cp.system.domain.vo.RoleCardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息
 *
 * @author cp
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {


    @Autowired
    private ISysRoleService roleService;

//    /**
//     * 根据id获取角色
//     */
//    @RequiresPermissions("system:user:add")
//    @GetMapping("/list")
//    public TableDataInfo list() {
//        return getDataTable(roleService.selectRolePermissionByUserId());
//    }


    @RequiresPermissions("system:role:list")
    @GetMapping("/cards")
    public R<java.util.List<RoleCardVO>> cards() {
        return R.ok(roleService.listRoleCards());
    }

}
