package com.akari.ppx.xp.hook.code.auto;

import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;

import static com.akari.ppx.common.constant.Prefs.AUTO_BROWSE_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_BROWSE_FREQUENCY;
import static com.akari.ppx.common.constant.Prefs.AUTO_BROWSE_VIDEO_MODE;
import static com.akari.ppx.common.constant.Prefs.AUTO_COMMENT_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_COMMENT_PAUSE;
import static com.akari.ppx.common.constant.Prefs.AUTO_COMMENT_TEXT;
import static com.akari.ppx.common.constant.Prefs.AUTO_DIGG_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_DIGG_PAUSE;
import static com.akari.ppx.common.constant.Prefs.AUTO_DISS_ENABLE;
import static com.akari.ppx.common.constant.Prefs.AUTO_DISS_PAUSE;
import static com.akari.ppx.common.constant.Prefs.DIGG_STYLE;
import static com.akari.ppx.common.constant.Prefs.DISS_STYLE;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.getStaticObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class DiggDissCommentHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		final boolean autoBrowse = XSP.get(AUTO_BROWSE_ENABLE), autoBrowseVideoMode = XSP.get(AUTO_BROWSE_VIDEO_MODE), autoDigg = XSP.get(AUTO_DIGG_ENABLE), autoDiss = XSP.get(AUTO_DISS_ENABLE), autoDissPause = XSP.get(AUTO_DISS_PAUSE), autoComment = XSP.get(AUTO_COMMENT_ENABLE);
		final int browseFrequency = XSP.getI(AUTO_BROWSE_FREQUENCY), diggStyle = XSP.getI(DIGG_STYLE), dissStyle = XSP.getI(DISS_STYLE);
		final String commentText = XSP.gets(AUTO_COMMENT_TEXT);
		if (autoBrowse || autoDigg || autoDiss || autoComment)
			hookMethod("com.sup.android.detail.ui.DetailPagerFragment", "onPageSelected", int.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) {
					Object feedCell = ((ArrayList) getObjectField(param.thisObject, "q")).get((int) param.args[0]);
					if (autoBrowse) {
						if (BrowseHook.getAutoBrowse()) {
							Object viewPager = BrowseHook.getViewPager();
							if (!autoBrowseVideoMode || findClass("com.sup.android.mi.feed.repo.bean.cell.NoteFeedItem", cl).isInstance(callMethod(feedCell, "getFeedItem"))) {
								callMethod(viewPager, "postDelayed", new ItemThread(viewPager), (int) param.args[0] == 1 ? 1000 : browseFrequency);
							}
						} else BrowseHook.setAutoBrowse(true);
					}
					try {
						if (autoDigg) {
							callMethod(newInstance(findClass("com.sup.android.detail.util.m", cl)), "a",
									callMethod(feedCell, "getCellType"), callMethod(feedCell, "getCellId"), true, diggStyle, 2);
						}
						if (autoDiss) {
							callMethod(newInstance(findClass("com.sup.superb.m_feedui_common.util.g", cl)), "b",
									callMethod(feedCell, "getCellType"), callMethod(feedCell, "getCellId"), true, dissStyle, Proxy.newProxyInstance(cl, new Class[]{findClass("com.sup.android.mi.usercenter.AsyncCallback", cl)}, (proxy, method, args) -> {
										if (autoDissPause && (int) callMethod(args[0], "getStatusCode") != 0)
											BrowseHook.setAutoBrowse(false);
										return null;
									}));
						}
						if (autoComment && !"".equals(commentText)) {
							Object userCenter = callStaticMethod(findClass("com.bytedance.news.common.service.manager.ServiceManager", cl), "getService"
									, findClass("com.sup.android.mi.usercenter.IUserCenterService", cl));
							long uid;
							if (userCenter == null) {
								uid = 0L;
							} else {
								Object myUserInfo = callMethod(userCenter, "getMyUserInfo");
								uid = myUserInfo == null ? 0L : (long) callMethod(myUserInfo, "getId");
							}
							callMethod(getStaticObjectField(findClass("com.sup.android.module.publish.publish.PublishLooper$enqueue$1", cl), "INSTANCE"), "invoke"
									, newInstance(findClass("com.sup.android.mi.publish.bean.CommentBean", cl)
											, commentText, uid, callMethod(feedCell, "getCellId"), callMethod(feedCell, "getCellType"), 0L, 0L, null
											, "cell_detail", "input", false, 0, false, false, -1L));
						}
					} catch (Exception ignored) {
					}
				}
			});
		hookMethod(AUTO_DIGG_PAUSE, "com.sup.android.module.feed.repo.FeedCellService", "diggCell", long.class, int.class, boolean.class, int.class, int.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				Object result = param.getResult();
				if ((int) callMethod(result, "getStatusCode") != 0)
					BrowseHook.setAutoBrowse(false);
			}
		});
		hookMethod(AUTO_COMMENT_PAUSE, "com.sup.android.module.publish.publish.f$a", "call", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) {
				if (!(boolean) callMethod(param.getResult(), "b"))
					BrowseHook.setAutoBrowse(false);
			}
		});
	}
}
