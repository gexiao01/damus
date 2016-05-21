package com.ximalaya.damus.example.ad.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ximalaya.damus.example.ad.bo.Position;
import com.ximalaya.damus.example.ad.mapper.PositionMapper;
import com.ximalaya.damus.protocol.config.Constant;

@Component
public class PositionServiceImpl implements InitializingBean {
	@Autowired
	private PositionMapper positionMapper;

	private Map<Integer, String> localCache;

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Position> positions = positionMapper.getAllPositions();
		localCache = new HashMap<Integer, String>();
		for (Position position : positions) {
			localCache.put(position.getId(), position.getTitle());
		}
	}

	public String getNamesById(int positionId) {
		return localCache.containsKey(positionId) ? localCache.get(positionId) : Constant.UNKNOWN_VALUE;
	}
}
