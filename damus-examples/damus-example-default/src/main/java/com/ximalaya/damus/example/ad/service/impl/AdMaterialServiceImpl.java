package com.ximalaya.damus.example.ad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ximalaya.damus.common.util.JsonUtils;
import com.ximalaya.damus.example.ad.bo.AdMaterial;
import com.ximalaya.damus.example.ad.bo.Schedules;
import com.ximalaya.damus.example.ad.bo.TargetContent;
import com.ximalaya.damus.example.ad.mapper.AdMaterialMapper;
import com.ximalaya.damus.example.ad.service.AdMaterialService;

@Component
public class AdMaterialServiceImpl implements AdMaterialService {

	@Autowired
	private AdMaterialMapper materialMapper;

	public AdMaterial getMaterialById(long id) {
		AdMaterial material = materialMapper.getMaterialById(id);

		material.setSchedules(JsonUtils.fromJsonString(Schedules.class, material.getScheduleJsonString()));
		material.setScheduleJsonString(null);

		material.setTargetContent(JsonUtils.fromJsonString(TargetContent.class, material.getTargetContentJsonString()));
		material.setTargetContentJsonString(null);

		return material;
	}

}
