CREATE TABLE material (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `user_id` int(11) NOT NULL COMMENT '广告主id',
  `name` varchar(64) NOT NULL,
  `material_type` varchar(64) NOT NULL COMMENT '物料类型',
  `material_content` text NOT NULL COMMENT '物料内容，json形式',
  `monitor_content` text NOT NULL COMMENT '监测信息',
  `status` varchar(64) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

CREATE TABLE plan (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) NOT NULL COMMENT '广告主id',
  `name` varchar(64) NOT NULL COMMENT '计划名',
  `budget` decimal(20,6) NULL COMMENT '每日预算',
  `status` varchar(64) NOT NULL COMMENT '计划状态',
  `start_time` datetime NOT NULL COMMENT '投放开始时间',
  `end_time` datetime NOT NULL COMMENT '投放结束时间',
  `schedules` varchar(512) NOT NULL COMMENT '7*24投放安排',
  `material_type` varchar(64) NOT NULL COMMENT '物料类型',
  `charge_method` varchar(64) COMMENT '计费方式',
  `finance_status` varchar(64) COMMENT '财务方式',
  `price` decimal(20,6) COMMENT '单价',
  `person_incharge` VARCHAR(20) NOT NULL DEFAULT '[]',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE unit (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `ad_type` varchar(64) NOT NULL COMMENT '广告类型',
  `target_content` text NOT NULL COMMENT '定向配置',
  `position_id` INT(11) NOT NULL DEFAULT 0 COMMENT '位置id',
  `show_style` INT(11) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE position (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '广告位id',
  `title` varchar(64) NOT NULL COMMENT '广告位标题',
  `app_type` varchar(20) NOT NULL COMMENT '所属app类型',
  `names` varchar(128) NOT NULL COMMENT '广告位名称',
  `upload_size` TEXT NOT NULL COMMENT '上传尺寸',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sub_app (
  `id` int(11) NOT NULL COMMENT '子appid',
  `name` varchar(64) NOT NULL COMMENT '子app名称',
  `package` varchar(64) COMMENT 'app包名',
  `platform` varchar(64) NOT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  UNIQUE KEY `sub_app_platform_name` (platform, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `province_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province_code` int(11) DEFAULT NULL,
  `province_name` varchar(32) DEFAULT NULL,
  `province_type` int(11) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `status` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `city_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(32) DEFAULT NULL,
  `city_type` varchar(32) NOT NULL DEFAULT 'OTHER' COMMENT 'MAJORCITIES:一线城市,METROPOLIS:二线城市,OTHER:其它',
  `province_id` int(11) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
