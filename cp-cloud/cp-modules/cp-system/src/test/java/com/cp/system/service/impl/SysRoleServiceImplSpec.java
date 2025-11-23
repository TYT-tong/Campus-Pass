package com.cp.system.service.impl;

import java.util.Arrays;
import java.util.List;
import com.cp.system.api.domain.SysRole;
import com.cp.system.mapper.SysMenuMapper;
import com.cp.system.mapper.SysRoleMapper;
import com.cp.system.domain.vo.RoleCardVO;

class SysRoleServiceImplSpec {

    public static void main(String[] args) {
        SysRoleMapper roleMapper = (SysRoleMapper) java.lang.reflect.Proxy.newProxyInstance(
                SysRoleMapper.class.getClassLoader(),
                new Class[]{SysRoleMapper.class},
                (proxy, method, methodArgs) -> {
                    if ("selectRoleAll".equals(method.getName())) {
                        SysRole r = new SysRole();
                        r.setRoleId(1L);
                        r.setRoleName("超级管理员");
                        r.setRoleKey("admin");
                        r.setRemark("拥有系统所有权限");
                        return Arrays.asList(r);
                    }
                    return null;
                }
        );

        SysMenuMapper menuMapper = (SysMenuMapper) java.lang.reflect.Proxy.newProxyInstance(
                SysMenuMapper.class.getClassLoader(),
                new Class[]{SysMenuMapper.class},
                (proxy, method, methodArgs) -> {
                    if ("selectMenuNamesByRoleId".equals(method.getName())) {
                        return Arrays.asList("用户管理", "角色管理", "系统设置", "数据报表");
                    }
                    return null;
                }
        );

        SysRoleServiceImpl service = new SysRoleServiceImpl(roleMapper, menuMapper);
        List<RoleCardVO> list = service.listRoleCards();
        if (list.size() != 1) throw new RuntimeException("size");
        RoleCardVO vo = list.get(0);
        if (vo.getId() != 1L) throw new RuntimeException("id");
        if (!"超级管理员".equals(vo.getName())) throw new RuntimeException("name");
        if (!"拥有系统所有权限".equals(vo.getDesc())) throw new RuntimeException("desc");
        if (vo.getTags() == null || vo.getTags().size() != 4) throw new RuntimeException("tags");
        if (!"#4E9F3D".equals(vo.getBadgeColor())) throw new RuntimeException("badge");
        if (!"#F0F7F2".equals(vo.getCardBgColor())) throw new RuntimeException("bg");
        if (!"#D9EAD3".equals(vo.getCardBorderColor())) throw new RuntimeException("border");
    }
}
