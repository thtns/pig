-- ----------------------------
-- Table structure for sys_user
-- ----------------------------

INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367536, '机器人查询记录表管理', '', '/admin/bizrobotqueryrecord/index', 1669705907359711234, 'icon-debug', 8, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:34:34');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367537, '机器人查询记录表查看', 'admin_bizrobotqueryrecord_get', NULL, 1686920367536, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367538, '机器人查询记录表新增', 'admin_bizrobotqueryrecord_add', NULL, 1686920367536, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367539, '机器人查询记录表修改', 'admin_bizrobotqueryrecord_edit', NULL, 1686920367536, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367540, '机器人查询记录表删除', 'admin_bizrobotqueryrecord_del', NULL, 1686920367536, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367565, '采购商订单表管理', '', '/admin/bizbuyerorder/index', 1669705907359711234, 'icon-tubiaozhizuomoban-27', 3, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:33:15');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367566, '采购商订单表查看', 'admin_bizbuyerorder_get', NULL, 1686920367565, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367567, '采购商订单表新增', 'admin_bizbuyerorder_add', NULL, 1686920367565, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367568, '采购商订单表修改', 'admin_bizbuyerorder_edit', NULL, 1686920367565, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367569, '采购商订单表删除', 'admin_bizbuyerorder_del', NULL, 1686920367565, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367592, '采购商表管理', '', '/admin/bizbuyer/index', 1669705907359711234, 'icon-principal', 2, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:32:53');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367593, '采购商表查看', 'admin_bizbuyer_get', NULL, 1686920367592, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367594, '采购商表新增', 'admin_bizbuyer_add', NULL, 1686920367592, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367595, '采购商表修改', 'admin_bizbuyer_edit', NULL, 1686920367592, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367596, '采购商表删除', 'admin_bizbuyer_del', NULL, 1686920367592, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367606, '品牌供应商关系表管理', '', '/admin/bizcarbrandsupplier/index', 1669705907359711234, 'icon-jiaoseguanli', 5, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:33:40');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367607, '品牌供应商关系表查看', 'admin_bizcarbrandsupplier_get', NULL, 1686920367606, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367608, '品牌供应商关系表新增', 'admin_bizcarbrandsupplier_add', NULL, 1686920367606, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367609, '品牌供应商关系表修改', 'admin_bizcarbrandsupplier_edit', NULL, 1686920367606, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367610, '品牌供应商关系表删除', 'admin_bizcarbrandsupplier_del', NULL, 1686920367606, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367621, '机器人管理', '', '/admin/bizrobot/index', 1669705907359711234, 'icon-cuowutishitubiao', 6, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:33:52');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367622, '机器人查看', 'admin_bizrobot_get', NULL, 1686920367621, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367623, '机器人新增', 'admin_bizrobot_add', NULL, 1686920367621, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367624, '机器人修改', 'admin_bizrobot_edit', NULL, 1686920367621, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367625, '机器人删除', 'admin_bizrobot_del', NULL, 1686920367621, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367634, '机器人供应商关系表管理', '', '/admin/bizrobotsupplier/index', 1669705907359711234, 'icon-web-icon-', 7, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:34:19');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367635, '机器人供应商关系表查看', 'admin_bizrobotsupplier_get', NULL, 1686920367634, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367636, '机器人供应商关系表新增', 'admin_bizrobotsupplier_add', NULL, 1686920367634, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367637, '机器人供应商关系表修改', 'admin_bizrobotsupplier_edit', NULL, 1686920367634, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367638, '机器人供应商关系表删除', 'admin_bizrobotsupplier_del', NULL, 1686920367634, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367648, '供应商表管理', '', '/admin/bizsupplier/index', 1669705907359711234, 'icon-yonghuguanli', 4, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:33:28');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367649, '供应商表查看', 'admin_bizsupplier_get', NULL, 1686920367648, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367650, '供应商表新增', 'admin_bizsupplier_add', NULL, 1686920367648, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367651, '供应商表修改', 'admin_bizsupplier_edit', NULL, 1686920367648, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367652, '供应商表删除', 'admin_bizsupplier_del', NULL, 1686920367648, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367661, '汽车品牌管理', '', '/admin/bizcarbrand/index', 1669705907359711234, 'icon-lingpaiguanli', 1, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:32:39');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367662, '汽车品牌查看', 'admin_bizcarbrand_get', NULL, 1686920367661, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367663, '汽车品牌新增', 'admin_bizcarbrand_add', NULL, 1686920367661, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367664, '汽车品牌修改', 'admin_bizcarbrand_edit', NULL, 1686920367661, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1686920367665, '汽车品牌删除', 'admin_bizcarbrand_del', NULL, 1686920367661, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1669705907359711234, '业务中心', 'admin_biz_root', '/biz', -1, 'icon-wangzhan', 1, '0', '0', '0', NULL, '2023-06-19 16:15:54', 'admin', '2023-06-19 16:31:19');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1671436062888046594, '在线加密', NULL, 'https://enc.pig4cloud.com/', -1, 'icon-wangzhan', 1000, '0', '0', '0', 'admin', '2023-06-21 16:33:06', 'admin', '2023-06-21 16:33:27');
INSERT INTO `pig`.`sys_menu`(`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort_order`, `keep_alive`, `type`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1671436500572057602, 'pig文档', NULL, 'https://www.yuque.com/pig4cloud/pig', -1, 'icon-tubiao1', 1001, '0', '0', '0', 'admin', '2023-06-21 16:34:50', 'admin', '2023-06-21 16:34:50');


