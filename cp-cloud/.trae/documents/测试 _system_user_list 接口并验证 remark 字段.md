## 测试步骤
1. 启动 cp-auth 服务（端口 9200），用于获取访问令牌。
2. 调用登录接口：POST http://localhost:9200/login，尝试使用默认管理员账号（admin/admin123）获取 JWT。
3. 调用网关接口：GET http://localhost:8081/system/user/list，设置 Header `Authorization: Bearer <jwt>`。
4. 验证响应列表中的每条用户对象是否包含 `remark` 字段。

## 注意
- 若默认账号密码不同，将无法获取令牌；我会回显登录结果。必要时可用你提供的正确账号测试。
- 若网关未配置 `/system/**` 路由，我会直接请求 cp-system 的 `/user/list` 作为备用验证。