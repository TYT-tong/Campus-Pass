package com.cp.system.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.domain.SysUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author cp
 */

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole>
{
    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    void deleteUserRoleByUserId(Long userId);

    void deleteUserRole(Long[] userIds);
}
