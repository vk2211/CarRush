/******************************************************************************
 * @file AutoRun.java
 * @brief
 * @author yaochuan (vk2211@163.com)
 * @module com.bkrcl.game_test
 * @date 2017年2月24日
 * @version 0.1
 * @history v0.1, 2017年2月24日, by yaochuan (vk2211@163.com)
 * <p>
 * <p>
 * Copyright (C) ChinaUnicom 2017.
 ******************************************************************************/

package com.exam.carrush.control;

import android.graphics.Point;
import android.os.Handler;
import android.util.Log;

import com.exam.carrush.control.CarModel.CarMovementListener;


public class AutoRun {
	private static final String TAG = "AutoRun.java";
	private int SLEEP = 100;
	private static AutoRun sInstance;
	private Client mCarClient;
	private CarModel mCarModel;
	private Handler mHandler = new Handler();
	private CarMovementListener mCarMovementListener;

	public static AutoRun get() {
		if (sInstance == null) {
			sInstance = new AutoRun();
		}

		return sInstance;
	}

	public AutoRun() {


		mCarClient = new Client();
		mCarModel = new CarModel(8, 2, new CarMovementListener() {
			@Override
			public void onPrepare() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTurn(int direction, int angle) {


				mCarClient.go(80, 80);
				mCarClient.rest(SLEEP);
				switch (direction) {
				case CarModel.L:
					mCarClient.left(80);
					break;
				case CarModel.T:
					break;
				case CarModel.R:
					mCarClient.right(80);
					break;
				case CarModel.B:
					break;
				}
				mCarClient.rest(SLEEP);
			}

			@Override
			public void onGoBack() {
				mCarClient.back(80, 140);
			}

			@Override
			public void onGoToNextCross() {

				mCarClient.go(80, 80);
				mCarClient.rest(SLEEP);
				mCarClient.line(80);
				mCarClient.rest(SLEEP);
			}

			@Override
			public void onReach(int x, int y) {
				if (x == 2 && y == 8) {
					// QRCode
				} else {
				}
			}

			@Override
			public void onEnd() {
				// TODO Auto-generated method stub

			}
		});
	}

	AutoRun setClient(Client client) {
		mCarClient = client;
		return this;
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
//			mCarModel.runTo(7, 4, 1);
//			mCarModel.runTo(7, 2, 2);
//			mCarModel.runTo(3, 2, 2);
//			mCarModel.runTo(3, 8, 2);
//			mCarModel.runTo(7, 8, 2);
//			mCarModel.runTo(7, 4, 2);
			new PrepareTask().excute();

			new MeasureTask().excute();

			new LightControlTask().excute();
			new TempTask().excute();
		}
	};

	public void start() {
		new Thread(runnable).start();
	}

	abstract class Task {
		public void excute() {
			before();
			exe();
			after();
		}

		protected abstract void before();

		protected abstract void exe();

		protected abstract void after();
	}

	class PrepareTask extends Task {
		@Override
		protected void before() {
		}

		@Override
		protected void exe() {
		}

		@Override
		protected void after() {
		}

	}

	class MeasureTask extends Task {
		@Override
		protected void before() {


			mCarModel.runTo(new Point(7, 6));
			Log.e("1111111111", "111111111111");

			mCarModel.turnTo(CarModel.B);
			Log.e("22222222222", "22222222222222222");

			mCarModel.back();
			Log.e("32222222222", "22222222222222222");


		}

		@Override
		protected void exe() {
			// measure
		}

		@Override
		protected void after() {
			mCarModel.runTo(new Point(7, 6));
		}
	}

	class LightControlTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(8, 8));
		}

		@Override
		protected void exe() {
			// control light
		}

		@Override
		protected void after() {
			mCarModel.back();
		}

	}

	class TempTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(2, 8));

		}

		@Override
		protected void exe() {

		}

		@Override
		protected void after() {

		}
	}

}
