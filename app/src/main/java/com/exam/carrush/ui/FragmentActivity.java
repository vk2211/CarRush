package com.exam.carrush.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.exam.carrush.R;
import com.exam.carrush.tools.color.fragment.DiscoverFragment;
import com.exam.carrush.tools.color.fragment.MainFragment;
import com.exam.carrush.tools.color.fragment.MyFragment;

public class FragmentActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


	private RadioGroup mRadioGroup;
	private DiscoverFragment mDiscoverFragment;

	private MyFragment mMyFragment;
	private MainFragment mMainFragment;


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

		/**
		 1、为什么需要判null呢？
		 主要是因为，当Activity因为配置发生改变（屏幕旋转）或者内存不足被系统杀死，造成重新创建时，
		 我们的fragment会被保存下来，但是会创建新的FragmentManager，
		 新的FragmentManager会首先会去获取保存下来的fragment队列，
		 重建fragment队列，从而恢复之前的状态。
		 */
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
