package com.cp.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cp.system.api.domain.SysUser;

/**
 * 用户 业务层
 * 
 * @author cp
 */
public interface ISysUserService extends IService<SysUser>
{
    /**
     * 通过用户ID查询用户
     *
     * @param userName 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);


    List<SysUser> selectUserList(SysUser user);

    IPage<SysUser> selectUserPage(SysUser user);

    SysUser selectUserByPhone(String phonenumber);

    int insertUser(SysUser user);
}
