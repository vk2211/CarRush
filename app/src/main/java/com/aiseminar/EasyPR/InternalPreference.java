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
import android.content.SharedPreferences;


/**
 * Created by yaochuan on 2016/7/17.
 */
public class InternalPreference {
	private static final String TAG = InternalPreference.class.getSimpleName();
	private String name;
	private SharedPreferences mSettings;
	private SharedPreferences.Editor mSettingsEditor;

	public InternalPreference(String name) {
		Context context = App.getContext();
		mSettings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		mSettingsEditor = mSettings.edit();
	}

	public void write(String key, int value) {
		mSettingsEditor.putInt(key, value);
		mSettingsEditor.commit();
	}

	public int read(String key, int defaultValue) {
		return mSettings.getInt(key, defaultValue);
	}

	public float read(String key, float defaultValue) {
		return mSettings.getFloat(key, defaultValue);
	}
}
