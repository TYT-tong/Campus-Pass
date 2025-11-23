package com.cp.personnel.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 家长表
 * </p>
 *
 * @author tyt
 * @since 2025-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("per_parent")
public class PerParent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 家长ID（关联用户ID）
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
     * JSON：备用字段
     */
    private String backup;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;


}
