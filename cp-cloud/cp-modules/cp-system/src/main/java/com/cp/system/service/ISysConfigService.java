package com.cp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cp.system.domain.SysConfig;

/**
 * @author tyt15
 */
public interface ISysConfigService extends IService<SysConfig> {
    String getConfigById(String configKey);
}
