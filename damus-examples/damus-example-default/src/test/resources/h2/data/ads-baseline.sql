insert into `plan` (`id`,`user_id`,`name`,`budget`,`status`,`start_time`,`end_time`,`schedules`,`material_type`) values
    (1,1,'soundPatch计划',100.2,'ENABLED','2015-07-09 00:00:00','2015-12-31 23:59:59','{"useSchedule":true,"scheduleMap":{"0":16777215,"1":16777215,"2":16777215,"3":16777215,"4":16777215,"5":16777215,"6":16777215}}','SOUND_PATCH');

insert into `unit` (`id`,`plan_id`,`user_id`,`ad_type`,`position_id`,`target_content`) values
    (1,1,1,'SOUND_PATCH',1,'{"targetPlatforms":["IOS","ANDROID","IPAD"],"targetNetworks":["MOBILE2G","MOBILE3G","MOBILE4G","WIFI","OTHER"],"targetOperators":["YIDONG","LIANTONG","DIANXIN","OTHER"],"targetCategories":[2,14],"targetBroadcasters":null,"targetAlbums":null,"targetRegions":[110000,120000]}');

insert into `material` (`id`,`plan_id`,`user_id`,`name`,`material_type`,`material_content`,`monitor_content`,`status`) values
    (1,1,1,'史上最牛物料','POSITION_JUMP','{"pictureUrls":{"130*130":"http://fdfs.xmcdn.com/group12/M00/DF/F3/wKgDXFaPQpiCiylgAABx5mapcwo026.png","195*195":"http://fdfs.xmcdn.com/group12/M00/DF/F3/wKgDXFaPQpeBObVlAACYdkkqbIA028.jpg"},"filterInstalledUser":"","description":"有身份证就能贷款！1分钟成功申请，当天放款！","loadingShowTime":null,"clickAction":"OPEN_INTERNAL","targetUrl":"http://m.rong360.com/express?from=sem21&utm_source=xmly&utm_medium=flxxl&utm_campaign=sem21"}','{"showMonitorProvider":"NO","showMonitorUrl":""}','ENABLED');
