/******************************************************************************
 * @project PlateRecognizer
 * @brief
 * @author yaochuan
 * @module com.exam.carrush.tools.EasyPR
 * @date 2016/9/21
 * @version 0.1
 * @history v0.1, 2016/9/21, by yaochuan
 * <p>
 * Copyright (C) 2016
 ******************************************************************************/
package com.aiseminar.EasyPR;

//import org.opencv.core.Mat;

/**
 * Created by yaochuan on 2016/9/21.
 */
public class PlateRecognizer {
	private static final String TAG = PlateRecognizer.class.getSimpleName();


	/**
	 * JNI Functions
	 */
	static {
		try {
			System.loadLibrary("EasyPR");
		} catch (UnsatisfiedLinkError ule) {
//			CLog.e("System.loadLibrary", "WARNING: Could not load EasyPR library!");
		}
	}

	public static native String stringFromJNI();

	public static native long initPR(String svmpath, String annpath);

	public static native long uninitPR(long recognizerPtr);

	public static native byte[] plateRecognize(long recognizerPtr, String imgpath);

	//public static native byte[] plateRecognizeMat(long recognizerPtr, Mat matPtr);

	public static native byte[] plateRecognizeByte(long recognizerPtr, int rows, int cols, byte[] bytes);

	public static native byte[] plateRecognizeBitmap(long recognizerPtr, Object bitmap);
}
