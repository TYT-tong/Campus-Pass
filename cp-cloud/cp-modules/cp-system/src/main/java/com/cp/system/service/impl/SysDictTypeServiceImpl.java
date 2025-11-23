package com.cp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.security.utils.DictUtils;
import com.cp.system.api.domain.SysDictData;
import com.cp.system.mapper.SysDictDataMapper;
import com.cp.system.mapper.SysDictTypeMapper;
import com.cp.system.plus.mapper.SysDictDataPlusMapper;
import com.cp.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author tyt15
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictDataPlusMapper, SysDictData> implements ISysDictTypeService {
    @Autowired
    private SysDictTypeMapper dictTypeMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        
        // 使用MyBatis-Plus查询
        LambdaQueryWrapper<SysDictData> qw = new LambdaQueryWrapper<>();
        qw.eq(SysDictData::getDictType, dictType)
          .eq(SysDictData::getStatus, "0")
          .orderByAsc(SysDictData::getDictSort);
        
        dictDatas = baseMapper != null ? baseMapper.selectList(qw) : null;
        if (dictDatas == null || dictDatas.isEmpty()) {
            // 降级到旧的mapper
            dictDatas = dictDataMapper.selectDictDataByType(dictType);
        }
        
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

}
