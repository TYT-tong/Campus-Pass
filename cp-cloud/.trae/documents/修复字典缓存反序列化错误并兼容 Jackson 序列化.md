## 问题与原因
- 报错一：`Unrecognized token 'Set'` → Redis中旧值不是合法JSON或是FastJson格式，与当前 `GenericJackson2JsonRedisSerializer` 不兼容。
- 报错二：`Unrecognized field "default" (SysDictData)` → 旧缓存使用字段 `default`，而实体为 `isDefault`；切换到 Jackson 后读取旧数据失败。
- 代码因素：`DictUtils.getDictCache` 依赖 FastJson 的 `JSONArray` 解析，已不适配 Jackson 反序列化后的List对象。

## 解决方案
1) 代码兼容：调整 `DictUtils.getDictCache`，同时支持两种数据形态：
   - 若读取到 `JSONArray`（旧FastJson缓存），用 `toList(SysDictData.class)` 转换；
   - 若读取到 `List`（新Jackson缓存），直接强转为 `List<SysDictData>` 返回；
   - 其它类型返回 null，避免类型冲突。
2) 清理旧缓存：
   - 删除 `SYS_DICT_KEY*` 字典缓存键，避免旧数据继续触发 Jackson 反序列化异常。
   - 可选顺带清理 `LOGIN_TOKEN_KEY*` 以完全切换到 Jackson 结构。
3) 重启并验证：
   - 重新访问 `/dict/data/type/sys_user_sex` 等接口；
   - 观察不再出现 `Unrecognized token 'Set'` 和 `Unrecognized field "default"`。

## 代码改动（最小化）
- 文件：`cp-common/cp-common-security/src/main/java/com/cp/common/security/utils/DictUtils.java`
- 仅修改 `getDictCache(String key)` 的返回逻辑以兼容两种序列化结果。

## 验证步骤
- 清理缓存后，调用 `/system` 相关接口拉取字典；
- 日志中不再出现 Jackson 反序列化错误；
- 登录与业务接口正常读取缓存对象。

## 影响评估
- 仅字典工具类的读取逻辑变更，向下兼容；
- 清理缓存是一次性操作，后续写入与读取均使用 Jackson 序列化格式。