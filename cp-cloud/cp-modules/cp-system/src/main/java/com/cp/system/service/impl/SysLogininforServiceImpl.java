package com.cp.system.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.common.core.utils.PageUtils;
import org.springframework.stereotype.Service;
import com.cp.system.api.domain.SysLogininfor;
import com.cp.system.plus.mapper.SysLogininforPlusMapper;
import com.cp.system.mapper.SysLogininforMapper;
import com.cp.system.service.ISysLogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author cp
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforPlusMapper, SysLogininfor> implements ISysLogininforService
{
    private final SysLogininforMapper logininforMapper;

    public SysLogininforServiceImpl(SysLogininforMapper logininforMapper) {
        this.logininforMapper = logininforMapper;
    }

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public int insertLogininfor(SysLogininfor logininfor)
    {
        if (baseMapper != null) {
            return baseMapper.insert(logininfor);
        }
        return logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        if (baseMapper != null) {
            LambdaQueryWrapper<SysLogininfor> qw = new LambdaQueryWrapper<>();
            if (logininfor != null) {
                if (com.cp.common.core.utils.StringUtils.isNotEmpty(logininfor.getUserName())) {
                    qw.eq(SysLogininfor::getUserName, logininfor.getUserName());
                }
                if (com.cp.common.core.utils.StringUtils.isNotEmpty(logininfor.getStatus())) {
                    qw.eq(SysLogininfor::getStatus, logininfor.getStatus());
                }
                if (com.cp.common.core.utils.StringUtils.isNotEmpty(logininfor.getIpaddr())) {
                    qw.eq(SysLogininfor::getIpaddr, logininfor.getIpaddr());
                }
            }
            return baseMapper.selectList(qw);
        }
        return logininforMapper.selectLogininforList(logininfor);
    }

    public IPage<SysLogininfor> selectLogininforPage(SysLogininfor logininfor) {
        LambdaQueryWrapper<SysLogininfor> qw = new LambdaQueryWrapper<>();
        if (logininfor != null) {
            if (com.cp.common.core.utils.StringUtils.isNotEmpty(logininfor.getUserName())) {
                qw.eq(SysLogininfor::getUserName, logininfor.getUserName());
            }
            if (com.cp.common.core.utils.StringUtils.isNotEmpty(logininfor.getStatus())) {
                qw.eq(SysLogininfor::getStatus, logininfor.getStatus());
            }
        }
        Page<SysLogininfor> page = PageUtils.buildPage();
        return baseMapper.selectPage(page, qw);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        if (baseMapper != null) {
            return baseMapper.deleteBatchIds(Arrays.asList(infoIds));
        }
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        if (baseMapper != null) {
            baseMapper.delete(null);
            return;
        }
        logininforMapper.cleanLogininfor();
    }
}
