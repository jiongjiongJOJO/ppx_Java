package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.DIGG_STYLE;
import static com.akari.ppx.common.constant.Prefs.DISS_STYLE;
import static com.akari.ppx.common.constant.Prefs.MODIFY_INTERACTION_STYLE;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class InteractionStyleHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(MODIFY_INTERACTION_STYLE)) return;
		int diggStyle = XSP.getI(DIGG_STYLE), dissStyle = XSP.getI(DISS_STYLE);
		hookMethod("com.sup.android.mi.feed.repo.bean.cell.AbsFeedItem$ItemRelation", "getDiggType", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (!(boolean) callMethod(param.thisObject, "isLike"))
					param.setResult(diggStyle);
			}
		});
		hookMethod("com.sup.android.mi.feed.repo.bean.cell.AbsFeedItem$ItemRelation", "getDissType", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (!(boolean) callMethod(param.thisObject, "isDiss"))
					param.setResult(dissStyle);
			}
		});
	}
}
