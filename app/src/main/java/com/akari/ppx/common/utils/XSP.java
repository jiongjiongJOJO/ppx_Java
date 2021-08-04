package com.akari.ppx.common.utils;

import com.akari.ppx.common.constant.Prefs;

import de.robv.android.xposed.XSharedPreferences;

public class XSP {
	public static XSharedPreferences xsp;

	public static void initXSP(XSharedPreferences xsp) {
		XSP.xsp = xsp;
	}

	public static boolean get(Prefs prefs) {
		return xsp.getBoolean(prefs.getKey(), false);
	}

	public static String gets(Prefs prefs) {
		return xsp.getString(prefs.getKey(), "");
	}

	public static int getI(Prefs prefs) {
		return Integer.parseInt(gets(prefs));
	}

	public static float getF(Prefs prefs) {
		return Float.parseFloat(gets(prefs));
	}
}