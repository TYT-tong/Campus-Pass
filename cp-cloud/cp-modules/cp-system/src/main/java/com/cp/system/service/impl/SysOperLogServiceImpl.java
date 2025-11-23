package com.cp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.common.core.utils.PageUtils;
import com.cp.system.api.domain.SysOperLog;
import com.cp.system.plus.mapper.SysOperLogPlusMapper;
import com.cp.system.mapper.SysOperLogMapper;
import com.cp.system.service.ISysOperLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author cp
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogPlusMapper, SysOperLog> implements ISysOperLogService
{
    private final SysOperLogMapper operLogMapper;

    public SysOperLogServiceImpl(SysOperLogMapper operLogMapper) {
        this.operLogMapper = operLogMapper;
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        if (baseMapper != null) {
            LambdaQueryWrapper<SysOperLog> qw = new LambdaQueryWrapper<>();
            if (operLog != null) {
                if (com.cp.common.core.utils.StringUtils.isNotEmpty(operLog.getTitle())) {
                    qw.like(SysOperLog::getTitle, operLog.getTitle());
                }
                if (operLog.getStatus() != null) {
                    qw.eq(SysOperLog::getStatus, operLog.getStatus());
                }
                if (com.cp.common.core.utils.StringUtils.isNotEmpty(operLog.getOperName())) {
                    qw.eq(SysOperLog::getOperName, operLog.getOperName());
                }
            }
            return baseMapper.selectList(qw);
        }
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 分页查询
     */
    public IPage<SysOperLog> selectOperLogPage(SysOperLog operLog) {
        LambdaQueryWrapper<SysOperLog> qw = new LambdaQueryWrapper<>();
        if (operLog != null) {
            if (com.cp.common.core.utils.StringUtils.isNotEmpty(operLog.getTitle())) {
                qw.like(SysOperLog::getTitle, operLog.getTitle());
            }
            if (operLog.getStatus() != null) {
                qw.eq(SysOperLog::getStatus, operLog.getStatus());
            }
            if (com.cp.common.core.utils.StringUtils.isNotEmpty(operLog.getOperName())) {
                qw.eq(SysOperLog::getOperName, operLog.getOperName());
            }
        }
        Page<SysOperLog> page = PageUtils.buildPage();
        return baseMapper.selectPage(page, qw);
    }

    @Override
    public int insertOperLog(SysOperLog operLog) {
        if (baseMapper != null) {
            return baseMapper.insert(operLog);
        }
        // 旧mapper若存在插入方法则调用；否则返回0
        try {
            java.lang.reflect.Method m = operLogMapper.getClass().getMethod("insertOperLog", SysOperLog.class);
            Object r = m.invoke(operLogMapper, operLog);
            return r instanceof Integer ? (Integer) r : 0;
        } catch (Exception ignore) {
            return 0;
        }
    }
}
