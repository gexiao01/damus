package com.ximalaya.damus.example.ad.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ximalaya.damus.actuary.service.RequestParser;
import com.ximalaya.damus.example.ad.bo.AdMaterial;
import com.ximalaya.damus.example.ad.bo.NetworkType;
import com.ximalaya.damus.example.ad.bo.Operator;
import com.ximalaya.damus.example.ad.bo.Platform;
import com.ximalaya.damus.example.ad.bo.Schedules;
import com.ximalaya.damus.example.ad.bo.TargetAppType;
import com.ximalaya.damus.example.ad.bo.TargetAppVersion;
import com.ximalaya.damus.example.ad.bo.TargetAppVersion.AppVersion;
import com.ximalaya.damus.example.ad.bo.TargetAppVersion.TargetAppVersionType;
import com.ximalaya.damus.example.ad.bo.TargetContent;
import com.ximalaya.damus.example.ad.bo.ZoneInfo;
import com.ximalaya.damus.example.ad.bo.ZoneType;
import com.ximalaya.damus.example.ad.service.AdMaterialService;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.request.CalcRequest;
import com.ximalaya.damus.protocol.resource.Resource;

@Component
public class AdRequestParser implements RequestParser<Integer> {

	@Autowired
	@Qualifier("dimDictResource")
	private Resource<DimDict> dict;
	@Autowired
	private PositionServiceImpl positionService;
	@Autowired
	private AdMaterialService adMaterialService;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public CalcRequest parseCalcRequest(Integer materialId) {

		logger.info("parseCalcRequest Start: " + materialId);
		AdMaterial material = adMaterialService.getMaterialById(materialId);

		TargetContent targetContent = material.getTargetContent();

		CalcRequest calcRequest = new CalcRequest();
		parsePosition(calcRequest, material.getPositionId());
		parseHour(calcRequest, material.getSchedules());
		parseOS(calcRequest, targetContent.getTargetPlatforms());
		parseCarrier(calcRequest, targetContent.getTargetOperators());
		parseApp(calcRequest, targetContent.getTargetAppType(), targetContent.getTargetApps());
		parsePackage(calcRequest);
		parseVersion(calcRequest, targetContent.getTargetAppVersion());
		parseZoneInfo(calcRequest, targetContent.getTargetZoneInfo());
		parseNetwork(calcRequest, targetContent.getTargetNetworks());

		logger.info("parseCalcRequest Finish: " + calcRequest);
		return calcRequest;
	}

	private void parseNetwork(CalcRequest calcRequest, List<NetworkType> targetNetworks) {
		Map<String, Long> valueMap = getValueMap(DimType.NETWORK);

		for (NetworkType network : targetNetworks) {
			for (String name : network.getNames()) {
				if (valueMap.containsKey(name)) {
					calcRequest.add(DimType.NETWORK, valueMap.get(name));
				}
			}
		}

		calcRequest.add(DimType.NETWORK, Constant.NOT_FOUND_ID);
		if (targetNetworks.contains(NetworkType.OTHER)) {
			// explicitly target unknown
			calcRequest.add(DimType.NETWORK, Constant.UNKNOWN_ID);
		}

	}

	private void parsePackage(CalcRequest calcRequest) {
		// TODO this dim is not supported for target, so treat as ALL, to be
		// supported later
	}

	private void parsePosition(CalcRequest calcRequest, int positionId) {
		Map<String, Long> valueMap = getValueMap(DimType.POSITION);

		String title = positionService.getNamesById(positionId);
		if (valueMap.containsKey(title)) {
			calcRequest.add(DimType.POSITION, valueMap.get(title));
		}

		// add a NOT_FOUND token unless explicitly targeted to "ALL"
		calcRequest.add(DimType.POSITION, Constant.NOT_FOUND_ID);
	}

	private void parseZoneInfo(CalcRequest calcRequest, ZoneInfo zoneInfo) {
		Map<String, Long> valueMapProvince = getValueMap(DimType.PROVINCE);
		Map<String, Long> valueMapCity = getValueMap(DimType.CITY);

		if (zoneInfo == null) {
			return; // ALL
		}

		ZoneType zoneType = zoneInfo.getType();
		List<Integer> zoneIds = zoneInfo.getValue();
		if (ZoneType.BYPROVINCE == zoneType) { // PROVINCE
			calcRequest.add(DimType.PROVINCE, Constant.NOT_FOUND_ID);
			if (zoneInfo.isFull()) { // explictly target unknown
				calcRequest.add(DimType.PROVINCE, Constant.UNKNOWN_ID);
			}
			for (Integer provinceId : zoneIds) {
				if (valueMapProvince.get(provinceId + "") != null) {
					calcRequest.add(DimType.PROVINCE, valueMapProvince.get(provinceId + ""));
				}
			}
		} else if (ZoneType.BYCITY == zoneType) { // CITY
			calcRequest.add(DimType.CITY, Constant.NOT_FOUND_ID);
			if (zoneInfo.isFull()) { // explictly target unknown
				calcRequest.add(DimType.CITY, Constant.UNKNOWN_ID);
			}
			for (Integer cityId : zoneIds) {
				if (valueMapCity.get(cityId + "") != null) {
					calcRequest.add(DimType.CITY, valueMapCity.get(cityId + ""));
				}
			}
		} else {
			throw new RuntimeException("parseZoneInfo exception");
		}
	}

