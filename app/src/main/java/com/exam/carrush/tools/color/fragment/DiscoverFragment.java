package com.exam.carrush.tools.color.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.exam.carrush.R;
import com.exam.carrush.tools.PictureReconizer;
import com.exam.carrush.tools.color.colorbean.ColorCutBitmap;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现模块Fragment
 */
public class DiscoverFragment extends Fragment {

	private EasyRecyclerView mCutBitmapSetingList;
	private ColorCutAdapter mColorCutAdapter;
	private List<ColorCutBitmap> mList;
	private PictureReconizer mPictureReconizer;
	private ImageView setImage;
	private EditText mEditText;
	private Button mBt_Cut;
	private Bitmap bmp;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			List<Bitmap> list = (List<Bitmap>) msg.obj;
			for (int i = 0; i < list.size(); i++) {

				ColorCutBitmap colorCutBitmap = new ColorCutBitmap();
				colorCutBitmap.setmBitmap(list.get(i));
				mList.add(colorCutBitmap);
			}
			mColorCutAdapter.addAll(mList);
		}
	};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_discover, container, false);
		mCutBitmapSetingList = (EasyRecyclerView) view.findViewById(R.id.colorCutList);
		mColorCutAdapter = new ColorCutAdapter(getActivity());
		LinearLayoutManager m = new LinearLayoutManager(getActivity());
		m.setOrientation(LinearLayoutManager.VERTICAL);
		mCutBitmapSetingList.setLayoutManager(m);
		mPictureReconizer = new PictureReconizer(getContext());
		setImage = (ImageView) view.findViewById(R.id.setImage);
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);
		mList = new ArrayList<ColorCutBitmap>();

		mEditText = (EditText) view.findViewById(R.id.colorEdit);
		mBt_Cut = (Button) view.findViewById(R.id.bt_Cut);
		mBt_Cut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {
					@Override
					public void run() {


						int num = mPictureReconizer.palseColor(mEditText.getText().toString());
						Log.e("###########num ", String.valueOf(num));
						IdentyColor identyColor = new IdentyColor(getContext());
						 Bitmap bitmap= mPictureReconizer.convertToBlack(bmp,identyColor,num);

						mPictureReconizer.shape_first_Division(bitmap, true, identyColor, num);
						List<Bitmap> list = mPictureReconizer.shape_second_Division(mPictureReconizer.getmBitmapList(), identyColor, num);
						Message message = new Message();
						message.obj = list;
						mHandler.sendMessage(message);

					}
				}).start();


			}
		});


		//  mColorCutAdapter.addAll();
		mCutBitmapSetingList.setAdapter(mColorCutAdapter);
		return view;
	}


//
//    public List<ColorCutBitmap> initRecyList(List<Bitmap> list){
//
//        List<ColorCutBitmap> bitmaplist=new ArrayList<ColorCutBitmap>();
//
//        for (int i=0;i<list.size();i++){
//
//           ColorCutBitmap colorCutBitmap=new ColorCutBitmap();
//            colorCutBitmap.setmBitmap(list.get(i));
//            bitmaplist.add(colorCutBitmap);
//
//        }
//        return bitmaplist;
//    }

}
