package com.cp.system.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserPlusMapper extends BaseMapper<SysUser> {
}