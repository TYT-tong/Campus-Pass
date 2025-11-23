spring:
  # Redis 配置
  data:
    redis:
      host: 8.134.249.238       # Redis服务器地址
      port: 6379                # Redis端口（默认6379）
      password: 123             # Redis连接密码
      database: 10              # 连接的Redis数据库索引（0-15）

  # Spring Cloud Gateway 网关配置
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true  # 服务ID转为小写（适配服务名小写场景）
          enabled: true             # 开启服务发现自动路由
      server:
        webflux:
          discovery:
            locator:
              enabled: true
              lower-case-service-id: true
      
      routes:
        # 认证服务路由
        - id: cp-auth
          uri: lb://cp-auth         # 负载均衡指向cp-auth服务
          predicates:
            - Path=/auth/**         # 匹配路径：/auth/*
          filters:
            - CacheRequestBody      # 缓存请求体（供后续过滤器使用）
            - ValidateCodeFilter    # 验证码校验过滤器
            - StripPrefix=1         # 移除1级前缀（/auth/login → /login）
        
        # 系统服务路由
        - id: cp-system
          uri: lb://cp-system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix=1
        
        # 卡片服务路由
        - id: cp-card
          uri: lb://cp-card
          predicates:
            - Path=/card/**
          filters:
            - StripPrefix=1
        
        # 支付服务路由
        - id: cp-payment
          uri: lb://cp-payment
          predicates:
            - Path=/payment/**
          filters:
            - StripPrefix=1
        
        # 商户服务路由
        - id: cp-merchant
          uri: lb://cp-merchant
          predicates:
            - Path=/merchant/**
          filters:
            - StripPrefix=1
        
        # 消息服务路由
        - id: cp-message
          uri: lb://cp-message
          predicates:
            - Path=/message/**
          filters:
            - StripPrefix=1

  # 安全配置（验证码、XSS、白名单）
  security:
    captcha:
      enabled: false               # 关闭验证码功能
      type: math                   # 验证码类型（数学计算型）
    xss:
      enabled: true                # 开启XSS防护（防跨站脚本攻击）
      excludeUrls:                 # 排除XSS过滤的路径
        - /system/notice
        - /message/group/message
    ignore:
      whites:                      # 免认证白名单路径
        - /auth/logout
        - /auth/login
        - /auth/register
        - /*/v3/api-docs             # Swagger v3文档
        - /csrf                    # CSRF令牌接口
        - /auth/sms/send           # 发送短信验证码
        - /auth/sms/login          # 短信登录接口

# SpringDoc 接口文档配置（OpenAPI/Swagger）
springdoc:
  webjars:
    prefix: /webjars               # WebJars资源访问前缀（如Swagger UI静态资源）

management:
  endpoints:
    web:
      exposure:
        include: gateway