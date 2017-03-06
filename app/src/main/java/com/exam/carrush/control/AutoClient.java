package com.exam.carrush.control;

import android.content.Context;
import android.os.Handler;

import com.exam.carrush.Global;
import com.exam.carrush.service.FileService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AutoClient extends Client {
	public int mark = -50;
	private String first_car = "2#车库";
	private String two_car = "3#车库";
	private boolean two_flag = true;
	public Boolean b = false;

	public AutoClient(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	// 上下文
	@SuppressWarnings("unused")
	private Context context;
	private Handler handler;

	//开启小车行驶线程
	public void start() {
		Thread thread = new Thread(runnable);
		thread.start();
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			b = true;
			while (b) {
				quan();
			}
		}
	};

	//小车自动行驶的路径规划
	public void quan() {
		switch (mark) {
//			// 摄像头左转90°
//			case 0:
//				stop();
//				handler.sendEmptyMessage(700);
//				yanchi(500);
//				mark=5;
//				break;
		case 0:
			//停止
			stop();
			//打开计数器
			digital_open();
			//延迟0.5秒
			rest(500);
			//打开闸门
			gate(1);
			//延迟0.5秒
			rest(500);
			mark = 2;
			break;
		case 2:
			//前进一步
			go(80, 60);
			mark = 3;
			break;
		case 3:
			//关闭闸门
//				gate(2);
//				//延迟0.5秒
//				yanchi(500);
			//循迹
			line(60);
			mark = 4;
			break;
		case 4:
			//扫二维码
			stop();
			rest(1000);
			handler.sendEmptyMessage(3000);
			rest(500);
			mark = 5;
			break;
		case 5:
			go(80, 60);
			mark = 6;
			break;
		case 6:
			line(80);
			mark = 7;
			break;
		case 7:
			rest(200);
			go(80, 80);
			mark = 8;
			break;
		case 8:
			light(0, 1);
			rest(500);
			right(80);
			mark = 9;
			break;
		case 9:
			//超声波测距
			stop();
			rest(1000);
			light(0, 0);
			rest(500);
			handler.sendEmptyMessage(800);
			rest(500);
			mark = 10;
			break;
		case 10:
			//数码管显示距离
			String m02 = null;
			try {
				m02 = new FileService().read(Global.M02 + ".txt");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//显示距离信息
			digital_dic(Integer.parseInt(m02));
			rest(500);
			mark = 11;
			break;
		case 11:
			//获得当前光照值！！！
			handler.sendEmptyMessage(900);
			rest(500);
			String m03 = null;
			try {
				m03 = new FileService().read(Global.M03 + ".txt");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			rest(500);
			gear(3);
			rest(500);
			mark = 12;
			break;
		case 12:
			//语音播报
			String src = "打开无线充电装置为指示灯供电";
			byte[] sbyte = null;
			try {
				sbyte = bytesend(src.getBytes("GBK"));
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			send_voice(sbyte);
			rest(500);
			mark = 13;
			break;
		case 13:
			//图片器下翻
			picture(2);
			handler.sendEmptyMessage(10000);
			rest(500);
			//车牌图片
			handler.sendEmptyMessage(800);
			//拍照图形照片
			handler.sendEmptyMessage(700);
			break;


//			case 20:
//				go(80, 70);
//				mark = 30;
//				// 循迹到1#任务点的停止位
//			case 30:
//				line(60);
//				mark = 40;
//				break;
//			case 40:
//				go(80,70);
//				mark =50;
//				break;
//			case 50:
//				light(0, 1);
//				yanchi(500);
//				right(80);
//				mark=60;
//				break;
//			case 60:
//				mark=70;
//				line(60);
//				break;
//			case 7:
//				mark=8;
		// 报警打开与关闭
//			case 30:
//				// 打开报警器
//				// 0x03 0x05 0x14 0x45 0xDE 0x92
//				infrared((byte) 0x03, (byte) 0x05, (byte) 0x14, (byte) 0x45, (byte) 0xDE, (byte) 0x92);
//				yanchi(5000);
//				// 关闭报警器
//				// 0x67 0x34 0x78 0xA2 0xFD 0x27
//				infrared((byte) 0x67, (byte) 0x34, (byte) 0x78, (byte) 0xA2, (byte) 0xFD, (byte) 0x27);
//				yanchi(2000);
//				// 打开蜂鸣器
//				buzzer(1);
//				// 延迟5秒
//				yanchi(2000);
//				try {
//					first_car = new FileService().read("nameCode1.txt");
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				yanchi(1000);
//				mark = 35;
//				break;
//			// 倒车到B点
//			case 35:
//				// 关闭蜂鸣器
//				buzzer(0);
//				yanchi(1000);
//				yanchi(500);
//				back(80, 100);
//				mark = 40;
//				break;
//			// 左转
//			case 40:
//				left(60);
//				mark = 45;
//				break;
//			// 循迹到C点
//			case 45:
//				handler.sendEmptyMessage(3000);
//				line(70);
//				mark = 50;
//				break;
//			// 出C点
//			case 50:
//				go(80, 70);
//				mark = 55;
//				break;
//			// 右转
//			case 55:
//				right(60);
//				mark = 60;
//				break;
//			// 循迹到D点
//			case 60:
//				line(80);
//				mark = 65;
//				break;
//			// 出D点
//			case 65:
//				go(80, 70);
//				mark = 70;
//				break;
//			// 左转
//			case 70:
//				left(60);
//				mark = 75;
//				break;
//			// 循迹到E点
//			case 75:
//				line(80);
//				mark = 80;
//				break;
//			// 出E点
//			case 80:
//				go(80, 70);
//				mark = 85;
//				break;
//			// 循迹到2#任务点到停止位
//			case 85:
//				line(80);
//				mark = 90;
//				break;
//			// 测光源
//			case 90:
//				long Light = 0;
//				Light = mbyte[7] & 0xff;
//				Light = Light << 8;
//				Light += mbyte[6] & 0xff;
//				try {
//					new FileService().saveToSDCard("light.txt", Light + "");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				mark = 95;
//				break;
//			// 倒车到E点
//			case 95:
//				back(80, 350);
//				mark = 100;
//				break;
//			// 左转
//			case 100:
//				left(60);
//				mark = 110;
//				break;
//			// 循迹到F点
//			case 110:
//				MAJOR = 0x06;
//				FIRST = (byte) (30 & 0xFF);
//				SECOND = 0x00;
//				THRID = 0x00;
//				send();
//				while (mbyte[2] != 1 && mbyte[2] != 4) {
//					if (mbyte[3] == 1) {
//						light(1, 1);
//					}
//				}
//				;
//				mark = 115;
//				break;
//			// 出F点
//			case 115:
//				go(80, 70);
//				mark = 120;
//				break;
//			// 循迹到3#任务点到停止位
//			case 120:
//				line(80);
//				light(0, 0);
//				mark = 125;
//				break;
//			// 测距
//			case 125:
//				long UltraSonic = 0;
//				UltraSonic = mbyte[5] & 0xff;
//				UltraSonic = UltraSonic << 8;
//				UltraSonic += mbyte[4] & 0xff;
//				try {
//					new FileService().saveToSDCard("M02.txt", UltraSonic + "");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				mark = 130;
//				break;
//			// 扫描二维码
//			case 130:
//				handler.sendEmptyMessage(500);
//				mark = -50;
//				break;
//			// 倒车到F点
//			case 135:
//				handler.sendEmptyMessage(3000);
//				back(80, 350);
//				mark = 140;
//				break;
//			// 右转
//			case 140:
//				right(60);
//				mark = 145;
//				break;
//			// 循迹到H点
//			case 145:
//				line(80);
//				mark = 150;
//				break;
//			// H点拍照
//			case 150:
//				handler.sendEmptyMessage(150);
//				mark = -50;
//				break;
//			// 在H点旋转掉头
//			case 155:
//				go(80, 50);
//				mark = 158;
//				break;
//			case 158:
//				right(60);
//				mark = 160;
//				break;
//			case 160:
//				back(70, 10);
//				right(60);
//				mark = 165;
//				break;
//			// 循迹到F点
//			case 165:
//				line(70);
//				mark = 170;
//				break;
//			// 出F点
//			case 170:
//				go(80, 70);
//				mark = 175;
//				break;
//			// 循迹到G点
//			case 175:
//				line(80);
//				mark = 180;
//				break;
//			// 识别颜色形状
//			case 180:
//				handler.sendEmptyMessage(180);
//				mark = -50;
//				break;
//			// 第一次车库
//			case 185:
//				if (first_car.equals("2#车库"))
//					mark = 190;
//				else if (first_car.equals("3#车库"))
//					mark = 210;
//				else if (first_car.equals("4#车库"))
//					mark = 200;
//				else if (first_car.equals("5#车库"))
//					mark = 210;
//				break;
//			// 2#车库
//			case 190:
//				go(80, 75);
//				mark = 500;
//				break;
//			// 3#车库
//			case 195:
//				go(80, 75);
//				mark = 400;
//				break;
//			// 4#车库
//			case 200:
//				go(80, 75);
//				mark = 400;
//				break;
//			// 5#车库
//			case 205:
//				go(80, 75);
//				mark = 500;
//				break;
//			// 从G到H点
//			case 210:
//				go(80, 50);
//				mark = 215;
//				break;
//			case 215:
//				right(60);
//				go(80, 10);
//				right(60);
//				mark = 220;
//				break;
//			case 220:
//				line(80);
//				mark = 225;
//				break;
//			case 225:
//				go(80, 70);
//				mark = 230;
//				break;
//			case 230:
//				line(80);
//				mark = 235;
//				break;
//			case 235:
//				if (first_car.equals("3#车库"))
//					mark = 195;
//				else if (first_car.equals("5#车库"))
//					mark = 205;
//				break;
//			// 左转进车库
//			case 400:
//				left(60);
//				mark = 800;
//				break;
//			// 右转进车库
//			case 500:
//				right(60);
//				mark = 800;
//				break;
//			// 进车库段
//			case 800:
//				line(80);
//				mark = 805;
//				break;
//			case 805:
//				go(80, 100);
//				if (two_flag)
//					mark = 900;
//				else {
//					mark = -50;
//					buzzer(1);
//					yanchi(2000);
//					buzzer(0);
//				}
//				break;
//			// 第二次入库方法右转
//			case 900:
//				MAJOR = 0x05;
//				FIRST = (byte) (90 & 0xFF);
//				SECOND = 0x00;
//				THRID = 0x00;
//				send();
//				try {
//					two_car = new FileService().read("nameCode2.txt");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				yanchi(2000);
//				two_flag = false;
//				mark = 905;
//				break;
//			case 905:
//				// go(80, 300);
//				mark = 910;
//				break;
//			case 910:
//				line(80);
//				mark = 1000;
//				break;
//			case 1000:
//				if (first_car.equals("2#车库")) {
//					if (two_car.equals("3#车库") || two_car.equals("5#车库"))
//						mark = 1200;
//					else if (two_car.equals("4#车库"))
//						mark = 1050;
//				} else if (first_car.equals("3#车库")) {
//					if (two_car.equals("2#车库") || two_car.equals("4#车库"))
//						mark = 1300;
//					else if (two_car.equals("5#车库"))
//						mark = 1050;
//				} else if (first_car.equals("4#车库")) {
//					if (two_car.equals("2#车库"))
//						mark = 1050;
//					else if (two_car.equals("3#车库") || two_car.equals("5#车库"))
//						mark = 1400;
//				} else if (first_car.equals("5#车库")) {
//					if (two_car.equals("2#车库") || two_car.equals("4#车库"))
//						mark = 1500;
//					else if (two_car.equals("3#车库"))
//						mark = 1050;
//				}
//				break;
//			// 从二进四号车库出G点
//			case 1050:
//				go(80, 70);
//				mark = 800;
//				break;
//			// 第一次为2#车库，进3或5车库前进出G点
//			case 1200:
//				go(80, 70);
//				mark = 1205;
//				break;
//			// 左转到F点
//			case 1205:
//				left(60);
//				mark = 1210;
//				break;
//			// 循迹到F点
//			case 1210:
//				line(80);
//				mark = 1215;
//				break;
//			// 出F点
//			case 1215:
//				go(80, 70);
//				mark = 1220;
//				break;
//			// 循迹到H点
//			case 1220:
//				line(80);
//				mark = 1225;
//				break;
//			// 判定进车库
//			case 1225:
//				if (two_car.equals("3#车库"))
//					mark = 195;
//				else if (two_car.equals("5#车库"))
//					mark = 205;
//				break;
//			// 第一次为3#车库进2或4#车库
//			case 1300:
//				go(80, 70);
//				mark = 1305;
//				break;
//			// 右转掉头到F点
//			case 1305:
//				right(60);
//				mark = 1310;
//				break;
//			// 循迹到F点进车库
//			case 1310:
//				line(80);
//				mark = 1315;
//				break;
//			// 前进出F点
//			case 1315:
//				go(80, 70);
//				mark = 1320;
//				break;
//			// 循迹到G点
//			case 1320:
//				line(80);
//				mark = 1325;
//				break;
//			// 判定进车库
//			case 1325:
//				if (two_car.equals("2#车库"))
//					mark = 190;
//				else if (two_car.equals("4#车库"))
//					mark = 200;
//				break;
//			// 第一次进的4#车库
//			case 1400:
//				go(80, 70);
//				mark = 1405;
//				break;
//			case 1405:
//				right(60);
//				mark = 1410;
//				break;
//			case 1410:
//				line(80);
//				mark = 1415;
//				break;
//			case 1415:
//				go(80, 70);
//				mark = 1420;
//				break;
//			case 1420:
//				line(80);
//				mark = 1425;
//				break;
//			case 1425:
//				if (two_car.equals("3#车库"))
//					mark = 195;
//				else if (two_car.equals("5#车库"))
//					mark = 205;
//				break;
//			// 第一次进的5#车库
//			case 1500:
//				go(80, 70);
//				mark = 1505;
//				break;
//			case 1505:
//				left(60);
//				mark = 1510;
//				break;
//			case 1510:
//				line(80);
//				mark = 1515;
//				break;
//			case 1515:
//				go(80, 70);
//				mark = 1520;
//				break;
//			case 1520:
//				line(80);
//				mark = 1525;
//				break;
//			case 1525:
//				if (two_car.equals("2#车库"))
//					mark = 190;
//				else if (two_car.equals("4#车库"))
//					mark = 200;
//				break;
		default:
			break;
		}
	}

}
