package com.cp.system.api.rpc;

import com.cp.common.core.domain.R;
import com.cp.system.api.domain.SysLogininfor;
import com.cp.system.api.domain.SysOperLog;

/**
 * 日志RPC服务
 * @author tyt15
 */
public interface LogRpcService {
    R<Boolean> saveLog(SysOperLog sysOperLog, String source) throws Exception;
    R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source);
}