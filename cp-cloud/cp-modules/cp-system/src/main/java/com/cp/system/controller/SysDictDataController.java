package com.cp.system.controller;

import com.cp.common.core.utils.StringUtils;
import com.cp.common.core.web.domain.AjaxResult;
import com.cp.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cp.common.core.web.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cp.system.api.domain.SysDictData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tyt15
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController{
    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }
}
