## 目标
- 你的缓存值包含 `GenericJackson2JsonRedisSerializer` 的类型标注包装：形如 `["java.util.ArrayList", [ {...}, {...} ]]`。
- 目前 `DictUtils.getDictCache` 已支持 `List<?>` 与字符串 JSON，但未显式处理这种“集合类型标注”结构，二次查询时仍可能走到不兼容路径。

## 修改点
- 文件：`cp-common/cp-common-security/src/main/java/com/cp/common/security/utils/DictUtils.java`
- 方法：`getDictCache(String key)`
- 变更内容：
  - 在 `cache instanceof String` 分支：`JSON.parse((String) cache)` 后判断：
    - 若解析结果为 `JSONArray` 且形如 `[typeName, dataArray]`，取第二项 `dataArray` 转 `List<SysDictData>` 返回；否则直接按数组解析。
  - 在兜底分支：对对象进行 `JSON.toJSONString(cache)` 后解析为数组，同样兼容 `[typeName, dataArray]` 的结构。

## 验证
- 清理 `sys_dict:*` 缓存后，首次查询走 DB 并写入；第二次命中缓存直接返回，不再出现 `ClassCastException`。
- 同时保留现有用户缓存的容错逻辑，避免 `LoginUser` 强转失败。

如确认，我将更新代码、构建并重启 `cp-system`，然后指导你复测。