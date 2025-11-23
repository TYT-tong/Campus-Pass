package com.cp.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cp.common.core.annotation.Excel;
import com.cp.common.core.annotation.Excel.ColumnType;
import com.cp.common.core.constant.UserConstants;
import com.cp.common.core.web.domain.BaseEntity;

import java.io.Serial;

/**
 * 字典数据�?sys_dict_data
 * 
 * @author cp
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class SysDictData extends BaseEntity
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @Getter
    @TableId
    @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    /** 字典排序 */
    @Getter
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /** 字典标签 */
    @Excel(name = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    /** 字典类型 */
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    /** 样式属性（其他样式扩展） */
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /** 表格字典样式 */
    @Getter
    private String listClass;

    /** 是否默认（Y=是 N=否） */
    @TableField(value = "is_default")
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    @JsonAlias("default")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @Getter
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    

    public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault);
    }


}
