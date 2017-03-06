package com.aiseminar.EasyPR;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by ares on 6/20/16.
 */
public class PrFileUtil {
	public static final int FILE_TYPE_IMAGE = 1;
	public static final int FILE_TYPE_SCREEN = 2;
	public static final int FILE_TYPE_IMAGE_SMALL = 5;
	public static final int FILE_TYPE_SVM_MODEL = 3;
	public static final int FILE_TYPE_ANN_MODEL = 4;

//	public static final String PictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
//	public static final String DocumentDir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
//


	public static final String PictureDir = Environment.getExternalStorageDirectory().toString();
	public static final String DocumentDir = Environment.getExternalStorageDirectory().toString();


	public static final String PLATE_RECT_SMALL_DIR = PictureDir + "/PlateRcognizer/PlateRectSmall";
	public static final String PLATE_RECT_DIR = PictureDir + "/PlateRcognizer/PlateRect";

	/**
	 * Create a File for saving an image or video
	 */
	public static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = null;

//		File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//		File documentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

		File pictureDir = Environment.getExternalStorageDirectory();
		File documentDir = Environment.getExternalStorageDirectory();

		switch (type) {
		case FILE_TYPE_IMAGE: {
			mediaStorageDir = new File(pictureDir, "PlateRcognizer");
			break;
		}
		case FILE_TYPE_IMAGE_SMALL: {
			mediaStorageDir = new File(PLATE_RECT_SMALL_DIR);
			break;
		}
		case FILE_TYPE_SCREEN: {
			mediaStorageDir = new File(PLATE_RECT_DIR);
			break;
		}
		case FILE_TYPE_ANN_MODEL:
		case FILE_TYPE_SVM_MODEL: {
			mediaStorageDir = new File(documentDir, "PlateRcognizer");
			break;
		}
		default:
			return null;
		}
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("PlateRcognizer", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		File mediaFile;
		switch (type) {
		case FILE_TYPE_ANN_MODEL: {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "ann.xml");
			break;
		}
		case FILE_TYPE_SVM_MODEL: {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "svm.xml");
			break;
		}
		default:
			return null;
		}

		return mediaFile;
	}

	public static String getMediaFilePath(int type) {
		File mediaStorageDir = null;
		File mediaFile;

		switch (type) {
		case FILE_TYPE_ANN_MODEL: {

			mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "PlateRcognizer");

			//mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "PlateRcognizer");

			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "ann.xml");
			break;
		}
		case FILE_TYPE_SVM_MODEL: {


			mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "PlateRcognizer");
			//mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "PlateRcognizer");


			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "svm.xml");

			break;
		}
		default:
			return null;
		}
		return mediaFile.getAbsolutePath();
	}

	public static boolean isDirectory(String path) {
		File file = new File(path);
		return file.exists() && file.isDirectory();
	}

	public static String getDirectoryPath(String path, boolean endWithSlash) {
		int end = 0;
		if (isDirectory(path)) {
			if (path.endsWith("/")) {
				end = path.lastIndexOf('/');
			} else {
				if (endWithSlash) {
					return path + "/";
				} else {
					return path;
				}
			}
		} else {
			end = path.lastIndexOf('/');
		}
		if (endWithSlash) {
			end++;
		}
		String dir = path.substring(0, end);
		return dir;
	}

	public static String createDir(String dirName) {
//		String s_SdcardPath = Environment.getExternalStorageDirectory().toString() + "/";
//		File dir = new File(s_SdcardPath + dirName);
//		dir.mkdirs();
//		String fullpath = getDirectoryPath(s_SdcardPath + dirName, false);
//		if (!fullpath.equals(dir.getAbsolutePath())) {
//			Log.d("PrFileUtil", "fullpath != dir.getAbsolutePath()");
//		}
//		return fullpath;
		SdcardFileUtil su = new SdcardFileUtil();
		return su.createDir(dirName);
	}

	public static void init() {
		SdcardFileUtil su = new SdcardFileUtil();
		su.createDir(PLATE_RECT_DIR);
		su.createDir(PLATE_RECT_SMALL_DIR);
	}

	public static void removeTempFiles() {
		SdcardFileUtil su = new SdcardFileUtil();
		su.remove(PLATE_RECT_DIR, null);
		su.remove(PLATE_RECT_SMALL_DIR, null);
	}
}
