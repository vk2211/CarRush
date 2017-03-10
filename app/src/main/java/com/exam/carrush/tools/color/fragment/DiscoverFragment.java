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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exam.carrush.R;
import com.exam.carrush.tools.PictureReconizer;
import com.exam.carrush.tools.color.colorbean.ColorCutBitmap;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.exam.carrush.tools.color.colorbean.StringColor;
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

	private EditText small_Critical;
	private EditText big_Critical;
	private EditText middle_Critical;
	private Button   bt_Color_Critical;
	private Spinner  mSpinner;
	private List<String> patten_list=new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			List<ColorCutBitmap> list = (List<ColorCutBitmap>) msg.obj;
			mColorCutAdapter.addAll(list);
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
		mSpinner=(Spinner)view.findViewById(R.id.sp_patten);
		initSpinner();  //初始化Spinner
		mSpinner.setAdapter(adapter);//设置适配器
		mSpinner.setOnItemSelectedListener(selectListener);

		small_Critical=(EditText)view.findViewById(R.id.small_Critical);
		big_Critical=(EditText)view.findViewById(R.id.big_Critical);
		middle_Critical=(EditText)view.findViewById(R.id.middle_Critical);
		bt_Color_Critical=(Button)view.findViewById(R.id.bt_Color_Critical);

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

		mBt_Cut.setOnClickListener(cutOnclickListener);


		small_Critical.setText(mPictureReconizer.getmIdentyColor().getmSharedPreferences().getInt(StringColor
			.Small_Critical,700)+"");
		big_Critical.setText(mPictureReconizer.getmIdentyColor().getmSharedPreferences().getInt(StringColor
			.Big_Critical,1100)+"");
		middle_Critical.setText(mPictureReconizer.getmIdentyColor().getmSharedPreferences().getInt(StringColor
		.Middle_Critical,900)+"");

		bt_Color_Critical.setOnClickListener(UpdateCriticalListener);


		mCutBitmapSetingList.setAdapter(mColorCutAdapter);

		return view;
	}


	private void initSpinner(){

		patten_list.add("模式选择");
		patten_list.add("三种颜色");
		patten_list.add("四种颜色");
		adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, patten_list);
		//第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}

	private AdapterView.OnItemSelectedListener selectListener=new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			StringColor.Model_Number=position;
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/**
	 * 更新范围值
	 */

	private View.OnClickListener UpdateCriticalListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			int small=Integer.parseInt(small_Critical.getText().toString());
			int big=Integer.parseInt(big_Critical.getText().toString());
			int middle=Integer.parseInt(middle_Critical.getText().toString());

			mPictureReconizer.getmIdentyColor().getEditor().putInt(StringColor.Small_Critical,small);
			mPictureReconizer.getmIdentyColor().getEditor().putInt(StringColor.Big_Critical,big);
			mPictureReconizer.getmIdentyColor().getEditor().putInt(StringColor.Middle_Critical,middle);
			mPictureReconizer.getmIdentyColor().getEditor().apply();
		}
	};



	/**
	 * 图形分割
	 */
	private View.OnClickListener cutOnclickListener=new View.OnClickListener() {
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

					ArrayList<ColorCutBitmap> listbitmap=new ArrayList<ColorCutBitmap>();


					List<Bitmap> list = mPictureReconizer.shape_second_Division(mPictureReconizer.getmBitmapList(), identyColor, num);


					for(int i=0;i<list.size();i++){

						ColorCutBitmap colorCutBitmap=new ColorCutBitmap();
						colorCutBitmap.setmBitmap(list.get(i));

						colorCutBitmap.setAllCoordinates(mPictureReconizer.getListOf_Coordinates(list.get(i),
							identyColor,num).size());

						listbitmap.add(colorCutBitmap);
					}


					Message message = new Message();
					message.obj = listbitmap;
					mHandler.sendMessage(message);

				}
			}).start();

		}
	};

	/**
	 * 转到系统相册
	 */
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
