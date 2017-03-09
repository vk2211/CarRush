package com.exam.carrush.tools.color.fragment;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.tools.color.colorbean.ColorCutBitmap;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


public class ColorCutAdapter extends RecyclerArrayAdapter<ColorCutBitmap> {

	private Context mContext;

	public ColorCutAdapter(Context context) {
		super(context);
	}

	@Override
	public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

		return new CutViewHolder(parent);
	}

	class CutViewHolder extends BaseViewHolder<ColorCutBitmap> {

		private ImageView mImagView;
		private TextView  AllCoordinates;


		public CutViewHolder(ViewGroup itemView) {
			super(itemView, R.layout.item_cut);
			mImagView = $(R.id.listImage);
			AllCoordinates=$(R.id.AllCoordinates);

		}

		@Override
		public void setData(ColorCutBitmap data) {
			super.setData(data);
			mImagView.setImageBitmap(data.getmBitmap());
			AllCoordinates.setText("总点数:"+data.getAllCoordinates());

		}
	}


}
