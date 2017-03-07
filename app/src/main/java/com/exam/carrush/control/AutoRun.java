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
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.exam.carrush.Global;
import com.exam.carrush.control.CarModel.CarMovementListener;


public class AutoRun {
	private static final String TAG = "AutoRun.java";
	private int SLEEP = 100;
	private static AutoRun sInstance;
	private Client mCarClient;
	private Handler mMainHandler;
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
		mCarModel = new CarModel(2, 8, new CarMovementListener() {
			@Override
			public void onPrepare() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTurn(int direction, int angle) {
				int sp = 1;
				int i;
				if (angle == 180) {
					sp = 2;
				}

				mCarClient.go(80, 80);
				mCarClient.rest(SLEEP);
				switch (direction) {
				case CarModel.L:
					for (i = 0; i < sp; i++) {
						mCarClient.left(80);
						mCarClient.rest(SLEEP);
					}
					break;
				case CarModel.T:
					break;
				case CarModel.R:
					for (i = 0; i < sp; i++) {
						mCarClient.right(80);
						mCarClient.rest(SLEEP);
						mCarClient.rest(SLEEP);
					}
					break;
				case CarModel.B:
					break;
				}
				mCarClient.rest(SLEEP * 15);
			}

			@Override
			public void onGoBack() {
				mCarClient.back(80, 160);
				mCarClient.rest(SLEEP);
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

	public AutoRun setClient(Client client) {
		mCarClient = client;
		return this;
	}

	public AutoRun setHandler(Handler handler) {
		mMainHandler = handler;
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

//			new MeasureTask().excute();
			new HoloTask().excute();

//			new LightControlTask().excute();
//			new TempTask().excute();

			new EndTask().excute();
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

	//开始
	class PrepareTask extends Task {
		@Override
		protected void before() {
		}

		@Override
		protected void exe() {
			mCarClient.gate(1);
			mCarClient.rest(SLEEP * 10);
			mCarClient.digital_clear();
			mCarClient.rest(SLEEP * 5);
			mCarClient.digital_open();
			mCarClient.rest(SLEEP * 5);
			mCarClient.digital_dic(0);
			mCarClient.rest(SLEEP * 5);
		}

		@Override
		protected void after() {
		}

	}

	//测距
	class MeasureTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(3, 4));
			mCarModel.turnTo(CarModel.T);
			mCarModel.back();
			mCarModel.runTo(new Point(3, 4));
		}

		@Override
		protected void exe() {
			//保存当时距离信息
			mMainHandler.sendEmptyMessage(80);
			mCarClient.rest(SLEEP * 30);
			//在数码管显示距离信息
			mCarClient.digital_dic(Integer.parseInt(Global.M02));
			mCarClient.rest(SLEEP * 10);
			// measure
		}

		@Override
		protected void after() {
		}
	}

	class LightControlTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(2, 6));
		}

		@Override
		protected void exe() {
			mCarClient.gear(1);
			Log.e("灯", "1111");
			mCarClient.rest(SLEEP * 20);
			mCarClient.gear(2);
			mCarClient.rest(SLEEP * 20);
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
			mCarModel.runTo(new Point(8, 8));
			mCarModel.back();
			mCarModel.runTo(new Point(7, 8));
			mCarModel.runTo(new Point(2, 8));
			mCarModel.back();
			mCarModel.runTo(new Point(3, 8));
			mCarModel.runTo(new Point(8, 8));
			mCarModel.back();
			mCarModel.runTo(new Point(7, 8));
			mCarModel.runTo(new Point(2, 8));
			mCarModel.back();
			mCarModel.runTo(new Point(3, 8));
			mCarModel.runTo(new Point(8, 8));
		}

		@Override
		protected void exe() {

		}

		@Override
		protected void after() {

		}
	}

	class HoloTask extends Task {
		@Override
		protected void before() {
			mCarClient.go(80, 80);
			mCarModel.runTo(new Point(5, 6));
			mCarModel.turnTo(CarModel.T);
			mCarClient.rest(SLEEP * 20);
		}

		@Override
		protected void exe() {
			mCarClient.left45();
			mCarClient.rest(SLEEP * 10);
			display("abcd1234");
			mCarClient.rest(SLEEP * 30);
		}

		@Override
		protected void after() {
			mCarClient.right(80);
			mCarClient.rest(SLEEP);
		}

		private void display(String str) {
			short[] li = StringToBytes(str);
			short[] data = new short[5];
			data[0] = 0x20;
			data[1] = (short) (li[0]);
			data[2] = (short) (li[1]);
			data[3] = (short) (li[2]);
			data[4] = (short) (li[3]);
			mCarClient.infrared_stereo(data);
			data[0] = 0x10;
			data[1] = (short) (li[4]);
			data[2] = (short) (li[5]);
			data[3] = (short) (li[6]);
			data[4] = (short) (li[7]);
			mCarClient.infrared_stereo(data);
		}

		private short[] StringToBytes(String licString) {
			if (licString == null || licString.equals("")) {
				return null;
			}
			licString = licString.toUpperCase();
			int length = licString.length();
			char[] hexChars = licString.toCharArray();
			short[] d = new short[length];
			for (int i = 0; i < length; i++) {
				d[i] = (short) hexChars[i];
			}
			return d;
		}

	}

	class TunnelTask extends Task {
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

	class PlateTask extends Task {
		@Override
		protected void before() {

		}

		@Override
		protected void exe() {
//			savePhoto(Global.M05 + ".png");
//			myRecognizer.recognizizePicture(Environment.getExternalStorageDirectory() + "/" + Global.M05 + ".png");

		}

		@Override
		protected void after() {

		}
	}

	class ShapeTask extends Task {
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

	class EndTask extends Task {

		@Override
		protected void before() {

		}

		@Override
		protected void exe() {
			mCarClient.digital_close();
			mCarClient.rest(SLEEP * 5);
			mCarClient.buzzer(1);
			mCarClient.rest(SLEEP * 16);
			mCarClient.buzzer(0);
			mCarClient.rest(SLEEP * 10);
		}

		@Override
		protected void after() {

		}
	}

}
