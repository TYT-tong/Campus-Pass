package com.cp.system.api.rpc;

import com.cp.common.core.domain.R;
import com.cp.system.api.domain.SysUser;
import com.cp.system.api.model.LoginUser;

/**
 * @author tyt15
 */
public interface UserRpcService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    R<LoginUser> getUserInfo(String username, String source);

    /**
     * 通过手机号查询用户信息
     *
     * @param phone 手机号
     * @param source 请求来源
     * @return 结果
     */

    R<LoginUser> getUserByPhone(String phone, String source);

    /**
     * 记录用户登录IP地址和登录时间
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    R<Boolean> recordUserLogin(SysUser sysUser, String source);

    /**
     * 注册用户信息
     */
    R<Boolean> registerUserInfo(SysUser sysUser, String source);
}