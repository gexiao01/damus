// 文件名称: Schedules.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年4月1日
 */
public class Schedules implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -8376020767838618677L;
	private static final int ALL_DAY_SCHEMA = 0xFFFFFF;
	private static final int WEEKDAY_COUNT = 7;

	private boolean useSchedule = true;
	private Map<Integer, Integer> scheduleMap = Maps.newHashMap();

	public Schedules() {
		for (int i = 0; i < WEEKDAY_COUNT; i++) {
			scheduleMap.put(i, ALL_DAY_SCHEMA);
		}
	}

	public Map<Integer, Integer> getScheduleMap() {
		return scheduleMap;
	}

	public void setScheduleMap(Map<Integer, Integer> scheduleMap) {
		this.scheduleMap = scheduleMap;
	}

	public void setSchedule(int weekday, int schema) {
		Preconditions.checkArgument(weekday >= 0 && weekday < WEEKDAY_COUNT);

		scheduleMap.put(weekday, schema);
	}

	public boolean isUseSchedule() {
		return useSchedule;
	}

	public void setUseSchedule(boolean useSchedule) {
		this.useSchedule = useSchedule;
	}

	public boolean valid(int dayInWeek, int hour) {
		if (!useSchedule) {
			return true;
		}
		int schedule = scheduleMap.get(dayInWeek);
		return (schedule & (1 << hour)) > 0;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scheduleMap == null) ? 0 : scheduleMap.hashCode());
		result = prime * result + (useSchedule ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Schedules other = (Schedules) obj;
		if (scheduleMap == null) {
			if (other.scheduleMap != null) {
				return false;
			}
		} else if (!scheduleMap.equals(other.scheduleMap)) {
			return false;
		}
		if (useSchedule != other.useSchedule) {
			return false;
		}
		return true;
	}

}
