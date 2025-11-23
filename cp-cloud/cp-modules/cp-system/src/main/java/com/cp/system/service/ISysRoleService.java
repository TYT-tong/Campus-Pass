package com.cp.system.service;

import java.util.List;
import java.util.Set;

import com.cp.system.domain.vo.RoleCardVO;

/**
 * 角色业务层
 * 
 * @author cp
 */
public interface ISysRoleService
{
    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    public List<RoleCardVO> listRoleCards();

}
