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
  `classify_id` varchar(100) NOT NULL COMMENT '����Ψһʶ���',
  `classify_name` varchar(100) NOT NULL COMMENT '������',
  `classify_desc` varchar(100) DEFAULT NULL COMMENT '����',
  `classify_insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '���ʱ��'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_book_dict` (
  `book_id` varchar(100) NOT NULL COMMENT '����Ψһʶ���',
  `book_name` varchar(100) NOT NULL COMMENT '����',
  `book_num` varchar(100) DEFAULT NULL COMMENT '�鼮���',
  `book_stock` varchar(100) DEFAULT NULL COMMENT '���',
  `book_author` varchar(100) DEFAULT NULL COMMENT '����',
  `book_desc` varchar(2000) DEFAULT NULL COMMENT '���',
  `book_classify_id` varchar(30) DEFAULT NULL,
  `book_insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '���ʱ��'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_borrow_dict` (
  `borrow_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `user_id` varchar(100) DEFAULT NULL COMMENT '�û�id',
  `book_id` varchar(100) DEFAULT NULL COMMENT '�鼮id',
  `status` varchar(100) DEFAULT '��' COMMENT '״̬',
  `borrow_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `repay_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '����ʱ��',
  `borrow_day` varchar(100) DEFAULT NULL COMMENT 'Ԥ�ڽ���ʱ��',
  `borrow_count` varchar(10) DEFAULT NULL COMMENT '��������',
  `mark` varchar(1000) DEFAULT NULL COMMENT '��ע',
  `pass_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '���ͨ��ʱ��',
  `get_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'ȡ��ʱ��',
  `pass_name` varchar(100) DEFAULT NULL COMMENT '�����'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_borrow_status` (
  `status_id` varchar(100) DEFAULT NULL COMMENT '״̬id',
  `status_name` varchar(100) DEFAULT NULL COMMENT '��'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_game_log` (
  `game_id` varchar(100) DEFAULT NULL COMMENT '�Ծ�id',
  `user_r` varchar(100) DEFAULT NULL COMMENT '��ɫ��',
  `user_b` varchar(100) DEFAULT NULL COMMENT '��ɫ��',
  `win_user` varchar(100) DEFAULT NULL COMMENT 'ʤ����',
  `round_count` varchar(100) DEFAULT NULL COMMENT '�غ���',
  `game_info` varchar(4000) DEFAULT NULL COMMENT '��������',
  `begin_time` varchar(100) DEFAULT NULL COMMENT '����ʱ��',
  `over_time` varchar(100) DEFAULT NULL COMMENT '����ʱ��',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '���ݲ���ʱ��',
  `game_type` varchar(10) DEFAULT NULL COMMENT '�Ծ����ͣ�0�Ծ֣�1����',
  `game_board` varchar(1000) DEFAULT NULL COMMENT '�����������',
  `game_mark` varchar(256) DEFAULT NULL COMMENT '��ע'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_msg_dict` (
  `msg_id` varchar(100) NOT NULL COMMENT '����id',
  `msg_desc` varchar(100) NOT NULL COMMENT '��������',
  `user_id` varchar(100) DEFAULT NULL COMMENT '������id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '����������',
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_notice_dict` (
  `notice_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `notice_title` varchar(100) DEFAULT NULL COMMENT '�������',
  `notice_desc` varchar(1000) DEFAULT NULL COMMENT '��ϸ',
  `notice_publisher` varchar(100) DEFAULT NULL COMMENT '���淢����',
  `notice_keyword` varchar(100) DEFAULT NULL COMMENT '�ؼ���',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `publisher_id` varchar(100) DEFAULT NULL COMMENT '������id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_role_dict` (
  `role_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `role_name` varchar(100) DEFAULT NULL COMMENT 'Ȩ����',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '��ϸ',
  `fine_standard` varchar(100) DEFAULT NULL COMMENT '�����׼  RMB/DAY',
  `borrow_capacity` varchar(100) DEFAULT NULL COMMENT '��������',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `day_pay` varchar(100) DEFAULT '1' COMMENT '�ո���׼ RMB/DAY'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_test` (
  `login_name` varchar(100) DEFAULT NULL,
  `login_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `user_account` varchar(100) DEFAULT NULL COMMENT '�˺�',
  `user_name` varchar(100) DEFAULT NULL COMMENT '����',
  `user_password` varchar(100) DEFAULT NULL,
  `user_desc` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user_dict` (
  `user_id` varchar(100) DEFAULT NULL COMMENT 'id',
  `user_account` varchar(100) DEFAULT NULL COMMENT '�˺�',
  `user_name` varchar(100) DEFAULT NULL COMMENT '����',
  `user_password` varchar(100) DEFAULT NULL COMMENT '����',
  `user_desc` varchar(100) DEFAULT NULL COMMENT '�û�����',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `role_id` varchar(100) DEFAULT NULL COMMENT '�ȼ�id',
  `money` varchar(100) DEFAULT '0' COMMENT '���'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
