// Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad.bo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年10月22日
 */
public final class Position {
	private int id;
	private String title;
	private List<String> names;
	private String namesJsonString;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getNamesJsonString() {
		return namesJsonString;
	}

	public void setNamesJsonString(String namesJsonString) {
		this.namesJsonString = namesJsonString;
	}

}
