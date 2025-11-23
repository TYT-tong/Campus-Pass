package com.cp.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp.personnel.domain.po.PerStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cp.personnel.domain.vo.PerStudentVO;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
public interface IPerStudentService extends IService<PerStudent> {

    IPage<PerStudentVO> selectStudentPage(PerStudentQueryDTO queryDTO);
}
