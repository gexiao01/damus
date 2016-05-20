package com.ximalaya.damus.example.ad.bo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ZoneInfo implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -8037754140342643926L;
	private Boolean isFull; // include unknown
	private ZoneType type;
	private List<Integer> value;

	public Boolean isFull() {
		return isFull;
	}

	public void setIsFull(Boolean isFull) {
		this.isFull = isFull;
	}

	public ZoneType getType() {
		return type;
	}

	public void setType(ZoneType type) {
		this.type = type;
	}

	public List<Integer> getValue() {
		return value;
	}

	public void setValue(List<Integer> value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}