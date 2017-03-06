package com.aiseminar.EasyPR;

import android.content.Context;
import android.util.Log;

import com.exam.carrush.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


/**
 * Created by ares on 6/19/16.
 */
public class PlateRecognizerUtil extends PlateRecognizer {
	private Context mContext;
	private String mSvmpath = null;
	private String mAnnpath = null;
	private boolean mRecognizerInited = false;
	private long mRecognizerPtr = 0;

	public PlateRecognizerUtil(Context context) {
		mContext = context;


		if (checkAndUpdateModelFile()) {


			mRecognizerPtr = initPR(mSvmpath, mAnnpath);
			if (0 != mRecognizerPtr) {
				mRecognizerInited = true;
			}
		}
	}

	protected void finalize() {
		Log.e("#####", "finalize");
		uninitPR(mRecognizerPtr);
		mRecognizerPtr = 0;
		mRecognizerInited = false;
	}

	public boolean checkAndUpdateModelFile() {


		if (null == mContext) {
			return false;
		}


		mSvmpath = PrFileUtil.getMediaFilePath(PrFileUtil.FILE_TYPE_SVM_MODEL);

		mAnnpath = PrFileUtil.getMediaFilePath(PrFileUtil.FILE_TYPE_ANN_MODEL);

		//如果模型文件不存在从APP的资源中拷贝
		File svmFile = PrFileUtil.getOutputMediaFile(PrFileUtil.FILE_TYPE_SVM_MODEL);
		File annFile = PrFileUtil.getOutputMediaFile(PrFileUtil.FILE_TYPE_ANN_MODEL);
		PrFileUtil.createDir(svmFile.getParent());
		if (/*! svmFile.exists()*/true) {
			try {
				InputStream fis = mContext.getResources().openRawResource(R.raw.svm);

				FileOutputStream fos = new FileOutputStream(svmFile);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				fis.close();
			} catch (FileNotFoundException e) {
				Log.d("PlateRecognizerUtil", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("PlateRecognizerUtil", "Error accessing file: " + e.getMessage());
			}
		}
		if (/*! annFile.exists()*/true) {
			try {
				InputStream fis = mContext.getResources().openRawResource(R.raw.ann);
				FileOutputStream fos = new FileOutputStream(annFile);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				fis.close();
			} catch (FileNotFoundException e) {
				Log.d("PlateRecognizerUtil", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("PlateRecognizerUtil", "Error accessing file: " + e.getMessage());
			}
		}

		if (svmFile.exists() && annFile.exists()) {
			return true;
		}
		return false;
	}


	public String recognize(String imagePath) {
		File imageFile = new File(imagePath);

		if (!mRecognizerInited || !imageFile.exists()) {


			Log.e("#########file extits", "file not exits");
			return null;
		}

		if (0 == mRecognizerPtr) {

			return null;
		}


		Log.e("#########", String.valueOf(mRecognizerPtr));

		byte[] retBytes = plateRecognize(mRecognizerPtr, imagePath);


		String result = null;
		try {
			result = new String(retBytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}


}
