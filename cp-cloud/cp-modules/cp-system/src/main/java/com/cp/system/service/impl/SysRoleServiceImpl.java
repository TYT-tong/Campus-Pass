package com.cp.system.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.cp.common.core.utils.StringUtils;
import com.cp.system.api.domain.SysRole;
import com.cp.system.mapper.SysMenuMapper;
import com.cp.system.domain.vo.RoleCardVO;

import com.cp.system.plus.mapper.SysRolePlusMapper;
import com.cp.system.mapper.SysRoleMapper;

import com.cp.system.service.ISysRoleService;

/**
 * 角色 业务层处理
 * 
 * @author cp
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRolePlusMapper, SysRole> implements ISysRoleService
{
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;

    public SysRoleServiceImpl(SysRoleMapper roleMapper, SysMenuMapper menuMapper) {
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<RoleCardVO> listRoleCards() {
        List<SysRole> roles = roleMapper.selectRoleAll();
        ArrayList<RoleCardVO> list = new ArrayList<>();
        for (SysRole role : roles) {
            RoleCardVO vo = new RoleCardVO();
            vo.setId(role.getRoleId());
            vo.setName(role.getRoleName());
           vo.setDescription(role.getDescription());

            List<String> tags = menuMapper.selectMenuNamesByRoleId(role.getRoleId());
            vo.setTags(tags);

            vo.setBadgeColor(role.getBadgeColor());
            vo.setCardBgColor(role.getCardBgColor());
            vo.setCardBorderColor(role.getCardBorderColor());
            list.add(vo);
        }
        return list;
    }

}
