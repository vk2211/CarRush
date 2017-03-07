package com.exam.carrush.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exam.carrush.R;
import com.exam.carrush.tools.color.fragment.DiscoverFragment;
import com.exam.carrush.tools.color.fragment.MainFragment;
import com.exam.carrush.tools.color.fragment.MyFragment;
import com.exam.carrush.tools.color.fragment.TouchImageView;

public class FragmentActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


	private RadioGroup mRadioGroup;
	private DiscoverFragment mDiscoverFragment;

	private MyFragment mMyFragment;
	private MainFragment mMainFragment;
	private String Imagepath;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_acticity);
		mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		mRadioGroup.setOnCheckedChangeListener(FragmentActivity.this);
		onCheckedChanged(mRadioGroup, R.id.first);

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {
		FragmentManager fm = getSupportFragmentManager();
		// 开启Fragment事务
		FragmentTransaction transaction = fm.beginTransaction();

		switch (i) {
		case R.id.first:
			if (mMainFragment == null) {
				mMainFragment = new MainFragment();


			}
			// 使用当前Fragment的布局替代id_content的控件
			transaction.replace(R.id.frameLayout_mian, mMainFragment);
			break;
		case R.id.two:
			if (mDiscoverFragment == null) {
				mDiscoverFragment = new DiscoverFragment();
			}
			transaction.replace(R.id.frameLayout_mian, mDiscoverFragment);

			break;
		case R.id.three:
			if (mMyFragment == null) {
				mMyFragment = new MyFragment();
			}
			transaction.replace(R.id.frameLayout_mian, mMyFragment);
			break;
		}
		// 事务提交
		transaction.commit();

	}



}
