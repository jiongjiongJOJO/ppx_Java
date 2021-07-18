package com.akari.ppx.common.constant;

import com.akari.ppx.BuildConfig;

public interface Const {
	String BEST_FIT_VERSION = "3.5.2";
	String TEST = "测试";
	String NOW = "当前：";
	String BANNED = "请卸载本模块后再启动皮皮虾";
	String ALIPAY_URI = "alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/fkx16213vf1yql4hjgu1k5c";
	String QQ_GROUP_URI = "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D";
	String GITHUB_URI = "https://github.com/Secack/ppx";
	String HOME_ACTIVITY_ALIAS = BuildConfig.APPLICATION_ID + ".HomeActivityAlias";
	String XSPreferencesName = BuildConfig.APPLICATION_ID + "_preferences";
	String[] CATEGORY_LIST_NAME = {"关注", "推荐", "视频", "虾聊", "颜值", "真香", "直播", "汽车", "图片", "文字", "游戏", "小说", "小康"};
	int[] CATEGORY_LIST_TYPE = {2, 1, 4, 52, 48, 48, 27, 50, 28, 10, 11, 16, 6666, 111};
	String[] CATEGORY_LIST_NAME_NEW = {"关注", "推荐", "视频", "虾聊", "全部", "颜值", "真香", "直播", "汽车", "图片", "文字", "游戏", "小康"};
	int[] CATEGORY_LIST_TYPE_NEW = {2, 55, 4, 52, 1, 48, 27, 50, 28, 10, 11, 16, 111};
	long AUTHOR_ID = 50086989143L;
}
