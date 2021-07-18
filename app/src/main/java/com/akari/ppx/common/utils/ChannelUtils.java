package com.akari.ppx.common.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.entity.ChannelEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.akari.ppx.common.constant.Prefs.USE_NEW_CATEGORY_LIST;

public class ChannelUtils {
	private static SharedPreferences preferences;

	public static void initSP(SharedPreferences preferences) {
		ChannelUtils.preferences = preferences;
	}

	public static boolean isNewCategory() {
		return preferences.getBoolean(USE_NEW_CATEGORY_LIST.getKey(), false);
	}

	@SuppressLint("ApplySharedPref")
	public static void setSPDataList(Prefs prefs, List<ChannelEntity> list) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(prefs.getKey(), new Gson().toJson(list));
		editor.apply();
	}

	public static List<ChannelEntity> getSPDataList(Prefs prefs) {
		return getDataList(preferences.getString(prefs.getKey(), ""));
	}

	@SuppressLint("ApplySharedPref")
	public static void setDefaultChannel(String name) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Prefs.DEFAULT_CHANNEL.getKey(), name);
		editor.apply();
	}

	@SuppressLint("ApplySharedPref")
	public static void setDefaultChannelNew(String name) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Prefs.DEFAULT_CHANNEL_NEW.getKey(), name);
		editor.apply();
	}

	public static List<ChannelEntity> getDataList(String content) {
		try {
			List<ChannelEntity> dataList;
			Gson gson = new Gson();
			dataList = gson.fromJson(content, new TypeToken<List<ChannelEntity>>() {
			}.getType());
			return dataList;
		} catch (Exception e) {
			return null;
		}
	}
}
