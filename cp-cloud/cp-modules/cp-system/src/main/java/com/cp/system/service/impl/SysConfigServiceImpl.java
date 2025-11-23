package com.cp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cp.common.core.constant.CacheConstants;
import com.cp.common.core.text.Convert;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.redis.service.RedisService;
import com.cp.system.domain.SysConfig;
import com.cp.system.plus.mapper.SysConfigPlusMapper;
import com.cp.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author tyt15
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigPlusMapper, SysConfig> implements ISysConfigService {

    @Autowired
    private RedisService redisService;


    @Override
    public String getConfigById(String configKey) {
        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));

        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigKey(configKey);

        LambdaQueryWrapper<SysConfig> qw = new LambdaQueryWrapper<>();
        qw.eq(SysConfig::getConfigKey, configKey);

        SysConfig value = baseMapper != null ? baseMapper.selectOne(qw) : null;

        if (value != null) {
            redisService.setCacheObject(getCacheKey(configKey), value.getConfigValue());
            return value.getConfigValue();
        }


        return StringUtils.EMPTY;
    }


    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
