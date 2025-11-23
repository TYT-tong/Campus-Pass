package com.cp.system.controller;

import com.cp.common.core.web.controller.BaseController;
import com.cp.common.core.web.page.TableDataInfo;
import com.cp.common.security.annotation.RequiresPermissions;
import com.cp.system.api.domain.SysOperLog;
import com.cp.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author cp
 */
@RestController
@RequestMapping("/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;

    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog)
    {
        return getDataTable(operLogService.selectOperLogPage(operLog));
    }
}
