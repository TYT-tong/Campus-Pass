-- 统一用户改为复用 RuoYi 的 sys_user 表；删除本地 user 表定义

CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    student_no VARCHAR(30) NOT NULL UNIQUE COMMENT '学号',
    grade VARCHAR(20) COMMENT '年级',
    class_name VARCHAR(50) COMMENT '班级',
    gender TINYINT COMMENT '性别:1-男,2-女',
    id_card VARCHAR(18) COMMENT '身份证号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    INDEX idx_student_no (student_no),
    INDEX idx_class (grade, class_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

CREATE TABLE parent (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    relation VARCHAR(20) COMMENT '与学生关系',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长表';

CREATE TABLE teacher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    teacher_no VARCHAR(30) COMMENT '工号',
    subject VARCHAR(50) COMMENT '任教科目',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    INDEX idx_teacher_no (teacher_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老师表';

CREATE TABLE ic_card (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    card_no VARCHAR(30) NOT NULL UNIQUE COMMENT '卡号',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额',
    status TINYINT DEFAULT 1 COMMENT '状态:0-挂失,1-正常,2-注销',
    loss_time DATETIME COMMENT '挂失时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    INDEX idx_card_no (card_no),
    INDEX idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IC卡表';

CREATE TABLE user_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    parent_id BIGINT NOT NULL COMMENT '家长ID',
    relation VARCHAR(20) COMMENT '关系',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (parent_id) REFERENCES parent(id),
    UNIQUE KEY uk_student_parent (student_id, parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关系表';

CREATE TABLE consume_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    card_no VARCHAR(30) NOT NULL COMMENT '卡号',
    merchant_id BIGINT NOT NULL COMMENT '商户ID',
    pos_id BIGINT COMMENT 'POS终端ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '消费金额',
    balance_before DECIMAL(10,2) COMMENT '消费前余额',
    balance_after DECIMAL(10,2) COMMENT '消费后余额',
    status TINYINT DEFAULT 1 COMMENT '状态:1-成功,2-失败,3-已退款',
    consume_time DATETIME NOT NULL COMMENT '消费时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_no (order_no),
    INDEX idx_student_id (student_id),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_consume_time (consume_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费订单表';

CREATE TABLE recharge_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    parent_id BIGINT NOT NULL COMMENT '家长ID',
    card_no VARCHAR(30) NOT NULL COMMENT '卡号',
    amount DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    channel VARCHAR(20) NOT NULL COMMENT '支付渠道:wechat,alipay',
    channel_order_no VARCHAR(64) COMMENT '渠道订单号',
    status TINYINT DEFAULT 0 COMMENT '状态:0-待支付,1-已支付,2-已关闭',
    pay_time DATETIME COMMENT '支付时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_no (order_no),
    INDEX idx_student_id (student_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_channel_order_no (channel_order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值订单表';

CREATE TABLE refund_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    refund_no VARCHAR(32) NOT NULL UNIQUE COMMENT '退款单号',
    consume_order_id BIGINT NOT NULL COMMENT '原消费订单ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    card_no VARCHAR(30) NOT NULL COMMENT '卡号',
    refund_amount DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    reason VARCHAR(200) COMMENT '退款原因',
    operator_id BIGINT COMMENT '操作人ID',
    status TINYINT DEFAULT 0 COMMENT '状态:0-处理中,1-成功,2-失败',
    refund_time DATETIME COMMENT '退款时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (operator_id) REFERENCES sys_user(user_id),
    INDEX idx_refund_no (refund_no),
    INDEX idx_consume_order_id (consume_order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款订单表';


CREATE TABLE merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_no VARCHAR(30) NOT NULL UNIQUE COMMENT '商户编号',
    merchant_name VARCHAR(100) NOT NULL COMMENT '商户名称',
    merchant_type VARCHAR(50) COMMENT '商户类型:食堂,超市,图书馆等',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    settlement_account VARCHAR(50) COMMENT '结算账户',
    fee_rate DECIMAL(5,4) DEFAULT 0 COMMENT '手续费率',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_merchant_no (merchant_no),
    INDEX idx_merchant_type (merchant_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户表';


CREATE TABLE pos_terminal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    terminal_no VARCHAR(30) NOT NULL UNIQUE COMMENT '终端编号',
    merchant_id BIGINT NOT NULL COMMENT '商户ID',
    terminal_name VARCHAR(100) COMMENT '终端名称',
    location VARCHAR(200) COMMENT '安装位置',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-正常',
    last_online_time DATETIME COMMENT '最后在线时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    INDEX idx_terminal_no (terminal_no),
    INDEX idx_merchant_id (merchant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='POS终端表';


CREATE TABLE merchant_settlement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    settlement_no VARCHAR(32) NOT NULL UNIQUE COMMENT '结算单号',
    merchant_id BIGINT NOT NULL COMMENT '商户ID',
    settlement_date DATE NOT NULL COMMENT '结算日期',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '总交易金额',
    fee_amount DECIMAL(12,2) DEFAULT 0 COMMENT '手续费',
    settlement_amount DECIMAL(12,2) NOT NULL COMMENT '结算金额',
    status TINYINT DEFAULT 0 COMMENT '状态:0-待结算,1-已结算',
    settlement_time DATETIME COMMENT '结算时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    INDEX idx_settlement_no (settlement_no),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_settlement_date (settlement_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户结算表';


CREATE TABLE class_group (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_no VARCHAR(30) NOT NULL UNIQUE COMMENT '群编号',
    group_name VARCHAR(100) NOT NULL COMMENT '群名称',
    grade VARCHAR(20) COMMENT '年级',
    class_name VARCHAR(50) COMMENT '班级',
    owner_id BIGINT NOT NULL COMMENT '群主ID(老师)',
    avatar VARCHAR(255) COMMENT '群头像',
    announcement TEXT COMMENT '群公告',
    member_count INT DEFAULT 0 COMMENT '成员数量',
    status TINYINT DEFAULT 1 COMMENT '状态:0-解散,1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES sys_user(user_id),
    INDEX idx_group_no (group_no),
    INDEX idx_owner_id (owner_id),
    INDEX idx_class (grade, class_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级群表';


CREATE TABLE group_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT NOT NULL COMMENT '群ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role TINYINT DEFAULT 1 COMMENT '角色:1-普通成员,2-管理员,3-群主',
    nickname VARCHAR(50) COMMENT '群昵称',
    mute TINYINT DEFAULT 0 COMMENT '是否禁言:0-否,1-是',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    FOREIGN KEY (group_id) REFERENCES class_group(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    UNIQUE KEY uk_group_user (group_id, user_id),
    INDEX idx_group_id (group_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群成员表';


CREATE TABLE group_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT NOT NULL COMMENT '群ID',
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    message_type TINYINT NOT NULL COMMENT '消息类型:1-文字,2-图片,3-视频,4-语音',
    content TEXT COMMENT '消息内容',
    media_url VARCHAR(255) COMMENT '媒体文件URL',
    send_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    FOREIGN KEY (group_id) REFERENCES class_group(id),
    FOREIGN KEY (sender_id) REFERENCES sys_user(user_id),
    INDEX idx_group_id (group_id),
    INDEX idx_send_time (send_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群消息表';


CREATE TABLE message_read_status (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id BIGINT NOT NULL COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    read_time DATETIME COMMENT '阅读时间',
    FOREIGN KEY (message_id) REFERENCES group_message(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    UNIQUE KEY uk_message_user (message_id, user_id),
    INDEX idx_message_id (message_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息阅读状态表';

CREATE TABLE risk_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(50) NOT NULL COMMENT '规则类型:单笔限额,日累计限额,异地消费等',
    rule_config JSON COMMENT '规则配置(JSON格式)',
    priority INT DEFAULT 0 COMMENT '优先级',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rule_type (rule_type),
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风控规则表';


CREATE TABLE risk_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_no VARCHAR(32) NOT NULL UNIQUE COMMENT '事件编号',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    card_no VARCHAR(30) COMMENT '卡号',
    event_type VARCHAR(50) NOT NULL COMMENT '事件类型',
    risk_level TINYINT NOT NULL COMMENT '风险等级:1-低,2-中,3-高',
    description TEXT COMMENT '事件描述',
    order_no VARCHAR(32) COMMENT '关联订单号',
    status TINYINT DEFAULT 0 COMMENT '处理状态:0-待处理,1-已处理,2-已忽略',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME COMMENT '处理时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_event_no (event_no),
    INDEX idx_student_id (student_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险事件表';


CREATE TABLE consumption_limit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL COMMENT '学生ID',
    single_limit DECIMAL(10,2) COMMENT '单笔限额',
    daily_limit DECIMAL(10,2) COMMENT '日累计限额',
    set_by BIGINT COMMENT '设置人ID(家长)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (set_by) REFERENCES sys_user(user_id),
    UNIQUE KEY uk_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费限额配置表';

CREATE TABLE reconciliation_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_no VARCHAR(32) NOT NULL UNIQUE COMMENT '任务编号',
    recon_date DATE NOT NULL COMMENT '对账日期',
    channel VARCHAR(20) NOT NULL COMMENT '支付渠道',
    status TINYINT DEFAULT 0 COMMENT '状态:0-进行中,1-完成,2-失败',
    total_count INT DEFAULT 0 COMMENT '总笔数',
    match_count INT DEFAULT 0 COMMENT '匹配笔数',
    diff_count INT DEFAULT 0 COMMENT '差异笔数',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_task_no (task_no),
    INDEX idx_recon_date (recon_date),
    INDEX idx_channel (channel)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账任务表';

CREATE TABLE reconciliation_difference (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL COMMENT '对账任务ID',
    diff_type TINYINT NOT NULL COMMENT '差异类型:1-长款,2-短款,3-金额不符',
    order_no VARCHAR(32) COMMENT '系统订单号',
    channel_order_no VARCHAR(64) COMMENT '渠道订单号',
    system_amount DECIMAL(10,2) COMMENT '系统金额',
    channel_amount DECIMAL(10,2) COMMENT '渠道金额',
    status TINYINT DEFAULT 0 COMMENT '处理状态:0-待处理,1-已处理',
    handler_id BIGINT COMMENT '处理人ID',
    handle_remark TEXT COMMENT '处理备注',
    handle_time DATETIME COMMENT '处理时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES reconciliation_task(id),
    INDEX idx_task_id (task_id),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账差异表';


CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    notify_no VARCHAR(32) NOT NULL UNIQUE COMMENT '通知编号',
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    notify_type VARCHAR(50) NOT NULL COMMENT '通知类型:消费通知,充值通知,系统通知等',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    related_id BIGINT COMMENT '关联业务ID',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读:0-未读,1-已读',
    read_time DATETIME COMMENT '阅读时间',
    send_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    INDEX idx_notify_no (notify_no),
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_send_time (send_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';


CREATE TABLE school_announcement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    target_type TINYINT NOT NULL COMMENT '目标范围:1-全校,2-年级,3-班级',
    target_value VARCHAR(100) COMMENT '目标值',
    publisher_id BIGINT NOT NULL COMMENT '发布人ID',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶:0-否,1-是',
    status TINYINT DEFAULT 1 COMMENT '状态:0-下线,1-发布',
    publish_time DATETIME COMMENT '发布时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES sys_user(user_id),
    INDEX idx_target (target_type, target_value),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学校公告表';

-- 移除登录日志表，复用 RuoYi 的 sys_logininfor

-- 移除操作日志表，复用 RuoYi 的 sys_oper_log