package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.REMOVE_ADS;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class AdHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(REMOVE_ADS)) return;
		hookMethod("com.sup.android.mi.feed.repo.bean.ad.AdFeedCell", "getAdInfo", XC_MethodReplacement.returnConstant(null));
		hookMethod("com.sup.android.superb.m_ad.initializer.c", "b", XC_MethodReplacement.returnConstant(false));
		hookMethod("com.sup.android.m_mine.utils.e", "b", XC_MethodReplacement.DO_NOTHING);
		hookMethod("com.sup.android.base.model.BannerModel", "getBannerData", XC_MethodReplacement.returnConstant(null));
		hookMethod("com.sup.android.m_mine.view.subview.d", "a", ArrayList.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				List beforeList = (List) param.args[0], afterList = new ArrayList();
				for (Object item : beforeList) {
					String params = (String) callMethod(item, "getEventParams");
					if (params != null && (params.contains("comment_identify") || params.contains("novel") || params.contains("option")))
						afterList.add(item);
				}
				param.args[0] = afterList;
			}
		});
	}
}
