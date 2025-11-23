package com.cp.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cp.common.core.constant.Constants;
import com.cp.common.core.constant.SecurityConstants;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.core.utils.ip.IpUtils;
import com.cp.system.api.rpc.LogRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import com.cp.system.api.domain.SysLogininfor;

/**
 * 记录日志方法
 * 
 * @author cp
 */
@Component
public class SysRecordLogService
{
    @DubboReference(version = "1.0.0", check = false)
    private LogRpcService remoteLogService;

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr());
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        try {
            remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
        } catch (Exception ignore) {}
    }
}
