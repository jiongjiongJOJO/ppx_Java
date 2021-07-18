package com.akari.ppx.xp.hook.code.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.common.constant.Const;
import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.lang.reflect.Proxy;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Const.AUTHOR_ID;
import static com.akari.ppx.common.utils.Utils.joinQQGroup;
import static com.akari.ppx.common.utils.Utils.showDialogXP;
import static com.akari.ppx.common.utils.Utils.showError;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class MiscHook extends SuperbHook {
	private void saveCurVersion(SharedPreferences sp) {
		sp.edit().putInt("version", BuildConfig.VERSION_CODE).apply();
	}

	@Override
	protected void onHook(ClassLoader cl) {
		final boolean[] executed = new boolean[1];
		hookMethod("com.sup.android.base.MainActivity", "onWindowFocusChanged", boolean.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (!executed[0]) {
					Context context = (Context) param.thisObject;
					if (SuperbHook.ex != null)
						showError(cl, context);
					Object UserCenterService = callStaticMethod(findClass("com.sup.android.module.usercenter.UserCenterService", cl), "getInstance");
					//callMethod(UserCenterService, "cancelBlockUser", AUTHOR_ID, null);
					Object callback = Proxy.newProxyInstance(cl, new Class[]{findClass("com.sup.android.mi.usercenter.AsyncCallback", cl)}, (proxy, method, args) -> {
						int status = (int) callMethod(args[0], "getStatusCode");
						if (status == 13016 || status == 13018) {
							Utils.showSystemToastXP(cl, Const.BANNED);
							new Handler().postDelayed(() -> {
								android.os.Process.killProcess(android.os.Process.myPid());
								System.exit(0);
							}, 5000);
						}
						return null;
					});
					callMethod(UserCenterService, "follow", 1, AUTHOR_ID, callback);
					SharedPreferences sp = context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0);
					if (sp.getInt("version", 0) < BuildConfig.VERSION_CODE)
						showDialogXP(cl, context, "皮皮虾助手 " + BuildConfig.VERSION_NAME, "激活成功，欢迎皮友使用！\n聊天&反馈&提建议请加下方QQ群"
								, "我才不要", v -> saveCurVersion(sp)
								, "点我进群", v -> {
									saveCurVersion(sp);
									joinQQGroup(context);
								});
					executed[0] = true;
				}
			}
		});
	}
}