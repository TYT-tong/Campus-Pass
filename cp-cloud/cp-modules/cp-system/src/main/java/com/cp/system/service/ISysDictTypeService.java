package com.cp.system.service;

import com.cp.system.api.domain.SysDictData;

import java.util.List;

/**
 * @author tyt15
 */
public interface ISysDictTypeService {
    List<SysDictData> selectDictDataByType(String dictType);
}