/*
 Navicat Premium Data Transfer

 Source Server         : excar
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 47.98.142.48:3306
 Source Schema         : pig

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 06/07/2023 21:32:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_buyer
-- ----------------------------
DROP TABLE IF EXISTS `biz_buyer`;
CREATE TABLE `biz_buyer`  (
                              `id` bigint(20) NOT NULL COMMENT '主键id',
                              `client_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商家API对接key',
                              `client_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商家API对接secret',
                              `aec_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '加密秘钥',
                              `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '采购商名称',
                              `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '注册手机号',
                              `director_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人名称',
                              `director_mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人联系方式',
                              `dailyLimit_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '每日限单数量',
                              `status` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态:正常/禁用/已过期',
                              `validity_start` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '有效期开始日期',
                              `validity_end` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '有效期结束日期',
                              `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '备注',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                              `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                              `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_buyer
-- ----------------------------
INSERT INTO `biz_buyer` VALUES (1673221326845882369, 'bdb2eb1d0f874391b4a9ad01a338e23f', '5ca25422be6c445f9b9ce8d0eb14a5be', 'ZbkPAvetmKkysONQ4MZMZxrYNraTxnOEgGBJx/bs5mk=', '二郎神', '15111111111', '方世玉', '15168521254', 100, 1, '2021-01-01 00:00:00', '2024-08-31 08:00:00', '', '2023-06-26 14:47:06', 'admin', '2023-07-06 21:04:55', 'admin');
INSERT INTO `biz_buyer` VALUES (1673258060681965569, 'e82d1f9db1da4c8a96c3a30d3a717975', '9dce4033876a498391cb3ba925652727', 'ab4NVfapmaho4uNS4MtBMmIHgHyVb+yEvc1ZaRE0ook=', 'huyuyhh', '15163254685', 'bhujyhuu', '', 50, 1, '2023-06-26 00:00:00', '2024-06-28 00:00:00', '', '2023-06-26 17:13:04', 'admin', '2023-06-26 17:13:04', 'admin');

-- ----------------------------
-- Table structure for biz_buyer_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_buyer_order`;
CREATE TABLE `biz_buyer_order`  (
                                    `id` bigint(20) NOT NULL COMMENT '主键id',
                                    `buyer_id` bigint(20) NOT NULL COMMENT '采购商Id',
                                    `buyer_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '采购商名称',
                                    `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商ID',
                                    `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商名称',
                                    `car_brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
                                    `car_brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '品牌名称',
                                    `robot_id` bigint(20) NULL DEFAULT NULL COMMENT '机器人ID',
                                    `request_ip_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求IP地址信息',
                                    `request_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '请求时间',
                                    `request_params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求参数',
                                    `request_header` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求header信息',
                                    `vin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'VIN码',
                                    `engine_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发动机号',
                                    `callback_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '回调地址',
                                    `request_status` int(10) NULL DEFAULT NULL COMMENT '结果状态码：1：下单成功，2：下单失败，3：回调成功，4：回调失败',
                                    `failure_reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '失败原因',
                                    `result` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '查询成功结果',
                                    `callback_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '回调时间',
                                    `spend_time` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '订单消耗时长',
                                    `retry_count` int(11) NOT NULL COMMENT '重试次数',
                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购商订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_buyer_order
-- ----------------------------
INSERT INTO `biz_buyer_order` VALUES (581420953, 1673221326845882369, '二郎神', NULL, NULL, 1673214908193419266, '上汽通用雪佛兰', NULL, '0:0:0:0:0:0:0:1', '2023-07-06 21:30:08', '{\"brand\":\"上汽通用雪佛兰\",\"call_back_url\":\"www.baidu.com\",\"engine_code\":\"\",\"vin\":\"LSGGG54Y2DS083730\"}', '{\"content-length\":\"138\",\"host\":\"localhost:4001\",\"accesskeysecret\":\"ZbkPAvetmKkysONQ4MZMZxrYNraTxnOEgGBJx/bs5mk=\",\"content-type\":\"application/json\",\"connection\":\"keep-alive\",\"accept-encoding\":\"gzip, deflate, br\",\"accesskeyid\":\"bdb2eb1d0f874391b4a9ad01a338e23f\",\"user-agent\":\"Apifox/1.0.0 (https://apifox.com)\",\"accept\":\"*/*\"}', 'LSGGG54Y2DS083730', '', 'www.baidu.com', 0, NULL, NULL, '2023-07-06 13:30:07', NULL, 1, 'api管理员', '2023-07-06 21:30:08', '2023-07-06 21:30:13', NULL);

