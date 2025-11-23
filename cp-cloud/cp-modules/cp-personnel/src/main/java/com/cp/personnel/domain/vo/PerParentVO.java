package com.cp.personnel.domain.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 家长表
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PerParentVO implements Serializable {


    /**
     * 学生ID=USER.user_id
     */
    @TableId(value = "parent_id", type = IdType.AUTO)
    private Integer parentId;

    /**
     * 姓名
     */
    private String parentName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 学生姓名
     */
    private String studentName;


    /**
     * 用户名
     */
    private String userName;


    /**
     * 是否可用
     */
    private String status;



}
