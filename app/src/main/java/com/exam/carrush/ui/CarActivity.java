package com.exam.carrush.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiseminar.EasyPR.ImageFileRecognizeTask;
import com.aiseminar.EasyPR.RecognizeTask;
import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;
import com.exam.carrush.Global;
import com.exam.carrush.R;
import com.exam.carrush.control.AutoClient;
import com.exam.carrush.control.AutoRun;
import com.exam.carrush.control.CameraThread;
import com.exam.carrush.service.FileService;
import com.exam.carrush.service.SearchService;
import com.exam.carrush.tools.RGBLuminanceSource;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CarActivity extends Activity {

	//车牌识别调用类
	private ImageFileRecognizeTask myRecognizer;
	private RecognizeTask.OnReconizeListener onReconizeListener = new RecognizeTask.OnReconizeListener() {
		@Override
		public void onRecognizeResult(String result) {
			Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_SHORT).show();
			Global.M06 = result;
		}
	};
	// 小车控制界面
	// 定义控件
	private Button quan, ceshi;
	private TextView show = null;
	private TextView content = null;
	private ImageView Pc1, Pc2 = null;
	// 点击监听类
	private carOnclick l;
	// WiFi管理器
	private WifiManager wifiManager;
	// 服务器信息
	private DhcpInfo dhcpInfo;
	// WiFi地址
	private String car_IP = null;
	// 端口方法类
	private AutoClient client;
	private byte[] mByte = new byte[10];
	// 接受传感器
	long psStatus = 0;
	long UltraSonic = 0;
	long Light = 0;
	long CodedDisk = 0;
	// 指示灯与LED标签
	int i = 3, j = 0, k = 0;
	//	// UI布局中摄像头的同类控件声明
	private ImageView image = null;
	// 监听类
	private myOnclick e = null;
	// 摄像头IP端口
	private String IP = null;
	// 摄像头旋转方法类
	private CameraCommandUtil cameraCommandUtil = null;
	// 广播名称
	public static final String A_S = "com.a_s";

	private AutoRun mAutoRun;
	private Button bt_set;

	// 广播接收器
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			IP = arg1.getStringExtra("IP");
			Log.e("IP地址", IP);
			progressDialog.dismiss();
			phThread = new CameraThread(phHandler, IP);
			phThread.start();

		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}

	//主線程
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//车牌识别类实例化
		myRecognizer = new ImageFileRecognizeTask(CarActivity.this, onReconizeListener);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.mainview);
		bt_set = (Button) findViewById(R.id.set_bt);

		// 4.0以上添加 主线程访问网络
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
			.detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
			.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(A_S);
		registerReceiver(myBroadcastReceiver, intentFilter);

		// 得到WiFi信息
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 取得服务器信息
		dhcpInfo = wifiManager.getDhcpInfo();
		// 取得服务器的IP地址
		car_IP = intToIp(dhcpInfo.gateway);

		Toast.makeText(getBaseContext(), "网关地址" + car_IP, Toast.LENGTH_SHORT).show();
		// 获取本机IP
		car_IP = Formatter.formatIpAddress(dhcpInfo.gateway);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		Toast.makeText(getBaseContext(), "本机地址" + intToIp(ipAddress), Toast.LENGTH_SHORT).show();

		cameraCommandUtil = new CameraCommandUtil();
		init();
		search();
		// 调用初始化的控件
		client = new AutoClient(this, phHandler);
		client.connect(car_myHandler, car_IP);

		bt_set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CarActivity.this, TestActivity01.class);
				startActivity(intent);

			}
		});

	}

	//IP地址的格式
	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	// UI布局与上层控件的绑定与监听
	private void init() {
		// 绑定摄像头控件
		image = (ImageView) findViewById(R.id.imageView);
		image.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				gDetector.onTouchEvent(arg1);
				return true;
			}
		});
		// 图片监听设置
		image.setLongClickable(true);
		gDetector.setIsLongpressEnabled(true);
		// 绑定小车控件
		show = (TextView) findViewById(R.id.show);
		content = (TextView) findViewById(R.id.text_content);
		quan = (Button) findViewById(R.id.quan);
		ceshi = (Button) findViewById(R.id.ceshi);
		Pc1 = (ImageView) findViewById(R.id.img_pc1);
		Pc2 = (ImageView) findViewById(R.id.img_pc2);
		e = new myOnclick();
		// 监听小车按钮
		l = new carOnclick();
		quan.setOnClickListener(l);
		ceshi.setOnClickListener(l);
	}

	// 摄像头旋转指令
	private int command = 0;
	// 摄像头旋转步数
	private int onestep = 0;

	// 监听类
	private class myOnclick implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			default:
				Log.e("輸出", "myonclick_default");
				break;
			}
			// 调用摄像头旋转方法
			Log.e("輸出", "myonclick");
			new Thread(new Runnable() {

				@Override
				public void run() {
					cameraCommandUtil.postHttp(IP, command, onestep);
				}
			}).start();
			// cameraCommandUtil.postHttp(IP, command, onestep);
		}
	}

	// 搜索进度
	private ProgressDialog progressDialog = null;

	// 搜索摄像IP进度条
	private void search() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.search));
		progressDialog.show();
		Intent intent = new Intent();
		intent.setClass(CarActivity.this, SearchService.class);
		startService(intent);
	}


	private Bitmap bitmap = null;
	// 开启线程接受摄像头当前图片
	private CameraThread phThread;// = new CameraThread(phHandler, IP);
