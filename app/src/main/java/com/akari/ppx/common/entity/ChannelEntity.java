package com.akari.ppx.common.entity;

import com.akari.ppx.common.constant.Const;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class ChannelEntity implements Serializable {
	@SerializedName("name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isParentChannel() {
		return Arrays.asList(Const.CATEGORY_LIST_NAME).indexOf(name) < 4;
	}
}
