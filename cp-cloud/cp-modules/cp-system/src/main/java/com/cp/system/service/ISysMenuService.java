package com.cp.system.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cp.system.domain.SysMenu;
import com.cp.system.domain.vo.RouterVo;

/**
 * 菜单 业务层
 * 
 * @author cp
 */
public interface ISysMenuService  extends IService<SysMenu>
{
    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);


    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);




    String getRouteName(SysMenu menu);

    List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

}
