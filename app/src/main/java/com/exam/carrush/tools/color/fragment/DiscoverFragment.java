package com.exam.carrush.tools.color.fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.tools.PictureReconizer;
import com.exam.carrush.tools.color.colorbean.ColorCutBitmap;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.w3c.dom.Text;

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
	private TextView pick_picture04;
	private String ImagePath;

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
		pick_picture04=(TextView)view.findViewById(R.id.pick_picture04);
		setImage=(ImageView)view.findViewById(R.id.setImage);
		pick_picture04.setOnClickListener(pickOnclickListener);
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

		mCutBitmapSetingList.setAdapter(mColorCutAdapter);
		return view;
	}

	private View.OnClickListener pickOnclickListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intentPicture = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intentPicture, getActivity().RESULT_FIRST_USER);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//获取图片路径
		if (resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = {MediaStore.Images.Media.DATA};
			Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			 ImagePath= c.getString(columnIndex);
			showImage(ImagePath);
			c.close();
		}
	}

	//加载图片
	private void showImage(String imaePath){
		bmp = BitmapFactory.decodeFile(imaePath);
		setImage.setImageBitmap(bmp);

	}


}
