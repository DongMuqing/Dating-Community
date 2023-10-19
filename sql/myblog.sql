/*
 Navicat Premium Data Transfer

 Source Server         : Cloud
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : myblog

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 19/10/2023 16:18:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `post_id` int NOT NULL,
  `username` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `created_time` datetime NOT NULL,
  `address` varchar(20) NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `tb_comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tb_friendlink
-- ----------------------------
DROP TABLE IF EXISTS `tb_friendlink`;
CREATE TABLE `tb_friendlink` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `intro` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `located` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

BEGIN;
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (1, '/', 'Home', '首页', 's-home', 'Home/Home', 'before');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (2, '/article', 'article', '文章', 'reading', 'Article/Article', 'before');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (3, '/dynamic', 'dynamic', '动态', 'attract', 'Dynamic/Dynamic', 'before');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (4, '/friendlink', '/friendlink', '友链', 'link', 'Link/Link', 'before');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (5, '/leavemessage', 'leavemessage', '留言', 'message', 'Message/Message', 'before');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (6, '/home', 'home', '首页', 's-home', 'Home/Home', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (7, '/dynamic', 'dynamic', '动态', 'attract', 'Dynamic/Dynamic', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (8, '/attract', 'attract', '文章', 'reading', 'Attract/Attract', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (9, '/VisitorInfo', 'VisitorInfo', '访客信息', 'link', 'Link/Link', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (10, '/user', 'user', '用户', 'message', 'User/User', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (11, '/comment', 'comment', '评论', 's-comment', 'Commment/Comment', 'backstage');
INSERT INTO `tb_menu` (`id`, `path`, `name`, `label`, `icon`, `url`, `located`) VALUES (12, '/upload', 'upload', '上传', 'upload', 'upload/upload', 'backstage');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
-- Table structure for tb_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_post`;
CREATE TABLE `tb_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `upvote_num` int NOT NULL DEFAULT '0',
  `music_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `img_srclist` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'https://oss.qingmumu.xyz/Userpics/Afterclap-2.png',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `login_time` datetime NOT NULL DEFAULT '2023-10-17 19:27:02',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'user',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_visitorinfo
-- ----------------------------
DROP TABLE IF EXISTS `tb_visitorinfo`;
CREATE TABLE `tb_visitorinfo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `access_time` datetime NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=535 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
