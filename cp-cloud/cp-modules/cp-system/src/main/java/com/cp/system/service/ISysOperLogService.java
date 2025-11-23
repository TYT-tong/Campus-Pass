package com.cp.system.service;

import com.cp.system.api.domain.SysOperLog;

import java.util.List;

/**
 * 操作日志 服务接口
 *
 * @author cp
 */
public interface ISysOperLogService
{
    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 新增系统操作日志
     * @param operLog 操作日志
     * @return 影响行数
     */
    int insertOperLog(SysOperLog operLog);

    /**
     * 分页查询操作日志
     */
    com.baomidou.mybatisplus.core.metadata.IPage<SysOperLog> selectOperLogPage(SysOperLog operLog);
}
