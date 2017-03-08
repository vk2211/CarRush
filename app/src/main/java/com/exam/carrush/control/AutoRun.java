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
		mCarModel = new CarModel(8, 2, 0, mCarMovementListener = new CarMovementListener() {
			@Override
			public void onTurn(int direction, int angle) {
				int sp = 1;
				int i;
				if (angle == 180) {
					sp = 2;
				}

//				mCarClient.go(80, 80);
//				mCarClient.rest(SLEEP);
				switch (direction) {
					case CarModel.L: {
						if (angle == 0) {
							mCarClient.left(80);
						} else {
							if (sp == 1) mCarClient.goLeft(0);
							if (sp == 2) mCarClient.head();
						}
					}
					break;
					case CarModel.T:
						break;
					case CarModel.R: {
						if (angle == 0) {
							mCarClient.right(80);
						} else {
							if (sp == 1) mCarClient.goRight(0);
							if (sp == 2) mCarClient.head();
						}
					}
					break;
					case CarModel.B:
						break;
				}
			}

			@Override
			public void onGoBack() {
				mCarClient.back(80, 160);
//				mCarClient.rest(SLEEP);
			}

			@Override
			public void onReach(int x, int y) {

			}

			@Override
			public void onGoToNextCross() {
				mCarClient.go(80, 80);
//				mCarClient.rest(SLEEP);
				mCarClient.line(80);
//				mCarClient.rest(SLEEP);
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
			//开始任务 ***计时器道闸打开***
			new PrepareTask().excute();
			//二维码识别 ***获取信息进行处理m01***
			new ORCTask().excute();
			//测距任务  ***获取当前距离信息在数码管显示m02***
			new MeasureTask().excute();
			//灯照任务  ***调挡或计算当前档位f1(m02)***
			new LightControlTask().excute();
			//车牌识别
			new PlateTask().excute();
			//隧道任务或报警器任务
			new TunnelTask().excute();
			//主从控制从车任务 ***计算出从车的的位置并让从车行驶到该位置***
			new SlaveTask().excute();
//			//立体显示任务 ***识别的车牌信息和从车的位置在立体显示上显示***
//			new HoloTask().excute();
//			//交通灯识别任务 ***识别当前信息并执行***
//			new TrafficLightsTask().excute();
//			//入库任务   ***计算出小车的停止位置并入库***
//			new EndTask().excute();
//			//测试任务
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
//			//打开道闸
//			mCarClient.gate(1);
//			mCarClient.rest(SLEEP * 10);
//			//清空数码管
//			mCarClient.digital_clear();
//			mCarClient.rest(SLEEP * 5);
//			//打开计数器
//			mCarClient.digital_open();
//			mCarClient.rest(SLEEP * 5);
//			mCarClient.digital_dic(0);
//			mCarClient.rest(SLEEP * 5);
			mCarClient.STT();
			mCarClient.rest(SLEEP*5);
		}

		@Override
		protected void after() {
		}

	}
	//二维码识别任务
	class ORCTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(8, 6));
		}

		@Override
		protected void exe() {

		}

		@Override
		protected void after() {
			mCarModel.back();

		}
	}



	//测距
	class MeasureTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(8, 8));
//			mCarModel.turnTo(CarModel.T);
//			mCarModel.back();
//			mCarModel.runTo(new Point(3, 4));
		}

		@Override
		protected void exe() {
//			//保存当时距离信息
//			mMainHandler.sendEmptyMessage(80);
//			mCarClient.rest(SLEEP * 30);
//			//在数码管显示距离信息
//			mCarClient.digital_dic(Integer.parseInt(Global.M02));
//			mCarClient.rest(SLEEP * 10);
		}

		@Override
		protected void after() {
			mCarModel.back();
		}
	}
    //灯照任务
	class LightControlTask extends Task {
		@Override
		protected void before() {
			mCarModel.runTo(new Point(2, 8));
		}

		@Override
		protected void exe() {
//			mCarClient.gear(1);
//			Log.e("灯", "1111");
//			mCarClient.rest(SLEEP * 20);
//			mCarClient.gear(2);
//			mCarClient.rest(SLEEP * 20);
//			// control light
		}

		@Override
		protected void after() {
			mCarModel.back();
		}

	}
    //立体显示任务
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
    //隧道任务
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
    //车牌识别任务
	class PlateTask extends Task {
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
    //图形识别任务
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
	//主车控制从车任务
	class SlaveTask extends Task {
		CarModel mOtherCar;

		@Override
		protected void before() {
			mCarClient.deputy(1);
			mOtherCar = new CarModel(5, 4, CarModel.L, mCarMovementListener);
		}

		@Override
		protected void exe() {
			mOtherCar.runTo(new Point(9, 9));
		}

		@Override
		protected void after() {
			mCarClient.deputy(2);
		}
	}

	//交通灯识别任务
	class TrafficLightsTask extends Task{
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
    //入库任务
	class EndTask extends Task {

		@Override
		protected void before() {
			if(Global.M13.equals("B2")){
				mCarModel.runTo(new Point(2, 2));
			}
			if (Global.M13.equals("D2")){
				mCarModel.runTo(new Point(2, 4));
			}
			if (Global.M13.equals("F2")){
				mCarModel.runTo(new Point(2, 6));
			}
			if (Global.M13.equals("H2")){
				mCarModel.runTo(new Point(2, 8));
			}
			if (Global.M13.equals("J2")){
				mCarModel.runTo(new Point(2, 10));
			}
		}

		@Override
		protected void exe() {

			mCarClient.go(80,80);
			mCarClient.rest(SLEEP*2);
			mCarClient.END();
		}

		@Override
		protected void after() {

		}
	}



}
