INSERT INTO `article` VALUES ('1', '2024-01-30 16:22:56', NULL, NULL, '文章-1', '1', NULL, 'user1', 'group1', NULL);
INSERT INTO `article` VALUES ('2', '2024-01-30 22:18:53', NULL, NULL, '文章-2', '2', NULL, 'user1', 'group2', NULL);
INSERT INTO `articlegroup` VALUES ('group1', '2024-01-30 22:18:26', NULL, NULL, '测试组-1', NULL, NULL);
INSERT INTO `articlegroup` VALUES ('group2', '2024-01-30 22:18:31', NULL, NULL, '测试组-2', NULL, NULL);
INSERT INTO `articletag` VALUES ('1', '2024-01-30 22:19:39', NULL, NULL, '文章1标签-1', '1');
INSERT INTO `articletag` VALUES ('2', '2024-01-30 22:19:42', NULL, NULL, '文章1标签-2', '1');
INSERT INTO `articletag` VALUES ('3', '2024-01-30 22:19:43', '2024-01-30 22:19:44', NULL, '文章2标签-1', '2');
INSERT INTO `user` VALUES ('100', '100', NULL, NULL, 'd309da18623a480ebaca7e20f5ac40da', '2024-01-10 08:48:57', '2024-01-10 08:48:57', NULL, 1, NULL);
INSERT INTO `user` VALUES ('user1', NULL, NULL, NULL, 'user1', NULL, NULL, NULL, 1, NULL);