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
BEGIN;
INSERT INTO `article` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `title`, `desc`, `detail`, `status`, `uid`, `groupId`) VALUES ('1', '2025-01-30 16:22:56.0000', NULL, NULL, '文章-1', '1', 'ascscbbbbbbbbbbbb', '', '1', 'group1');
INSERT INTO `article` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `title`, `desc`, `detail`, `status`, `uid`, `groupId`) VALUES ('2', '2025-01-30 22:18:53.0000', NULL, NULL, '文章-2', '2', 'rgbgmjxas', '', '2', 'group2');
COMMIT;
BEGIN;
INSERT INTO `articleGroup` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `groupName`, `desc`, `uid`) VALUES ('group1', '2025-01-30 22:18:26.0000', NULL, NULL, '测试组-1', NULL, NULL);
INSERT INTO `articleGroup` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `groupName`, `desc`, `uid`) VALUES ('group2', '2025-01-30 22:18:31.0000', NULL, NULL, '测试组-2', NULL, NULL);
COMMIT;
BEGIN;
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('1', '2025-01-30 22:19:39.0000', NULL, NULL, '文章1标签-1', '1');
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('2', '2025-01-30 22:19:42.0000', NULL, NULL, '文章1标签-2', '1');
INSERT INTO `articleTag` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `tagName`, `articleId`) VALUES ('3', '2025-01-30 22:19:43.0000', '2025-01-30 22:19:44.0000', NULL, '文章2标签-1', '2');
COMMIT;
BEGIN;
INSERT INTO `user` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `name`, `readMe`) VALUES ('1', NULL, NULL, NULL, '1', '1');
INSERT INTO `user` (`id`, `createdAt`, `updatedAt`, `deletedAt`, `name`, `readMe`) VALUES ('2', NULL, NULL, NULL, '2', '2');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
