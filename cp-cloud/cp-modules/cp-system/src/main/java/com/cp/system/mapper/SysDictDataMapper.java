package com.cp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cp.system.api.domain.SysDictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tyt15
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
    List<SysDictData> selectDictDataByType(String dictType);
}
