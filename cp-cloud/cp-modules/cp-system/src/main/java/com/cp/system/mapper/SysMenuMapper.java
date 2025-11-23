package com.cp.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单表 数据层
 * 
 * @author cp
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu>
{
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    List<SysMenu> selectMenuTreeAll();

    List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<String> selectMenuNamesByRoleId(Long roleId);
}
