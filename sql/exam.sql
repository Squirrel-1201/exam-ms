/*
Navicat MySQL Data Transfer

Source Server         : gtwx231
Source Server Version : 80025
Source Host           : 172.17.1.231:13306
Source Database       : exam

Target Server Type    : MYSQL
Target Server Version : 80025
File Encoding         : 65001

考试管理系统数据库脚本
Date: 2022-03-11 11:04:34
Update Date: 2022-08-22 10:03:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` bigint NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '试卷名称',
  `start_time` datetime DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '考试截止时间',
  `last_time` int DEFAULT NULL COMMENT '考试时长（分钟）',
  `score` decimal(10,1) DEFAULT NULL COMMENT '试卷总分',
  `pass_score` decimal(10,1) DEFAULT NULL COMMENT '通过分数',
  `status` tinyint DEFAULT NULL COMMENT '状态 0 待发布  1已发布  2 阅卷中 3 已结束',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷表';


-- ----------------------------
-- Table structure for exam_question
-- ----------------------------
DROP TABLE IF EXISTS `exam_question`;
CREATE TABLE `exam_question` (
  `id` bigint NOT NULL,
  `exam_id` bigint DEFAULT NULL COMMENT '试卷id',
  `question_category_id` bigint DEFAULT NULL COMMENT '试卷问题类别id',
  `difficulty` tinyint DEFAULT NULL COMMENT '试题难度 0简单 1普通 2困难',
  `type` tinyint DEFAULT NULL COMMENT '问题类型：0单选，1多选，2判断',
  `num` int DEFAULT NULL COMMENT '题数',
  `score` decimal(10,0) DEFAULT NULL COMMENT '分数',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_id` (`exam_id`),
  KEY `question_category_id` (`question_category_id`),
  KEY `difficulty` (`difficulty`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷题目设置表';


-- ----------------------------
-- Table structure for exam_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record` (
  `id` bigint NOT NULL,
  `exam_id` bigint NOT NULL COMMENT '试卷id',
  `stu_id` bigint NOT NULL COMMENT '考生id',
  `type` tinyint DEFAULT NULL COMMENT '题型： 0单选 1 多选 2判断',
  `title` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '题干',
  `difficulty` tinyint DEFAULT NULL COMMENT '试题难度：0简单 1普通 2困难',
  `stu_answer` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '考生答案',
  `answer` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '答案（多选已都好分割）',
  `analyse` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '答案解析',
  `score` decimal(10,0) DEFAULT NULL COMMENT '问题分数',
  `final_score` decimal(10,0) DEFAULT '0' COMMENT '最终成绩',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_id` (`exam_id`),
  KEY `stu_id` (`stu_id`),
  KEY `type` (`type`),
  KEY `difficulty` (`difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='考试记录';


-- ----------------------------
-- Table structure for exam_record_option
-- ----------------------------
DROP TABLE IF EXISTS `exam_record_option`;
CREATE TABLE `exam_record_option` (
  `id` bigint NOT NULL,
  `exam_id` bigint DEFAULT NULL COMMENT '试卷id',
  `exam_record_id` bigint DEFAULT NULL COMMENT '试卷记录id',
  `stu_id` bigint NOT NULL COMMENT '考生id',
  `serial_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '问题选项',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '问题选项内容',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_record_id` (`exam_record_id`),
  KEY `exam_id` (`exam_id`),
  KEY `stu_id` (`stu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考生试卷-问题选项表';


-- ----------------------------
-- Table structure for exam_student
-- ----------------------------
DROP TABLE IF EXISTS `exam_student`;
CREATE TABLE `exam_student` (
  `id` bigint NOT NULL,
  `stu_id` bigint DEFAULT NULL COMMENT '考生id',
  `exam_id` bigint DEFAULT NULL COMMENT '试卷id',
  `status` tinyint DEFAULT '0' COMMENT '状态: 0 待考，1正在考试，2已交卷，3通过，4未通过，5缺考',
  `total_score` decimal(10,1) DEFAULT NULL COMMENT '总分',
  `start_time` datetime DEFAULT NULL COMMENT '开始考试时间',
  `end_time` datetime DEFAULT NULL COMMENT '交卷时间',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `stu_id` (`stu_id`),
  KEY `exam_id` (`exam_id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='试卷和考生的关联表';


-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` bigint NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `app_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微服务名称(EN）',
  `sys_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微服务名称（CN）',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `level` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志级别',
  `code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态  0 成功 , 其他 失败',
  `message` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '详情',
  `thread_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '线程名称',
  `warning` tinyint DEFAULT NULL COMMENT '是否告警',
  `action` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '动作',
  `trace_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '链路id',
  `sub_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '子类型code',
  `sub_type_value` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '子类型',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录用户id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录用户名',
  `generate_time` datetime DEFAULT NULL COMMENT '日志生成时间',
  `create_by` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（精确到秒）',
  `update_by` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间（精确到秒）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日志表';


-- ----------------------------
-- Table structure for log_set
-- ----------------------------
DROP TABLE IF EXISTS `log_set`;
CREATE TABLE `log_set` (
  `id` bigint NOT NULL,
  `save_day` int DEFAULT NULL COMMENT '日志保存天数',
  `level` int DEFAULT NULL COMMENT '日志级别',
  `create_by` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（精确到秒）',
  `update_by` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间（精确到秒）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日志设置表';


-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int NOT NULL,
  `parent_id` int DEFAULT NULL COMMENT '父节点id',
  `menu_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `route_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单路由id',
  `type` int NOT NULL COMMENT '菜单类型：0 目录；1 菜单；2 路由',
  `order_num` int DEFAULT NULL COMMENT '菜单排序',
  `icon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '图标',
  `env` tinyint DEFAULT NULL COMMENT '0 开发环境 1 生产环境  ',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('112233', null, '考生管理', 'stuManage', '0', '1', null, null, '考生端', '2022-03-01 14:03:43', '2022-03-01 14:03:46', null, null);
INSERT INTO `menu` VALUES ('222222', '112233', '考试计划', 'planManage', '1', '22', null, null, '考试计划菜单', '2022-03-01 14:06:09', '2022-03-01 14:06:12', null, null);
INSERT INTO `menu` VALUES ('222333', '112233', '已结束的考试', 'planOverManage', '1', '23', null, null, '已结束的考试菜单', '2022-03-01 14:06:15', '2022-03-01 14:06:17', null, null);
INSERT INTO `menu` VALUES ('771001', null, '考题管理', 'subjectPaperManage', '0', '1', null, null, '管理端', '2022-03-01 14:12:39', '2022-03-01 14:12:42', null, null);
INSERT INTO `menu` VALUES ('772001', null, '考试管理', 'examManage', '0', '2', null, null, '管理端', '2022-03-01 14:12:44', '2022-03-01 14:12:46', null, null);
INSERT INTO `menu` VALUES ('773001', null, '系统管理', 'sysManage', '0', '3', null, null, '管理端', '2022-03-01 14:12:49', '2022-03-01 14:12:51', null, null);
INSERT INTO `menu` VALUES ('771010', '771001', '试题分类', 'subjectTypeManage', '1', '10', null, null, '试题分类菜单', '2022-03-01 14:14:46', '2022-03-01 14:14:43', null, null);
INSERT INTO `menu` VALUES ('771020', '771001', '题目管理', 'subjectManage', '1', '10', null, null, '题目管理菜单', '2022-03-01 14:14:37', '2022-03-01 14:14:40', null, null);
INSERT INTO `menu` VALUES ('772010', '772001', '试卷管理', 'testPapeManage', '1', '11', null, null, '试卷管理菜单', '2022-03-11 11:28:41', '2022-03-11 11:28:47', null, null);
INSERT INTO `menu` VALUES ('772020', '772001', '成绩管理', 'achievementManage', '1', '11', null, null, '成绩管理菜单', '2022-03-11 11:28:50', '2022-03-11 11:28:53', null, null);
INSERT INTO `menu` VALUES ('773010', '773001', '组织机构管理', 'organizationManage', '1', '12', null, null, '组织机构管理菜单', '2022-03-11 11:28:56', '2022-03-11 11:28:58', null, null);
INSERT INTO `menu` VALUES ('773020', '773001', '用户管理', 'userManage', '1', '12', null, null, '用户管理菜单', '2022-03-11 11:29:01', '2022-03-11 11:29:04', null, null);
INSERT INTO `menu` VALUES ('774001', null, '日志管理', 'journalManage', '0', '5', null, null, '日志管理菜单', '2022-03-11 11:29:07', '2022-03-11 11:29:09', null, null);
INSERT INTO `menu` VALUES ('774010', '774001', '操作日志', 'logManage', '1', '13', null, null, '操作日志菜单', '2022-03-11 11:29:12', '2022-03-11 11:29:14', null, null);
INSERT INTO `menu` VALUES ('774020', '774001', '日志设置', 'logSetManage', '1', '13', null, null, '日志设置菜单', '2022-03-11 11:29:18', '2022-03-11 11:29:20', null, null);
INSERT INTO `menu` VALUES ('222444', '222222', '开始考试', 'examinationManage', '2', '13', null, null, '开始考试菜单', '2022-03-11 11:29:23', '2022-03-11 11:29:25', null, null);


-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组织机构名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父节点',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '父节点id集合，/分开;',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织机构表';

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES ('1', '北京国泰网信科技有限公司', null, '1', null, '2022-03-09 10:19:02', '2022-03-10 09:23:49', 'admin', 'admin');
INSERT INTO `organization` VALUES ('2', '人力资源部', '1', '1', '1/', '2022-03-09 11:19:02', '2022-03-10 09:25:41', 'admin', 'admin');
INSERT INTO `organization` VALUES ('1501397117078388737', '综合部', '1', '2', '1/', '2022-03-09 11:30:02', '2022-03-10 09:26:23', 'admin', 'admin');
INSERT INTO `organization` VALUES ('1501731371658293249', '成都研发中心', '1', '2', '1/', '2022-03-10 09:27:14', '2022-03-10 09:27:14', 'admin', 'admin');
INSERT INTO `organization` VALUES ('1501731839432241154', '应用网关产品线', '1501731371658293249', '1', '1/1501731371658293249/', '2022-03-10 09:29:06', '2022-03-10 09:29:06', 'admin', 'admin');
INSERT INTO `organization` VALUES ('1501731931115532290', '密码机产品线', '1501731371658293249', '2', '1/1501731371658293249/', '2022-03-10 09:29:28', '2022-03-10 10:27:34', 'admin', 'admin');

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` bigint NOT NULL,
  `status` tinyint DEFAULT NULL COMMENT '状态: 0 禁用  1启用',
  `question_category_id` bigint DEFAULT NULL COMMENT '题库类目id',
  `difficulty` tinyint DEFAULT NULL COMMENT '试题难度 0简单 1普通 2困难',
  `type` tinyint DEFAULT NULL COMMENT '问题类型：0单选，1多选，2判断',
  `title` text COMMENT '题干',
  `answer` text COMMENT '答案',
  `analyse` text COMMENT '解析',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `question_category_id` (`question_category_id`),
  KEY `difficulty` (`difficulty`),
  KEY `type` (`type`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='题库';


-- ----------------------------
-- Table structure for question_category
-- ----------------------------
DROP TABLE IF EXISTS `question_category`;
CREATE TABLE `question_category` (
  `id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '问题分类名称',
  `status` tinyint DEFAULT NULL COMMENT '状态: 0 禁用  1启用',
  `parent_id` bigint DEFAULT NULL COMMENT '父节点',
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '父集id集合，/分开;',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `question_desc` varchar(500) DEFAULT NULL COMMENT '问题分类描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题类目';


-- ----------------------------
-- Table structure for question_option
-- ----------------------------
DROP TABLE IF EXISTS `question_option`;
CREATE TABLE `question_option` (
  `id` bigint NOT NULL,
  `question_id` bigint DEFAULT NULL COMMENT '问题id',
  `serial_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '问题选项',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '问题选项内容',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题选项表';


-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint NOT NULL,
  `resource_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资源名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资源路径',
  `login_verify` tinyint DEFAULT '1' COMMENT '登录认证: 0 不认证 1 认证',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表';

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES ('1', '考生端', '/stu/**', '1', null, null, null, null, null);
INSERT INTO `resource` VALUES ('2', '管理端', '/admin/**', '1', null, null, null, null, null);
INSERT INTO `resource` VALUES ('3', '通用', '/common/**', '1', null, null, null, null, null);

-- ----------------------------
-- Table structure for resource_menu
-- ----------------------------
DROP TABLE IF EXISTS `resource_menu`;
CREATE TABLE `resource_menu` (
  `id` bigint NOT NULL,
  `resource_id` bigint NOT NULL COMMENT '资源id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源菜单中间表';

-- ----------------------------
-- Records of resource_menu
-- ----------------------------
INSERT INTO `resource_menu` VALUES ('1', '1', '222222', null, null, null, null);
INSERT INTO `resource_menu` VALUES ('2', '2', '771001', null, null, null, null);
INSERT INTO `resource_menu` VALUES ('3', '3', '771001', null, null, null, null);
INSERT INTO `resource_menu` VALUES ('4', '3', '222222', null, null, null, null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色名称',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '考生', '', '2022-02-25 09:21:27', '2022-02-25 09:21:27', null, null);
INSERT INTO `role` VALUES ('2', '管理员', '', '2022-02-25 09:22:25', '2022-02-25 09:22:25', null, null);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` bigint NOT NULL,
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('101', '1', '222444', '2022-03-01 19:54:20', '2022-03-01 19:54:22', null, null);
INSERT INTO `role_menu` VALUES ('102', '1', '112233', '2022-03-07 10:52:58', '2022-03-07 10:53:01', null, null);
INSERT INTO `role_menu` VALUES ('103', '1', '222222', '2022-03-01 14:08:27', '2022-03-01 14:08:32', null, null);
INSERT INTO `role_menu` VALUES ('104', '1', '222333', '2022-03-01 14:08:30', '2022-03-01 14:08:36', null, null);
INSERT INTO `role_menu` VALUES ('602', '2', '771001', '2022-03-01 14:17:37', '2022-03-01 14:17:39', null, null);
INSERT INTO `role_menu` VALUES ('603', '2', '772001', '2022-03-01 14:17:41', '2022-03-01 14:17:43', null, null);
INSERT INTO `role_menu` VALUES ('604', '2', '773001', '2022-03-01 14:17:48', '2022-03-01 14:17:51', null, null);
INSERT INTO `role_menu` VALUES ('605', '2', '771010', '2022-03-01 15:21:22', '2022-03-01 15:21:25', null, null);
INSERT INTO `role_menu` VALUES ('606', '2', '771020', '2022-03-01 15:21:28', '2022-03-01 15:21:31', null, null);
INSERT INTO `role_menu` VALUES ('607', '2', '772010', '2022-03-01 17:33:36', '2022-03-01 17:33:39', null, null);
INSERT INTO `role_menu` VALUES ('608', '2', '772020', '2022-03-01 17:33:41', '2022-03-01 17:33:44', null, null);
INSERT INTO `role_menu` VALUES ('609', '2', '773010', '2022-03-01 17:33:48', '2022-03-01 17:33:51', null, null);
INSERT INTO `role_menu` VALUES ('610', '2', '773020', '2022-03-01 17:33:54', '2022-03-01 17:33:57', null, null);
INSERT INTO `role_menu` VALUES ('611', '2', '774001', '2022-03-01 17:34:01', '2022-03-01 17:34:04', null, null);
INSERT INTO `role_menu` VALUES ('612', '2', '774010', '2022-03-01 19:40:28', '2022-03-01 19:40:30', null, null);
INSERT INTO `role_menu` VALUES ('613', '2', '774020', '2022-03-01 19:54:15', '2022-03-01 19:54:17', null, null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `user_pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `status` tinyint DEFAULT NULL COMMENT '状态 0禁用  1启用  2锁定 3注销',
  `org_id` bigint DEFAULT NULL COMMENT '组织机构id',
  `phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户电话',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系地址',
  `mail` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户邮件',
  `login_fail_count` int DEFAULT NULL COMMENT '连续登录错误次数',
  `lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `login_last` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1501082445087326209', 'admin', '系统管理员', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', '1', '1501731839432241154', '15182899123', '成都市高新区孵化园1号楼B座', '2548981234@163.com', '0', null, '2022-03-11 09:26:41', '2022-03-08 14:28:38', '2022-03-11 09:26:41', 'admin', 'admin');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint NOT NULL,
  `role_id` bigint NOT NULL COMMENT '角色id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色中间表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1501835767650054145', '2', '1501082445087326209', '2022-03-10 16:22:04', '2022-03-10 16:22:04', 'admin', 'admin');
