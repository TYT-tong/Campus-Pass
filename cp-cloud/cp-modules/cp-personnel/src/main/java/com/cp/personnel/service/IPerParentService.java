package com.cp.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp.personnel.domain.dto.PerParentQueryDTO;
import com.cp.personnel.domain.po.PerParent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cp.personnel.domain.vo.PerParentVO;

import java.util.List;

/**
 * <p>
 * 家长表 服务类
 * </p>
 *
 * @author tyt
 * @since 2025-11-22
 */
public interface IPerParentService extends IService<PerParent> {

    IPage<PerParentVO> selectParentPage(PerParentQueryDTO queryDTO);
}
