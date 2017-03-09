package com.exam.carrush.control;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	public Client() {

	}

	public byte TYPE = (byte) 0xAA;
	public byte MAJOR = 0x00;
	public byte FIRST = 0x00;
	public byte SECOND = 0x00;
	public byte THRID = 0x00;
	public byte CHECKSUM = 0x00;
	// WiFi端口号
	private int port = 60000;
	// 端口
	private Socket socket = null;
	// 输入流
	private DataInputStream dataInputStream = null;
	// 输出流
	private DataOutputStream dataOutputStream = null;
	// 接受数据字节数组
	public byte[] mbyte = new byte[10];
	private Timer timer;
	// 接受数据线程
	Thread thread = new Thread(new Runnable() {
		public void run() {
			// 端口打开且有数据
			while (socket != null && !socket.isClosed()) {
				try {
					// 读出数据到字节数组中
					dataInputStream.read(mbyte);
				} catch (IOException e) {// 数据读取异常处理
					e.printStackTrace();
				}
			}
		}
	});

	// 连接WiFi
	public void connect(final Handler car_handler, String IP) {
		try {
			// 客户与服务连接
			socket = new Socket(IP, port);
			// 数据传输
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			// 数据接受
			dataInputStream = new DataInputStream(socket.getInputStream());
			// 开启线程时时接受数据
			thread.start();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Message message = new Message();
					message.obj = mbyte;
					message.what = 1;
					car_handler.sendMessage(message);
				}
			}, 0, 500);
		} catch (UnknownHostException e) {
			// IP不正确处理
			e.printStackTrace();
		} catch (IOException e) {
			// 端口不正确处理
			e.printStackTrace();
		}
	}

	//给小车发送指令类
	public void send() {
		try {
			if (socket != null && !socket.isClosed()) {
				CHECKSUM = (byte) ((MAJOR + FIRST + SECOND + THRID) % 256);

				// 发送数据字节数组
				byte[] sbyte = {0x55, (byte) TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID,
					(byte) CHECKSUM, (byte) 0xBB};

				dataOutputStream.write(sbyte, 0, sbyte.length);
				dataOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//语音发送指令类
	public void send_voice(byte[] textbyte) {
		try {
			// 发送数据字节数组
			if (socket != null && !socket.isClosed()) {
				dataOutputStream.write(textbyte, 0, textbyte.length);
				dataOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 前进
	public void go(int sp_n, int en_n) {
		MAJOR = 0x02;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = (byte) (en_n & 0xff);
		THRID = (byte) (en_n >> 8);
		send();
		while (mbyte[2] != 3) ;
	}
	public void gotest(int sp_n, int en_n) {
		MAJOR = 0x17;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = (byte) (en_n & 0xff);
		THRID = (byte) (en_n >> 8);
		send();
		while (mbyte[2] != 3) ;
	}

	// 后退
	public void back(int sp_n, int en_n) {
		MAJOR = 0x03;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = (byte) (en_n & 0xff);
		THRID = (byte) (en_n >> 8);
		send();
		while (mbyte[2] != 3)
			;
	}

	// 左转
	public void left(int sp_n) {
		MAJOR = 0x04;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}
	// 前进and左转
	public void goLeft(int sp_n) {
		MAJOR = 0x0A;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2) ;
	}


	// 左转45
	public void left45() {
		MAJOR = 0x08;
		FIRST = (byte) (80 & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}
	// 转头
	public void head() {
		MAJOR = 0x13;
		FIRST = (byte) (80 & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}

	// 右转
	public void right(int sp_n) {
		MAJOR = 0x05;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}
	// 前进and右转
	public void goRight(int sp_n) {
		MAJOR = 0x0B;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}

	// 右转
	public void right45() {
		MAJOR = 0x09;
		FIRST = (byte) (80 & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 2)
			;
	}

	// 停车
	public void stop() {
		MAJOR = 0x01;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	// 循迹
	public void line(int sp_n) {
		MAJOR = 0x06;
		FIRST = (byte) (sp_n & 0xFF);
		SECOND = 0x00;
		THRID = 0x00;
		send();
		while (mbyte[2] != 1 && mbyte[2] != 4) ;
	}

	public void deputy(int i) {//从车
		if (i == 1)//从车
			TYPE = 0x02;
		else if (i == 2)//主车
			TYPE = (byte) 0xAA;
	}

	public void vice(int i) {//主从车状态转换
		byte type = TYPE;
		if (i == 1) {//从车状态
			TYPE = (byte) 0xAA;
			MAJOR = (byte) 0x80;
			FIRST = 0x01;
			SECOND = 0x00;
			THRID = 0x00;
			send();
			rest(500);
			TYPE = 0x02;
			MAJOR = (byte) 0x80;
			FIRST = 0x01;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		} else if (i == 2) {//主车状态
			TYPE = (byte) 0xAA;
			MAJOR = (byte) 0x80;
			FIRST = 0x00;
			SECOND = 0x00;
			THRID = 0x00;
			send();
			rest(500);
			TYPE = 0x02;
			MAJOR = (byte) 0x80;
			FIRST = 0x00;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		}
		TYPE = type;
	}

	// 红外
	public void infrared(byte one, byte two, byte thrid, byte four, byte five, byte six) {
		MAJOR = 0x10;
		FIRST = one;
		SECOND = two;
		THRID = thrid;
		send();
		rest(1000);
		MAJOR = 0x11;
		FIRST = four;
		SECOND = five;
		THRID = six;
		send();
		rest(1000);
		MAJOR = 0x12;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	// 双色LED灯
	public void lamp(byte command) {
		MAJOR = 0x40;
		FIRST = command;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	// 指示灯
	public void light(int left, int right) {
		//双闪
		if (left == 1 && right == 1) {
			MAJOR = 0x20;
			FIRST = 0x01;
			SECOND = 0x01;
			THRID = 0x00;
			send();
			//左转
		} else if (left == 1 && right == 0) {
			MAJOR = 0x20;
			FIRST = 0x01;
			SECOND = 0x00;
			THRID = 0x00;
			send();
			//右转
		} else if (left == 0 && right == 1) {
			MAJOR = 0x20;
			FIRST = 0x00;
			SECOND = 0x01;
			THRID = 0x00;
			send();
			//全灭
		} else if (left == 0 && right == 0) {
			MAJOR = 0x20;
			FIRST = 0x00;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		}
	}

	// 蜂鸣器
	public void buzzer(int i) {
		//开
		if (i == 1) {
			MAJOR = 0x30;
			FIRST = 0x01;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		}
		//关
		if (i == 0) {
			MAJOR = 0x30;
			FIRST = 0x00;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		}

	}

	// 图片上翻和下翻
	public void picture(int i) {
		if (i == 1)//上
			MAJOR = 0x50;
		else
			MAJOR = 0x51;//下
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void gear(int i) {// 光照档位加
		if (i == 1)
			MAJOR = 0x61;
		else if (i == 2)
			MAJOR = 0x62;
		else if (i == 3)
			MAJOR = 0x63;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void fan() {// 风扇
		MAJOR = 0x70;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	//立体显示
	public void infrared_stereo(short[] data) {
		MAJOR = 0x10;
		FIRST = (byte) 0xff;
		SECOND = (byte) data[0];
		THRID = (byte) data[1];
		send();
		rest(500);
		MAJOR = 0x11;
		FIRST = (byte) data[2];
		SECOND = (byte) data[3];
		THRID = (byte) data[4];
		send();
		rest(500);
		MAJOR = 0x12;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
		rest(500);
	}

	public void gate(int i) {// 闸门
		byte type = (byte) TYPE;
		if (i == 1) {
			TYPE = 0x03;
			MAJOR = 0x01;
			FIRST = 0x01;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		} else if (i == 2) {
			TYPE = 0x03;
			MAJOR = 0x01;
			FIRST = 0x02;
			SECOND = 0x00;
			THRID = 0x00;
			send();
		}
		TYPE = type;
	}

	//LCD显示标志物进入计时模式
	public void digital_close() {//数码管关闭
		byte type = (byte) TYPE;
		TYPE = 0x04;
		MAJOR = 0x03;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
		TYPE = type;
	}

	public void digital_open() {//数码管打开
		byte type = (byte) TYPE;
		TYPE = 0x04;
		MAJOR = 0x03;
		FIRST = 0x01;
		SECOND = 0x00;
		THRID = 0x00;
		send();
		TYPE = type;
	}

	public void digital_clear() {//数码管清零
		byte type = (byte) TYPE;
		TYPE = 0x04;
		MAJOR = 0x03;
		FIRST = 0x02;
		SECOND = 0x00;
		THRID = 0x00;
		send();
		TYPE = type;
	}

	public void digital_dic(int dis) {//LCD显示标志物第二排显示距离
		byte type = (byte) TYPE;
		TYPE = 0x04;
		MAJOR = 0x04;
		FIRST = 0x00;
		SECOND = (byte) (dis / 100);
		THRID = (byte) (dis % 100);
		send();
		TYPE = type;
	}

	//语音合成系统
	public byte[] bytesend(byte[] sbyte) {
		byte[] textbyte = new byte[sbyte.length + 5];
		textbyte[0] = (byte) 0xFD;
		textbyte[1] = (byte) (((sbyte.length + 2) >> 8) & 0xff);
		textbyte[2] = (byte) ((sbyte.length + 2) & 0xff);
		textbyte[3] = 0x01;// 合成语音命令
		textbyte[4] = (byte) 0x01;// 编码格式
		for (int i = 0; i < sbyte.length; i++) {
			textbyte[i + 5] = sbyte[i];
		}
		return textbyte;
	}

	public void digital(int i, int one, int two, int three) {// 数码管
		byte type = (byte) TYPE;
		TYPE = 0x04;
		if (i == 1) {//数据写入第一排数码管
			MAJOR = 0x01;
			FIRST = (byte) one;
			SECOND = (byte) two;
			THRID = (byte) three;
		} else if (i == 2) {//数据写入第二排数码管
			MAJOR = 0x02;
			FIRST = (byte) one;
			SECOND = (byte) two;
			THRID = (byte) three;
		}
		send();
		TYPE = type;
	}

	// 延迟
	public void rest(int time) {
		try {
			for (int i = 0; i < 1; i++) {
				Thread.sleep(time);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.e("###### error", e.toString());
		}
	}
	//开始
	public void STT() {
		MAJOR = 0x0C;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	//结束
	public void END() {
		MAJOR = 0x0D;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

}