	private void parseVersion(CalcRequest calcRequest, TargetAppVersion targetAppVersion) {
		Map<String, Long> valueMapVersion = getValueMap(DimType.VERSION);
		if (TargetAppVersionType.ALL_APP_VERSION == targetAppVersion.getTargetAppVersionType()) {
			return;
		}

		calcRequest.add(DimType.VERSION, Constant.NOT_FOUND_ID);

		if (TargetAppVersionType.PART_APP_VERSION == targetAppVersion.getTargetAppVersionType()) {
			List<AppVersion> targetAppVersions = targetAppVersion.getTargetAppVersions();

			for (String versionString : valueMapVersion.keySet()) {
				AppVersion version = AppVersion.fromName(versionString);
				if (targetAppVersions.contains(version)) {
					calcRequest.add(DimType.VERSION, valueMapVersion.get(versionString));
				}
			}

			if (targetAppVersions.contains(AppVersion.OTHER)) {
				// explicitly target unknown
				calcRequest.add(DimType.VERSION, Constant.UNKNOWN_ID);
			}
		} else {
			throw new RuntimeException("parseVersion exception");
		}
	}

	// XXX 与unknown_id有冲突，不过app不会未识别
	private static final long TING_APPID = 0L;

	// APP
	private void parseApp(CalcRequest calcRequest, TargetAppType targetAppType, List<Integer> targetApps) {

		if (TargetAppType.ALL == targetAppType) { // ALL
			return;
		} else if (TargetAppType.ALL_SUBAPP == targetAppType) {
			calcRequest.exclude(DimType.APP, TING_APPID);
		} else if (TargetAppType.TING == targetAppType) {
			calcRequest.add(DimType.APP, TING_APPID);
		} else if (TargetAppType.PART_SUBAPP == targetAppType) {
			calcRequest.add(DimType.APP, Constant.NOT_FOUND_ID);
			if (targetApps == null) {
				return;
			}

			for (Integer appId : targetApps) {
				calcRequest.add(DimType.APP, appId.longValue());
			}
		} else {
			throw new RuntimeException("parseApp exception");
		}

	}

	// CARRIER
	private void parseCarrier(CalcRequest calcRequest, List<Operator> operators) {
		Map<String, Long> valueMap = getValueMap(DimType.CARRIER);

		for (Operator operator : operators) {
			if (valueMap.containsKey("" + operator.getCode())) {
				calcRequest.add(DimType.CARRIER, valueMap.get("" + operator.getCode()));
			}
		}

		calcRequest.add(DimType.CARRIER, Constant.NOT_FOUND_ID);
		if (operators.contains(Operator.OTHER)) {
			// explicitly target unknown
			calcRequest.add(DimType.CARRIER, Constant.UNKNOWN_ID);
		}
	}

	// OS
	private void parseOS(CalcRequest calcRequest, List<Platform> platforms) {
		Map<String, Long> valueMap = getValueMap(DimType.OS);
		for (Platform platform : platforms) {
			for (String name : platform.getNames()) {
				if (valueMap.containsKey(name.toLowerCase())) {
					calcRequest.add(DimType.OS, valueMap.get(name.toLowerCase()));
				}
			}
		}

		calcRequest.add(DimType.OS, Constant.NOT_FOUND_ID);
		if (platforms.contains(Platform.OTHER)) {
			// explicitly target unknown
			calcRequest.add(DimType.OS, Constant.UNKNOWN_ID);
		}
	}

	// HOUR
	private void parseHour(CalcRequest calcRequest, Schedules schedules) {
		Date date = new Date();
		int dayInWeek = getDayInWeek(date);
		for (int hour = 0; hour < 24; hour++) {
			if (schedules.valid(dayInWeek, hour)) {
				calcRequest.add(DimType.HOUR, hour);
			}
		}
	}

	private int getDayInWeek(Date date) {
		Calendar calendar = DateUtils.toCalendar(date);
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// TODO the delta tobe configed
		if (dayInWeek == 1) {
			return 6;
		} else {
			return dayInWeek - 2;
		}
	}

	private Map<String, Long> getValueMap(DimType dimType) {
		return dict.get().getDimMeta(dimType).getValueMap();
	}
}
