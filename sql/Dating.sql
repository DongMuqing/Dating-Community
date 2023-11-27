/*
 Navicat Premium Data Transfer

 Source Server         : Cloud server
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : Dating-pro

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 28/10/2023 17:26:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `user_id` int NULL DEFAULT NULL,
                               `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                               `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               `create_time` datetime NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
                               `comment_id` int NOT NULL AUTO_INCREMENT,
                               `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `user_id` int NULL DEFAULT NULL,
                               `post_id` int NOT NULL,
                               `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               `created_time` datetime NOT NULL,
                               `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               PRIMARY KEY (`comment_id`) USING BTREE,
                               INDEX `post_id`(`post_id` ASC) USING BTREE,
                               CONSTRAINT `tb_comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_friendlink
-- ----------------------------
DROP TABLE IF EXISTS `tb_friendlink`;
CREATE TABLE `tb_friendlink`  (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `located` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, '/', 'Home', '首页', 'HomeFilled', 'Home/Home', 'before');
INSERT INTO `tb_menu` VALUES (2, '/article', 'article', '文章', 'Reading', 'Article/Article', 'before');
INSERT INTO `tb_menu` VALUES (3, '/post', 'post', '动态', 'Share', 'Dynamic/Dynamic', 'before');
INSERT INTO `tb_menu` VALUES (4, '/friendlink', '/friendlink', '友链', 'Link', 'Link/Link', 'before');
INSERT INTO `tb_menu` VALUES (5, '/leavemessage', 'leavemessage', '留言', 'Message', 'Message/Message', 'before');
INSERT INTO `tb_menu` VALUES (6, '/home', 'home', '首页', 'HomeFilled', 'Home/Home', 'backstage');
INSERT INTO `tb_menu` VALUES (7, '/post', 'post', '动态', 'CameraFilled', 'Dynamic/Dynamic', 'backstage');
INSERT INTO `tb_menu` VALUES (8, '/article', 'article', '文章', 'Notebook', 'Attract/Attract', 'backstage');
INSERT INTO `tb_menu` VALUES (9, '/publish', 'article', '文章发布', 'Notebook', 'Article/Article', 'backstage');
INSERT INTO `tb_menu` VALUES (10, '/VisitorInfo', 'VisitorInfo', '访客信息', 'Avatar', 'Link/Link', 'backstage');
INSERT INTO `tb_menu` VALUES (11, '/user', 'user', '用户', 'User', 'User/User', 'backstage');
INSERT INTO `tb_menu` VALUES (12, '/comment', 'comment', '评论', 'Comment', 'Comment/Comment', 'backstage');
INSERT INTO `tb_menu` VALUES (13, '/upload', 'upload', '上传', 'UploadFilled', 'upload/upload', 'backstage');
INSERT INTO `tb_menu` VALUES (14, '/home', 'home', '首页', 'HomeFilled', 'Home/Home', 'user_backstage');
INSERT INTO `tb_menu` VALUES (15, '/post', 'post', '动态', 'CameraFilled', 'Dynamic/Dynamic', 'user_backstage');
INSERT INTO `tb_menu` VALUES (16, '/article', 'article', '文章', 'Notebook', 'Attract/Attract', 'user_backstage');
INSERT INTO `tb_menu` VALUES (17, '/publish', 'article', '文章发布', 'Notebook', 'Article/Article', 'user_backstage');
INSERT INTO `tb_menu` VALUES (18, '/comment', 'comment', '评论', 'Comment', 'Comment/Comment', 'user_backstage');
INSERT INTO `tb_menu` VALUES (19, '/chat', 'chat', '聊天', 'ChatSquare', 'chat/chat', 'backstage');
INSERT INTO `tb_menu` VALUES (20, '/chat', 'chat', '聊天', 'ChatSquare', 'chat/chat', 'user_backstage');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `user_id` int NOT NULL,
                               `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               `sending_time` datetime NOT NULL,
                               `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 140 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for tb_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_post`;
CREATE TABLE `tb_post`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `user_id` int NULL DEFAULT NULL,
                            `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                            `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                            `create_time` datetime NULL DEFAULT NULL,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `upvote_num` int NOT NULL DEFAULT 0,
                            `img_srclist` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'https://oss.qingmumu.xyz/Userpics/Afterclap-2.png',
                            `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                            `login_time` datetime NULL DEFAULT NULL,
                            `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '用户',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_visitorinfo
-- ----------------------------
DROP TABLE IF EXISTS `tb_visitorinfo`;
CREATE TABLE `tb_visitorinfo`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `access_time` datetime NOT NULL,
                                   `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                   `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                   `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                   `os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                   `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 622 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
