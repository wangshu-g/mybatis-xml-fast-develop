/*
 Navicat Premium Dump SQL

 Source Server         : mysql@localhost
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41-0ubuntu0.24.04.1)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41-0ubuntu0.24.04.1)
 File Encoding         : 65001

 Date: 15/04/2025 19:51:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `createdAt` timestamp(4) NULL DEFAULT NULL COMMENT '创建时间',
  `updatedAt` timestamp(4) NULL DEFAULT NULL COMMENT '更新时间',
  `deletedAt` timestamp(4) NULL DEFAULT NULL COMMENT '删除时间',
  `title` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章标题',
  `desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `detail` longtext COLLATE utf8mb4_bin COMMENT '文章详情',
  `status` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态',
  `uid` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发布者',
  `groupId` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文章所属分组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of article
-- ----------------------------
BEGIN;
INSERT INTO `article` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `title`, `desc`, `detail`, `status`, `uid`, `groupId`) VALUES ('1', '2024-01-30 16:22:56.0000', NULL, NULL, '文章-1', '1', 'ascscbbbbbbbbbbbb', '', '1', 'group1');
INSERT INTO `article` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `title`, `desc`, `detail`, `status`, `uid`, `groupId`) VALUES ('2', '2024-01-30 22:18:53.0000', NULL, NULL, '文章-2', '2', 'rgbgmjxas', '', '2', 'group2');
COMMIT;

-- ----------------------------
-- Table structure for articleGroup
-- ----------------------------
DROP TABLE IF EXISTS `articleGroup`;
CREATE TABLE `articleGroup` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `createdAt` timestamp(4) NULL DEFAULT NULL COMMENT '创建时间',
  `updatedAt` timestamp(4) NULL DEFAULT NULL COMMENT '更新时间',
  `deletedAt` timestamp(4) NULL DEFAULT NULL COMMENT '删除时间',
  `groupName` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分组名称',
  `desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `uid` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of articleGroup
-- ----------------------------
BEGIN;
INSERT INTO `articleGroup` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `groupName`, `desc`, `uid`) VALUES ('group1', '2024-01-30 22:18:26.0000', NULL, NULL, '测试组-1', NULL, NULL);
INSERT INTO `articleGroup` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `groupName`, `desc`, `uid`) VALUES ('group2', '2024-01-30 22:18:31.0000', NULL, NULL, '测试组-2', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for articleTag
-- ----------------------------
DROP TABLE IF EXISTS `articleTag`;
CREATE TABLE `articleTag` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `createdAt` timestamp(4) NULL DEFAULT NULL COMMENT '创建时间',
  `updatedAt` timestamp(4) NULL DEFAULT NULL COMMENT '更新时间',
  `deletedAt` timestamp(4) NULL DEFAULT NULL COMMENT '删除时间',
  `tagName` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签名称',
  `articleId` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属文章',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of articleTag
-- ----------------------------
BEGIN;
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('1', '2024-01-30 22:19:39.0000', NULL, NULL, '文章1标签-1', '1');
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('2', '2024-01-30 22:19:42.0000', NULL, NULL, '文章1标签-2', '1');
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('3', '2024-01-30 22:19:43.0000', '2024-01-30 22:19:44.0000', NULL, '文章2标签-1', '2');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `createdAt` timestamp(4) NULL DEFAULT NULL COMMENT '创建时间',
  `updatedAt` timestamp(4) NULL DEFAULT NULL COMMENT '更新时间',
  `deletedAt` timestamp(4) NULL DEFAULT NULL COMMENT '删除时间',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `readMe` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `name`, `readMe`) VALUES ('1', NULL, NULL, NULL, '1', '1');
INSERT INTO `user` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `name`, `readMe`) VALUES ('2', NULL, NULL, NULL, '2', '2');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