-- ----------------------------
-- Table structure for biz_car_brand
-- ----------------------------
DROP TABLE IF EXISTS `biz_car_brand`;
CREATE TABLE `biz_car_brand`  (
                                  `id` bigint(20) NOT NULL COMMENT '主键id',
                                  `brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌',
                                  `letter` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌首字母',
                                  `logo_url` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌logo地址',
                                  `wmi` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'wmi即vin码前三位',
                                  `type` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '类型 10:国产,20:合资',
                                  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '汽车品牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_car_brand
-- ----------------------------
INSERT INTO `biz_car_brand` VALUES (1673214908193419266, '上汽通用雪佛兰', '雪佛兰', '', '', 20, 'admin', '2023-06-26 14:21:36', '2023-07-06 21:30:04', 'admin');

-- ----------------------------
-- Table structure for biz_car_brand_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_car_brand_supplier`;
CREATE TABLE `biz_car_brand_supplier`  (
                                           `id` bigint(20) NOT NULL COMMENT '主键id',
                                           `car_brand_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '品牌id',
                                           `supplier_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商id',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '品牌供应商关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_car_brand_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for biz_robot
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot`;
CREATE TABLE `biz_robot`  (
                              `id` bigint(20) NOT NULL COMMENT '主键id',
                              `status` bit(1) NOT NULL COMMENT '状态:启用/禁用',
                              `robot_url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '机器人访问地址',
                              `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'host',
                              `robot_proxies` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'robotProxies',
                              `robot_account_password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '账号-密码（多个）',
                              `robot_proxies_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '代理名称',
                              `need_dynamic_proxy` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否需要代理 0；关闭 1，开启',
                              `test_vin` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '测试VIN码',
                              `service_start_time` time(0) NULL DEFAULT NULL COMMENT '机器人服务开始时间',
                              `service_end_time` time(0) NULL DEFAULT NULL COMMENT '机器人服务结束时间',
                              `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                              `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                              `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_robot
-- ----------------------------
INSERT INTO `biz_robot` VALUES (1673222204495605761, b'1', 'http://localhosy:8888/bieke', NULL, 'http://excar:excar@47.98.142.48:9505', 'admin/yes', '本地代理', 1, 'LSGGG54Y2DS083730', '08:30:00', '18:00:00', 'admin', '2023-06-26 14:50:35', '2023-07-06 20:39:52', 'admin');

-- ----------------------------
-- Table structure for biz_robot_query_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot_query_record`;
CREATE TABLE `biz_robot_query_record`  (
                                           `id` bigint(20) NOT NULL COMMENT '主键id',
                                           `vin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'vin',
                                           `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
                                           `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商名称',
                                           `robot_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '机器人id',
                                           `result_status` int(10) UNSIGNED NOT NULL COMMENT '结果状态码：0：失败，1：成功，2：无结果',
                                           `failure_reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '失败原因',
                                           `result` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '成功结果',
                                           `queryTime` datetime(0) NULL DEFAULT NULL COMMENT '查询时间',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人查询记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_robot_query_record
-- ----------------------------

-- ----------------------------
-- Table structure for biz_robot_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot_supplier`;
CREATE TABLE `biz_robot_supplier`  (
                                       `id` bigint(20) NOT NULL COMMENT '主键id',
                                       `robot_id` bigint(20) NOT NULL COMMENT '机器人id',
                                       `supplier_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商id',
                                       `sort` int(11) NOT NULL DEFAULT 1 COMMENT '排序(权重)',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                       `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人供应商关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_robot_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for biz_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_supplier`;
CREATE TABLE `biz_supplier`  (
                                 `id` bigint(20) NOT NULL COMMENT '主键id',
                                 `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商名称',
                                 `director_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人姓名',
                                 `contact_mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '联系方式',
                                 `daily_limit_count` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '每日限单量',
                                 `weight` int(5) NULL DEFAULT NULL COMMENT '权重',
                                 `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0；关闭 1，开启',
                                 `logo` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商logo',
                                 `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_supplier
-- ----------------------------
INSERT INTO `biz_supplier` VALUES (1673215521878814721, '雪佛兰', '黄飞鸿', '15111111111', 100, NULL, 1, '', 'admin', '2023-06-26 14:24:02', '2023-07-06 20:33:01', 'admin');
INSERT INTO `biz_supplier` VALUES (1673261932368482306, '唐伯虎', '祝枝山', '15167627892', 100, NULL, 1, '', 'admin', '2023-06-26 17:28:27', '2023-06-26 17:28:27', 'admin');
INSERT INTO `biz_supplier` VALUES (1673262701192794114, '赵子龙', '刘备', '15111324521', 50, NULL, 0, '', 'admin', '2023-06-26 17:31:30', '2023-06-26 17:31:30', 'admin');

-- ----------------------------
-- Table structure for biz_vin_parsing
-- ----------------------------
DROP TABLE IF EXISTS `biz_vin_parsing`;
CREATE TABLE `biz_vin_parsing`  (
                                    `id` bigint(20) NOT NULL,
                                    `vin_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
                                    `brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '品牌',
                                    `sub_brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '厂商名称',
                                    `doctor_brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '查博士品牌',
                                    `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '三方内容',
                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_vin_parsing
-- ----------------------------
INSERT INTO `biz_vin_parsing` VALUES (498976170, 'LSGGG54Y2DS083730', '雪佛兰', '上汽通用雪佛兰', NULL, '{\"amVehicleId\":\"45E1A59E3ACDFA01E050A8C077507226\",\"bodyType\":\"三厢车\",\"factoryCode\":\"AUTO0135\",\"cnHorsePower\":\"154.47\",\"cnPower\":\"113\",\"amMainBrandName\":\"雪佛兰\",\"amBrandName\":\"上汽通用雪佛兰\",\"amBrandId\":\"4028d00c57bd37430157e697c9240282\",\"purchasePrice\":\"144900\",\"seats\":\"5\",\"countriesName\":\"美国\",\"vehCateTwoNames\":\"中型轿车(B)\",\"absFlag\":\"有\",\"price\":\"169900\",\"importFlag\":\"合资\",\"publicationNos\":\"SGM7209ATA\",\"amMainBrandId\":\"4028d00c57bd37430157e694e7290281\",\"displacement\":\"2.0\",\"marketDate\":\"2013-02-18\",\"frontTyreSize\":\"225/55 R17\",\"arrayType\":\"L\",\"amVinYear\":\"2013\",\"amGroupName\":\"(八代)迈锐宝 三厢(12.02-15.10)\",\"factoryName\":\"上汽通用雪佛兰\",\"amGroupId\":\"4028d00c5bf226e6015c43eadab913f2\",\"vehCateOneNames\":\"轿车\",\"uploadDate\":\"2013-05-30\",\"airIntakeType\":\"自然吸气\",\"gearboxType\":\"手自一体\",\"fullWeight\":\"1565\",\"amYear\":\"2013\",\"bigGroupGeneration\":\"八代\",\"engineDesc\":\"2.0L\",\"powerType\":\"汽油\",\"fuelJetType\":\"多点电喷\",\"wheelBase\":\"2737\",\"cnDisplacement\":\"1998\",\"rearTyreSize\":\"225/55 R17\",\"amVehicleName\":\"2013款 2.0L 自动 舒适版\",\"drivenType\":\"前置前驱\",\"doorNum\":\"四门\",\"valveNum\":\"4\",\"vehicleSize\":\"4869*1854*1472\",\"power\":\"113\",\"trackFront\":\"1585\",\"roz\":\"93号(京92号)\",\"vinSource\":\"实码解析\",\"vehicleColor\":\"元黑\",\"engineModel\":\"LTD\",\"amSeriesName\":\"迈锐宝\",\"effluentStandard\":\"国IV(国V)\",\"trackRear\":\"1587\",\"gearNum\":\"6\",\"cfgLevel\":\"舒适版\",\"vehCateNames\":\"乘用车\",\"amSeriesId\":\"4028d00c57bd37430157ff6efd1b02e0\",\"stopDate\":\"2014-03-03\"}', 'api管理员', '2023-07-06 21:19:08', '2023-07-06 21:19:08', NULL);

SET FOREIGN_KEY_CHECKS = 1;
