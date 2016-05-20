CREATE TABLE ad_material (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) NOT NULL COMMENT '计划名',
  `schedules` varchar(512) NOT NULL COMMENT '7*24投放安排',
  `target_content` text NOT NULL COMMENT '定向配置',
  `position_id` INT(11) NOT NULL DEFAULT 0 COMMENT '位置id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE position (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '广告位id',
  `title` varchar(64) NOT NULL COMMENT '广告位标题',
  `app_type` varchar(20) NOT NULL COMMENT '所属app类型',
  `upload_size` TEXT NOT NULL COMMENT '上传尺寸',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sub_app (
  `id` int(11) NOT NULL COMMENT '子appid',
  `name` varchar(64) NOT NULL COMMENT '子app名称',
  `package` varchar(64) COMMENT 'app包名',
  `platform` varchar(64) NOT NULL COMMENT '平台',
  UNIQUE KEY `sub_app_platform_name` (platform, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


