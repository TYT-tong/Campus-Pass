package com.cp.common.security.utils;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.cp.common.core.constant.CacheConstants;
import com.cp.common.core.utils.SpringUtils;
import com.cp.common.core.utils.StringUtils;
import com.cp.common.redis.service.RedisService;
import com.cp.system.api.domain.SysDictData;

/**
 * 字典工具类
 * 
 * @author cp
 */
public class DictUtils
{
    /**
     * 设置字典缓存
     * 
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     * 
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key)
    {
        Object cache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(cache))
        {
            if (cache instanceof List)
            {
                List<?> list = (List<?>) cache;
                if (list.isEmpty())
                {
                    return null;
                }
                Object first = list.get(0);
                if (first instanceof SysDictData)
                {
                    return (List<SysDictData>) list;
                }
                List<SysDictData> result = new ArrayList<>(list.size());
                for (Object item : list)
                {
                    if (item instanceof SysDictData)
                    {
                        result.add((SysDictData) item);
                    }
                    else if (item instanceof Map)
                    {
                        SysDictData d = JSON.parseObject(JSON.toJSONString(item), SysDictData.class);
                        if (d != null)
                        {
                            result.add(d);
                        }
                    }
                }
                if (StringUtils.isNotEmpty(result))
                {
                    return result;
                }
            }
            if (cache instanceof String)
            {
                Object parsed = JSON.parse((String) cache);
                if (parsed instanceof JSONArray)
                {
                    JSONArray ja = (JSONArray) parsed;
                    if (ja.size() == 2 && ja.get(0) instanceof String && ja.get(1) instanceof JSONArray)
                    {
                        JSONArray inner = (JSONArray) ja.get(1);
                        List<SysDictData> arr = inner.toList(SysDictData.class);
                        if (StringUtils.isNotEmpty(arr))
                        {
                            return arr;
                        }
                    }
                    List<SysDictData> arr = ja.toList(SysDictData.class);
                    if (StringUtils.isNotEmpty(arr))
                    {
                        return arr;
                    }
                }
                else if (parsed instanceof List)
                {
                    List<?> list = (List<?>) parsed;
                    if (list.size() == 2 && list.get(0) instanceof String && list.get(1) instanceof List)
                    {
                        List<?> inner = (List<?>) list.get(1);
                        List<SysDictData> result = new ArrayList<>(inner.size());
                        for (Object item : inner)
                        {
                            SysDictData d = JSON.parseObject(JSON.toJSONString(item), SysDictData.class);
                            if (d != null)
                            {
                                result.add(d);
                            }
                        }
                        if (StringUtils.isNotEmpty(result))
                        {
                            return result;
                        }
                    }
                }
            }
            // 兜底处理：将对象统一转为字符串再解析为列表
            try
            {
                Object parsed = JSON.parse(JSON.toJSONString(cache));
                if (parsed instanceof JSONArray)
                {
                    JSONArray ja = (JSONArray) parsed;
                    if (ja.size() == 2 && ja.get(0) instanceof String && ja.get(1) instanceof JSONArray)
                    {
                        JSONArray inner = (JSONArray) ja.get(1);
                        List<SysDictData> arr = inner.toList(SysDictData.class);
                        if (StringUtils.isNotEmpty(arr))
                        {
                            return arr;
                        }
                    }
                    List<SysDictData> arr = ja.toList(SysDictData.class);
                    if (StringUtils.isNotEmpty(arr))
                    {
                        return arr;
                    }
                }
                else if (parsed instanceof List)
                {
                    List<?> list = (List<?>) parsed;
                    if (list.size() == 2 && list.get(0) instanceof String && list.get(1) instanceof List)
                    {
                        List<?> inner = (List<?>) list.get(1);
                        List<SysDictData> result = new ArrayList<>(inner.size());
                        for (Object item : inner)
                        {
                            SysDictData d = JSON.parseObject(JSON.toJSONString(item), SysDictData.class);
                            if (d != null)
                            {
                                result.add(d);
                            }
                        }
                        if (StringUtils.isNotEmpty(result))
                        {
                            return result;
                        }
                    }
                }
            }
            catch (Exception ignore)
            {
            }
        }
        return null;
    }

    /**
     * 删除指定字典缓存
     * 
     * @param key 字典键
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
