package com.cp.auth.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户登录对象
 * 
 * @author cp
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

}
