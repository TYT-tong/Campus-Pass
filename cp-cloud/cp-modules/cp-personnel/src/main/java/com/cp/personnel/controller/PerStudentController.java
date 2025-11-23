package com.cp.personnel.controller;


import com.cp.common.core.web.page.TableDataInfo;
import com.cp.personnel.domain.dto.PerStudentQueryDTO;
import com.cp.personnel.service.IPerStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cp.common.core.web.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author tyt
 * @since 2025-11-21
 */
@RestController
@RequestMapping("/student")
public class PerStudentController extends BaseController{
    @Autowired
    private IPerStudentService perStudentService;
    /**
     * 获取学生列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerStudentQueryDTO queryDTO) {
        return getDataTable(perStudentService.selectStudentPage(queryDTO));
    }
}
