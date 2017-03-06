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


/**
 * Created by yaochuan on 2016/9/21.
 */
public abstract class RecognizeTask {
	private static final String TAG = RecognizeTask.class.getSimpleName();
	protected PlateRecognizerUtil mPlateRecognizer;
	protected Activity mActivity;
	protected OnReconizeListener mOnReconizeListener;


	public interface OnReconizeListener {

		void onRecognizeResult(String result);


	}

	public RecognizeTask(Activity activity, OnReconizeListener onReconizeListener) {
		mActivity = activity;
		mOnReconizeListener = onReconizeListener;
		mPlateRecognizer = new PlateRecognizerUtil(activity);
	}

	public abstract void recognizeFram(byte[] frameData, Camera camera);

	public abstract void recognizizePicture(String path);


}
