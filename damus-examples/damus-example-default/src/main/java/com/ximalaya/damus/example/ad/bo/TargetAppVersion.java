// 文件名称: TargetAppVersionType.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年8月14日
 */
public class TargetAppVersion implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -4048611708443894897L;

	public enum TargetAppVersionType {
		ALL_APP_VERSION, PART_APP_VERSION;
	}

	private TargetAppVersionType targetAppVersionType;
	private List<AppVersion> targetAppVersions;

	public TargetAppVersion() {
		super();
	}

	public TargetAppVersion(TargetAppVersionType targetAppVersionType, List<AppVersion> targetAppVersions) {
		super();
		this.targetAppVersionType = targetAppVersionType;
		this.targetAppVersions = targetAppVersions;
	}

	public List<AppVersion> getTargetAppVersions() {
		return targetAppVersions;
	}

	public void setTargetAppVersions(List<AppVersion> targetAppVersions) {
		this.targetAppVersions = targetAppVersions;
	}

	public TargetAppVersionType getTargetAppVersionType() {
		return targetAppVersionType;
	}

	public void setTargetAppVersionType(TargetAppVersionType targetAppVersionType) {
		this.targetAppVersionType = targetAppVersionType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public enum AppVersion {
		VERSION_2X("2.X+版本"), VERSION_3X("3.X+版本"), VERSION_4X("4.X+版本"), OTHER("其他版本");
		private static int INVALID_INT = -1;
		private String showName;

		private AppVersion(String showName) {
			this.showName = showName;
		}

		public String getShowName() {
			return showName;
		}

		public static AppVersion fromName(String version) {
			String[] targetVersiontmp = version.split("\\.");
			if (targetVersiontmp.length <= 0) {
				return OTHER;
			}

			Integer targetVersionId = NumberUtils.toInt(targetVersiontmp[0], INVALID_INT);
			if (targetVersionId == 2) {
				return VERSION_2X;
			} else if (targetVersionId == 3) {
				return VERSION_3X;
			} else if (targetVersionId == 4) {
				return VERSION_4X;
			}
			return OTHER;
		}
	}

}
