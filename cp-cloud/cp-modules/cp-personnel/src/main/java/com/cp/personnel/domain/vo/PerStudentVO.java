package com.cp.personnel.domain.vo;



import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PerStudentVO implements Serializable {


    /**
     * 学生ID=USER.user_id
     */
    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    /**
     * IC卡编号
     */
    private String cardId;

    /**
     * 姓名
     */
    private String studentName;

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
     * 班级
     */

    private String stuClass;

    /**
     * 年级
     */
    private String grade;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 是否可用
     */
    private String status;

    /**
     * 家长名字
     */
    private String parentName;

}
