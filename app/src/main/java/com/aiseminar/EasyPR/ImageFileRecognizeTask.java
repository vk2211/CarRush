/******************************************************************************
 * @project PlateRecognizer
 * @brief
 * @author yaochuan
 * @module com.aiseminar.logic
 * @date 2016/9/21
 * @version 0.1
 * @history v0.1, 2016/9/21, by yaochuan
 * <p>
 * Copyright (C) 2016
 ******************************************************************************/
package com.aiseminar.EasyPR;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;


/**
 * Created by yaochuan on 2016/9/21.
 */
public class ImageFileRecognizeTask extends RecognizeTask {
	private static final String TAG = ImageFileRecognizeTask.class.getSimpleName();

	public ImageFileRecognizeTask(Activity activity, OnReconizeListener onReconizeListener) {
		super(activity, onReconizeListener);
	}

	@Override
	public void recognizeFram(byte[] frameData, Camera camera) {


	}

	@Override
	public void recognizizePicture(String path) {

		cropBitmapAndRecognize(path);
	}


	protected synchronized void cropBitmapAndRecognize(String path) {

//		// 进行车牌识别
		String plate = mPlateRecognizer.recognize(path);
		plate = "" + plate.substring(plate.length() - 6, plate.length());
		Log.e("##### result:", plate);
		mOnReconizeListener.onRecognizeResult(plate);
	}


}
