package com.cp.personnel.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;
import com.cp.personnel.domain.po.PerStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.personnel.domain.vo.PerStudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
@Mapper
public interface PerStudentMapper extends BaseMapper<PerStudent> {

    Page<PerStudentVO> selectStudentPage(Page<PerStudentVO> page, @Param("dto") PerStudentQueryDTO dto);
}
