CREATE TABLE `t_account_data` (
  `account_data` varchar(100) DEFAULT NULL,
  `account_time` varchar(100) DEFAULT NULL,
  `account_type` varchar(100) DEFAULT NULL,
  `account_consume` varchar(100) DEFAULT NULL,
  `account_gain` varchar(100) DEFAULT NULL,
  `account_mark` varchar(1000) DEFAULT NULL,
  `account_site` varchar(100) DEFAULT NULL,
  `account_ip` varchar(100) DEFAULT NULL,
  `account_mac` varchar(500) DEFAULT NULL,
  `account_user_account` varchar(100) DEFAULT NULL,
  `account_user_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_book_classify` (
  `classify_id` varchar(100) NOT NULL COMMENT '库内唯一识别号',
  `classify_name` varchar(100) NOT NULL COMMENT '分类名',
  `classify_desc` varchar(100) DEFAULT NULL COMMENT '详情',
  `classify_insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_book_dict` (
  `book_id` varchar(100) NOT NULL COMMENT '库内唯一识别号',
  `book_name` varchar(100) NOT NULL COMMENT '书名',
  `book_num` varchar(100) DEFAULT NULL COMMENT '书籍编号',
  `book_stock` varchar(100) DEFAULT NULL COMMENT '库存',
  `book_author` varchar(100) DEFAULT NULL COMMENT '作者',
  `book_desc` varchar(2000) DEFAULT NULL COMMENT '简介',
  `book_classify_id` varchar(30) DEFAULT NULL,
  `book_insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_borrow_dict` (
  `borrow_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `book_id` varchar(100) DEFAULT NULL COMMENT '书籍id',
  `status` varchar(100) DEFAULT '借' COMMENT '状态',
  `borrow_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `repay_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '还书时间',
  `borrow_day` varchar(100) DEFAULT NULL COMMENT '预期借阅时间',
  `borrow_count` varchar(10) DEFAULT NULL COMMENT '借阅数量',
  `mark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `pass_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '审核通过时间',
  `get_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '取书时间',
  `pass_name` varchar(100) DEFAULT NULL COMMENT '审核人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_borrow_status` (
  `status_id` varchar(100) DEFAULT NULL COMMENT '状态id',
  `status_name` varchar(100) DEFAULT NULL COMMENT '名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_game_log` (
  `game_id` varchar(100) DEFAULT NULL COMMENT '对局id',
  `user_r` varchar(100) DEFAULT NULL COMMENT '红色方',
  `user_b` varchar(100) DEFAULT NULL COMMENT '黑色方',
  `win_user` varchar(100) DEFAULT NULL COMMENT '胜利者',
  `round_count` varchar(100) DEFAULT NULL COMMENT '回合数',
  `game_info` varchar(4000) DEFAULT NULL COMMENT '中文棋谱',
  `begin_time` varchar(100) DEFAULT NULL COMMENT '开局时间',
  `over_time` varchar(100) DEFAULT NULL COMMENT '结束时间',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据插入时间',
  `game_type` varchar(10) DEFAULT NULL COMMENT '对局类型，0对局，1打谱',
  `game_board` varchar(1000) DEFAULT NULL COMMENT '棋盘坐标相关',
  `game_mark` varchar(256) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_msg_dict` (
  `msg_id` varchar(100) NOT NULL COMMENT '留言id',
  `msg_desc` varchar(100) NOT NULL COMMENT '留言内容',
  `user_id` varchar(100) DEFAULT NULL COMMENT '发布者id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '发布者姓名',
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_notice_dict` (
  `notice_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `notice_title` varchar(100) DEFAULT NULL COMMENT '公告标题',
  `notice_desc` varchar(1000) DEFAULT NULL COMMENT '详细',
  `notice_publisher` varchar(100) DEFAULT NULL COMMENT '公告发布者',
  `notice_keyword` varchar(100) DEFAULT NULL COMMENT '关键字',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `publisher_id` varchar(100) DEFAULT NULL COMMENT '发布者id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_role_dict` (
  `role_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `role_name` varchar(100) DEFAULT NULL COMMENT '权限名',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '详细',
  `fine_standard` varchar(100) DEFAULT NULL COMMENT '罚金标准  RMB/DAY',
  `borrow_capacity` varchar(100) DEFAULT NULL COMMENT '借阅容量',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `day_pay` varchar(100) DEFAULT '1' COMMENT '日付标准 RMB/DAY'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_test` (
  `login_name` varchar(100) DEFAULT NULL,
  `login_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `user_account` varchar(100) DEFAULT NULL COMMENT '账号',
  `user_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `user_password` varchar(100) DEFAULT NULL,
  `user_desc` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user_dict` (
  `user_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `user_account` varchar(100) DEFAULT NULL COMMENT '账号',
  `user_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `user_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `user_desc` varchar(100) DEFAULT NULL COMMENT '用户介绍',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `role_id` varchar(100) DEFAULT NULL COMMENT '等级id',
  `money` varchar(100) DEFAULT '0' COMMENT '余额'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
