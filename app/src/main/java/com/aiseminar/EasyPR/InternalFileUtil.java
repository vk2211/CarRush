/******************************************************************************
 * @project Zebra
 * @brief
 * @author yaochuan
 * @module com.ezebra.zebra.utils.file
 * @date 2016/7/17
 * @version 0.1
 * @history v0.1, 2016/7/17, by yaochuan
 * <p/>
 * Copyright (C) 2016
 ******************************************************************************/
package com.aiseminar.EasyPR;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by yaochuan on 2016/7/17.
 */
public class InternalFileUtil extends FileUtil {
	private static final String TAG = InternalFileUtil.class.getSimpleName();
	private Context mContext = null;

	public InternalFileUtil() {
		mContext = App.getContext();
	}

	/**
	 * Create a directory in app storage
	 *
	 * @param dirName
	 * @return
	 */
	@Override
	public String createDir(String dirName) {
		File dir = mContext.getDir(dirName, Context.MODE_PRIVATE);
		String fullpath = dir.getAbsolutePath();
		Log.v(TAG, fullpath);
		return fullpath;
	}

	/**
	 * Get the full path for the specified dir & fileName
	 *
	 * @param dir      directory, either ends with "/" or not is ok
	 * @param fileName file name
	 * @return if fileName is blank, return directory(ends with "/"),
	 * else return the full path for fileName in the directory
	 */
	@Override
	public String getFullPath(String dir, String fileName) {
		String spp_path = mContext.getFilesDir().getAbsolutePath();
		String sep = "";
		if (!dir.endsWith("/")) {
			sep = "/";
		}
		String pre = "";
		if (!dir.startsWith(spp_path)) {
			pre = spp_path;
		}
		String name = fileName;
		if (fileName == null) {
			name = "";
		}
		return pre + dir + sep + name;
	}
}
