## 目标
- 修复 Redis 使用 `GenericJackson2JsonRedisSerializer` 反序列化为 `ArrayList<LinkedHashMap>` 时，`DictUtils.getDictCache` 的类型不匹配导致的 `ClassCastException`。

## 变更点
- 文件：`cp-common/cp-common-security/src/main/java/com/cp/common/security/utils/DictUtils.java`
- 方法：`getDictCache(String key)`（约 36–50 行）
- 新增 import：`com.alibaba.fastjson2.JSON`、`java.util.ArrayList`、`java.util.Map`

## 实现思路
- 保留现有 `JSONArray` 分支：`((JSONArray) cache).toList(SysDictData.class)`。
- 针对 `cache instanceof List`：
  - 空列表直接返回 `null`（保持原有语义：未命中缓存时走数据库）。
  - 若首元素是 `SysDictData`，直接强转返回。
  - 否则逐项转换：
    - `Map/LinkedHashMap` → 使用 fastjson2：`JSON.parseObject(JSON.toJSONString(item), SysDictData.class)`。
    - `SysDictData` 实例 → 直接加入结果。
- 返回 `List<SysDictData>`；若转换后为空，返回 `null` 以保持服务层现有逻辑。

## 验证步骤
- 场景复现：Redis 中已有由 Jackson 反序列化生成的 `ArrayList<LinkedHashMap>` 字典缓存。
- 调用：`GET /dict/data/type/{dictType}`（如 `sys_user_sex`）。
- 期望：无 `ClassCastException`；命中缓存时直接返回字典列表；未命中缓存时走 DB 并写入缓存。

## 影响范围
- 仅影响字典缓存读取逻辑；不改动缓存写入与其它 Redis 读写。
- 与现有 `SysDictTypeServiceImpl.selectDictDataByType` 的判空语义保持一致。