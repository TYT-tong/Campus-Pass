package com.cp.system.service.impl;

import com.cp.common.core.domain.R;
import com.cp.system.api.domain.SysLogininfor;
import com.cp.system.api.domain.SysOperLog;
import com.cp.system.api.rpc.LogRpcService;
import com.cp.system.service.ISysLogininforService;
import com.cp.system.service.ISysOperLogService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0")
public class LogRpcServiceImpl implements LogRpcService {

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysLogininforService logininforService;

    @Override
    public R<Boolean> saveLog(SysOperLog sysOperLog, String source) throws Exception {
        int rows = operLogService.insertOperLog(sysOperLog);
        return rows > 0 ? R.ok(Boolean.TRUE) : R.fail("保存操作日志失败");
    }

    @Override
    public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
        int rows = logininforService.insertLogininfor(sysLogininfor);
        return rows > 0 ? R.ok(Boolean.TRUE) : R.fail("保存登录信息失败");
    }
}