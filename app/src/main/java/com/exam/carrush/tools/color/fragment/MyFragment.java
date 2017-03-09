package com.exam.carrush.tools.color.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exam.carrush.R;
import com.exam.carrush.tools.Trafficlight_Recognizer;
import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.exam.carrush.tools.color.colorbean.StringColor;


public class MyFragment extends Fragment {

	private ImageView mTrafficImage;
	private TextView mTe_traffic;
	private Button   mBt_traffic;
	private TextView pick_picture03;
	//调用系统相册-选择图片
	private static final int IMAGE = 1;
	private String ImagePath;
	private Bitmap bitmap;
	private Trafficlight_Recognizer mTrafficlight_recognizer;

	private  TextView left_Coordinates;
	private  TextView right_Coordinates;
	private  Button   bt_update_critical;
	private EditText  et_critical;
	private IdentyColor mIdentyColor;


	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view=inflater.inflate(R.layout.fragment_my, container, false);

		mTe_traffic=(TextView)view.findViewById(R.id.te_traffic);
		mBt_traffic=(Button)view.findViewById(R.id.bt_traffc);
		mTrafficImage=(ImageView)view.findViewById(R.id.trafficImageview);
		pick_picture03=(TextView)view.findViewById(R.id.pick_picture03);

		mTrafficlight_recognizer=new Trafficlight_Recognizer(getContext());
		bt_update_critical=(Button)view.findViewById(R.id.bt_update_critical);
		et_critical=(EditText)view.findViewById(R.id.et_critical);



		left_Coordinates=(TextView)view.findViewById(R.id.left_Coordinates);
		right_Coordinates=(TextView)view.findViewById(R.id.right_Coordinates);

		mIdentyColor=mTrafficlight_recognizer.getmIdentyColor();
		et_critical.setText(mIdentyColor.getmSharedPreferences().getInt(StringColor.Critical_Number,3000)+"");

		mBt_traffic.setOnClickListener(OnRecognozerListener);
		pick_picture03.setOnClickListener(pickOnClickListener);
		bt_update_critical.setOnClickListener(updateCriticalListener);


		return view;

	}


	/**
	 * 识别
	 */
	private View.OnClickListener OnRecognozerListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Bitmap bmp=mTrafficlight_recognizer.convertToBlack(bitmap);  //过滤背景

			Bitmap mbmp=mTrafficlight_recognizer.shape_first_Division(bmp);//图像分割

			int num=mTrafficlight_recognizer.shapeIdentfyTraffic(mbmp);

			mTe_traffic.setText("识别结果:"+num+" "+palseString(num));
			mTrafficImage.setImageBitmap(mbmp);

			int left_num=mTrafficlight_recognizer.getLeft_Coordinates();
            int right_num=mTrafficlight_recognizer.getRight_Coordinates();


			left_Coordinates.setText("左边点: "+left_num);

			right_Coordinates.setText("右边点: "+right_num+"   左右差值: "+(right_num-left_num));

		}
	};


	/**
	 * 更新临界值
	 */

	private View.OnClickListener updateCriticalListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mIdentyColor.getEditor().putInt(StringColor.Critical_Number,Integer.parseInt(et_critical.getText()
				.toString()));
			mIdentyColor.getEditor().apply();

			Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
		}
	};




	private String palseString(int num){

		switch (num){

		case 1:

			return "红色左转";

		case 2:
			return "红色右转";

		case 3:
			return "绿色左转";

		case 4:
			return "绿色右转";

		case 5:
			return "掉头";
		case 6:
			return "错误";
		default:

			return "错误";
		}
	}



	/**
	 * 调用系统相册
	 */
	private View.OnClickListener pickOnClickListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//调用相册
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
			ImagePath = c.getString(columnIndex);
			showImage(ImagePath);
			c.close();
		}
	}

	//加载图片
	private void showImage(String imaePath){
		bitmap = BitmapFactory.decodeFile(imaePath);
		mTrafficImage.setImageBitmap(bitmap);

	}

}
