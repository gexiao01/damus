// 文件名称: AdMaterial.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年4月2日
 */
public class AdMaterial implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -5621219827315940972L;

	private long id;
	private String name;
	private int positionId;

	private String scheduleJsonString;
	private Schedules schedules;

	private String targetContentJsonString;
	private TargetContent targetContent;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public String getScheduleJsonString() {
		return scheduleJsonString;
	}

	public void setScheduleJsonString(String scheduleJsonString) {
		this.scheduleJsonString = scheduleJsonString;
	}

	public Schedules getSchedules() {
		return schedules;
	}

	public void setSchedules(Schedules schedules) {
		this.schedules = schedules;
	}

	public String getTargetContentJsonString() {
		return targetContentJsonString;
	}

	public void setTargetContentJsonString(String targetContentJsonString) {
		this.targetContentJsonString = targetContentJsonString;
	}

	public TargetContent getTargetContent() {
		return targetContent;
	}

	public void setTargetContent(TargetContent targetContent) {
		this.targetContent = targetContent;
	}
}
