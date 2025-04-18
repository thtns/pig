DROP DATABASE IF EXISTS `pig`;

CREATE DATABASE  `pig` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

USE `pig`;

-- ----------------------------
-- Table structure for biz_buyer
-- ----------------------------
DROP TABLE IF EXISTS `biz_buyer`;
CREATE TABLE `biz_buyer`  (
                              `id` bigint(0) NOT NULL COMMENT '主键id',
                              `client_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商家API对接key',
                              `client_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商家API对接secret',
                              `aec_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '加密秘钥',
                              `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '采购商名称',
                              `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '注册手机号',
                              `director_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人名称',
                              `director_mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人联系方式',
                              `dailyLimit_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '每日限单数量',
                              `status` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态:正常/禁用/已过期',
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
INSERT INTO `biz_buyer` VALUES (1001, 'bdb2eb1d0f874391b4a9ad01a338e23f', '5ca25422be6c445f9b9ce8d0eb14a5be', 'ZbkPAvetmKkysONQ4MZMZxrYNraTxnOEgGBJx/bs5mk=', '查博士', '15111111111', '查博士', '15168521254', 1000000, 1, '2021-01-01 00:00:00', '2024-08-31 08:00:00', '', '2023-06-26 14:47:06', 'admin', '2024-12-16 10:08:08', 'admin');
INSERT INTO `biz_buyer` VALUES (1701852172223709185, '15823ffae51042938263930a346771db', '9dce4033876a498391cb3ba925652727', 'ZbgMAvH4yP804rcA4JFMNFMxAt+jmpYHx3xH5Rj7Dgw=', '客户-D', '15111111111', 'D', '15111111111', 1000000, 1, '2024-12-14 10:29:55', '2024-12-14 10:29:55', '1', NULL, NULL, '2024-12-16 10:21:30', 'admin');

-- ----------------------------
-- Table structure for biz_buyer_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_buyer_order`;
CREATE TABLE `biz_buyer_order`  (
                                    `id` bigint(0) NOT NULL COMMENT '主键id',
                                    `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '三方订单编号',
                                    `buyer_id` bigint(0) NULL DEFAULT NULL COMMENT '采购商Id',
                                    `buyer_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '采购商名称',
                                    `supplier_id` bigint(0) NULL DEFAULT NULL COMMENT '供应商ID',
                                    `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商名称',
                                    `car_brand_id` bigint(0) NULL DEFAULT NULL COMMENT '品牌id',
                                    `car_brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '品牌名称',
                                    `manufacturer` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '厂商名称',
                                    `robot_id` bigint(0) NULL DEFAULT NULL COMMENT '机器人ID',
                                    `request_ip_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求IP地址信息',
                                    `request_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '请求时间',
                                    `request_params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求参数',
                                    `request_header` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求header信息',
                                    `vin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'VIN码',
                                    `engine_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发动机号',
                                    `callback_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回调地址',
                                    `order_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单类型 区分订单客户类型',
                                    `request_status` int(0) NULL DEFAULT NULL COMMENT '结果状态码：1：下单成功，2：下单失败，3：回调成功，4：回调失败',
                                    `any_data` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否有记录 true 有记录 false 无记录',
                                    `failure_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '失败原因',
                                    `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '查询成功结果',
                                    `callback_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '回调时间',
                                    `spend_time` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '订单消耗时长',
                                    `retry_count` int(0) NULL DEFAULT NULL COMMENT '重试次数',
                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_requestTime`(`request_time`) USING BTREE,
                                    INDEX `idx_vin`(`vin`) USING BTREE,
                                    INDEX `idx_requestStatus`(`request_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购商订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_buyer_order
-- ----------------------------

-- ----------------------------
-- Table structure for biz_car_brand
-- ----------------------------
DROP TABLE IF EXISTS `biz_car_brand`;
CREATE TABLE `biz_car_brand`  (
                                  `id` bigint(0) NOT NULL COMMENT '主键id',
                                  `brand` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌',
                                  `manufacturer` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '厂商名称',
                                  `letter` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌首字母',
                                  `logo_url` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '车辆品牌logo地址',
                                  `wmi` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'wmi即vin码前三位',
                                  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '类型 10:国产,20:合资',
                                  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '汽车品牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_car_brand
-- ----------------------------
INSERT INTO `biz_car_brand` VALUES (1673214908193419266, '雪佛兰', '上汽通用雪佛兰', '上汽通用雪佛兰', '', '', '20', 'admin', '2023-06-26 14:21:36', '2023-09-13 15:17:13', 'admin');
INSERT INTO `biz_car_brand` VALUES (1677650895803793410, '别克', '上汽通用别克', '上汽通用别克', '', '', '20', 'admin', '2023-07-08 20:08:37', '2023-09-13 15:17:41', 'admin');
INSERT INTO `biz_car_brand` VALUES (1678024811844005890, '凯迪拉克', '上汽通用凯迪拉克', '上汽通用凯迪拉克', '', '', '20', 'admin', '2023-07-09 20:54:26', '2023-09-13 15:17:23', 'admin');
INSERT INTO `biz_car_brand` VALUES (1678647702161162242, '捷豹', '奇瑞捷豹', '奇瑞捷豹', '', '', '20', 'admin', '2023-07-11 14:09:35', '2023-09-13 15:17:48', 'admin');
INSERT INTO `biz_car_brand` VALUES (1679677729313558529, '路虎', '奇瑞路虎', '奇瑞路虎', '', '', '20', 'admin', '2023-07-14 10:22:32', '2023-09-13 15:17:53', 'admin');
INSERT INTO `biz_car_brand` VALUES (1682269203439980545, '奥迪', '奥迪', 'audi', '', '', '20', 'admin', '2023-07-21 14:00:08', '2023-09-13 15:18:18', 'admin');
INSERT INTO `biz_car_brand` VALUES (1682269756945502209, '大众', '一汽大众', '一汽大众', '', '', '20', 'admin', '2023-07-21 14:02:20', '2023-08-02 14:38:31', 'admin');
INSERT INTO `biz_car_brand` VALUES (1682273407705452545, '现代', '北京现代', '北京现代', '', '', '20', 'admin', '2023-07-21 14:16:50', '2023-09-13 15:18:08', 'admin');
INSERT INTO `biz_car_brand` VALUES (1686627682996895745, '大众', '上海大众', 'saic', '', '', '20', 'admin', '2023-08-02 14:39:10', '2023-08-02 17:25:01', 'admin');
INSERT INTO `biz_car_brand` VALUES (1686631300634427394, '奇瑞捷豹', '奇瑞捷豹路虎1', '奇瑞捷豹', '', '', '20', 'admin', '2023-08-02 14:53:33', '2023-08-02 17:24:53', 'admin');
INSERT INTO `biz_car_brand` VALUES (1688784774801301505, '丰田', '一汽丰田', '丰田', '', '', '20', 'admin', '2023-08-08 13:30:41', '2023-08-29 17:40:24', 'admin');
INSERT INTO `biz_car_brand` VALUES (1692411185877393410, '五菱', '上汽通用五菱', '五菱', '', '', '20', 'admin', '2023-08-18 13:40:45', '2023-08-29 17:40:10', 'admin');
INSERT INTO `biz_car_brand` VALUES (1693136390463594497, '丰田', '广汽丰田', '丰田', '', '', '20', 'admin', '2023-08-20 13:42:27', '2023-08-29 17:40:01', 'admin');
INSERT INTO `biz_car_brand` VALUES (1693526472790024194, '丰田', '进口丰田', '丰田', '', '', '20', 'admin', '2023-08-21 15:32:30', '2023-08-29 17:39:52', 'admin');
INSERT INTO `biz_car_brand` VALUES (1693856509485555713, '吉利', '吉利', '吉利帝豪,吉利全球鹰,吉利英伦,吉利汽车', '', '', '20', 'admin', '2023-08-22 13:23:57', '2023-08-22 15:54:53', 'admin');
INSERT INTO `biz_car_brand` VALUES (1695241290689454081, '宝骏', '上汽通用五菱', '五菱', '', '', '20', 'admin', '2023-08-26 09:06:34', '2025-03-11 10:47:18', 'admin');
INSERT INTO `biz_car_brand` VALUES (1695298707427074049, '威麟', '奇瑞汽车', '奇瑞', '', '', '20', 'admin', '2023-08-26 12:54:44', '2023-08-29 17:27:42', 'admin');
INSERT INTO `biz_car_brand` VALUES (1695978378217078785, '新宝骏', '上汽通用五菱', '五菱', '', '', '20', 'admin', '2023-08-28 09:55:30', '2023-08-29 17:27:39', 'admin');
INSERT INTO `biz_car_brand` VALUES (1698675801678872577, '沃尔沃', '沃尔沃', '沃尔沃', '', '', '20', 'admin', '2023-09-04 20:34:06', '2023-09-13 15:18:34', 'admin');
INSERT INTO `biz_car_brand` VALUES (1702624994764193794, '长安', '长安', '', '', '', '20', 'admin', '2023-09-15 18:06:47', '2025-01-12 12:23:17', 'admin');
INSERT INTO `biz_car_brand` VALUES (1702955624853078017, '长安(跨越)', '长安汽车', '', '', '', '0', 'admin', '2023-09-16 16:00:35', '2023-09-16 16:00:35', 'admin');
INSERT INTO `biz_car_brand` VALUES (1704403352879562754, '荣威', '上汽荣威', '', '', '', '0', 'admin', '2023-09-20 15:53:20', '2023-09-20 15:53:20', 'admin');
INSERT INTO `biz_car_brand` VALUES (1704403392045973506, '名爵', '上汽名爵', '', '', '', '0', 'admin', '2023-09-20 15:53:30', '2023-09-20 15:53:30', 'admin');
INSERT INTO `biz_car_brand` VALUES (1723156543406276609, '大众', '上汽大众', '第一车网叫上汽 茶博士叫上海 做区分', '', '', '20', 'admin', '2023-11-11 09:51:49', '2023-11-11 09:52:24', 'admin');
INSERT INTO `biz_car_brand` VALUES (1780124440850239489, '吉利汽车', '吉利汽车', '', '', '', '0', 'admin', '2024-04-16 14:41:54', '2024-04-16 14:41:54', 'admin');
INSERT INTO `biz_car_brand` VALUES (1795044712661291010, '标致', '东风标致', '', '', '', '0', 'admin', '2024-05-27 18:49:44', '2024-05-28 09:44:25', 'admin');
INSERT INTO `biz_car_brand` VALUES (1795081873326510082, '雪铁龙', '东风雪铁龙', '', '', '', '0', 'admin', '2024-05-27 21:17:24', '2024-05-28 09:44:29', 'admin');
INSERT INTO `biz_car_brand` VALUES (1832741450733752321, '福特', '福特', '', '', '', '20', 'admin', '2024-09-08 19:23:07', '2024-09-08 19:23:19', 'admin');
INSERT INTO `biz_car_brand` VALUES (1855163692746706946, 'Jeep', '吉普', '', '', '', '20', 'admin', '2024-11-09 16:21:06', '2024-11-09 16:49:07', 'admin');
INSERT INTO `biz_car_brand` VALUES (1867834376270635010, '本田', '东风本田', '', '', '', '0', 'admin', '2024-12-14 15:29:53', '2024-12-14 16:21:01', 'admin');
INSERT INTO `biz_car_brand` VALUES (1897931653211217921, '几何', '吉利几何', '', '', '', '20', 'admin', '2025-03-07 16:45:43', '2025-03-07 16:45:59', 'admin');

-- ----------------------------
-- Table structure for biz_car_brand_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_car_brand_supplier`;
CREATE TABLE `biz_car_brand_supplier`  (
                                           `id` bigint(0) NOT NULL COMMENT '主键id',
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
INSERT INTO `biz_car_brand_supplier` VALUES (1682210993530114050, '1673214908193419266', '1673215521878814721', 'admin', '2023-07-21 10:08:49', '2023-07-21 10:08:58', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682267799044395009, '1673214908193419266', '1678352264958775298', 'admin', '2023-07-21 13:54:33', '2023-07-21 13:54:49', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682267824109555714, '1673214908193419266', '1682266492447395841', 'admin', '2023-07-21 13:54:39', '2023-07-21 13:55:13', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682268217715625986, '1678647702161162242', '1678647802358890497', 'admin', '2023-07-21 13:56:13', '2023-07-21 13:59:47', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682269079796092929, '1679677729313558529', '1678647802358890497', 'admin', '2023-07-21 13:59:38', '2023-07-21 13:59:54', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682273368115417089, '1682273407705452545', '1682267106409615361', 'admin', '2023-07-21 14:16:41', '2023-07-21 14:16:55', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682273484792565761, '1678024811844005890', '1682267437243731969', 'admin', '2023-07-21 14:17:09', '2023-07-21 14:17:25', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682284346198626305, '1682269756945502209', '1682266976130338817', 'admin', '2023-07-21 15:00:18', '2023-07-21 15:00:32', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383264248926209, '1673214908193419266', '1682267437243731969', 'admin', '2023-07-21 21:33:22', '2023-07-21 21:33:46', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383439008796673, '1677650895803793410', '1673215521878814721', 'admin', '2023-07-21 21:34:04', '2023-07-21 21:34:23', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383449477779457, '1677650895803793410', '1678352264958775298', 'admin', '2023-07-21 21:34:06', '2023-07-21 21:34:31', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383472194129922, '1677650895803793410', '1682266492447395841', 'admin', '2023-07-21 21:34:12', '2023-07-21 21:34:47', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383484785430529, '1677650895803793410', '1682267437243731969', 'admin', '2023-07-21 21:34:15', '2023-07-21 21:34:56', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383718529798145, '1678024811844005890', '1673215521878814721', 'admin', '2023-07-21 21:35:10', '2023-07-21 21:35:44', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383729942499330, '1678024811844005890', '1678352264958775298', 'admin', '2023-07-21 21:35:13', '2023-07-21 21:35:50', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1682383752470106113, '1678024811844005890', '1682266492447395841', 'admin', '2023-07-21 21:35:18', '2023-07-21 21:36:21', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1685515148956983297, '1682269756945502209', '1685514983349084162', 'admin', '2023-07-30 12:58:22', '2023-07-30 12:58:22', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1686627764378976257, '1686627682996895745', '1686621886137425922', 'admin', '2023-08-02 14:39:30', '2023-08-02 14:39:30', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1686631404854493186, '1686631300634427394', '1678647802358890497', 'admin', '2023-08-02 14:53:58', '2023-08-02 14:53:58', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1688784892401197058, '1688784774801301505', '1686311884671606786', 'admin', '2023-08-08 13:31:09', '2023-08-08 13:31:09', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1690676550508126210, '1686627682996895745', '1690676399609651201', 'qgadmin', '2023-08-13 18:47:56', '2023-08-13 18:47:56', 'qgadmin');
INSERT INTO `biz_car_brand_supplier` VALUES (1692107039026618369, '1686627682996895745', '1692106883334053889', 'admin', '2023-08-17 17:32:11', '2023-08-17 17:32:11', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1692411372280651777, '1692411185877393410', '1692411319231094785', 'admin', '2023-08-18 13:41:29', '2023-08-18 13:41:29', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1693137346462916609, '1693136390463594497', '1693137225776013314', 'admin', '2023-08-20 13:46:15', '2023-08-20 13:46:15', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1693856571041161218, '1693856509485555713', '1693856304992264194', 'admin', '2023-08-22 13:24:12', '2023-08-22 13:24:12', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1695241496088715265, '1695241290689454081', '1692411319231094785', 'admin', '2023-08-26 09:07:23', '2023-08-26 09:07:23', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1695281665365942273, '1682269203439980545', '1695281566665580545', 'admin', '2023-08-26 11:47:00', '2023-08-26 11:47:00', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1695978411301748738, '1695978378217078785', '1692411319231094785', 'admin', '2023-08-28 09:55:38', '2023-08-28 09:55:38', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1697091203790487554, '1682269756945502209', '1697091054385184769', 'admin', '2023-08-31 11:37:28', '2023-08-31 11:37:28', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1698676155480997890, '1698675801678872577', '1698675848885764098', 'admin', '2023-09-04 20:35:30', '2023-09-04 20:35:30', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1698978957763739650, '1673214908193419266', '1698978101765013506', 'admin', '2023-09-05 16:38:44', '2023-09-05 16:38:44', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1698979016416886785, '1677650895803793410', '1698978101765013506', 'admin', '2023-09-05 16:38:58', '2023-09-05 16:38:58', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1698979043965075458, '1678024811844005890', '1698978101765013506', 'admin', '2023-09-05 16:39:04', '2023-09-05 16:39:04', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1699245921983725570, '1673214908193419266', '1699245332033896450', 'admin', '2023-09-06 10:19:33', '2023-09-06 10:19:33', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1699245941743091713, '1677650895803793410', '1699245332033896450', 'admin', '2023-09-06 10:19:38', '2023-09-06 10:19:38', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1699245967185739778, '1678024811844005890', '1699245332033896450', 'admin', '2023-09-06 10:19:44', '2023-09-06 10:19:44', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1704403422630838273, '1704403352879562754', '1704403210122231809', 'admin', '2023-09-20 15:53:37', '2023-09-20 15:53:37', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1704403445129084929, '1704403392045973506', '1704403210122231809', 'admin', '2023-09-20 15:53:42', '2023-09-20 15:53:42', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1713851639902699521, '1693136390463594497', '1713851581379575810', 'admin', '2023-10-16 17:37:27', '2023-10-16 17:37:27', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1714143731459751938, '1693526472790024194', '1693137225776013314', 'admin', '2023-10-17 12:58:07', '2023-10-17 12:58:07', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1719963511001382913, '1688784774801301505', '1719960264522006530', 'admin', '2023-11-02 14:23:51', '2023-11-02 14:23:51', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1723156936362229761, '1723156543406276609', '1686621886137425922', 'admin', '2023-11-11 09:53:23', '2023-11-11 09:53:23', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1723156977462214658, '1723156543406276609', '1690676399609651201', 'admin', '2023-11-11 09:53:33', '2023-11-11 09:53:33', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1723157025981923329, '1723156543406276609', '1692106883334053889', 'admin', '2023-11-11 09:53:44', '2023-11-11 09:53:44', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1727252001963573249, '1673214908193419266', '1727251894895575041', 'admin', '2023-11-22 17:05:43', '2023-11-22 17:05:43', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1727252023400660994, '1677650895803793410', '1727251894895575041', 'admin', '2023-11-22 17:05:48', '2023-11-22 17:05:48', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1727252047928950786, '1678024811844005890', '1727251894895575041', 'admin', '2023-11-22 17:05:54', '2023-11-22 17:05:54', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1729029748801077249, '1702624994764193794', '1729029681868374017', 'admin', '2023-11-27 14:49:51', '2023-11-27 14:49:51', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1749357488230010881, '1693526472790024194', '1713851581379575810', 'admin', '2024-01-22 17:05:01', '2024-01-22 17:05:01', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1750042369297649665, '1749304536182198273', '1685514983349084162', 'admin', '2024-01-24 14:26:30', '2024-01-24 14:26:30', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1771406435085619201, '1682269203439980545', '1771404211743789057', 'admin', '2024-03-23 13:19:40', '2024-03-23 13:19:40', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1780124643430928385, '1780124440850239489', '1693856304992264194', 'admin', '2024-04-16 14:42:43', '2024-04-16 14:42:43', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1788764386397949953, '1673214908193419266', '1788764194357547009', 'admin', '2024-05-10 10:53:58', '2024-05-10 10:53:58', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1788764413207941121, '1677650895803793410', '1788764194357547009', 'admin', '2024-05-10 10:54:04', '2024-05-10 10:54:04', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1788764440932290562, '1678024811844005890', '1788764194357547009', 'admin', '2024-05-10 10:54:11', '2024-05-10 10:54:11', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1789207569166667778, '1682269203439980545', '1789206256483733505', 'admin', '2024-05-11 16:15:01', '2024-05-11 16:15:01', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1790611113950220289, '1673214908193419266', '1790610742653652993', 'admin', '2024-05-15 13:12:12', '2024-05-15 13:12:12', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1790611142307909633, '1677650895803793410', '1790610742653652993', 'admin', '2024-05-15 13:12:19', '2024-05-15 13:12:19', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1790611163816300546, '1678024811844005890', '1790610742653652993', 'admin', '2024-05-15 13:12:24', '2024-05-15 13:12:24', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1795045426447945729, '1795044712661291010', '1795044758647640065', 'admin', '2024-05-27 18:52:35', '2024-05-27 18:52:35', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1795082841690640386, '1795081873326510082', '1795082049122373633', 'admin', '2024-05-27 21:21:15', '2024-05-27 21:21:15', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1797897458678665218, '1673214908193419266', '1797896567514894337', 'admin', '2024-06-04 15:45:32', '2024-06-04 15:45:32', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1797897484079370242, '1677650895803793410', '1797896567514894337', 'admin', '2024-06-04 15:45:38', '2024-06-04 15:45:38', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1797897505357074433, '1678024811844005890', '1797896567514894337', 'admin', '2024-06-04 15:45:43', '2024-06-04 15:45:43', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1798244222459875330, '1673214908193419266', '1798243708942848002', 'admin', '2024-06-05 14:43:27', '2024-06-05 14:43:27', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1798244241585901569, '1677650895803793410', '1798243708942848002', 'admin', '2024-06-05 14:43:32', '2024-06-05 14:43:32', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1798244262217682945, '1678024811844005890', '1798243708942848002', 'admin', '2024-06-05 14:43:37', '2024-06-05 14:43:37', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799047531294789633, '1673214908193419266', '1799047189580648450', 'admin', '2024-06-07 19:55:31', '2024-06-07 19:55:31', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799047552270503938, '1677650895803793410', '1799047189580648450', 'admin', '2024-06-07 19:55:36', '2024-06-07 19:55:36', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799047576798793729, '1678024811844005890', '1799047189580648450', 'admin', '2024-06-07 19:55:42', '2024-06-07 19:55:42', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799048709814521858, '1673214908193419266', '1799048104417071105', 'admin', '2024-06-07 20:00:12', '2024-06-07 20:00:12', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799048730324668417, '1677650895803793410', '1799048104417071105', 'admin', '2024-06-07 20:00:17', '2024-06-07 20:00:17', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1799048811937435649, '1678024811844005890', '1799048104417071105', 'admin', '2024-06-07 20:00:36', '2024-06-07 20:00:36', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1800793699506884610, '1682269203439980545', '1800793591096709122', 'admin', '2024-06-12 15:34:10', '2024-06-12 15:34:10', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1807701773970739202, '1673214908193419266', '1807694669931253761', 'admin', '2024-07-01 17:04:23', '2024-07-01 17:04:23', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1807701813594329090, '1677650895803793410', '1807694669931253761', 'admin', '2024-07-01 17:04:32', '2024-07-01 17:04:32', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1807701850122522625, '1678024811844005890', '1807694669931253761', 'admin', '2024-07-01 17:04:41', '2024-07-01 17:04:41', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1809820309497221122, '1673214908193419266', '1809819906252640257', 'admin', '2024-07-07 13:22:41', '2024-07-07 13:22:41', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1809820328304480257, '1677650895803793410', '1809819906252640257', 'admin', '2024-07-07 13:22:46', '2024-07-07 13:22:46', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1809820350010003457, '1678024811844005890', '1809819906252640257', 'admin', '2024-07-07 13:22:51', '2024-07-07 13:22:51', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1811597545413971969, '1693526472790024194', '1719960264522006530', 'admin', '2024-07-12 11:04:47', '2024-07-12 11:04:47', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1830777255930793986, '1673214908193419266', '1830776228649275393', 'admin', '2024-09-03 09:18:07', '2024-09-03 09:18:07', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1830777274893242369, '1677650895803793410', '1830776228649275393', 'admin', '2024-09-03 09:18:11', '2024-09-03 09:18:11', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1830777297689284610, '1678024811844005890', '1830776228649275393', 'admin', '2024-09-03 09:18:17', '2024-09-03 09:18:17', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1831146795508338690, '1673214908193419266', '1831146495963729921', 'admin', '2024-09-04 09:46:32', '2024-09-04 09:46:32', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1831146814634364929, '1677650895803793410', '1831146495963729921', 'admin', '2024-09-04 09:46:36', '2024-09-04 09:46:36', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1831146837363298305, '1678024811844005890', '1831146495963729921', 'admin', '2024-09-04 09:46:42', '2024-09-04 09:46:42', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1832741686604632065, '1832741450733752321', '1832741070289408002', 'admin', '2024-09-08 19:24:03', '2024-09-08 19:24:03', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1855164125187837953, '1855163692746706946', '1855163770102255617', 'admin', '2024-11-09 16:22:49', '2024-11-09 16:22:49', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1856964821595885570, '1673214908193419266', '1856964446864183297', 'admin', '2024-11-14 15:38:09', '2024-11-14 15:38:09', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1856964842533851137, '1677650895803793410', '1856964446864183297', 'admin', '2024-11-14 15:38:14', '2024-11-14 15:38:14', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1856964863496982529, '1678024811844005890', '1856964446864183297', 'admin', '2024-11-14 15:38:19', '2024-11-14 15:38:19', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1867835334388080641, '1867834376270635010', '1867834481694466050', 'admin', '2024-12-14 15:33:41', '2024-12-14 15:33:41', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1895355171251150850, '1673214908193419266', '1895354918074572802', 'admin', '2025-02-28 14:07:41', '2025-02-28 14:07:41', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1895355191589330946, '1677650895803793410', '1895354918074572802', 'admin', '2025-02-28 14:07:46', '2025-02-28 14:07:46', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1895355216558022658, '1678024811844005890', '1895354918074572802', 'admin', '2025-02-28 14:07:52', '2025-02-28 14:07:52', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1896506628302168066, '1673214908193419266', '1896404068044103681', 'admin', '2025-03-03 18:23:10', '2025-03-03 18:23:10', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1896506648577433601, '1677650895803793410', '1896404068044103681', 'admin', '2025-03-03 18:23:15', '2025-03-03 18:23:15', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1896506668508766210, '1678024811844005890', '1896404068044103681', 'admin', '2025-03-03 18:23:20', '2025-03-03 18:23:20', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1897931795234545665, '1897931653211217921', '1693856304992264194', 'admin', '2025-03-07 16:46:17', '2025-03-07 16:46:17', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1901914085933441025, '1673214908193419266', '1901913795813433345', 'admin', '2025-03-18 16:30:29', '2025-03-18 16:30:29', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1901914104061222914, '1677650895803793410', '1901913795813433345', 'admin', '2025-03-18 16:30:33', '2025-03-18 16:30:33', 'admin');
INSERT INTO `biz_car_brand_supplier` VALUES (1901914122956562434, '1678024811844005890', '1901913795813433345', 'admin', '2025-03-18 16:30:37', '2025-03-18 16:30:37', 'admin');

-- ----------------------------
-- Table structure for biz_robot
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot`;
CREATE TABLE `biz_robot`  (
                              `id` bigint(0) NOT NULL COMMENT '主键id',
                              `status` bit(1) NOT NULL COMMENT '状态:启用/禁用',
                              `robot_url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '机器人访问地址',
                              `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'host',
                              `robot_proxies` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'robotProxies',
                              `robot_account_password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '账号-密码（多个）',
                              `robot_proxies_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '代理名称',
                              `need_dynamic_proxy` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否需要代理 0；关闭 1，开启',
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
INSERT INTO `biz_robot` VALUES (1680851853515853825, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dtj26x\",\"password\":\"Wbxs971122\"}', '别克1', 1, '', NULL, NULL, 'admin', '2023-07-17 16:08:05', '2025-02-07 22:29:30', 'admin');
INSERT INTO `biz_robot` VALUES (1680855820681515009, b'1', 'http://127.0.0.1:5000/api/buickNw', 'dealerhome.dms.saic-gm.com', '', '{\"username\":\"dmgqpn\",\"password\":\"Ls12345678\"}', '雪佛兰1', 1, '', NULL, NULL, 'admin', '2023-07-17 16:23:51', '2025-02-07 22:29:52', 'admin');
INSERT INTO `biz_robot` VALUES (1680864210791075842, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dhr3hp\",\"password\":\"CYja2007\"}', '雪佛兰2', 1, '', NULL, NULL, 'admin', '2023-07-17 16:57:11', '2025-03-09 11:40:07', 'admin');
INSERT INTO `biz_robot` VALUES (1681131305638465538, b'1', 'http://127.0.0.1:5000/api/volkswagen', NULL, '47.98.142.48:9504', '{\"username\":\"P415538\",\"password\":\"Mcmy20201014.\"}', '一汽大众1', 1, '', NULL, NULL, 'admin', '2023-07-18 10:38:32', '2025-04-14 08:52:15', 'admin');
INSERT INTO `biz_robot` VALUES (1681137784797958146, b'1', 'http://127.0.0.1:5000/api/hyundai', '172.24.171.10', '47.98.142.48:9506', '{\"username\":\"D0138764\",\"password\":\"AA89021010\"}', '北京现代1', 1, '', NULL, NULL, 'admin', '2023-07-18 11:04:17', '2024-12-02 10:33:25', 'admin');
INSERT INTO `biz_robot` VALUES (1681140457005813761, b'1', 'http://127.0.0.1:5000/api/jaguar', '', '47.98.142.48:9507', '{\"username\":\"GDDCZQ\",\"password\":\"Zxcvb123456@\"}', '路虎捷豹1', 1, '', NULL, NULL, 'admin', '2023-07-18 11:14:54', '2025-04-01 12:19:57', 'admin');
INSERT INTO `biz_robot` VALUES (1681176508055326722, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"do0aft\",\"password\":\"Qaz147258369.\"}', '凯迪拉克1', 1, '', NULL, NULL, 'admin', '2023-07-18 13:38:09', '2025-02-08 09:52:44', 'admin');
INSERT INTO `biz_robot` VALUES (1681177498175311873, b'1', 'http://127.0.0.1:5000/api/buickNw', '192.168.33.243:9080', '', '{\"username\":\"ddk7qc\",\"password\":\"wW123456\"}', '别克4', 1, '', NULL, NULL, 'admin', '2023-07-18 13:42:05', '2025-03-08 10:31:25', 'admin');
INSERT INTO `biz_robot` VALUES (1685514766470012930, b'1', 'http://127.0.0.1:5000/api/volkswagen', '', '47.98.142.48:9552', '{\"username\":\"P532888\",\"password\":\"Zhang@2023.0\"}', '一汽大众2', 1, '', NULL, NULL, 'admin', '2023-07-30 12:56:50', '2025-02-28 16:43:45', 'admin');
INSERT INTO `biz_robot` VALUES (1686342965403770881, b'1', 'http://127.0.0.1:5000/api/saicVolkswagen', '', '47.98.142.48:9512', '{\"username\":\"18326189180\",\"password\":\"CHENYAAo222111\"}', '上海大众1', 1, '', NULL, NULL, 'admin', '2023-08-01 19:47:48', '2025-03-13 10:59:31', 'admin');
INSERT INTO `biz_robot` VALUES (1690675465336823809, b'1', 'http://127.0.0.1:5000/api/saicVolkswagen', '', '47.98.142.48:9516', '{\"username\":\"18631239369\",\"password\":\"Qwe12345678\"}', '上海大众2', 1, '', NULL, NULL, 'qgadmin', '2023-08-13 18:43:37', '2025-02-02 15:38:56', 'admin');
INSERT INTO `biz_robot` VALUES (1692105743208980482, b'1', 'http://127.0.0.1:5000/api/saicVolkswagen', '', '47.98.142.48:9549', '{\"username\":\"18855579906\",\"password\":\"Xyy55556666\"}', '上海大众3', 1, '', NULL, NULL, 'admin', '2023-08-17 17:27:02', '2025-02-23 08:19:29', 'admin');
INSERT INTO `biz_robot` VALUES (1692375225059942401, b'1', 'http://127.0.0.1:5000/api/wuLing', '', '', '{\"username\":\"6221121_张文超\",\"password\":\"Sgmw@50505050@\"}', '五菱1', 1, '', NULL, NULL, 'admin', '2023-08-18 11:17:51', '2025-03-11 16:01:23', 'admin');
INSERT INTO `biz_robot` VALUES (1693136577093345282, b'1', 'http://127.0.0.1:5000/api/gacToyota', '172.16.255.36', '47.98.142.48:9521', '{\"username\":\"33A90_000037\",\"password\":\"DLRua123\"}', '广汽丰田1', 1, '', NULL, NULL, 'admin', '2023-08-20 13:43:12', '2024-10-21 09:18:32', 'admin');
INSERT INTO `biz_robot` VALUES (1693855598239457282, b'1', 'http://127.0.0.1:5000/api/geely', 'dms-prod.geely.com:443', '47.98.142.48:9502', '{\"username\":\"1102\",\"password\":\"a9638521\"}', '吉利1', 1, '', NULL, NULL, 'admin', '2023-08-22 13:20:20', '2024-10-31 13:34:39', 'admin');
INSERT INTO `biz_robot` VALUES (1695281263186714626, b'1', 'http://127.0.0.1:5000/api/audi', 'https://audiep.faw-vw.com/login', '47.98.142.48:9546', '{\"username\":\"P148638\",\"password\":\"ZCZ521010@\"}', '奥迪3', 1, '', NULL, NULL, 'admin', '2023-08-26 11:45:25', '2024-12-05 15:10:27', 'admin');
INSERT INTO `biz_robot` VALUES (1697090065565433857, b'1', 'http://127.0.0.1:5000/api/volkswagen', '', '47.98.142.48:9525', '{\"username\":\"P395828\",\"password\":\"Zl18656027624\"}', '一汽大众3', 1, '', NULL, NULL, 'admin', '2023-08-31 11:32:57', '2025-02-17 08:54:20', 'admin');
INSERT INTO `biz_robot` VALUES (1698675913217998850, b'1', 'http://127.0.0.1:5000/api/volvo', '', '', '{\"username\":\"XCAXWB\",\"password\":\"Xca@99999999\"}', '沃尔沃1', 1, '', NULL, NULL, 'admin', '2023-09-04 20:34:32', '2025-03-05 08:47:39', 'admin');
INSERT INTO `biz_robot` VALUES (1698976304082124801, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"drk5es\",\"password\":\"Dms123777\"}', '别克5', 1, '', NULL, NULL, 'admin', '2024-12-25 16:57:12', '2025-02-05 12:01:10', 'admin');
INSERT INTO `biz_robot` VALUES (1698977437487923201, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dnryja\",\"password\":\"Lzy.111111\"}', '雪佛兰5', 1, '', NULL, NULL, 'admin', '2023-09-05 16:32:41', '2025-04-14 22:40:02', 'admin');
INSERT INTO `biz_robot` VALUES (1704402856638873601, b'1', 'http://127.0.0.1:5000/api/rowemg', 'api-ecdm-pv.saicmotor.com', '47.98.142.48:9533', '{\"username\":\"2200055lhf\",\"password\":\"Fm2200055!\"}', '荣威名爵1', 1, '', NULL, NULL, 'admin', '2023-09-20 15:51:22', '2025-02-13 09:19:08', 'admin');
INSERT INTO `biz_robot` VALUES (1713851679694061569, b'1', 'http://127.0.0.1:5000/api/gacToyota', '172.16.255.36', '47.98.142.48:9534', '{\"username\":\"44T50_xujiajing\",\"password\":\"Xjj111111\"}', '广汽丰田2', 1, '', NULL, NULL, 'admin', '2023-10-16 17:37:37', '2024-11-01 09:00:17', 'admin');
INSERT INTO `biz_robot` VALUES (1727251591500595202, b'1', 'http://127.0.0.1:5000/api/buickNw', '10.0.0.116:9080', '', '{\"username\":\"dh1wya\",\"password\":\"Th555555\"}', '别克2', 1, '', NULL, NULL, 'admin', '2023-11-22 17:04:05', '2025-02-10 12:59:11', 'admin');
INSERT INTO `biz_robot` VALUES (1729029351936032770, b'1', 'http://localhost:5000/api/changanNew', '', '47.98.142.48:9524', '{\"username\":\"10001\",\"password\":\"caqc12345\"}', '长安1', 1, '', NULL, NULL, 'admin', '2023-11-27 14:48:16', '2024-08-04 10:29:58', 'admin');
INSERT INTO `biz_robot` VALUES (1771404302806323201, b'1', 'http://127.0.0.1:5000/api/audi', '', '47.98.142.48:9505', '{\"username\":\"P203202\",\"password\":\"Luanbo556600!\"}', '奥迪1', 1, '', NULL, NULL, 'admin', '2024-03-23 13:11:12', '2025-04-07 10:53:19', 'admin');
INSERT INTO `biz_robot` VALUES (1789206376977698817, b'1', 'http://127.0.0.1:5000/api/audi', '', '47.98.142.48:9541', '{\"username\":\"P417731\",\"password\":\"A0123456789*\"}', '奥迪4', 1, '', NULL, NULL, 'admin', '2024-05-11 16:10:17', '2025-02-22 16:04:08', 'admin');
INSERT INTO `biz_robot` VALUES (1790610795220865025, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dqpvvu\",\"password\":\"GDsk2222\"}', '凯迪拉克2', 1, '', NULL, NULL, 'admin', '2024-05-15 13:10:56', '2025-03-27 13:15:38', 'admin');
INSERT INTO `biz_robot` VALUES (1795044857914232833, b'1', 'http://127.0.0.1:5000/api/peugeotWeb', '', '', '{\"username\":\"66269ZSHUANG\",\"password\":\"Aa123456\"}', '东风标致1', 1, '', NULL, NULL, 'admin', '2024-05-27 18:50:19', '2024-08-27 09:31:39', 'admin');
INSERT INTO `biz_robot` VALUES (1795082205121122305, b'1', 'http://127.0.0.1:5000/api/citroenWeb', '', '', '{\"username\":\"71103GSHUANG\",\"password\":\"Aa123456\"}', '东风雪铁龙1', 1, '', NULL, NULL, 'admin', '2024-05-27 21:18:43', '2024-06-23 09:07:37', 'admin');
INSERT INTO `biz_robot` VALUES (1797896622078595073, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dat5z6\",\"password\":\"SGm123654\"}', '别克7', 1, '', NULL, NULL, 'admin', '2024-06-04 15:42:13', '2025-04-03 18:48:48', 'admin');
INSERT INTO `biz_robot` VALUES (1798243815385894914, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dek1ec\",\"password\":\"As12345678\"}', '雪佛兰4', 1, '', NULL, NULL, 'admin', '2024-06-05 14:41:50', '2025-02-09 09:46:52', 'admin');
INSERT INTO `biz_robot` VALUES (1799047213647564802, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dhg4cz\",\"password\":\"  Sh2100035\"}', '雪佛兰7', 1, '', NULL, NULL, 'admin', '2024-06-07 19:54:15', '2025-04-14 14:42:52', 'admin');
INSERT INTO `biz_robot` VALUES (1799048161908396033, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dh4mwa\",\"password\":\"Th555555\"}', '别克8', 1, '', NULL, NULL, 'admin', '2024-06-07 19:58:01', '2025-02-22 11:03:39', 'admin');
INSERT INTO `biz_robot` VALUES (1800793421378392066, b'1', 'http://127.0.0.1:5000/api/audi', '', '47.98.142.48:9541', '{\"username\":\"P246508\",\"password\":\"A123456*\"}', '奥迪4-1', 1, '', NULL, NULL, 'admin', '2024-06-12 15:33:03', '2024-06-12 15:35:09', 'admin');
INSERT INTO `biz_robot` VALUES (1830776285851193346, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dytg6b\",\"password\":\"As123456\"}', '凯迪拉克3', 1, '', NULL, NULL, 'admin', '2024-09-03 09:14:15', '2025-02-08 13:40:57', 'admin');
INSERT INTO `biz_robot` VALUES (1831146517925105665, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dggg0h\",\"password\":\"Yqs1234567\"}', '别克9', 1, '', NULL, NULL, 'admin', '2024-09-04 09:45:26', '2025-02-09 09:47:22', 'admin');
INSERT INTO `biz_robot` VALUES (1832741142976696321, b'1', 'http://127.0.0.1:5000/api/ford', '', '47.98.142.48:9551', '{\"username\":\"X-LIU34\",\"password\":\"Sp@0123456789\"}', '福特1', 1, '', NULL, NULL, 'admin', '2024-09-08 19:21:54', '2025-02-23 14:25:10', 'admin');
INSERT INTO `biz_robot` VALUES (1855163838469410818, b'1', 'http://127.0.0.1:5000/api/jeep', '', '47.98.142.48:9534', '{\"username\":\"S47J00009\",\"password\":\"Hc123456789$++\"}', '吉普1', 1, '公司代码：S47J1590161', '16:22:17', NULL, 'admin', '2024-11-09 16:21:41', '2025-02-13 12:48:49', 'admin');
INSERT INTO `biz_robot` VALUES (1856964463893057537, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dwq2ck\",\"password\":\"123456Sgm\"}', '别克3', 1, '', NULL, NULL, 'admin', '2024-11-14 15:36:44', '2025-02-08 15:13:31', 'admin');
INSERT INTO `biz_robot` VALUES (1895354948382613506, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dt85z5\",\"password\":\"Asd123456\"}', '雪佛兰3', 1, '', NULL, NULL, 'admin', '2025-02-28 14:06:48', '2025-03-19 12:17:27', 'admin');
INSERT INTO `biz_robot` VALUES (1896404148738318338, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dkfaxu\",\"password\":\"XJb123456\"}', '别克6', 1, '', NULL, NULL, 'admin', '2025-03-03 11:35:57', '2025-03-03 11:36:43', 'admin');
INSERT INTO `biz_robot` VALUES (1901913851610259458, b'1', 'http://127.0.0.1:5000/api/buickNw', '', '', '{\"username\":\"dbpsql\",\"password\":\"aQ123456\"}', '雪佛兰6', 1, '', NULL, NULL, 'admin', '2025-03-18 16:29:33', '2025-03-18 16:30:08', 'admin');

-- ----------------------------
-- Table structure for biz_robot_query_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot_query_record`;
CREATE TABLE `biz_robot_query_record`  (
                                           `id` bigint(0) NOT NULL COMMENT '主键id',
                                           `vin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'vin',
                                           `supplier_id` bigint(0) NOT NULL COMMENT '供应商ID',
                                           `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商名称',
                                           `robot_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '机器人id',
                                           `result_status` int(0) UNSIGNED NOT NULL COMMENT '结果状态码：0：失败，1：成功，2：无结果',
                                           `failure_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '失败原因',
                                           `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '成功结果',
                                           `queryTime` datetime(0) NULL DEFAULT NULL COMMENT '查询时间',
                                           `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                           `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                           PRIMARY KEY (`id`) USING BTREE,
                                           INDEX `idx_vin`(`vin`) USING BTREE,
                                           INDEX `idx_querytime`(`queryTime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人查询记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_robot_query_record
-- ----------------------------

-- ----------------------------
-- Table structure for biz_robot_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_robot_supplier`;
CREATE TABLE `biz_robot_supplier`  (
                                       `id` bigint(0) NOT NULL COMMENT '主键id',
                                       `robot_id` bigint(0) NOT NULL COMMENT '机器人id',
                                       `supplier_id` bigint(0) NOT NULL COMMENT '供应商id',
                                       `sort` int(0) NOT NULL DEFAULT 1 COMMENT '排序(权重)',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                       `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人供应商关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_robot_supplier
-- ----------------------------
INSERT INTO `biz_robot_supplier` VALUES (1682210802483761153, 1680855820681515009, 1673215521878814721, 1, 'admin', '2023-07-21 10:08:04', '2023-07-21 10:08:25', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682266207473799169, 1680864210791075842, 1678352264958775298, 1, 'admin', '2023-07-21 13:48:14', '2023-07-21 13:48:21', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682266520087859201, 1681177498175311873, 1682266492447395841, 1, 'admin', '2023-07-21 13:49:28', '2023-07-21 13:49:41', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682266996112003073, 1681131305638465538, 1682266976130338817, 1, 'admin', '2023-07-21 13:51:22', '2023-07-21 13:51:28', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682267130593972225, 1681137784797958146, 1682267106409615361, 1, 'admin', '2023-07-21 13:51:54', '2023-07-21 13:52:01', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682267323863306241, 1681140457005813761, 1678647802358890497, 1, 'admin', '2023-07-21 13:52:40', '2023-07-21 13:52:47', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1682267455509925889, 1681176508055326722, 1682267437243731969, 1, 'admin', '2023-07-21 13:53:11', '2023-07-21 13:53:27', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1685515105466245122, 1685514766470012930, 1685514983349084162, 1, 'admin', '2023-07-30 12:58:11', '2023-07-30 12:58:11', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1686622126215192577, 1686342965403770881, 1686621886137425922, 1, 'admin', '2023-08-02 14:17:05', '2023-08-02 14:17:05', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1690676507608784897, 1690675465336823809, 1690676399609651201, 1, 'qgadmin', '2023-08-13 18:47:45', '2023-08-13 18:47:45', 'qgadmin');
INSERT INTO `biz_robot_supplier` VALUES (1692106994248228866, 1692105743208980482, 1692106883334053889, 1, 'admin', '2023-08-17 17:32:00', '2023-08-17 17:32:00', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1692411399027728385, 1692375225059942401, 1692411319231094785, 1, 'admin', '2023-08-18 13:41:36', '2023-08-18 13:41:36', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1693137307405557761, 1693136577093345282, 1693137225776013314, 1, 'admin', '2023-08-20 13:46:06', '2023-08-20 13:46:06', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1693856353587470338, 1693855598239457282, 1693856304992264194, 1, 'admin', '2023-08-22 13:23:20', '2023-08-22 13:23:20', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1695281595446894593, 1695281263186714626, 1695281566665580545, 1, 'admin', '2023-08-26 11:46:44', '2023-08-26 11:46:44', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1697091126187474945, 1697090065565433857, 1697091054385184769, 1, 'admin', '2023-08-31 11:37:10', '2023-08-31 11:37:10', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1698676130025766914, 1698675913217998850, 1698675848885764098, 1, 'admin', '2023-09-04 20:35:24', '2023-09-04 20:35:24', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1698978289577558017, 1698976304082124801, 1698978101765013506, 1, 'admin', '2023-09-05 16:36:04', '2023-09-05 16:36:04', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1704403256251187201, 1704402856638873601, 1704403210122231809, 1, 'admin', '2023-09-20 15:52:57', '2023-09-20 15:52:57', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1713855030452224002, 1713851679694061569, 1713851581379575810, 1, 'admin', '2023-10-16 17:50:56', '2023-10-16 17:50:56', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1727251975795310594, 1727251591500595202, 1727251894895575041, 1, 'admin', '2023-11-22 17:05:36', '2023-11-22 17:05:36', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1729029714638471169, 1729029351936032770, 1729029681868374017, 1, 'admin', '2023-11-27 14:49:42', '2023-11-27 14:49:42', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1771408704543821825, 1771404302806323201, 1771404211743789057, 1, 'admin', '2024-03-23 13:28:41', '2024-03-23 13:28:41', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1788764317485535234, 1698977437487923201, 1788764194357547009, 1, 'admin', '2024-05-10 10:53:42', '2024-05-10 10:53:42', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1789207535800979458, 1789206376977698817, 1789206256483733505, 1, 'admin', '2024-05-11 16:14:53', '2024-05-11 16:14:53', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1790611065757667330, 1790610795220865025, 1790610742653652993, 1, 'admin', '2024-05-15 13:12:01', '2024-05-15 13:12:01', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1795045352078741506, 1795044857914232833, 1795044758647640065, 1, 'admin', '2024-05-27 18:52:17', '2024-05-27 18:52:17', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1795082612555812865, 1795082205121122305, 1795082049122373633, 1, 'admin', '2024-05-27 21:20:21', '2024-05-27 21:20:21', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1797897556892487682, 1797896622078595073, 1797896567514894337, 1, 'admin', '2024-06-04 15:45:56', '2024-06-04 15:45:56', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1798244192508350465, 1798243815385894914, 1798243708942848002, 1, 'admin', '2024-06-05 14:43:20', '2024-06-05 14:43:20', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1799047504300249089, 1799047213647564802, 1799047189580648450, 1, 'admin', '2024-06-07 19:55:24', '2024-06-07 19:55:24', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1799048684967464961, 1799048161908396033, 1799048104417071105, 1, 'admin', '2024-06-07 20:00:06', '2024-06-07 20:00:06', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1800793659451281409, 1800793421378392066, 1800793591096709122, 1, 'admin', '2024-06-12 15:34:00', '2024-06-12 15:34:00', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1807703206283612161, 1680851853515853825, 1807694669931253761, 1, 'admin', '2024-07-01 17:10:05', '2024-07-01 17:10:05', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1830777325744984066, 1830776285851193346, 1830776228649275393, 1, 'admin', '2024-09-03 09:18:23', '2024-09-03 09:18:23', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1831146867478401026, 1831146517925105665, 1831146495963729921, 1, 'admin', '2024-09-04 09:46:49', '2024-09-04 09:46:49', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1832741713800499201, 1832741142976696321, 1832741070289408002, 1, 'admin', '2024-09-08 19:24:10', '2024-09-08 19:24:10', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1855164153503584257, 1855163838469410818, 1855163770102255617, 1, 'admin', '2024-11-09 16:22:56', '2024-11-09 16:22:56', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1856964899232452609, 1856964463893057537, 1856964446864183297, 1, 'admin', '2024-11-14 15:38:27', '2024-11-14 15:38:27', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1867835362435391490, 1867834962823077889, 1867834481694466050, 1, 'admin', '2024-12-14 15:33:48', '2024-12-14 15:33:48', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1895355145825280001, 1895354948382613506, 1895354918074572802, 1, 'admin', '2025-02-28 14:07:35', '2025-02-28 14:07:35', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1896507473953546241, 1896404148738318338, 1896404068044103681, 1, 'admin', '2025-03-03 18:26:32', '2025-03-03 18:26:32', 'admin');
INSERT INTO `biz_robot_supplier` VALUES (1901914055612817409, 1901913851610259458, 1901913795813433345, 1, 'admin', '2025-03-18 16:30:21', '2025-03-18 16:30:21', 'admin');

-- ----------------------------
-- Table structure for biz_supplier
-- ----------------------------
DROP TABLE IF EXISTS `biz_supplier`;
CREATE TABLE `biz_supplier`  (
                                 `id` bigint(0) NOT NULL COMMENT '主键id',
                                 `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '供应商名称',
                                 `director_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '负责人姓名',
                                 `contact_mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '联系方式',
                                 `daily_limit_count` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '每日限单量',
                                 `daily_count` int(0) NULL DEFAULT 0 COMMENT '每日单量',
                                 `weight` int(0) NOT NULL DEFAULT 1 COMMENT '权重',
                                 `status` int(0) NOT NULL DEFAULT 0 COMMENT '状态 0；关闭 1，开启',
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
INSERT INTO `biz_supplier` VALUES (1673215521878814721, '雪佛兰1', '雪佛兰1', '', 18, 19, 0, 0, '', 'admin', '2023-06-26 14:24:02', '2025-04-17 16:27:27', 'admin');
INSERT INTO `biz_supplier` VALUES (1678352264958775298, '雪佛兰2', '雪佛兰2', '', 18, 19, 5, 0, '', 'admin', '2023-07-10 18:35:37', '2025-04-17 15:36:16', '管理员');
INSERT INTO `biz_supplier` VALUES (1678647802358890497, '路虎捷豹1', '路虎捷豹1', '', 150, 135, 1, 1, '', 'admin', '2023-07-11 14:09:59', '2025-04-17 19:17:31', '管理员');
INSERT INTO `biz_supplier` VALUES (1682266492447395841, '雪-别克4', '雪-别克4', '', 18, 15, 16, 0, '', 'admin', '2023-07-21 13:49:21', '2025-04-17 17:05:10', '管理员');
INSERT INTO `biz_supplier` VALUES (1682266976130338817, '一汽大众1', '一汽大众1', '', 120, 122, 1, 0, '', 'admin', '2023-07-21 13:51:17', '2025-04-17 15:34:51', '管理员');
INSERT INTO `biz_supplier` VALUES (1682267106409615361, '北京现代1', '北京现代1', '', 150, 108, 1, 1, '', 'admin', '2023-07-21 13:51:48', '2025-04-17 19:19:07', '管理员');
INSERT INTO `biz_supplier` VALUES (1682267437243731969, '雪-凯迪拉克1', '雪-凯迪拉克1', '', 18, 19, 11, 0, '', 'admin', '2023-07-21 13:53:07', '2025-04-17 15:38:44', '管理员');
INSERT INTO `biz_supplier` VALUES (1685514983349084162, '一汽大众2', '一汽大众2', '', 150, 48, 1, 1, '', 'admin', '2023-07-30 12:57:42', '2025-04-17 19:13:43', '管理员');
INSERT INTO `biz_supplier` VALUES (1686621886137425922, '上海大众1', '上海大众1', '', 150, 86, 1, 1, '', 'admin', '2023-08-02 14:16:08', '2025-04-17 19:08:06', '管理员');
INSERT INTO `biz_supplier` VALUES (1690676399609651201, '上海大众2', '上海大众2', '', 150, 127, 1, 0, '', 'qgadmin', '2023-08-13 18:47:20', '2025-04-17 18:03:09', 'admin');
INSERT INTO `biz_supplier` VALUES (1692106883334053889, '上海大众3', '上海大众3', '', 150, 74, 1, 1, '', 'admin', '2023-08-17 17:31:33', '2025-04-17 19:19:10', '管理员');
INSERT INTO `biz_supplier` VALUES (1692411319231094785, '上汽通用五菱1', '上汽通用五菱1', '', 90, 46, 1, 1, '', 'admin', '2023-08-18 13:41:17', '2025-04-17 19:22:40', '管理员');
INSERT INTO `biz_supplier` VALUES (1693137225776013314, '广汽丰田1', '广汽丰田1', '', 150, 69, 1, 1, '', 'admin', '2023-08-20 13:45:46', '2025-04-17 19:00:13', '管理员');
INSERT INTO `biz_supplier` VALUES (1693856304992264194, '吉利1', '吉利1', '', 150, 102, 1, 0, '', 'admin', '2023-08-22 13:23:08', '2025-04-17 18:06:58', 'admin');
INSERT INTO `biz_supplier` VALUES (1695281566665580545, '奥迪3', '奥迪3', '', 150, 109, 1, 1, '', 'admin', '2023-08-26 11:46:37', '2025-04-17 19:22:55', '管理员');
INSERT INTO `biz_supplier` VALUES (1697091054385184769, '一汽大众3', '一汽大众3', '', 150, 41, 1, 0, '', 'admin', '2023-08-31 11:36:52', '2025-04-17 15:22:24', '管理员');
INSERT INTO `biz_supplier` VALUES (1698675848885764098, '沃尔沃1', '沃尔沃1', '', 150, 31, 1, 1, '', 'admin', '2023-09-04 20:34:17', '2025-04-17 19:24:43', '管理员');
INSERT INTO `biz_supplier` VALUES (1698978101765013506, '雪-别克5', '雪-别克5', '', 18, 18, 17, 0, '', 'admin', '2023-09-05 16:35:20', '2025-04-17 17:36:14', '管理员');
INSERT INTO `biz_supplier` VALUES (1704403210122231809, '荣威名爵1', '荣威名爵1', '', 100, 34, 1, 1, '', 'admin', '2023-09-20 15:52:46', '2025-04-17 19:16:39', '管理员');
INSERT INTO `biz_supplier` VALUES (1713851581379575810, '广汽丰田2', '广汽丰田2', '', 150, 79, 1, 1, '', 'admin', '2023-10-16 17:37:13', '2025-04-17 19:15:28', '管理员');
INSERT INTO `biz_supplier` VALUES (1727251894895575041, '雪-别克2', '雪-别克2', '', 38, 38, 19, 0, '', 'admin', '2023-11-22 17:05:17', '2025-04-17 19:25:36', '管理员');
INSERT INTO `biz_supplier` VALUES (1729029681868374017, '长安1', '长安1', '', 150, 76, 1, 1, '', 'admin', '2025-01-13 09:00:04', '2025-04-17 19:25:56', '管理员');
INSERT INTO `biz_supplier` VALUES (1771404211743789057, '奥迪1', '奥迪1', '', 150, 100, 1, 1, '', 'admin', '2024-03-23 13:10:50', '2025-04-17 19:19:53', '管理员');
INSERT INTO `biz_supplier` VALUES (1788764194357547009, '雪佛兰5', '雪佛兰5', '', 18, 17, 8, 0, '', 'admin', '2024-05-10 10:53:12', '2025-04-17 13:41:31', '管理员');
INSERT INTO `biz_supplier` VALUES (1789206256483733505, '奥迪4', '奥迪4', '', 150, 115, 1, 1, '', 'admin', '2024-05-11 16:09:48', '2025-04-17 18:37:21', '管理员');
INSERT INTO `biz_supplier` VALUES (1790610742653652993, '雪-凯迪拉克2', '雪-凯迪拉克2', '', 18, 18, 12, 0, '', 'admin', '2024-05-15 13:10:44', '2025-04-17 14:36:12', '管理员');
INSERT INTO `biz_supplier` VALUES (1795044758647640065, '东风标致1', '东风标致1', '', 60, 31, 1, 1, '', 'admin', '2024-05-27 18:49:55', '2025-04-17 19:16:04', '管理员');
INSERT INTO `biz_supplier` VALUES (1795082049122373633, '东风雪铁龙1', '东风雪铁龙1', '', 60, 10, 1, 1, '', 'admin', '2024-05-27 21:18:06', '2025-04-17 15:40:56', '管理员');
INSERT INTO `biz_supplier` VALUES (1797896567514894337, '雪-别克7', '雪-别克7', '', 18, 19, 3, 0, '', 'admin', '2024-06-04 15:42:00', '2025-04-17 15:34:29', '管理员');
INSERT INTO `biz_supplier` VALUES (1798243708942848002, '雪佛兰4', '雪佛兰4', '', 18, 19, 7, 0, '', 'admin', '2024-06-05 14:41:25', '2025-04-17 15:37:06', '管理员');
INSERT INTO `biz_supplier` VALUES (1799047189580648450, '雪佛兰7', '雪佛兰7', '', 18, 0, 10, 1, '', 'admin', '2024-06-07 19:54:09', '2025-04-17 15:22:08', 'admin');
INSERT INTO `biz_supplier` VALUES (1799048104417071105, '雪-别克8', '雪-别克8', '', 18, 19, 4, 0, '', 'admin', '2024-06-07 19:57:47', '2025-04-17 15:35:06', '管理员');
INSERT INTO `biz_supplier` VALUES (1800793591096709122, '奥迪4-1', '奥迪4-1', '', 150, 0, 1, 0, '', 'admin', '2024-06-12 15:33:44', '2025-04-17 15:15:44', 'admin');
INSERT INTO `biz_supplier` VALUES (1807694669931253761, '雪-别克1', '雪-别克1', '', 18, 18, 14, 0, '', 'admin', '2024-07-01 16:36:09', '2025-04-17 16:02:27', '管理员');
INSERT INTO `biz_supplier` VALUES (1830776228649275393, '雪-凯迪拉克3', '雪-凯迪拉克3', '', 18, 19, 13, 0, '', 'admin', '2024-09-03 09:14:02', '2025-04-17 15:41:19', '管理员');
INSERT INTO `biz_supplier` VALUES (1831146495963729921, '雪-别克9', '雪-别克9', '', 18, 18, 6, 0, '', 'admin', '2024-09-04 09:45:20', '2025-04-17 12:38:36', '管理员');
INSERT INTO `biz_supplier` VALUES (1832741070289408002, '福特1', '福特1', '', 80, 80, 1, 0, '', 'admin', '2024-09-08 19:21:36', '2025-04-17 16:44:30', '管理员');
INSERT INTO `biz_supplier` VALUES (1855163770102255617, '吉普1', '吉普', '', 60, 34, 1, 1, '', 'admin', '2024-11-09 16:21:25', '2025-04-17 18:57:28', '管理员');
INSERT INTO `biz_supplier` VALUES (1856964446864183297, '雪-别克3', '雪-别克3', '', 35, 35, 15, 0, '', 'admin', '2024-11-14 15:36:40', '2025-04-17 16:40:01', '管理员');
INSERT INTO `biz_supplier` VALUES (1895354918074572802, '雪佛兰3', '雪佛兰3', '', 18, 18, 2, 0, '', 'admin', '2025-02-28 14:06:41', '2025-04-17 10:24:09', '管理员');
INSERT INTO `biz_supplier` VALUES (1896404068044103681, '雪-别克6', '雪-别克6', '', 18, 18, 18, 0, '', 'admin', '2025-03-03 11:35:38', '2025-04-17 18:05:27', '管理员');
INSERT INTO `biz_supplier` VALUES (1901913795813433345, '雪佛兰6', '雪佛兰6', '', 10, 10, 1, 0, '', 'admin', '2025-03-18 16:29:19', '2025-04-17 15:31:26', '管理员');

-- ----------------------------
-- Table structure for biz_vin_parsing
-- ----------------------------
DROP TABLE IF EXISTS `biz_vin_parsing`;
CREATE TABLE `biz_vin_parsing`  (
                                    `id` bigint(0) NOT NULL,
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
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `dept_id` bigint(0) NOT NULL COMMENT '部门ID',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '部门名称',
                             `sort_order` int(0) NOT NULL DEFAULT 0 COMMENT '排序值',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
                             `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父部门ID',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                             PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '总经办', 0, '0', 0, '2020-03-13 13:13:16', ' ', '2020-03-13 13:14:31', ' ');
INSERT INTO `sys_dept` VALUES (2, '行政中心', 0, '0', 1, '2020-03-13 13:13:30', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (3, '技术中心', 0, '0', 1, '2020-03-13 13:14:55', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (4, '运营中心', 0, '0', 1, '2020-03-13 13:15:15', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (5, '研发中心', 0, '0', 3, '2020-03-13 13:15:34', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (6, '产品中心', 0, '0', 3, '2020-03-13 13:15:49', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (7, '测试中心', 0, '0', 3, '2020-03-13 13:16:02', ' ', '2021-12-31 06:59:56', ' ');

-- ----------------------------
-- Table structure for sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation`  (
                                      `ancestor` bigint(0) NOT NULL COMMENT '祖先节点',
                                      `descendant` bigint(0) NOT NULL COMMENT '后代节点',
                                      PRIMARY KEY (`ancestor`, `descendant`) USING BTREE,
                                      INDEX `idx1`(`ancestor`) USING BTREE,
                                      INDEX `idx2`(`descendant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept_relation
-- ----------------------------
INSERT INTO `sys_dept_relation` VALUES (1, 1);
INSERT INTO `sys_dept_relation` VALUES (1, 2);
INSERT INTO `sys_dept_relation` VALUES (1, 3);
INSERT INTO `sys_dept_relation` VALUES (1, 4);
INSERT INTO `sys_dept_relation` VALUES (1, 5);
INSERT INTO `sys_dept_relation` VALUES (1, 6);
INSERT INTO `sys_dept_relation` VALUES (1, 7);
INSERT INTO `sys_dept_relation` VALUES (2, 2);
INSERT INTO `sys_dept_relation` VALUES (3, 3);
INSERT INTO `sys_dept_relation` VALUES (3, 5);
INSERT INTO `sys_dept_relation` VALUES (3, 6);
INSERT INTO `sys_dept_relation` VALUES (3, 7);
INSERT INTO `sys_dept_relation` VALUES (4, 4);
INSERT INTO `sys_dept_relation` VALUES (5, 5);
INSERT INTO `sys_dept_relation` VALUES (6, 6);
INSERT INTO `sys_dept_relation` VALUES (7, 7);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `id` bigint(0) NOT NULL,
                             `dict_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标识',
                             `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
                             `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否是系统内置',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标记',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `sys_dict_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'dict_type', '字典类型', NULL, '0', '0', '2019-05-16 14:16:20', '', 'admin', '2021-12-29 12:29:18');
INSERT INTO `sys_dict` VALUES (2, 'log_type', '日志类型', NULL, '0', '0', '2020-03-13 14:21:01', '', 'admin', '2021-12-29 12:30:14');
INSERT INTO `sys_dict` VALUES (3, 'ds_type', '驱动类型', NULL, '0', '0', '2021-10-15 16:24:35', '', 'admin', '2021-12-29 12:30:18');
INSERT INTO `sys_dict` VALUES (4, 'param_type', '参数配置', '检索、原文、报表、安全、文档、消息、其他', '1', '0', '2022-03-25 20:51:26', 'admin', 'admin', '2022-03-25 20:51:26');
INSERT INTO `sys_dict` VALUES (5, 'status_type', '租户状态', '租户状态', '1', '0', '2022-03-25 20:56:51', 'admin', 'admin', '2022-03-25 20:56:51');
INSERT INTO `sys_dict` VALUES (6, 'menu_type_status', '菜单类型', NULL, '1', '0', '2022-09-18 17:12:05', 'admin', 'admin', '2022-09-18 17:12:05');
INSERT INTO `sys_dict` VALUES (7, 'dict_css_type', '字典项展示样式', NULL, '1', '0', '2022-09-28 21:37:23', 'admin', 'admin', '2022-09-28 21:37:23');
INSERT INTO `sys_dict` VALUES (8, 'keepalive_status', '菜单是否开启缓冲', NULL, '1', '0', '2022-09-28 21:46:12', 'admin', 'admin', '2022-09-28 21:46:12');
INSERT INTO `sys_dict` VALUES (9, 'user_lock_flag', '用户锁定标记', NULL, '1', '0', '2022-09-28 21:51:39', 'admin', 'admin', '2022-09-28 21:51:39');
INSERT INTO `sys_dict` VALUES (1682583671829295106, 'order_status', '订单状态', '', '0', '0', '2023-07-22 10:49:43', 'admin', 'admin', '2023-07-22 10:49:43');
INSERT INTO `sys_dict` VALUES (1682590092083982337, 'car_type', '汽车品牌类型', '', '0', '0', '2023-07-22 11:15:14', 'admin', 'admin', '2023-07-22 11:15:14');
INSERT INTO `sys_dict` VALUES (1682631400324460545, 'order_is_data', '订单是否有记录', '', '0', '0', '2023-07-22 13:59:22', 'admin', 'admin', '2023-07-22 13:59:22');
INSERT INTO `sys_dict` VALUES (1683688004459061250, 'base_status', 'base状态', '', '0', '0', '2023-07-25 11:57:56', 'admin', 'admin', '2023-07-25 11:57:56');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
                                  `id` bigint(0) NOT NULL,
                                  `dict_id` bigint(0) NOT NULL COMMENT '字典ID',
                                  `dict_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典标识',
                                  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值',
                                  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签',
                                  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字典类型',
                                  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
                                  `sort_order` int(0) NOT NULL DEFAULT 0 COMMENT '排序（升序）',
                                  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT ' ' COMMENT '备注',
                                  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标记',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改人',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `sys_dict_value`(`value`) USING BTREE,
                                  INDEX `sys_dict_label`(`label`) USING BTREE,
                                  INDEX `sys_dict_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 1, 'dict_type', '1', '系统类', NULL, '系统类字典', 0, ' ', '0', '2019-05-16 14:20:40', NULL, NULL, '2019-05-16 14:20:40');
INSERT INTO `sys_dict_item` VALUES (2, 1, 'dict_type', '0', '业务类', NULL, '业务类字典', 0, ' ', '0', '2019-05-16 14:20:59', NULL, NULL, '2019-05-16 14:20:59');
INSERT INTO `sys_dict_item` VALUES (3, 2, 'log_type', '0', '正常', NULL, '正常', 0, ' ', '0', '2020-03-13 14:23:22', NULL, NULL, '2020-03-13 14:23:22');
INSERT INTO `sys_dict_item` VALUES (4, 2, 'log_type', '9', '异常', NULL, '异常', 0, ' ', '0', '2020-03-13 14:23:35', NULL, NULL, '2020-03-13 14:23:35');
INSERT INTO `sys_dict_item` VALUES (5, 3, 'ds_type', 'com.mysql.cj.jdbc.Driver', 'MYSQL8', NULL, 'MYSQL8', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (6, 3, 'ds_type', 'com.mysql.jdbc.Driver', 'MYSQL5', NULL, 'MYSQL5', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (7, 3, 'ds_type', 'oracle.jdbc.OracleDriver', 'Oracle', NULL, 'Oracle', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (8, 3, 'ds_type', 'org.mariadb.jdbc.Driver', 'mariadb', NULL, 'mariadb', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (9, 3, 'ds_type', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'sqlserver2005+', NULL, 'sqlserver2005+', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (10, 3, 'ds_type', 'com.microsoft.jdbc.sqlserver.SQLServerDriver', 'sqlserver2000', NULL, 'sqlserver2000', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (11, 3, 'ds_type', 'com.ibm.db2.jcc.DB2Driver', 'db2', NULL, 'db2', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (12, 3, 'ds_type', 'org.postgresql.Driver', 'postgresql', NULL, 'postgresql', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (13, 4, 'param_type', '1', '检索', NULL, '检索', 0, '检索', '0', '2022-03-25 20:51:51', 'admin', 'admin', '2022-03-25 20:51:51');
INSERT INTO `sys_dict_item` VALUES (14, 4, 'param_type', '2', '原文', NULL, '原文', 1, '原文', '0', '2022-03-25 20:52:06', 'admin', 'admin', '2022-03-25 20:52:06');
INSERT INTO `sys_dict_item` VALUES (15, 4, 'param_type', '3', '报表', NULL, '报表', 2, '报表', '0', '2022-03-25 20:52:16', 'admin', 'admin', '2022-03-25 20:52:16');
INSERT INTO `sys_dict_item` VALUES (16, 4, 'param_type', '4', '安全', NULL, '安全', 3, '安全', '0', '2022-03-25 20:52:32', 'admin', 'admin', '2022-03-25 20:52:32');
INSERT INTO `sys_dict_item` VALUES (17, 4, 'param_type', '5', '文档', NULL, '文档', 4, '文档', '0', '2022-03-25 20:52:52', 'admin', 'admin', '2022-03-25 20:52:52');
INSERT INTO `sys_dict_item` VALUES (18, 4, 'param_type', '6', '消息', NULL, '消息', 5, '消息', '0', '2022-03-25 20:53:07', 'admin', 'admin', '2022-03-25 20:53:07');
INSERT INTO `sys_dict_item` VALUES (19, 4, 'param_type', '9', '其他', NULL, '其他', 6, '其他', '0', '2022-03-25 20:54:50', 'admin', 'admin', '2022-03-25 20:54:50');
INSERT INTO `sys_dict_item` VALUES (20, 4, 'param_type', '0', '默认', NULL, '默认', 7, '默认', '0', '2022-03-25 20:55:23', 'admin', 'admin', '2022-03-25 20:55:23');
INSERT INTO `sys_dict_item` VALUES (21, 5, 'status_type', '0', '正常', NULL, '状态正常', 0, '状态正常', '0', '2022-03-25 20:57:12', 'admin', 'admin', '2022-03-25 20:57:12');
INSERT INTO `sys_dict_item` VALUES (22, 5, 'status_type', '9', '冻结', NULL, '状态冻结', 1, '状态冻结', '0', '2022-03-25 20:57:34', 'admin', 'admin', '2022-03-25 20:57:34');
INSERT INTO `sys_dict_item` VALUES (23, 6, 'menu_type_status', '0', '菜单', NULL, '菜单', 0, ' ', '0', '2022-09-18 17:15:52', 'admin', 'admin', '2022-09-18 17:15:52');
INSERT INTO `sys_dict_item` VALUES (24, 6, 'menu_type_status', '1', '按钮', 'success', '按钮', 1, ' ', '0', '2022-09-18 17:16:06', 'admin', 'admin', '2022-09-18 17:16:06');
INSERT INTO `sys_dict_item` VALUES (25, 7, 'dict_css_type', 'success', 'success', 'success', 'success', 2, ' ', '0', '2022-09-28 21:41:49', 'admin', 'admin', '2022-09-28 21:41:49');
INSERT INTO `sys_dict_item` VALUES (26, 7, 'dict_css_type', 'info', 'info', 'info', 'info', 3, ' ', '0', '2022-09-28 21:41:59', 'admin', 'admin', '2022-09-28 21:41:59');
INSERT INTO `sys_dict_item` VALUES (27, 7, 'dict_css_type', 'warning', 'warning', 'warning', 'warning', 4, ' ', '0', '2022-09-28 21:42:09', 'admin', 'admin', '2022-09-28 21:42:09');
INSERT INTO `sys_dict_item` VALUES (28, 7, 'dict_css_type', 'danger', 'danger', 'danger', 'danger', 5, ' ', '0', '2022-09-28 21:42:19', 'admin', 'admin', '2022-09-28 21:42:19');
INSERT INTO `sys_dict_item` VALUES (29, 8, 'keepalive_status', '0', '否', 'info', '不开启缓冲', 0, ' ', '0', '2022-09-28 21:46:32', 'admin', 'admin', '2022-09-28 21:46:32');
INSERT INTO `sys_dict_item` VALUES (30, 8, 'keepalive_status', '1', '是', NULL, '开启缓冲', 1, ' ', '0', '2022-09-28 21:46:54', 'admin', 'admin', '2022-09-28 21:46:54');
INSERT INTO `sys_dict_item` VALUES (31, 9, 'user_lock_flag', '0', '正常', NULL, '正常状态', 0, ' ', '0', '2022-09-28 21:51:55', 'admin', 'admin', '2022-09-28 21:51:55');
INSERT INTO `sys_dict_item` VALUES (32, 9, 'user_lock_flag', '9', '锁定', 'info', '已锁定', 9, ' ', '0', '2022-09-28 21:52:13', 'admin', 'admin', '2022-09-28 21:52:13');
INSERT INTO `sys_dict_item` VALUES (1682590153492787201, 1682590092083982337, 'car_type', '10', '国产', 'success', '国产', 1, '', '0', '2023-07-22 11:15:28', 'admin', 'admin', '2023-07-22 11:15:28');
INSERT INTO `sys_dict_item` VALUES (1682590211483234306, 1682590092083982337, 'car_type', '20', '合资', 'success', '合资', 2, '', '0', '2023-07-22 11:15:42', 'admin', 'admin', '2023-07-22 11:15:42');
INSERT INTO `sys_dict_item` VALUES (1682628860438507522, 1682583671829295106, 'order_status', '101', '已下单', 'success', '已下单', 1, '', '0', '2023-07-22 13:49:17', 'admin', 'admin', '2023-07-22 13:49:17');
INSERT INTO `sys_dict_item` VALUES (1682628922547761153, 1682583671829295106, 'order_status', '102', '查询中', 'success', '查询中', 2, '', '0', '2023-07-22 13:49:32', 'admin', 'admin', '2023-07-22 13:49:32');
INSERT INTO `sys_dict_item` VALUES (1682628989962809345, 1682583671829295106, 'order_status', '103', '回调成功', 'success', '回调成功', 3, '', '0', '2023-07-22 13:49:48', 'admin', 'admin', '2023-07-22 13:49:48');
INSERT INTO `sys_dict_item` VALUES (1682629059105910786, 1682583671829295106, 'order_status', '104', '回调失败', 'danger', '回调失败', 4, '', '0', '2023-07-22 13:50:04', 'admin', 'admin', '2023-07-22 13:50:04');
INSERT INTO `sys_dict_item` VALUES (1682629124071485441, 1682583671829295106, 'order_status', '106', '驳回订单', 'info', '驳回订单', 6, '', '0', '2023-07-22 13:50:20', 'admin', 'admin', '2023-07-22 18:19:34');
INSERT INTO `sys_dict_item` VALUES (1682631617878814722, 1682631400324460545, 'order_is_data', 'true', '是', 'success', '有查询记录', 1, '', '0', '2023-07-22 14:00:14', 'admin', 'admin', '2023-07-22 14:10:38');
INSERT INTO `sys_dict_item` VALUES (1682631713030795265, 1682631400324460545, 'order_is_data', 'false', '否', 'warning', '无查询记录', 2, '', '0', '2023-07-22 14:00:37', 'admin', 'admin', '2023-07-22 14:10:42');
INSERT INTO `sys_dict_item` VALUES (1682696951021174786, 1682583671829295106, 'order_status', '105', '无记录', 'info', '无记录', 5, '', '0', '2023-07-22 18:19:51', 'admin', 'admin', '2023-07-22 18:19:51');
INSERT INTO `sys_dict_item` VALUES (1683688059853234178, 1683688004459061250, 'base_status', '1', '开启', '', '开启', 1, '', '0', '2023-07-25 11:58:10', 'admin', 'admin', '2023-07-25 11:58:10');
INSERT INTO `sys_dict_item` VALUES (1683688110583341057, 1683688004459061250, 'base_status', '0', '关闭', 'danger', '关闭', 2, '', '0', '2023-07-25 11:58:22', 'admin', 'admin', '2023-07-25 11:58:22');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
                             `id` bigint(0) NOT NULL COMMENT '文件ID',
                             `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件名称',
                             `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件存储桶名称',
                             `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '原始文件名',
                             `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件类型',
                             `file_size` bigint(0) NULL DEFAULT NULL COMMENT '文件大小',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
                            `id` bigint(0) NOT NULL,
                            `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '日志类型',
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '日志标题',
                            `service_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '服务ID',
                            `remote_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作IP地址',
                            `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户代理',
                            `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '请求URI',
                            `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作方式',
                            `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '操作提交的数据',
                            `time` bigint(0) NULL DEFAULT NULL COMMENT '执行时间',
                            `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标记',
                            `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '异常信息',
                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                            `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `sys_log_create_by`(`create_by`) USING BTREE,
                            INDEX `sys_log_request_uri`(`request_uri`) USING BTREE,
                            INDEX `sys_log_type`(`type`) USING BTREE,
                            INDEX `sys_log_create_date`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `menu_id` bigint(0) NOT NULL,
                             `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
                             `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单权限标识',
                             `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '前端URL',
                             `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID',
                             `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
                             `sort_order` int(0) NOT NULL DEFAULT 0 COMMENT '排序值',
                             `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-开启，1- 关闭',
                             `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1000, '权限管理', NULL, '/admin', -1, 'icon-quanxianguanli', 2, '0', '0', '0', '', '2018-09-28 08:29:53', 'admin', '2023-07-21 13:47:07');
INSERT INTO `sys_menu` VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'icon-yonghuguanli', 0, '0', '0', '0', ' ', '2017-11-02 22:24:37', ' ', '2020-03-12 00:12:57');
INSERT INTO `sys_menu` VALUES (1101, '用户新增', 'sys_user_add', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:52:09', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1102, '用户修改', 'sys_user_edit', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:52:48', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1103, '用户删除', 'sys_user_del', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1104, '导入导出', 'sys_user_import_export', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'icon-caidanguanli', 1, '0', '0', '0', ' ', '2017-11-08 09:57:27', ' ', '2020-03-12 00:13:52');
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', 'sys_menu_add', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:15:53', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', 'sys_menu_edit', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:16:23', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', 'sys_menu_del', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:16:43', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1300, '角色管理', NULL, '/admin/role/index', 1000, 'icon-jiaoseguanli', 2, '0', '0', '0', ' ', '2017-11-08 10:13:37', ' ', '2020-03-12 00:15:40');
INSERT INTO `sys_menu` VALUES (1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:18', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:41', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1303, '角色删除', 'sys_role_del', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:59', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1304, '分配权限', 'sys_role_perm', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2018-04-20 07:22:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1305, '导入导出', 'sys_role_import_export', NULL, 1300, NULL, 0, '0', '1', '0', 'admin', '2022-03-21 11:14:52', 'admin', '2022-03-21 11:15:07');
INSERT INTO `sys_menu` VALUES (1400, '部门管理', NULL, '/admin/dept/index', 1000, 'icon-web-icon-', 3, '0', '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-12 00:15:44');
INSERT INTO `sys_menu` VALUES (1401, '部门新增', 'sys_dept_add', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:56:16', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1402, '部门修改', 'sys_dept_edit', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:56:59', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1403, '部门删除', 'sys_dept_del', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1500, '岗位管理', '', '/admin/post/index', 1000, 'icon-gangweiguanli', 4, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2022-11-10 21:35:55');
INSERT INTO `sys_menu` VALUES (1501, '岗位查看', 'sys_post_get', NULL, 1500, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:32:54');
INSERT INTO `sys_menu` VALUES (1502, '岗位新增', 'sys_post_add', NULL, 1500, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:32:48');
INSERT INTO `sys_menu` VALUES (1503, '岗位修改', 'sys_post_edit', NULL, 1500, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:33:10');
INSERT INTO `sys_menu` VALUES (1504, '岗位删除', 'sys_post_del', NULL, 1500, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:33:27');
INSERT INTO `sys_menu` VALUES (1505, '导入导出', 'sys_post_import_export', NULL, 1500, NULL, 4, '0', '1', '0', 'admin', '2022-03-21 12:53:05', 'admin', '2022-03-21 12:53:05');
INSERT INTO `sys_menu` VALUES (2000, '系统管理', NULL, '/setting', -1, 'icon-wxbgongju1', 3, '0', '0', '0', '', '2017-11-07 20:56:00', 'admin', '2023-07-21 13:47:03');
INSERT INTO `sys_menu` VALUES (2100, '日志管理', NULL, '/admin/log/index', 2000, 'icon-rizhiguanli', 3, '0', '0', '0', ' ', '2017-11-20 14:06:22', ' ', '2020-03-12 00:15:49');
INSERT INTO `sys_menu` VALUES (2101, '日志删除', 'sys_log_del', NULL, 2100, NULL, 0, '0', '1', '0', ' ', '2017-11-20 20:37:37', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2102, '导入导出', 'sys_log_import_export', NULL, 2100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2200, '字典管理', NULL, '/admin/dict/index', 2000, 'icon-tubiaozhizuomoban-27', 2, '0', '0', '0', '', '2017-11-29 11:30:52', 'admin', '2022-11-10 21:38:09');
INSERT INTO `sys_menu` VALUES (2201, '字典删除', 'sys_dict_del', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2202, '字典新增', 'sys_dict_add', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2203, '字典修改', 'sys_dict_edit', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2300, '令牌管理', NULL, '/admin/token/index', 2000, 'icon-lingpaiguanli', 4, '0', '0', '0', '', '2018-09-04 05:58:41', 'admin', '2022-11-10 21:38:33');
INSERT INTO `sys_menu` VALUES (2301, '令牌删除', 'sys_token_del', NULL, 2300, NULL, 0, '0', '1', '0', ' ', '2018-09-04 05:59:50', ' ', '2020-03-13 12:57:34');
INSERT INTO `sys_menu` VALUES (2400, '终端管理', '', '/admin/client/index', 2000, 'icon-shouji', 0, '0', '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-12 00:15:54');
INSERT INTO `sys_menu` VALUES (2401, '客户端新增', 'sys_client_add', NULL, 2400, '1', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2402, '客户端修改', 'sys_client_edit', NULL, 2400, NULL, 0, '0', '1', '0', ' ', '2018-05-15 21:37:06', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2403, '客户端删除', 'sys_client_del', NULL, 2400, NULL, 0, '0', '1', '0', ' ', '2018-05-15 21:39:16', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2600, '文件管理', NULL, '/admin/file/index', 2000, 'icon-wenjianjiawenjianguanli', 1, '0', '0', '0', '', '2018-06-26 10:50:32', 'admin', '2022-11-10 21:35:40');
INSERT INTO `sys_menu` VALUES (2601, '文件删除', 'sys_file_del', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2602, '文件新增', 'sys_file_add', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2603, '文件修改', 'sys_file_edit', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2700, '参数管理', NULL, '/admin/param/index', 2000, 'icon-canshu', 5, '0', '0', '0', 'admin', '2022-03-25 20:40:27', 'admin', '2022-11-10 21:38:50');
INSERT INTO `sys_menu` VALUES (2701, '参数新增', 'sys_publicparam_add', NULL, 2700, NULL, 0, '0', '1', '0', 'admin', '2022-03-25 20:45:05', 'admin', '2022-03-25 20:45:05');
INSERT INTO `sys_menu` VALUES (2702, '参数删除', 'sys_publicparam_del', NULL, 2700, NULL, 1, '0', '1', '0', 'admin', '2022-03-25 20:45:43', 'admin', '2022-03-25 20:45:43');
INSERT INTO `sys_menu` VALUES (2703, '参数修改', 'sys_publicparam_edit', NULL, 2700, NULL, 3, '0', '1', '0', 'admin', '2022-03-25 20:46:04', 'admin', '2022-03-25 20:46:04');
INSERT INTO `sys_menu` VALUES (3000, '开发平台', NULL, '/gen', -1, 'icon-keshihuapingtaiicon_zujian', 4, '1', '0', '0', '', '2020-03-11 22:15:40', 'admin', '2023-07-21 13:47:00');
INSERT INTO `sys_menu` VALUES (3100, '数据源管理', NULL, '/gen/datasource', 3000, 'icon-shujuyuan', 3, '1', '0', '0', '', '2020-03-11 22:17:05', 'admin', '2022-11-10 21:40:27');
INSERT INTO `sys_menu` VALUES (3200, '代码生成', NULL, '/gen/index', 3000, 'icon-didaima', 0, '0', '0', '0', '', '2020-03-11 22:23:42', 'admin', '2022-11-10 21:39:54');
INSERT INTO `sys_menu` VALUES (3300, '表单管理', NULL, '/gen/form', 3000, 'icon-biaodanguanli', 1, '1', '0', '0', '', '2020-03-11 22:19:32', 'admin', '2022-11-10 21:40:06');
INSERT INTO `sys_menu` VALUES (3301, '表单新增', 'gen_form_add', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:08');
INSERT INTO `sys_menu` VALUES (3302, '表单修改', 'gen_form_edit', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:09');
INSERT INTO `sys_menu` VALUES (3303, '表单删除', 'gen_form_del', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:11');
INSERT INTO `sys_menu` VALUES (3400, '表单设计', NULL, '/gen/design', 3000, 'icon-sheji', 2, '1', '0', '0', '', '2020-03-11 22:18:05', 'admin', '2022-11-10 21:40:16');
INSERT INTO `sys_menu` VALUES (4000, '服务监控', NULL, 'http://localhost:5001/login', -1, 'icon-iconset0265', 5, '0', '0', '0', 'admin', '2022-03-21 09:44:50', 'admin', '2023-07-21 13:46:54');
INSERT INTO `sys_menu` VALUES (9999, '系统官网', NULL, 'https://pig4cloud.com/#/', -1, 'icon-web-icon-', 999, '0', '0', '0', '', '2019-01-17 17:05:19', 'admin', '2022-11-10 21:40:53');
INSERT INTO `sys_menu` VALUES (1686920367536, '机器人查询记录表管理', '', '/admin/bizrobotqueryrecord/index', 1669705907359711234, 'icon-debug', 4, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:16:56');
INSERT INTO `sys_menu` VALUES (1686920367537, '机器人查询记录表查看', 'admin_bizrobotqueryrecord_get', NULL, 1686920367536, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367538, '机器人查询记录表新增', 'admin_bizrobotqueryrecord_add', NULL, 1686920367536, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367539, '机器人查询记录表修改', 'admin_bizrobotqueryrecord_edit', NULL, 1686920367536, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367540, '机器人查询记录表删除', 'admin_bizrobotqueryrecord_del', NULL, 1686920367536, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367565, '采购商订单表管理', '', '/admin/bizbuyerorder/index', 1682212735344873474, 'icon-tubiaozhizuomoban-27', 2, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:16:19');
INSERT INTO `sys_menu` VALUES (1686920367566, '采购商订单表查看', 'admin_bizbuyerorder_get', NULL, 1686920367565, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367567, '采购商订单表新增', 'admin_bizbuyerorder_add', NULL, 1686920367565, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367568, '采购商订单表修改', 'admin_bizbuyerorder_edit', NULL, 1686920367565, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367569, '采购商订单表删除', 'admin_bizbuyerorder_del', NULL, 1686920367565, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367592, '采购商表管理', '', '/admin/bizbuyer/index', 1682212735344873474, 'icon-principal', 2, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:16:05');
INSERT INTO `sys_menu` VALUES (1686920367593, '采购商表查看', 'admin_bizbuyer_get', NULL, 1686920367592, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367594, '采购商表新增', 'admin_bizbuyer_add', NULL, 1686920367592, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367595, '采购商表修改', 'admin_bizbuyer_edit', NULL, 1686920367592, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367596, '采购商表删除', 'admin_bizbuyer_del', NULL, 1686920367592, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367606, '品牌供应商关系表管理', '', '/admin/bizcarbrandsupplier/index', 1669705907359711234, 'icon-jiaoseguanli', 5, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-06-19 16:33:40');
INSERT INTO `sys_menu` VALUES (1686920367607, '品牌供应商关系表查看', 'admin_bizcarbrandsupplier_get', NULL, 1686920367606, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367608, '品牌供应商关系表新增', 'admin_bizcarbrandsupplier_add', NULL, 1686920367606, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367609, '品牌供应商关系表修改', 'admin_bizcarbrandsupplier_edit', NULL, 1686920367606, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367610, '品牌供应商关系表删除', 'admin_bizcarbrandsupplier_del', NULL, 1686920367606, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367621, '机器人管理', '', '/admin/bizrobot/index', 1669705907359711234, 'icon-cuowutishitubiao', 3, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:16:46');
INSERT INTO `sys_menu` VALUES (1686920367622, '机器人查看', 'admin_bizrobot_get', NULL, 1686920367621, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367623, '机器人新增', 'admin_bizrobot_add', NULL, 1686920367621, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367624, '机器人修改', 'admin_bizrobot_edit', NULL, 1686920367621, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367625, '机器人删除', 'admin_bizrobot_del', NULL, 1686920367621, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367634, '机器人供应商关系表管理', '', '/admin/bizrobotsupplier/index', 1669705907359711234, 'icon-web-icon-', 6, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:17:05');
INSERT INTO `sys_menu` VALUES (1686920367635, '机器人供应商关系表查看', 'admin_bizrobotsupplier_get', NULL, 1686920367634, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367636, '机器人供应商关系表新增', 'admin_bizrobotsupplier_add', NULL, 1686920367634, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367637, '机器人供应商关系表修改', 'admin_bizrobotsupplier_edit', NULL, 1686920367634, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367638, '机器人供应商关系表删除', 'admin_bizrobotsupplier_del', NULL, 1686920367634, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367648, '供应商表管理', '', '/admin/bizsupplier/index', 1669705907359711234, 'icon-yonghuguanli', 2, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-21 10:16:42');
INSERT INTO `sys_menu` VALUES (1686920367649, '供应商表查看', 'admin_bizsupplier_get', NULL, 1686920367648, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367650, '供应商表新增', 'admin_bizsupplier_add', NULL, 1686920367648, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367651, '供应商表修改', 'admin_bizsupplier_edit', NULL, 1686920367648, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367652, '供应商表删除', 'admin_bizsupplier_del', NULL, 1686920367648, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367661, '汽车品牌管理', '', '/admin/bizcarbrand/index', 1669705907359711234, 'icon-lingpaiguanli', 1, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2023-07-22 13:47:45');
INSERT INTO `sys_menu` VALUES (1686920367662, '汽车品牌查看', 'admin_bizcarbrand_get', NULL, 1686920367661, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367663, '汽车品牌新增', 'admin_bizcarbrand_add', NULL, 1686920367661, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367664, '汽车品牌修改', 'admin_bizcarbrand_edit', NULL, 1686920367661, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1686920367665, '汽车品牌删除', 'admin_bizcarbrand_del', NULL, 1686920367661, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', NULL, '2018-07-29 13:38:59');
INSERT INTO `sys_menu` VALUES (1669705907359711234, '业务中心', 'admin_biz_root', '/biz', -1, 'icon-wangzhan', 1, '0', '0', '0', NULL, '2023-06-19 16:15:54', 'admin', '2023-06-19 16:31:19');
INSERT INTO `sys_menu` VALUES (1671436062888046594, '在线加密', NULL, 'https://enc.pig4cloud.com/', -1, 'icon-wangzhan', 1000, '0', '0', '0', 'admin', '2023-06-21 16:33:06', 'admin', '2023-06-21 16:33:27');
INSERT INTO `sys_menu` VALUES (1671436500572057602, 'pig文档', NULL, 'https://www.yuque.com/pig4cloud/pig', -1, 'icon-tubiao1', 1001, '0', '0', '0', 'admin', '2023-06-21 16:34:50', 'admin', '2023-06-21 16:34:50');
INSERT INTO `sys_menu` VALUES (1682212735344873474, '采购管理', NULL, '/admin/bizbuy', 1669705907359711234, 'icon-quanxian', 0, '0', '0', '0', 'admin', '2023-07-21 10:15:45', 'admin', '2023-07-22 20:29:00');

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details`  (
                                             `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端ID',
                                             `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '资源列表',
                                             `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端密钥',
                                             `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '域',
                                             `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '认证类型',
                                             `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '重定向地址',
                                             `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色列表',
                                             `access_token_validity` int(0) NULL DEFAULT NULL COMMENT 'token 有效期',
                                             `refresh_token_validity` int(0) NULL DEFAULT NULL COMMENT '刷新令牌有效期',
                                             `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '令牌扩展字段JSON',
                                             `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否自动放行',
                                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                                             PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '终端信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
INSERT INTO `sys_oauth_client_details` VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('client', NULL, 'client', 'server', 'client_credentials', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('custom', NULL, 'custom', 'server', 'password,refresh_token', NULL, NULL, 10000, 6000, '', 'true', NULL, '2023-06-20 15:15:02', NULL, 'admin');
INSERT INTO `sys_oauth_client_details` VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('open', NULL, 'open', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'https://pig4cloud.com', NULL, NULL, NULL, NULL, 'false', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('pig', NULL, 'pig', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'https://pigx.vip', NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
                             `post_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
                             `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '岗位编码',
                             `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '岗位名称',
                             `post_sort` int(0) NOT NULL COMMENT '岗位排序',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新人',
                             `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注信息',
                             PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'user', '员工', 2, '0', '2022-03-19 10:05:15', 'admin', '2022-03-19 10:42:28', 'admin', '打工人');
INSERT INTO `sys_post` VALUES (2, 'cto', 'cto', 0, '0', '2022-03-19 10:06:20', 'admin', '2022-03-19 10:06:20', 'admin', 'cto666');
INSERT INTO `sys_post` VALUES (3, 'boss', '董事长', -1, '0', '2022-03-19 10:06:35', 'admin', '2022-03-19 10:42:44', 'admin', '大boss');

-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param`  (
                                     `public_id` bigint(0) NOT NULL COMMENT '编号',
                                     `public_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数名称',
                                     `public_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数键名',
                                     `public_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数键值',
                                     `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态，1-启用，0-禁用',
                                     `validate_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '校验码',
                                     `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT ' ' COMMENT '创建人',
                                     `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT ' ' COMMENT '修改人',
                                     `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                     `public_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '参数类型，1-系统参数，2-业务参数',
                                     `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否为系统内置参数，1-是，0-否',
                                     PRIMARY KEY (`public_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公共参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_public_param
-- ----------------------------
INSERT INTO `sys_public_param` VALUES (1, '接口文档不显示的字段', 'GEN_HIDDEN_COLUMNS', 'tenant_id', '0', '', ' ', ' ', '2020-05-12 04:25:19', NULL, '9', '1');
INSERT INTO `sys_public_param` VALUES (2, '注册用户默认角色', 'USER_DEFAULT_ROLE', 'GENERAL_USER', '0', '', 'admin', 'admin', '2022-03-30 10:00:57', '2022-03-30 02:05:59', '2', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                             `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
                             `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色代码',
                             `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色描述',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标识：0-正常，1-删除',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改人',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
                             PRIMARY KEY (`role_id`) USING BTREE,
                             UNIQUE INDEX `role_idx1_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '0', '2017-10-29 15:45:51', '2018-12-26 14:09:11', NULL, NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'GENERAL_USER', '普通用户', '0', '2022-03-30 09:59:24', '2022-03-30 09:59:24', 'admin', 'admin');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                                  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
                                  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1100);
INSERT INTO `sys_role_menu` VALUES (1, 1101);
INSERT INTO `sys_role_menu` VALUES (1, 1102);
INSERT INTO `sys_role_menu` VALUES (1, 1103);
INSERT INTO `sys_role_menu` VALUES (1, 1104);
INSERT INTO `sys_role_menu` VALUES (1, 1200);
INSERT INTO `sys_role_menu` VALUES (1, 1201);
INSERT INTO `sys_role_menu` VALUES (1, 1202);
INSERT INTO `sys_role_menu` VALUES (1, 1203);
INSERT INTO `sys_role_menu` VALUES (1, 1300);
INSERT INTO `sys_role_menu` VALUES (1, 1301);
INSERT INTO `sys_role_menu` VALUES (1, 1302);
INSERT INTO `sys_role_menu` VALUES (1, 1303);
INSERT INTO `sys_role_menu` VALUES (1, 1304);
INSERT INTO `sys_role_menu` VALUES (1, 1305);
INSERT INTO `sys_role_menu` VALUES (1, 1400);
INSERT INTO `sys_role_menu` VALUES (1, 1401);
INSERT INTO `sys_role_menu` VALUES (1, 1402);
INSERT INTO `sys_role_menu` VALUES (1, 1403);
INSERT INTO `sys_role_menu` VALUES (1, 1500);
INSERT INTO `sys_role_menu` VALUES (1, 1501);
INSERT INTO `sys_role_menu` VALUES (1, 1502);
INSERT INTO `sys_role_menu` VALUES (1, 1503);
INSERT INTO `sys_role_menu` VALUES (1, 1504);
INSERT INTO `sys_role_menu` VALUES (1, 1505);
INSERT INTO `sys_role_menu` VALUES (1, 2000);
INSERT INTO `sys_role_menu` VALUES (1, 2100);
INSERT INTO `sys_role_menu` VALUES (1, 2101);
INSERT INTO `sys_role_menu` VALUES (1, 2102);
INSERT INTO `sys_role_menu` VALUES (1, 2200);
INSERT INTO `sys_role_menu` VALUES (1, 2201);
INSERT INTO `sys_role_menu` VALUES (1, 2202);
INSERT INTO `sys_role_menu` VALUES (1, 2203);
INSERT INTO `sys_role_menu` VALUES (1, 2300);
INSERT INTO `sys_role_menu` VALUES (1, 2301);
INSERT INTO `sys_role_menu` VALUES (1, 2400);
INSERT INTO `sys_role_menu` VALUES (1, 2401);
INSERT INTO `sys_role_menu` VALUES (1, 2402);
INSERT INTO `sys_role_menu` VALUES (1, 2403);
INSERT INTO `sys_role_menu` VALUES (1, 2600);
INSERT INTO `sys_role_menu` VALUES (1, 2601);
INSERT INTO `sys_role_menu` VALUES (1, 2602);
INSERT INTO `sys_role_menu` VALUES (1, 2603);
INSERT INTO `sys_role_menu` VALUES (1, 2700);
INSERT INTO `sys_role_menu` VALUES (1, 2701);
INSERT INTO `sys_role_menu` VALUES (1, 2702);
INSERT INTO `sys_role_menu` VALUES (1, 2703);
INSERT INTO `sys_role_menu` VALUES (1, 3000);
INSERT INTO `sys_role_menu` VALUES (1, 3100);
INSERT INTO `sys_role_menu` VALUES (1, 3200);
INSERT INTO `sys_role_menu` VALUES (1, 3300);
INSERT INTO `sys_role_menu` VALUES (1, 3301);
INSERT INTO `sys_role_menu` VALUES (1, 3302);
INSERT INTO `sys_role_menu` VALUES (1, 3303);
INSERT INTO `sys_role_menu` VALUES (1, 3400);
INSERT INTO `sys_role_menu` VALUES (1, 4000);
INSERT INTO `sys_role_menu` VALUES (1, 9999);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367536);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367537);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367538);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367539);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367540);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367565);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367566);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367567);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367568);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367569);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367592);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367593);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367594);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367595);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367596);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367606);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367607);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367608);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367609);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367610);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367621);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367622);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367623);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367624);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367625);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367634);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367635);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367636);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367637);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367638);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367648);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367649);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367650);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367651);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367652);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367661);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367662);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367663);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367664);
INSERT INTO `sys_role_menu` VALUES (1, 1686920367665);
INSERT INTO `sys_role_menu` VALUES (1, 1669705907359711234);
INSERT INTO `sys_role_menu` VALUES (1, 1671436500572057602);
INSERT INTO `sys_role_menu` VALUES (1, 1682212735344873474);
INSERT INTO `sys_role_menu` VALUES (2, 4000);
INSERT INTO `sys_role_menu` VALUES (2, 9999);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `user_id` bigint(0) NOT NULL,
                             `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
                             `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
                             `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '随机盐',
                             `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '简介',
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
                             `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '部门ID',
                             `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，9-锁定',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0-正常，1-删除',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建者',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
                             PRIMARY KEY (`user_id`) USING BTREE,
                             INDEX `user_idx1_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$.WvBfiVgHEVDTae8U7TZa.tZrVmY.pjItFkDuESixN9yxhYpA1pVO', NULL, '17034642999', '', 1, '0', '0', '2018-04-20 07:15:18', '2023-07-11 19:02:25', NULL, 'admin');
INSERT INTO `sys_user` VALUES (1682625718325997569, 'wei', '$2a$10$XKPiEtzuywOo8KGqCoyIt.9/MXOxjqY24gYYua.u.DE4s7l5tFKeK', NULL, '15111111111', NULL, 4, '0', '0', '2023-07-22 13:36:48', '2023-07-22 14:34:04', 'admin', 'admin');
INSERT INTO `sys_user` VALUES (1689494089954664450, 'qgadmin', '$2a$10$.ZVQIHDEmQ9QNP2W5k7E5.IcRWO.mMduBVLb1BI3pgEoHXl1gbA9e', NULL, '15168213145', NULL, 5, '0', '0', '2023-08-10 12:29:15', '2023-08-10 12:29:15', 'admin', 'admin');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
                                  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (1682625718325997569, 1);
INSERT INTO `sys_user_post` VALUES (1689494089954664450, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1682625718325997569, 1);
INSERT INTO `sys_user_role` VALUES (1689494089954664450, 1);

SET FOREIGN_KEY_CHECKS = 1;
