package com.akari.ppx.xp.hook.code.auto;

import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.utils.XSP;
import com.akari.ppx.xp.hook.code.SuperbHook;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class CommentDiggHook extends SuperbHook {
	@Override
	protected void onHook(ClassLoader cl) {
		if (XSP.get(Prefs.AUTO_COMMENT_DIGG)) {
			hookMethod("com.sup.android.mi.publish.bean.CommentBean", "setRealCommentId", long.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) {
					callMethod(newInstance(findClass("com.sup.android.detail.util.o", cl)), "a", 8, param.args[0], true, 10, 1);
				}
			});
			hookMethod("com.sup.android.m_comment.docker.holder.c", "p", "com.sup.android.m_comment.docker.holder.c", new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) {
					try {
						Object comment = callMethod(param.getResult(), "getComment");
						if ((long) callMethod(comment, "getCommentId") < 0L) {
							callMethod(comment, "setHasLiked", true);
							callMethod(comment, "setLikeCount", 1L);
						}
					} catch (Exception ignored) {
					}
				}
			});
		}
	}
}
