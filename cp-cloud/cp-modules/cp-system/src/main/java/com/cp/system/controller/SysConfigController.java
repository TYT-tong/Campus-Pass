package com.cp.system.controller;


import com.alibaba.nacos.api.config.ConfigService;
import com.cp.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tyt15
 */
@RestController
@RequestMapping("/config")
public class SysConfigController {

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/configKey/{configKey}")
    public String configKey(@PathVariable String configKey) {
        return configService.getConfigById(configKey);
    }

}
