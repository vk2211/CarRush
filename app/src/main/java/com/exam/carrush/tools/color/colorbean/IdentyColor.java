package com.exam.carrush.tools.color.colorbean;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.exam.carrush.tools.color.fragment.Coordinates;

import java.util.ArrayList;

/**
 * Created by epai on 17/3/3.
 */

public class IdentyColor extends BaseColor {

	protected ArrayList<Coordinates> listl;//左边点集合
	protected ArrayList<Coordinates> listr;//右边点集合

	protected SharedPreferences mSharedPreferences;
	protected SharedPreferences.Editor editor;
	protected Context mContext;
	private String mColor;


	public IdentyColor(Context context) {
		listl = new ArrayList<Coordinates>();
		listr = new ArrayList<Coordinates>();
		mContext = context;
		mSharedPreferences = context.getSharedPreferences("RGBconfig", Activity.MODE_PRIVATE);
		//实例化SharedPreferences.Editor对象
		editor = mSharedPreferences.edit();
	}


	public IdentyColor(Context context, String color) {
		mColor = color;
		mContext = context;
		listl = new ArrayList<Coordinates>();
		listr = new ArrayList<Coordinates>();
		mSharedPreferences = context.getSharedPreferences("RGBconfig", Activity.MODE_PRIVATE);
		//实例化SharedPreferences.Editor对象
		editor = mSharedPreferences.edit();
	}

	public String getmColor() {
		return mColor;
	}

	public void setmColor(String mColor) {
		this.mColor = mColor;
	}

	public SharedPreferences getmSharedPreferences() {
		return mSharedPreferences;
	}

	public SharedPreferences.Editor getEditor() {
		return editor;
	}

	public ArrayList<Coordinates> getListl() {
		return listl;
	}

	public void setListl(ArrayList<Coordinates> listl) {
		this.listl = listl;
	}

	public ArrayList<Coordinates> getListr() {
		return listr;
	}

	public void setListr(ArrayList<Coordinates> listr) {
		this.listr = listr;
	}


	/**
	 * 颜色确定,根据r,g,b的范围确定颜色
	 *
	 * @param r
	 * @param g
	 * @param b
	 * @param colornum
	 * @return
	 */

	public boolean isIdentyColor(int r, int g, int b, int colornum) {


		if (setRgb(colornum)) {

			if ((getmRmax() + 15 >= r && r >= getmRmin() - 15) && (getmGmax() + 15 >= g && g >= getmGmin() - 15) && (getmBmax()
				+ 15 > b && b > getmBmin() - 15)) {


				Log.e("#####   ********",String.valueOf(getmRmax()));
				Log.e("#####   ********",String.valueOf(getmRmin()));


				return true;
			} else {
				return false;
			}


		} else {
			return false;
		}
	}






	/**
	 * 设置RGB
	 *
	 * @param pos
	 * @return
	 */

	public boolean setRgb(int pos) {

		setmRmax(getmSharedPreferences().getInt(StringColor.getRmax(pos), 0));
		setmRmin(getmSharedPreferences().getInt(StringColor.getRmin(pos), 255));

		setmGmax(getmSharedPreferences().getInt(StringColor.getGmax(pos), 0));
		setmGmin(getmSharedPreferences().getInt(StringColor.getGmin(pos), 255));

		setmBmax(getmSharedPreferences().getInt(StringColor.getBmax(pos), 0));
		setmBmin(getmSharedPreferences().getInt(StringColor.getBmin(pos), 255));

		if (getmRmax() == 0) {
			return false;
		} else return true;
	}


	/**
	 * 更新RGB
	 *
	 * @param r
	 * @param g
	 * @param b
	 */
	public void update(int r, int g, int b) {
		if (r > getmRmax()) {
			setmRmax(r);
		} else if (r < getmRmin()) {
			setmRmin(r);
		}
		if (g > getmGmax()) {
			setmGmax(g);
		} else if (g < getmGmin()) {
			setmGmin(g);
		}
		if (b > getmBmax()) {
			setmBmax(b);
		} else if (b < getmBmin()) {
			setmBmin(b);
		}
	}


	/**
	 * 重置RGB
	 */

	public void reset() {
		setmRmax(0);
		setmRmin(255);

		setmGmax(0);
		setmGmin(255);

		setmBmax(0);
		setmBmin(255);

	}

	/**
	 * 向sharedpreferences
	 *
	 * @param colornum
	 */

	public void saveRGB(int colornum) {

		this.editor.putInt(StringColor.getRmax(colornum), this.getmRmax());
		this.editor.putInt(StringColor.getRmin(colornum), this.getmRmin());

		this.editor.putInt(StringColor.getGmax(colornum), this.getmGmax());
		this.editor.putInt(StringColor.getGmin(colornum), this.getmGmin());

		this.editor.putInt(StringColor.getBmax(colornum), this.getmBmax());
		this.editor.putInt(StringColor.getBmin(colornum), this.getmBmin());
		this.editor.apply();

	}


}
