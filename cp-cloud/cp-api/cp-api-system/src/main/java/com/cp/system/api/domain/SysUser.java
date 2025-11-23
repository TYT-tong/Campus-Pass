package com.cp.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cp.common.core.annotation.Excel;
import com.cp.common.core.annotation.Excel.ColumnType;
import com.cp.common.core.annotation.Excel.Type;
import com.cp.common.core.constant.UserConstants;
import com.cp.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author cp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("sys_user")
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户账号
     */
    @Excel(name = "登录名称")
    private String userName;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    private String nickName;
    /**
     * 用户昵称
     */
    @Excel(name = "用户类型")

    private String userType;
    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    private String phonenumber;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户密码
     */
    private String  password;


    /**
     * 账号状态（0正常 1停用）
     */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 密码最后更新时间
     */
    private Date pwdUpdateDate;


    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;


    /**
     * 角色ID
     */
    @TableField(exist = false)
    private Long roleId;


    public SysUser(Long userId, String userName, String nickName, String userType, String email, String phonenumber, String sex, String avatar, String password, String status, String delFlag, String loginIp, Date loginDate, Date pwdUpdateDate, List<SysRole> roles, Long[] roleIds, Long roleId) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.userType = "99";
        this.email = email;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.avatar = avatar;
        this.status = "0";
        this.delFlag = "0";
        this.loginIp = loginIp;
        this.loginDate = loginDate;
        this.pwdUpdateDate = pwdUpdateDate;
        this.roles = roles;
        this.roleIds = roleIds;
        this.roleId = roleId;
    }

    public static boolean isAdmin(Long userId) {
        return UserConstants.isAdmin(userId);
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }


}