//	private Thread phThread = new Thread(new Runnable() {
//
//		public void run() {
//			while (true) {
//				bitmap = cameraCommandUtil.httpForImage(IP);
//				phHandler.sendEmptyMessage(10);
//			}
//		}
//	});


	// 接受传感器
	private Handler car_myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mByte = (byte[]) msg.obj;
				if (mByte[0] == 0x55 && mByte[1] == (byte) 0xaa) {

					psStatus = mByte[3] & 0xff;

					UltraSonic = mByte[5] & 0xff;
					UltraSonic = UltraSonic << 8;
					UltraSonic += mByte[4] & 0xff;

					Light = mByte[7] & 0xff;
					Light = Light << 8;
					Light += mByte[6] & 0xff;

					CodedDisk = mByte[9] & 0xff;
					CodedDisk = CodedDisk << 8;
					CodedDisk += mByte[8] & 0xff;

					show.setText(" 超声波：" + UltraSonic + "mm 光照：" + Light + "lx" + "\n" + "  码盘：" + CodedDisk + "光敏状态："
						+ psStatus + "  状态：" + (mByte[2]) + "mark值：" + client.mark);
				}
			}
		}
	};

	Handler actionHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:// 前进
				client.MAJOR = 0x02;
				client.FIRST = (byte) (80 & 0xFF);
				client.SECOND = (byte) (80 & 0xff);
				client.THRID = (byte) (80 >> 8);
				client.send();
				break;
			case 3:// 后退
				client.MAJOR = 0x03;
				client.FIRST = (byte) (80 & 0xFF);
				client.SECOND = (byte) (80 & 0xff);
				client.THRID = (byte) (80 >> 8);
				client.send();
				break;
			case 4:// 左转
				client.MAJOR = 0x04;
				client.FIRST = (byte) (80 & 0xFF);
				client.SECOND = 0x00;
				client.THRID = 0x00;
				client.send();
				break;
			case 5:// 右转
				client.MAJOR = 0x05;
				client.FIRST = (byte) (80 & 0xFF);
				client.SECOND = 0x00;
				client.THRID = 0x00;
				client.send();
				break;
			case 6:// 循迹
				client.MAJOR = 0x06;
				client.FIRST = (byte) (80 & 0xFF);
				client.SECOND = 0x00;
				client.THRID = 0x00;
				client.send();
				break;
			case 7://打开蜂鸣器
				client.buzzer(1);
				break;
			case 8://关闭蜂鸣器
				client.buzzer(0);
				break;
			case 11:
				// 打开报警器
				client.infrared((byte) 0x99, (byte) 0x56, (byte) 0x1A,
					(byte) 0xFC, (byte) 0xC5, (byte) 0x89);
				// 打开风扇
				client.infrared((byte) 0x20, (byte) 0x17, (byte) 0x9C,
					(byte) 0xB6, (byte) 0x53, (byte) 0x56);
				break;
			case 12:
				// 打开报警器
				// 0x03 0x05 0x14 0x45 0xDE 0x92
				// 关闭报警器
				// 0x67 0x34 0x78 0xA2 0xFD 0x27
				client.infrared((byte) 0x03, (byte) 0x05, (byte) 0x14,
					(byte) 0x45, (byte) 0xDE, (byte) 0x92);
				client.rest(5000);
				// 关闭报警器
				// 0x67 0x34 0x78 0xA2 0xFD 0x27
				client.infrared((byte) 0x67, (byte) 0x34, (byte) 0x78,
					(byte) 0xA2, (byte) 0xFD, (byte) 0x27);
				break;
			case 20:
				client.lamp((byte) 0xAA);
				break;
			case 21:
				client.lamp((byte) 0x55);
				break;
			case 30:
				client.light(1, 0);
				break;
			case 31:
				client.light(1, 1);
				break;
			case 32:
				client.light(0, 1);
				break;
			case 33:
				client.light(0, 0);
				break;

			case 40:
				// 打开蜂鸣器
				client.buzzer(1);
				// 延迟5秒
				client.rest(5000);
				// 关闭蜂鸣器
				client.buzzer(0);
				break;
			// 二维码识别
			case 100:
				Message message = new Message();
				message.what = 13;

				codeHandler.sendMessage(message);
				break;
			default:
				break;
			}
		}

		;
	};


	// 监听类
	private class carOnclick implements View.OnClickListener {
		// 点击事件处理方法
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			// 全自动
			case R.id.quan:
				AutoRun.get().setClient(client).setHandler(phHandler).start();

//				mAutoRun=AutoRun.get();
//				mAutoRun.start();
//				
//				client.mark=0;
//				client.start();
				break;
			case R.id.ceshi:
				AlertDialog.Builder ceshi_builder = new AlertDialog.Builder(CarActivity.this);
				ceshi_builder.setTitle("测试");
				String[] infrare_item = {"前进", "蜂鸣器开", "蜂鸣器关", "二维码识别", "车牌识别",
					"图形识别", "交通信号灯识别", "照明档位测量", "测距", "报警器打开", "语音播报"
					, "立体显示", "数码管显示"};
				ceshi_builder.setSingleChoiceItems(infrare_item, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {       // 前进
								actionHandler.sendEmptyMessage(2);
							} else if (which == 1) {// 蜂鸣器开
								actionHandler.sendEmptyMessage(7);
							} else if (which == 2) {// 蜂鸣器关
								actionHandler.sendEmptyMessage(8);
							} else if (which == 3) {// 二维码识别
								phHandler.sendEmptyMessage(2000);
							} else if (which == 4) {// 车牌识别
								phHandler.sendEmptyMessage(700);
							} else if (which == 5) {// 图形识别

							} else if (which == 6) {// 交通信号灯识别

							} else if (which == 7) {// 照明档位测量
								phHandler.sendEmptyMessage(90);
							} else if (which == 8) {// 测距
								phHandler.sendEmptyMessage(80);
							} else if (which == 9) {// 报警器打开
								client.infrared((byte) 0x99, (byte) 0x56, (byte) 0x1A,
									(byte) 0xFC, (byte) 0xC5, (byte) 0x89);
							} else if (which == 10) {// 语音播报
								//语音播报
								String src = "打开无线充电装置为指示灯供电";
								byte[] sbyte = null;
								try {
									sbyte = client.bytesend(src.getBytes("GBK"));
								} catch (UnsupportedEncodingException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								client.send_voice(sbyte);
							}
							dialog.cancel();
						}
					});
				ceshi_builder.create().show();
				break;
			default:
				Log.e("輸出：", "carOnclick_default");
				break;
			}
			Log.e("輸出：", "carOnclick");

		}
	}

	// 拍照
	private void savePhoto(String name) {
		// new FileService().savePhoto(convertToGrayscale(bitmap), name);
		new FileService().savePhoto(bitmap, name);
	}

	// 二维码解析
	private Timer timer1 = null;
	private String nameCode = null;
	private int mark = -60;
	public Result result;
	private Handler codeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 11 || msg.what == 12 || msg.what == 13) {
				mark = msg.what;
				nameCode = (String) msg.obj;
				timer1 = new Timer();
				timer1.schedule(new TimerTask() {
					@Override
					public void run() {
						Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
						hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
						RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
						BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
						QRCodeReader reader = new QRCodeReader();

						try {
							// bitmap1二维码的图片，二维码的内容；
							result = reader.decode(bitmap1, hints);
							Log.e("结果：", result.toString());
							if (result.toString() != null) {
								if (mark == 11) {
									phHandler.sendEmptyMessage(15);
								} else if (mark == 12) {
									phHandler.sendEmptyMessage(15);
								} else
									phHandler.sendEmptyMessage(15);
								// 二维码5成功
								try {
									new FileService().savePhoto(bitmap, nameCode + ".png");
									new FileService().saveToSDCard(nameCode + ".txt", result.toString());

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								timer1.cancel();
							}
						} catch (com.google.zxing.NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ChecksumException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 0, 100);
			}
		}

		;
	};
	// 手势监听器
	GestureDetector gDetector = new GestureDetector(new GestureDetector.OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2, float arg3) {
			// TODO Auto-generated method stub
			float x1 = e1.getX();
			float x2 = e2.getX();
			float y1 = e1.getY();
			float y2 = e2.getY();
			float xx = x1 > x2 ? x1 - x2 : x2 - x1;
			float yy = y1 > y2 ? y1 - y2 : y2 - y1;
			if (xx > yy) {
				if (x1 > x2 && xx > 20)
					cameraCommandUtil.postHttp(IP, 4, 1);// 左滑
				else
					cameraCommandUtil.postHttp(IP, 6, 1);// 右滑
			} else {
				if (y1 > y2 && yy > 20)
					cameraCommandUtil.postHttp(IP, 0, 1);// 上滑
				else
					cameraCommandUtil.postHttp(IP, 2, 1);// 下滑
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub

			return false;
		}
	});
	// 图片，位置，结果处理线程
	public Handler phHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10) {
				image.setImageBitmap(phThread.bitmap);
			}
			if (msg.what == 15) {
				Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_SHORT).show();
			}
			// 扫二维码
			if (msg.what == 200) {
				Message message = new Message();
				message.what = 11;
				message.obj = Global.M01;
				codeHandler.sendMessage(message);
			}
			//扫二维码
			if (msg.what == 300) {
				Message message = new Message();
				message.what = 12;
				message.obj = Global.M04;
				codeHandler.sendMessage(message);
			}
			if (msg.what == 400) {
				//获取当前的距离信息
				Global.M02 = "" + UltraSonic / 10;
				try {
					new FileService().saveToSDCard("M02.txt", Global.M02);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			if (msg.what == 500) {
				//获取当前光照值
				try {
					new FileService().saveToSDCard(Global.M03, Light + "");
					Toast.makeText(getBaseContext(), Light + "", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 拍照车牌
			if (msg.what == 700) {
				savePhoto(Global.M05 + ".png");
				Global.M05 = new FileService().readPhoto(Global.M05 + ".png");
				myRecognizer.recognizizePicture(Environment.getExternalStorageDirectory() + "/" + Global.M05 + ".png");
			}
			// 拍照图形
			if (msg.what == 800) {
				savePhoto(Global.M07 + ".png");
				Global.M07 = new FileService().readPhoto(Global.M07 + ".png");
			}
			//计算从车M09和立体显示内容M10
			if (msg.what == 801) {
				//从车的坐标
//				Global.M09=Global.M06+Global.M08;
				//立体显示的内容
				Global.M10 = Global.M06 + Global.M09;
			}
			//计算主车的停车坐标M13
			if (msg.what == 802) {
//				Global.M13=Global.M02+Global.M06+Global.M08+Global.M09;
			}
			//交通信号灯
			if (msg.what == 900) {
				savePhoto(Global.M12 + ".png");
			}
			//语音播报
			if (msg.what == 1000) {
				String src = "打开无线充电装置为指示灯供电";
				byte[] sbyte = null;
				try {
					sbyte = client.bytesend(src.getBytes("GBK"));
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				client.send_voice(sbyte);
			}
			// 结果
			if (msg.what == 10000) {
				content.setText("控制码：" + Global.M01 + "测得距离为:" + Global.M02 + "COM" + "光强调至第" + Global.M03 + "挡" +
					"车牌号码为：国" + Global.M06 + "X色Y图形有" + Global.M08 + "个" + "运输目的地:" + Global.M09 + "交通灯信号为:" + Global.M12);
				Pc1.setImageBitmap(Global.M05);
				Pc2.setImageBitmap(Global.M07);
			}
		}
	};

}
