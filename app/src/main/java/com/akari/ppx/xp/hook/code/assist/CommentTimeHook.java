package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.EXACT_COMMENT_TIME_FORMAT;
import static com.akari.ppx.common.constant.Prefs.RECENT_COMMENT_TIME_FORMAT;
import static com.akari.ppx.common.constant.Prefs.SHOW_COMMENT_TIME;

public class CommentTimeHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final ArrayList<String> recentFormatList = new ArrayList<>();
		final String exactFormat = XSP.gets(EXACT_COMMENT_TIME_FORMAT);
		Utils.str2list(XSP.gets(RECENT_COMMENT_TIME_FORMAT), recentFormatList);
		hookMethod(SHOW_COMMENT_TIME, "com.sup.superb.m_feedui_common.util.a", "a", long.class, "kotlin.jvm.functions.Function0", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				try {
					return Utils.ts2date((Long) param.args[0], recentFormatList.get(Utils.getDiffDays((Long) param.args[0])), false);
				} catch (Exception ignored) {
					return Utils.ts2date((Long) param.args[0], exactFormat, false);
				}
			}
		});
	}
}
