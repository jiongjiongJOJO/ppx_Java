package com.akari.ppx.xp.hook.code.purity;

import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.util.ArrayList;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.REMOVE_ITEM;
import static com.akari.ppx.common.constant.Prefs.REMOVE_ITEM_KEYWORDS;
import static com.akari.ppx.common.constant.Prefs.REMOVE_ITEM_OFFICIAL;
import static com.akari.ppx.common.constant.Prefs.REMOVE_ITEM_PROMOTION;
import static com.akari.ppx.common.constant.Prefs.REMOVE_ITEM_USERS;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class ItemHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		boolean removeItem = XSP.get(REMOVE_ITEM), removeItemOfficial = XSP.get(REMOVE_ITEM_OFFICIAL), removeItemPromotion = XSP.get(REMOVE_ITEM_PROMOTION);
		if (!removeItem && !removeItemOfficial && !removeItemPromotion) return;
		final ArrayList<String> keywords = new ArrayList<>(), users = new ArrayList<>();
		Utils.str2list(XSP.gets(REMOVE_ITEM_KEYWORDS), keywords);
		Utils.str2list(XSP.gets(REMOVE_ITEM_USERS), users);
		hookMethod("com.sup.android.module.feed.repo.manager.a", "b", "java.lang.String", "com.sup.android.mi.feed.repo.bean.FeedResponse", boolean.class, int.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) {
				ArrayList items = (ArrayList) callMethod(param.args[1], "getData");
				for (int i = items.size() - 1; i >= 0; i--) {
					try {
						Object item = callMethod(items.get(i), "getFeedItem");
						Object user = callMethod(item, "getAuthor");
						String text = (String) callMethod(item, "getContent");
						String name = (String) callMethod(user, "getName");
						if (removeItem) {
							for (String k : keywords) {
								if (Pattern.matches(k, text)) {
									items.remove(i);
									break;
								}
							}
							for (String u : users) {
								if (Pattern.matches(u, name)) {
									items.remove(i);
									break;
								}
							}
						}
						if (removeItemOfficial) {
							Object info = callMethod(user, "getCertifyInfo");
							if (info != null) {
								String desc = (String) callMethod(info, "getDescription");
								if (desc.contains("官方账号") || desc.contains("视频号") || desc.contains("新媒体"))
									items.remove(i);
							}
						}
						if (removeItemPromotion) {
							if (callMethod(item, "getPromotionInfo") != null)
								items.remove(i);
						}
					} catch (Exception ignored) {
					}
				}
			}
		});
	}
}
