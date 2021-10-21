package com.akari.ppx.xp.hook.code.assist;

import android.content.Context;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

import static com.akari.ppx.common.constant.Prefs.SHOW_REGISTER_ESCAPE_TIME;
import static com.akari.ppx.common.constant.Prefs.SHOW_USER_ID;
import static de.robv.android.xposed.XposedBridge.invokeOriginalMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class AchievementHook extends SuperbHook {
	protected void onHook(ClassLoader cl) {
		final boolean showRegisterEscapeTime = XSP.get(SHOW_REGISTER_ESCAPE_TIME), showUserId = XSP.get(SHOW_USER_ID);
		if (!showRegisterEscapeTime && !showUserId) return;
		hookMethod("com.sup.android.mi.usercenter.model.UserInfo", "getAchievements", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				ArrayList achievements = (ArrayList) getObjectField(param.thisObject, "achievements");
				if (achievements == null)
					achievements = new ArrayList();
				if (showRegisterEscapeTime) {
					long createTime = getLongField(param.thisObject, "createTime");
					if (createTime != 0) {
						tryAddAchievement(achievements, "注册:", Utils.ts2date(createTime, "yyyy-MM-dd HH:mm:ss", false));
						try {
							long expireTime = getLongField(getObjectField(((ArrayList) callMethod(param.thisObject, "getPunishmentList")).get(0), "status"), "expireTime");
							if (expireTime != -1)
								tryAddAchievement(achievements, "出黑屋:", Utils.ts2date(expireTime, "yyyy-MM-dd HH:mm:ss", false));
						} catch (Exception ignored) {
						}
					}
				}
				if (showUserId) {
					tryAddAchievement(achievements, "ID:", getLongField(param.thisObject, "id"));
				}
				param.setResult(achievements);
			}

			private void tryAddAchievement(ArrayList achievements, String description, String time) {
				for (Object achievement : achievements)
					if (((String) callMethod(achievement, "getDescription")).contains(description))
						return;
				Object achievementInfo = newInstance(findClass("com.sup.android.mi.usercenter.model.UserInfo$AchievementInfo", cl));
				callMethod(achievementInfo, "setDescription", description + time);
				callMethod(achievementInfo, "setIcon", "https://p1-ppx.bytecdn.cn/tos-cn-i-ppx/6d603a87e14741bcbbc941af3a2a623a~tplv-ppx-q75.image");
				callMethod(achievementInfo, "setSchema", "copy://" + time.replace(':', '='));
				achievements.add(achievementInfo);
			}

			private void tryAddAchievement(ArrayList achievements, String description, long id) {
				for (Object achievement : achievements)
					if (((String) callMethod(achievement, "getDescription")).contains(description))
						return;
				Object achievementInfo = newInstance(findClass("com.sup.android.mi.usercenter.model.UserInfo$AchievementInfo", cl));
				callMethod(achievementInfo, "setDescription", description + id);
				callMethod(achievementInfo, "setSchema", "copy://" + id);
				achievements.add(achievementInfo);
			}
		});
		hookMethod("com.bytedance.router.b", "a", Context.class, "com.bytedance.router.RouteIntent", new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
				if ("copy".equals(callMethod(param.args[1], "getScheme"))) {
					callMethod(callStaticMethod(
							findClass("com.bytedance.news.common.service.manager.ServiceManager", cl), "getService"
							, findClass("com.sup.android.i_sharecontroller.IBaseShareService", cl)), "copyLink"
							, param.args[0], ((String) callMethod(param.args[1], "getHost")).replace('=', ':'));
					Utils.showToastXP(cl, "已复制到剪贴板");
				} else invokeOriginalMethod(param.method, param.thisObject, param.args);
				return null;
			}
		});
	}
}
