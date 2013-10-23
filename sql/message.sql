/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : sqlcon

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2013-10-20 19:33:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sendReceiveGroup` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `sendUsername` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `isRead` char(255) DEFAULT '0' COMMENT '0：未读；1：已读',
  `isDel` char(255) DEFAULT '0' COMMENT '0：未删除；1：已删除',
  `createBy` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
