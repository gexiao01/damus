// 文件名称: TargetContent.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 定向信息封装
 *
 * @author rocky.wang@ximalaya.com
 * @date 2015年4月1日
 */
public class TargetContent implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 5265254332675617161L;
	private List<Platform> targetPlatforms;
	private List<Integer> targetCategories;
	private List<Long> targetBroadcasters;
	private List<Long> targetAlbums;
	private TargetAppType targetAppType;
	private List<Integer> targetApps;
	private List<NetworkType> targetNetworks;
	private List<Operator> targetOperators;
	private List<Integer> targetRegions;
	private ZoneInfo targetZoneInfo;
	private int sort;
	private TargetAppVersion targetAppVersion;
	private TargetChannel targetChannel;

	public ZoneInfo getTargetZoneInfo() {
		return targetZoneInfo;
	}

	public void setTargetZoneInfo(ZoneInfo targetZoneInfo) {
		this.targetZoneInfo = targetZoneInfo;
	}

	public List<Platform> getTargetPlatforms() {
		return targetPlatforms;
	}

	public void setTargetPlatforms(List<Platform> targetPlatforms) {
		this.targetPlatforms = targetPlatforms;
	}

	public List<Integer> getTargetCategories() {
		return targetCategories;
	}

	public void setTargetCategories(List<Integer> targetCategories) {
		this.targetCategories = targetCategories;
	}

	public List<Long> getTargetBroadcasters() {
		return targetBroadcasters;
	}

	public void setTargetBroadcasters(List<Long> targetBroadcasters) {
		this.targetBroadcasters = targetBroadcasters;
	}

	public List<Long> getTargetAlbums() {
		return targetAlbums;
	}

	public void setTargetAlbums(List<Long> targetAlbums) {
		this.targetAlbums = targetAlbums;
	}

	public TargetAppType getTargetAppType() {
		return targetAppType;
	}

	public void setTargetAppType(TargetAppType targetAppType) {
		this.targetAppType = targetAppType;
	}

	public List<Integer> getTargetApps() {
		return targetApps;
	}

	public void setTargetApps(List<Integer> targetApps) {
		this.targetApps = targetApps;
	}

	public List<NetworkType> getTargetNetworks() {
		return targetNetworks;
	}

	public void setTargetNetworks(List<NetworkType> targetNetworks) {
		this.targetNetworks = targetNetworks;
	}

	public List<Operator> getTargetOperators() {
		return targetOperators;
	}

	public void setTargetOperators(List<Operator> targetOperators) {
		this.targetOperators = targetOperators;
	}

	public List<Integer> getTargetRegions() {
		return targetRegions;
	}

	public void setTargetRegions(List<Integer> targetRegions) {
		this.targetRegions = targetRegions;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public TargetAppVersion getTargetAppVersion() {
		return targetAppVersion;
	}

	public void setTargetAppVersion(TargetAppVersion targetAppVersion) {
		this.targetAppVersion = targetAppVersion;
	}

	public TargetChannel getTargetChannel() {
		return targetChannel;
	}

	public void setTargetChannel(TargetChannel targetChannel) {
		this.targetChannel = targetChannel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sort;
		result = prime * result + ((targetAlbums == null) ? 0 : targetAlbums.hashCode());
		result = prime * result + ((targetAppType == null) ? 0 : targetAppType.hashCode());
		result = prime * result + ((targetApps == null) ? 0 : targetApps.hashCode());
		result = prime * result + ((targetBroadcasters == null) ? 0 : targetBroadcasters.hashCode());
		result = prime * result + ((targetCategories == null) ? 0 : targetCategories.hashCode());
		result = prime * result + ((targetNetworks == null) ? 0 : targetNetworks.hashCode());
		result = prime * result + ((targetOperators == null) ? 0 : targetOperators.hashCode());
		result = prime * result + ((targetPlatforms == null) ? 0 : targetPlatforms.hashCode());
		result = prime * result + ((targetRegions == null) ? 0 : targetRegions.hashCode());
		result = prime * result + ((targetZoneInfo == null) ? 0 : targetZoneInfo.hashCode());
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
		TargetContent other = (TargetContent) obj;
		if (sort != other.sort) {
			return false;
		}
		if (targetAlbums == null) {
			if (other.targetAlbums != null) {
				return false;
			}
		} else if (!targetAlbums.equals(other.targetAlbums)) {
			return false;
		}
		if (targetAppType != other.targetAppType) {
			return false;
		}
		if (targetApps == null) {
			if (other.targetApps != null) {
				return false;
			}
		} else if (!targetApps.equals(other.targetApps)) {
			return false;
		}
		if (targetBroadcasters == null) {
			if (other.targetBroadcasters != null) {
				return false;
			}
		} else if (!targetBroadcasters.equals(other.targetBroadcasters)) {
			return false;
		}
		if (targetCategories == null) {
			if (other.targetCategories != null) {
				return false;
			}
		} else if (!targetCategories.equals(other.targetCategories)) {
			return false;
		}
		if (targetNetworks == null) {
			if (other.targetNetworks != null) {
				return false;
			}
		} else if (!targetNetworks.equals(other.targetNetworks)) {
			return false;
		}
		if (targetOperators == null) {
			if (other.targetOperators != null) {
				return false;
			}
		} else if (!targetOperators.equals(other.targetOperators)) {
			return false;
		}
		if (targetPlatforms == null) {
			if (other.targetPlatforms != null) {
				return false;
			}
		} else if (!targetPlatforms.equals(other.targetPlatforms)) {
			return false;
		}
		if (targetRegions == null) {
			if (other.targetRegions != null) {
				return false;
			}
		} else if (!targetRegions.equals(other.targetRegions)) {
			return false;
		}
		if (targetZoneInfo == null) {
			if (other.targetZoneInfo != null) {
				return false;
			}
		} else if (!targetZoneInfo.equals(other.targetZoneInfo)) {
			return false;
		}
		return true;
	}
}
