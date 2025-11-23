package com.cp.common.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.cp.common.core.constant.SecurityConstants;
import com.cp.system.api.rpc.LogRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import com.cp.system.api.domain.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * @author cp
 */
@Service
public class AsyncLogService
{
    @DubboReference(version = "1.0.0")
    private LogRpcService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception
    {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
