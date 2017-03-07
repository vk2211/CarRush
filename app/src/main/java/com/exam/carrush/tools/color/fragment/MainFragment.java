package com.exam.carrush.tools.color.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.service.FileService;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

	private TouchImageView mConfigImageView;
	private TextView mRgbText;
	private ColorSettingAdapter mColorSettingAdapter;
	private EasyRecyclerView mColorSettingList;
	private Bitmap mShotBitmap;
	private Button bt_save;
	private List<IdentyColor> mList;






	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_main, container, false);
		mConfigImageView = (TouchImageView) view.findViewById(R.id.configImage);
		mColorSettingList = (EasyRecyclerView) view.findViewById(R.id.colorSettingList);
		mRgbText = (TextView) view.findViewById(R.id.rgbText);
		bt_save = (Button) view.findViewById(R.id.save);
		bt_save.setOnClickListener(saveRGBOnclickListener);
		mConfigImageView.setOnTouchListener(onTouchListener);
		LinearLayoutManager m = new LinearLayoutManager(getActivity());
		m.setOrientation(LinearLayoutManager.VERTICAL);
		mColorSettingList.setLayoutManager(m);

		mColorSettingAdapter = new ColorSettingAdapter(getActivity());

		mColorSettingAdapter.setOnSettingColorListener(new ColorSettingAdapter.OnSettingColorListener() {
			@Override
			public void onSettingColor(boolean isSetting) {
				mConfigImageView.enableDrag(!isSetting);
			}
		});

		mList = initListdata();

		mColorSettingAdapter.addAll(mList);
		mColorSettingList.setAdapter(mColorSettingAdapter);
		mConfigImageView.setImageResource(R.drawable.pic2);




		return view;

	}



	public TouchImageView getmConfigImageView() {
		return mConfigImageView;
	}
	/**
	 * 保存RGB的值
	 */
	private View.OnClickListener saveRGBOnclickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			saveAllRGB(mList);
		}
	};

	/**
	 * 触屏获取RGB的值
	 */
	private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent event) {

			// TODO Auto-generated method stub
			int x = (int) event.getX();
			int y = (int) event.getY();
			int pixel;

			if (mShotBitmap != null) {
				mShotBitmap.recycle();
				mShotBitmap = null;
			}
			mShotBitmap = shot(mConfigImageView);
			if (x <= mShotBitmap.getWidth() && y <= mShotBitmap.getHeight()) {
				pixel = mShotBitmap.getPixel(x, y);
				int r = (pixel & 0xff0000) >> 16;
				int g = (pixel & 0xff00) >> 8;
				int b = (pixel & 0xff);
				mRgbText.setText("R:" + r + ",G:" + g + ",B:" + b);
				int pos = mColorSettingAdapter.getCurrentPos();
				if (pos > -1) {
					IdentyColor color = mColorSettingAdapter.getItem(pos);
					color.update(r, g, b);
					mColorSettingAdapter.update(color, pos);
					mColorSettingAdapter.notifyItemChanged(pos);
				}
			}
			return false;
		}
	};


	/**
	 * 初始化ListView 的数据
	 *
	 * @return
	 */
	public List<IdentyColor> initListdata() {

		List<IdentyColor> list = new ArrayList<IdentyColor>();
		String[] data = {"红色", "绿色", "蓝色", "黄色", "品色", "青色", "黑色", "白色", "底色"};
		for (int i = 0; i < data.length; i++) {
			IdentyColor baseColor = new IdentyColor(getContext(), data[i]);
			baseColor.setRgb(i);
			list.add(baseColor);
		}
		return list;
	}


	/**
	 * 保存所有的RGB值
	 */
	public void saveAllRGB(List<IdentyColor> list) {

		for (int i = 0; i < list.size(); i++) {

			list.get(i).saveRGB(i);
		}
	}

	/**
	 * 截屏
	 *
	 * @param activity
	 * @return
	 */

	private Bitmap shot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.buildDrawingCache();
		view.setDrawingCacheEnabled(true);
		int x = (int) view.getX();
		int y = (int) view.getY();
		int w = view.getWidth();
		int h = view.getHeight();
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), x, y, w, h);
		view.destroyDrawingCache();
		return bmp;
	}


	/**
	 * 截屏
	 *
	 * @param
	 * @return
	 */

	private Bitmap shot(View view) {
		view.buildDrawingCache();
		view.setDrawingCacheEnabled(true);
		int x = (int) view.getX();
		int y = (int) view.getY();
		int w = view.getWidth();
		int h = view.getHeight();
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), x, y, w, h);
		view.destroyDrawingCache();
		return bmp;
	}
}
