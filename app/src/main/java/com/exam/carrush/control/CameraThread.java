/******************************************************************************
 * @project CarRush
 * @brief
 * @author yaochuan
 * @module com.exam.carrush.control
 * @date 2017/3/7
 * @version 0.1
 * @history v0.1, 2017/3/7, by yaochuan
 * <p>
 * Copyright (C) 2017
 ******************************************************************************/
package com.exam.carrush.control;

import android.graphics.Bitmap;
import android.os.Handler;

import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;

/**
 * Created by yaochuan on 2017/3/7.
 *
 */
public class CameraThread extends Thread {
	private static final String TAG = CameraThread.class.getSimpleName();
	private Handler mMainHandler;
	private CameraCommandUtil cameraCommandUtil;
	private String IP;
	public Bitmap bitmap;

	public CameraThread(Handler handler , String ip) {
		mMainHandler = handler;
		IP = ip;
		cameraCommandUtil = new CameraCommandUtil();
	}

	@Override
	public void run() {
		while (true) {
			bitmap = cameraCommandUtil.httpForImage(IP);
			mMainHandler.sendEmptyMessage(10);
		}
	}
}
