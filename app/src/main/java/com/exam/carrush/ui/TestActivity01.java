package com.exam.carrush.ui;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.service.FileService;
import com.exam.carrush.tools.PictureReconizer;

public class TestActivity01 extends AppCompatActivity {
	private ImageView mImage_REC;
	private Button mBt_REC;
	private EditText mInputET;
	private PictureReconizer mPictureReccognizer;
	private Bitmap bitmap;
	private TextView te_result;
	//调用系统相册-选择图片
	private static final int IMAGE = 1;
	private String ImagePath=null;


	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int num = (int) msg.obj;

			te_result.setText("识别结果: " + num);


		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mImage_REC = (ImageView) findViewById(R.id.sourceImage);
		mBt_REC = (Button) findViewById(R.id.bt_REC);
		mInputET = (EditText) findViewById(R.id.inputet);
		te_result = (TextView) findViewById(R.id.te_result);


		mPictureReccognizer = new PictureReconizer(TestActivity01.this);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);

		mImage_REC.setImageBitmap(bitmap);

		mBt_REC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				te_result.setText("识别中。。。: ");

				new Thread(new Runnable() {
					@Override
					public void run() {
						int num = mPictureReccognizer.getRecognizer(mInputET.getText().toString(), bitmap);

						Message message = new Message();
						message.obj = num;
						mHandler.sendMessage(message);

					}
				}).start();

			}
		});


		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
		case R.id.action_settings:
			Intent intent=new Intent();
			intent.setClass(TestActivity01.this,FragmentActivity.class);
			startActivity(intent);

			break;
		case R.id.pick_picture01:
			//调用相册
			Intent intentPicture = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intentPicture, IMAGE);

			break;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//获取图片路径
		if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = {MediaStore.Images.Media.DATA};
			Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
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
		mImage_REC.setImageBitmap(bitmap);

	}

}
