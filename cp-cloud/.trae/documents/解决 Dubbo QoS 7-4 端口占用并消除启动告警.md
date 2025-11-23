## 目标与范围
- 在项目根目录下新建一个文档文件夹，对项目中的“工具类”进行系统化归档说明，每个工具类一个 Markdown 文档。
- 工具类识别标准：包内以 `utils` 命名，以静态/无状态方法为主。优先覆盖：
  - `cp-common-core` → `com.cp.common.core.utils.*`
  - `cp-common-security` → `com.cp.common.security.utils.*`
- 首批覆盖清单（按模块）：
  - core/utils：`DateUtils`、`StringUtils`、`ServletUtils`、`SpringUtils`、`JwtUtils`、`ExceptionUtil`、`IpUtils`、`SqlUtil`、`PageUtils`
  - core/utils/file：`FileUtils`、`FileTypeUtils`、`ImageUtils`、`MimeTypeUtils`
  - core/utils/html：`EscapeUtil`、`HTMLFilter`
  - core/utils/poi：`ExcelUtil`、`ExcelHandlerAdapter`
  - core/utils/reflect：`ReflectUtils`
  - core/utils/uuid：`IdUtils`、`Seq`、`UUID`
  - core/utils/bean：`BeanUtils`、`BeanValidators`
  - security/utils：`SecurityUtils`、`DictUtils`

## 输出结构
- 新建文件夹：`docs/utils/`
- 命名规则：按包路径分层，文件名为类名。例如：
  - `docs/utils/com.cp.common.core.utils/DateUtils.md`
  - `docs/utils/com.cp.common.core.utils.file/FileUtils.md`
  - `docs/utils/com.cp.common.security.utils/SecurityUtils.md`

## 文档模板（每个类）
- 标题：`类名（包名）`
- 简介：用途概述与适用场景
- 依赖与前置：涉及的三方库/项目内依赖
- 方法清单：逐个方法，包含
  - 方法签名（含访问修饰符、静态/实例、泛型）
  - 参数说明（类型、含义、取值限制）
  - 返回值与异常（返回类型、可能抛出）
  - 逻辑说明（核心行为、边界处理、复杂度）
  - 使用示例（代码片段，典型输入/输出）
- 注意事项：线程安全、空值约定、国际化/时区、性能与大对象处理等
- 相关类：与之配合的其它工具类或替代方案
- 源码定位：文件路径 + 关键行

## 生成流程
1. 扫描并确认首批工具类清单。
2. 逐类解析源码，提取公开方法（`public` / `protected`），补充典型使用示例。
3. 按模板生成 Markdown 文档至对应路径。
4. 交付后可按需扩展至其它模块的工具类。

## 校验与交付
- 交叉检查：方法签名与示例一致性、边界条件说明完整性。
- 完成后提供索引文件（可选），汇总所有工具类文档链接。

## 预计产出
- `docs/utils/…` 目录下约 25–30 个 Markdown 文档，覆盖项目核心工具类方法与用法说明。

请确认以上结构与模板是否符合预期；确认后我将开始生成并提交这些文档。