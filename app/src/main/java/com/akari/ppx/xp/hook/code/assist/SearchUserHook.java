package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.UNLOCK_SEARCH_USER;

public class SearchUserHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(UNLOCK_SEARCH_USER)) return;
		final boolean[] entered = new boolean[1];
		hookMethod("com.sup.android.module.profile.view.ProfileParentFragment", "p", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				entered[0] = true;
			}

			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				entered[0] = false;
			}
		});
		hookMethod("com.sup.android.module.profile.d", "a", long.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (entered[0])
					param.setResult(true);
			}
		});
		hookMethod("com.sup.android.module.profile.search.d", "a", String.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				if (entered[0])
					param.args[0] = ((String) param.args[0]).replace("我", "他");
			}
		});
	}
}
