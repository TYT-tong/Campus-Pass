package com.cp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.api.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作日志 数据访问层
 *
 * @author cp
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog>
{
    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

}
