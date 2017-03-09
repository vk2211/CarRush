package com.exam.carrush;

import android.graphics.Bitmap;

public class Global {
	public static String ENCODE1 = "nameCode1";
	public static String ENCODE2 = "nameCode2";
	public static String DISTANCE = "distance";
	public static String SHAPER = "形状";
	public static String M01 = "二维码中16进制的数";
	public static String M02 = "距离信息";
	public static long M02_int=0;
	public static String M03 = "调光的档位f1(M02)";
	public static String M04 = "二维码中的‘请找出照片中X色的Y图形的数量’";
	public static Bitmap M05 = null;//"车牌照片";
	public static String M05_string ="M05.png";


	public static String M06 = null;//"车牌号码";
	public static Bitmap M07 = null;//"图形照片";
	public static int M08 = 0;
	public static String M09 = "从车停下的坐标f2(M06,M08)";
	public static String M10 = "车牌加坐标,国XYYYXY|XY";
	public static String M11 = "M01+CRC校验码16的代码";
	public static String M12 = "交通信号灯的识别";
	public static String M13 = "主车停放的位置f3(M02,M06,M08,M09)";
	public static String M14 = "如果M13的车位上有小车就停放到f4(M13)";
}
