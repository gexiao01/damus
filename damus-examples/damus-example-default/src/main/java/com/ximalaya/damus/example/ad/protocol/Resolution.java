package com.ximalaya.damus.example.ad.protocol;

import java.io.Serializable;

public class Resolution implements Serializable {

	private static final long serialVersionUID = -9174139920327229244L;
	private int height;
	private int width;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return height + "*" + width;
	}
}
