package com.cp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户表 数据层
 * 
 * @author cp
 */

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser>
{
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    List<SysUser> selectUserList(SysUser user);

    public SysUser selectUserByPhone(String phonenumber);

    public int insertUser(SysUser user);
}
