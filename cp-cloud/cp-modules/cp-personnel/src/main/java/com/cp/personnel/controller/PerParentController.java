package com.cp.personnel.controller;


import com.cp.common.core.web.controller.BaseController;
import com.cp.common.core.web.page.TableDataInfo;
import com.cp.personnel.domain.dto.PerParentQueryDTO;
import com.cp.personnel.service.IPerParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 家长表 前端控制器
 * </p>
 *
 * @author tyt
 * @since 2025-11-22
 */
@RestController
@RequestMapping("/parent")
public class PerParentController extends BaseController {
    @Autowired
    private IPerParentService perParentService;

    /**
     * 获取学生列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerParentQueryDTO queryDTO) {
        return getDataTable(perParentService.selectParentPage(queryDTO));
    }
}
