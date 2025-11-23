package com.cp.personnel.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp.personnel.domain.dto.PerParentQueryDTO;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;
import com.cp.personnel.domain.po.PerParent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.personnel.domain.vo.PerParentVO;
import com.cp.personnel.domain.vo.PerStudentVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 家长表 Mapper 接口
 * </p>
 *
 * @author tyt
 * @since 2025-11-22
 */
public interface PerParentMapper extends BaseMapper<PerParent> {

    IPage<PerParentVO> selectParentPage(Page<PerParentVO> page, @Param("dto") PerParentQueryDTO dto);
}
