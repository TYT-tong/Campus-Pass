package com.cp.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp.common.core.utils.PageUtils;
import com.cp.personnel.domain.dto.PerParentQueryDTO;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;
import com.cp.personnel.domain.po.PerParent;
import com.cp.personnel.domain.vo.PerParentVO;
import com.cp.personnel.domain.vo.PerStudentVO;
import com.cp.personnel.mapper.PerParentMapper;
import com.cp.personnel.service.IPerParentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 家长表 服务实现类
 * </p>
 *
 * @author tyt
 * @since 2025-11-22
 */
@Service
public class PerParentServiceImpl extends ServiceImpl<PerParentMapper, PerParent> implements IPerParentService {

    @Override
    public IPage<PerParentVO> selectParentPage(PerParentQueryDTO dto) {
        Page<PerParentVO> page = PageUtils.buildPage();
        return this.baseMapper.selectParentPage(page, dto);
    }
}
