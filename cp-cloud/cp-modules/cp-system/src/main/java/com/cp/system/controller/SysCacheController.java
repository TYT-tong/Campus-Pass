package com.cp.system.controller;

import com.cp.common.core.web.controller.BaseController;
import com.cp.common.core.web.domain.AjaxResult;
import com.cp.common.core.web.page.TableDataInfo;
import com.cp.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tyt15
 */

@RestController
@ConditionalOnBean(RedisService.class)
@RequestMapping("/cache")
public class SysCacheController extends BaseController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/list/{name}")
    public TableDataInfo list(@PathVariable("name") String name) {
        Collection<String> keys = redisService.keys(name + "*");

        ArrayList<String> list = new ArrayList<>(keys);

        return getDataTable(list);
    }


    @GetMapping("/key/{name}")
    public AjaxResult cacheValue(@PathVariable("name") String name) {
        System.out.println(redisService.getCacheObject(name).toString() );
        return success(redisService.getCacheObject(name).toString());
    }

    @DeleteMapping("/clear/prefix/{prefix}")
    public AjaxResult clearByPrefix(@PathVariable("prefix") String prefix) {
        Collection<String> keys = redisService.keys(prefix + "*");
        redisService.deleteObject(keys);
        return success(keys.size());
    }

    @DeleteMapping("/clear/dict")
    public AjaxResult clearDict() {
        Collection<String> keys = redisService.keys("sys_dict:*");
        redisService.deleteObject(keys);
        return success(keys.size());
    }
}
