package com.cp.personnel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp.personnel.domain.po.PerStudent;
import com.cp.personnel.domain.vo.PerStudentVO;
import com.cp.personnel.mapper.PerStudentMapper;
import com.cp.personnel.service.IPerStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp.common.core.utils.PageUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.system.api.rpc.UserRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
@Service
public class PerStudentServiceImpl extends ServiceImpl<PerStudentMapper, PerStudent> implements IPerStudentService {


    @Override
    public IPage<PerStudentVO> selectStudentPage(PerStudentQueryDTO dto) {
        Page<PerStudentVO> page = PageUtils.buildPage();
        return this.baseMapper.selectStudentPage(page, dto);
    }
}
