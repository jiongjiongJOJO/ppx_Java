package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.Locale;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.SET_COLOR_COMMENT;
import static com.akari.ppx.common.constant.Prefs.SET_COLOR_TYPE;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

public class ColorCommentHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (!XSP.get(SET_COLOR_COMMENT)) return;
		final int colorType = XSP.getI(SET_COLOR_TYPE);
		hookMethod("com.sup.android.module.publish.publish.b", "f", "com.sup.android.mi.publish.bean.PublishBean", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				String text = (String) callMethod(param.args[0], "getText");
				if (!text.contains("[/b]"))
					setObjectField(param.args[0], "text", String.format(Locale.CHINA, "[b type=%d id＝@]%s[/b]", colorType, text));
			}
		});
	}
}
