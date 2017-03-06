/******************************************************************************
 * @project ColorShape
 * @brief
 * @author yaochuan
 * @module com.exam.carrush.tools.function.fragment
 * @date 2017/3/4
 * @version 0.1
 * @history v0.1, 2017/3/4, by yaochuan
 * <p>
 * Copyright (C) 2017
 ******************************************************************************/
package com.exam.carrush.tools.color.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by yaochuan on 2017/3/4.
 */
public class ColorSettingAdapter extends RecyclerArrayAdapter<IdentyColor> {
	private static final String TAG = ColorSettingAdapter.class.getSimpleName();
	private int mCurrentPos = -1;
	private OnSettingColorListener mOnSettingColorListener;

	public interface OnSettingColorListener {
		void onSettingColor(boolean isSetting);
	}

	public ColorSettingAdapter(Context context) {
		super(context);
	}

	public ColorSettingAdapter(Context context, IdentyColor[] objects) {
		super(context, objects);
	}

	public int getCurrentPos() {
		return mCurrentPos;
	}

	public void setOnSettingColorListener(OnSettingColorListener onSettingColorListener) {
		mOnSettingColorListener = onSettingColorListener;
	}

	public void save() {
		for (int i = 0; i < this.getCount(); i++) {
			IdentyColor color = this.getItem(i);
		}
	}

	@Override
	public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
		return new ColorViewHolder(parent);
	}

	class ColorViewHolder extends BaseViewHolder<IdentyColor> {
		TextView mTv_ColorName, mTv_Rmin, mTv_Rmax, mTv_Gmin, mTv_Gmax, mTv_Bmin, mTv_Bmax;
		Button bt_config;
		Button bt_reset;

		public ColorViewHolder(ViewGroup itemView) {
			super(itemView, R.layout.item_rgbconfig);
			mTv_ColorName = $(R.id.tv_colorName);
			mTv_Rmax = $(R.id.tv_r_max);
			mTv_Rmin = $(R.id.tv_r_min);
			mTv_Gmax = $(R.id.tv_g_max);
			mTv_Gmin = $(R.id.tv_g_min);
			mTv_Bmax = $(R.id.tv_b_max);
			mTv_Bmin = $(R.id.tv_b_min);
			bt_config = $(R.id.bt_config);
			bt_reset = $(R.id.reset);
			bt_config.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mCurrentPos > -1) {
						if (ColorViewHolder.this.getAdapterPosition() == mCurrentPos) {
							bt_config.setText(R.string.config);
							mCurrentPos = -1;
							if (mOnSettingColorListener != null) {
								mOnSettingColorListener.onSettingColor(false);
							}
							ColorViewHolder.this.itemView.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
						}
					} else {
						bt_config.setText(R.string.complete);
						mCurrentPos = ColorViewHolder.this.getAdapterPosition();
						if (mOnSettingColorListener != null) {
							mOnSettingColorListener.onSettingColor(true);
						}
						ColorViewHolder.this.itemView.setBackgroundColor(Color.rgb(0x7f, 0x7f, 0x7f));
					}
				}
			});
			bt_reset.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = ColorViewHolder.this.getAdapterPosition();
					IdentyColor color = ColorSettingAdapter.this.getItem(pos);
					color.reset();
					ColorSettingAdapter.this.update(color, pos);
				}
			});
		}

		@Override
		public void setData(IdentyColor data) {
			mTv_ColorName.setText(data.getmColor());
			mTv_Rmax.setText("" + data.getmRmax());
			mTv_Rmin.setText("" + data.getmRmin());
			mTv_Gmax.setText("" + data.getmGmax());
			mTv_Gmin.setText("" + data.getmGmin());
			mTv_Bmax.setText("" + data.getmBmax());
			mTv_Bmin.setText("" + data.getmBmin());
		}
	}
}
