package com.cp.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.common.core.text.Convert;
import com.cp.common.core.utils.PageUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.datascope.annotation.DataScope;
import com.cp.system.api.domain.SysUser;
import com.cp.system.mapper.SysUserMapper;
import com.cp.system.plus.mapper.SysUserPlusMapper;
import com.cp.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户 业务层处理
 *
 * @author cp
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserPlusMapper, SysUser> implements ISysUserService
{
    @Autowired
    private SysUserMapper userMapper;

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getUserName, userName)
          .eq(SysUser::getDelFlag, "0")
          .last("limit 1");
        SysUser byPlus = baseMapper != null ? baseMapper.selectOne(qw) : null;
        if (byPlus != null) {
            return byPlus;
        }
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        if (baseMapper != null) {
           LambdaQueryWrapper<SysUser> qw = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(user.getUserName())) {
                qw.like(SysUser::getUserName, user.getUserName());
            }
            if (StringUtils.isNotEmpty(user.getStatus())) {
                qw.eq(SysUser::getStatus, user.getStatus());
            }
            if (StringUtils.isNotEmpty(user.getPhonenumber())) {
                qw.eq(SysUser::getPhonenumber, user.getPhonenumber());
            }
            qw.eq(SysUser::getDelFlag, "0");
            String ds = Convert.toStr(user.getParams().get("dataScope"));
            if (StringUtils.isNotEmpty(ds)) {
                qw.apply(ds);
            }
            return baseMapper.selectList(qw);
        }
        return userMapper.selectUserList(user);
    }

    @Override
    @DataScope(userAlias = "u")
    public IPage<SysUser> selectUserPage(SysUser user) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getUserName())) {
                qw.like(SysUser::getUserName, user.getUserName());
            }
            if (StringUtils.isNotEmpty(user.getStatus())) {
                qw.eq(SysUser::getStatus, user.getStatus());
            }
            if (StringUtils.isNotEmpty(user.getPhonenumber())) {
                qw.eq(SysUser::getPhonenumber, user.getPhonenumber());
            }
            String beginTime = Convert.toStr(user.getParams().get("beginTime"));
            String endTime = Convert.toStr(user.getParams().get("endTime"));
            if (StringUtils.isNotEmpty(beginTime) && !"undefined".equalsIgnoreCase(beginTime)) {
                qw.ge(SysUser::getCreateTime, com.cp.common.core.utils.DateUtils.parseDate(beginTime));
            }
            if (StringUtils.isNotEmpty(endTime) && !"undefined".equalsIgnoreCase(endTime)) {
                qw.le(SysUser::getCreateTime, com.cp.common.core.utils.DateUtils.parseDate(endTime));
            }
        }
        qw.eq(SysUser::getDelFlag, "0");
        String ds = user != null ? Convert.toStr(user.getParams().get("dataScope")) : null;
        if (StringUtils.isNotEmpty(ds)) {
            qw.apply(ds);
        }
        Page<SysUser> page = PageUtils.buildPage();
        IPage<SysUser> result = baseMapper.selectPage(page, qw);
        if (result != null && result.getTotal() == 0) {
            Long count = baseMapper.selectCount(qw);
            if (count != null) {
                page.setTotal(count);
            }
            long size = page.getSize();
            long offset = (page.getCurrent() - 1) * size;
            List<SysUser> limited = baseMapper.selectList(qw.last(" limit " + size + " offset " + offset));
            page.setRecords(limited);
        }
        return page;
    }

    @Override
    public SysUser selectUserByPhone(String phonenumber)
    {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getPhonenumber, phonenumber)
          .eq(SysUser::getDelFlag, "0")
          .last("limit 1");
        SysUser byPlus = baseMapper != null ? baseMapper.selectOne(qw) : null;
        if (byPlus != null) {
            return byPlus;
        }
        return userMapper.selectUserByPhone(phonenumber);
    }

    @Override
    public int insertUser(SysUser user)
    {
        if (baseMapper != null) {
            return baseMapper.insert(user);
        }
        return userMapper.insertUser(user);
    }

}
