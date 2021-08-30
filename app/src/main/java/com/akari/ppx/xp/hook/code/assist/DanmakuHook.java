package com.akari.ppx.xp.hook.code.assist;

import com.akari.ppx.xp.hook.code.SuperbHook;

import java.lang.reflect.Proxy;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.QUERY_DANMAKU_SENDER;
import static com.akari.ppx.common.constant.Prefs.UNLOCK_DANMAKU;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;
import static de.robv.android.xposed.XposedHelpers.setBooleanField;

public class DanmakuHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		hookMethod(UNLOCK_DANMAKU, "com.sup.android.mi.usercenter.model.UserInfo", "getUserPrivilege", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				setBooleanField(param.getResult(), "canSendAdvanceDanmaku", true);
			}
		});
		hookMethod(QUERY_DANMAKU_SENDER, "com.sup.android.m_danmaku.widget.l", "b", "com.sup.android.m_danmaku.danmaku.model.d", float.class, float.class, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) {
				callMethod(newInstance(findClass("com.sup.android.module.usercenter.b.e", cl)), "a", getLongField(param.args[0], "V"), Proxy.newProxyInstance(cl, new Class[]{findClass("com.sup.android.mi.usercenter.AsyncCallback", cl)}, (proxy, method, args) -> callMethod(callStaticMethod(findClass("com.bytedance.router.SmartRouter", cl), "buildRoute", callMethod(getObjectField(param.thisObject, "b"), "getContext"), callMethod(callMethod(args[0], "getData"), "getProfileSchema")), "open")));
				return null;
			}
		});

	}
}
