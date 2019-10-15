/*
Navicat MySQL Data Transfer

Source Server         : 新测试环境histar
Source Server Version : 50727
Source Host           : 47.103.127.5:13306
Source Database       : histar_dev

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-10-15 13:04:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for opt_log
-- ----------------------------
DROP TABLE IF EXISTS `opt_log`;
CREATE TABLE `opt_log` (
  `log_id` varchar(32) NOT NULL COMMENT '日志ID',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `user_ip` varchar(32) DEFAULT NULL COMMENT '用户IP',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方法 GET POST',
  `jmethod_desc` varchar(64) DEFAULT NULL COMMENT '方法说明',
  `jmethod` varchar(255) DEFAULT NULL COMMENT '类方法',
  `param` varchar(255) DEFAULT NULL COMMENT '请求参数',
  `create_time` datetime DEFAULT NULL,
  `request_url` varchar(255) DEFAULT NULL,
  `response_status` varchar(255) DEFAULT NULL COMMENT '响应状态',
  `opts` text COMMENT '操作结果',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
