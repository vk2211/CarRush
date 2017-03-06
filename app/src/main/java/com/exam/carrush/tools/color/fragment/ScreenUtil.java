/******************************************************************************
 * @file ScreenUtil.java
 * @brief
 * @author YaoChuan (vk2211@gmail.com)
 * @module com.trustworthy.wineplatformapp.util
 * @date 2014.10月31日
 * @version 0.1
 * @history v0.1, 2014.10月31日, by YaoChuan (vk2211@gmail.com)
 * <p/>
 * <p/>
 * Copyright (C) 2014 YaoChuan.
 ******************************************************************************/

package com.exam.carrush.tools.color.fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;


public class ScreenUtil {
	private static final String TAG = "ScreenUtil";
	private static float density = 1.0f;

//	public static void printScreenProperty() {
//		Context context = App.getContext();
//		DisplayMetrics dm = context.getResources().getDisplayMetrics();
//		float density = dm.density;
//		int screenWidthPx = dm.widthPixels;
//		int screenHeightPx = dm.heightPixels;
//		int screenWidthDp = px2dip(screenWidthPx);
//		int screenHeightDp = px2dip(screenHeightPx);
//		CLog.d(TAG, "screenWidthPx.x=" + screenWidthPx + ",screenHeightPx.y=" + screenHeightPx + ",density=" + density +
//			",dpi=" + dm.densityDpi + ",screenDip.x=" + screenWidthDp + ",screenDip.y=" + screenHeightDp);
//	}

//	public static int dip2px(float dipValue) {
//		Context context = App.getContext();
//		final float scale = context.getResources().getDisplayMetrics().density;
//		return (int) (dipValue * scale + 0.5f);
//	}
//
//	public static int px2dip(float pxValue) {
//		Context context = App.getContext();
//		final float scale = context.getResources().getDisplayMetrics().density;
//		return (int) (pxValue / scale + 0.5f);
//	}

	public static int px2dip(int pxValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(float dipValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static Rect getScreenRect() {
		DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
		Rect rect = new Rect(0, 0, displayMetric.widthPixels, displayMetric.heightPixels);
		return rect;
	}

	/**
	 * 获取屏幕宽度     *
	 */
	public static int getScreenWidth() {
		return getScreenRect().width();
	}

	/**
	 * 获取屏幕高度     *
	 */
	public static int getScreenHeight() {
		return getScreenRect().height();
	}
}
