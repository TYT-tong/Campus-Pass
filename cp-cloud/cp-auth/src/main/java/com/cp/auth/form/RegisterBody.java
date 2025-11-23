package com.cp.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册对象
 * 
 * @author cp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户确认密码
     */
    private String affirmPassword;

    /**
     * 用户手机号
     */
    private String phoneNumber;
}
