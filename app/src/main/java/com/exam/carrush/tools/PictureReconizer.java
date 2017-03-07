package com.exam.carrush.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.exam.carrush.tools.color.fragment.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by epai on 17/3/3.
 */

public class PictureReconizer {


	private Context mContext;
	private int rectNum = 0;//矩形
	private int triaNum = 0;//三角形
	private int circNum = 0;//圆形
	private List<Bitmap> mBitmapList, mResultList;
	private int[] location_X = new int[512];
	private int index1 = 0;
	String shapeResult = null;
	int minNum = 100;


	public PictureReconizer(Context context) {
		mContext = context;
		mBitmapList = new ArrayList<Bitmap>();
		mResultList = new ArrayList<Bitmap>();
	}

	/**
	 * 获取识别结果
	 *
	 * @param colorshape
	 * @return
	 */

	public int getRecognizer(String colorshape, Bitmap bmp) {

		int num[] = palseOrder(colorshape);  //命令解析
		int numbershape = 0;
		Bitmap bitmap;

		IdentyColor identyColor = new IdentyColor(mContext);//创建颜色对象

		if (identyColor.setRgb(num[0])) {
			bitmap=convertToBlack(bmp,identyColor,num[0]);  //过滤背景
			shape_first_Division(bitmap, true, identyColor, num[0]);//第一次形状分割
			shape_second_Division(mBitmapList, identyColor, num[0]);//第二次形状分割
			//numbershape = getnumOfshape(mResultList, identyColor, num[0], num[1]);  //进行识别

			numbershape=GetnumberOfshape(mResultList,identyColor,num[0],num[1]);

		} else {
			Toast.makeText(mContext, "请配置RGB", Toast.LENGTH_SHORT).show();
		}
		return numbershape;

	}

	public List<Bitmap> getmResultList() {
		return mResultList;
	}

	public List<Bitmap> getmBitmapList() {
		return mBitmapList;
	}




	public int  GetnumberOfshape(List<Bitmap> list,IdentyColor identyColor,int colornum,int shapenum){

		triaNum = 0;
		rectNum = 0;
		circNum = 0;


		for(int i=0;i<list.size();i++){

			ArrayList<Coordinates> listcolor=getListOf_Coordinates(list.get(i),identyColor,colornum);

			Log.e("###########list size ",String.valueOf(listcolor.size()));
			if(listcolor.size()>=13000&&listcolor.size()<17000){

				circNum++;
			}
			else if(listcolor.size()>=17000){

				rectNum++;
			}
			else if(listcolor.size()<13000){

				triaNum++;
			}

		}
		if (shapenum == 0) {

			return triaNum;
		} else if (shapenum == 1) {
			return circNum;
		} else {
			return rectNum;
		}



	}





	public int getnumOfshape(List<Bitmap> list, IdentyColor identyColor, int colornum, int shapenum) {

		triaNum = 0;
		rectNum = 0;
		circNum = 0;

		for (int i = 0; i < list.size(); i++) {

			Bitmap bitmap = list.get(i);
			shapeIdentification(bitmap, identyColor, colornum);

		}

		if (shapenum == 0) {

			return triaNum;
		} else if (shapenum == 1) {
			return circNum;
		} else {
			return rectNum;
		}

	}

	public int palseColor(String colorshape) {

		/**
		 * 颜色命令解析
		 */
		switch (colorshape) {

		case "红色":
			return 0;


		case "绿色":
			return 1;

		case "蓝色":
			return 2;

		case "黄色":
			return 3;

		case "品色":
			return 4;

		case "青色":
			return 5;

		case "黑色":
			return 6;

		case "白色":
			return 7;

		case "底色":
			return 8;


		default:
			return -1;

		}
	}

	public int[] palseOrder(String colorshape) {


		int[] num = new int[2];
		String color = colorshape.substring(0, 2);
		String shape = colorshape.substring(2, colorshape.length());


		/**
		 * 颜色命令解析
		 */
		switch (color) {

		case "红色":
			num[0] = 0;
			break;
		case "绿色":
			num[0] = 1;
			break;
		case "蓝色":
			num[0] = 2;
			break;
		case "黄色":
			num[0] = 3;
			break;
		case "品色":
			num[0] = 4;
			break;
		case "青色":
			num[0] = 5;
			break;
		case "黑色":
			num[0] = 6;
			break;
		case "白色":
			num[0] = 7;
			break;

		case "底色":
			num[0] = 8;
			break;
		default:
			num[0] = -1;
			break;

		}


		/**
		 * 形状命令解析
		 */
		switch (shape) {

		case "三角形":

			num[1] = 0;
			break;
		case "圆形":

			num[1] = 1;
			break;
		case "矩形":
			num[1] = 2;

			break;

		default:
			num[1] = -1;
			break;

		}

		return num;

	}


	/**
	 * 过滤
	 *
	 * @param
	 * @param
	 * @return
	 */

	public Bitmap convertToBlack(Bitmap bip, IdentyColor identyColor, int colorm) {// 像素处理背景变为黑色，红绿蓝不变
		int width = bip.getWidth();
		int height = bip.getHeight();
		int[] pixels = new int[width * height];
		bip.getPixels(pixels, 0, width, 0, 0, width, height);
		int[] pl = new int[bip.getWidth() * bip.getHeight()];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;

				if (identyColor.isIdentyColor(r, g, b, colorm)) {  //提取需要的颜色
					pl[offset + x] = pixel;
				} else {
					pl[offset + x] = 0xffff00ff;// 紫色
				}
			}
		}
		Bitmap result = Bitmap.createBitmap(width, height,
			Bitmap.Config.ARGB_8888);
		result.setPixels(pl, 0, width, 0, 0, width, height);
		return result;

	}


	/**
	 * @param bp
	 * @param identyColor
	 * @return
	 */
	private ArrayList<Coordinates> getListOf_Coordinates(Bitmap bp, IdentyColor identyColor, int colornum) {

		// // 存储图片上方坐标值
		ArrayList<Coordinates> list = new ArrayList<Coordinates>();

		int width = bp.getWidth();
		int height = bp.getHeight();
		int[] pixels = new int[width * height];
		bp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel = pixels[y * width + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				/**
				 * 将过滤的颜色添加到list集合
				 */
				if (identyColor.isIdentyColor(r, g, b, colornum)) {
					list.add(new Coordinates(x, y));
				}
			}
		}

		return list;

	}


	/**
	 * 第一次进行形状分割
	 *
	 * @param
	 */
	public void shape_first_Division(Bitmap bp, boolean flag, IdentyColor identyColor, int colornum) {


		if (flag) {
			mBitmapList.clear();
			mResultList.clear();
		}

		ArrayList<Coordinates> list = getListOf_Coordinates(bp, identyColor, colornum);

		index1 = 0;
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {

				if ((list.get(i).getX() - list.get(i - 1).getX() > 4) || i == list.size() - 1) {
					index1++;
					location_X[index1] = list.get(i).getX();
					Bitmap bitmap = Bitmap.createBitmap(bp, location_X[index1 - 1],
						0, location_X[index1] - location_X[index1 - 1], bp.getHeight());

					if (flag) {
						bitmap = adjustPhotoRotation(bitmap, -90);
						mBitmapList.add(bitmap);  //将分割并且旋转的图片保存到list集合
						Log.e("******mBitmapList ", String.valueOf(mBitmapList.size()));

					} else {
						bitmap = adjustPhotoRotation(bitmap, 90);
						mResultList.add(bitmap);
					}

				}
			}
		}
	}


	/**
	 * 第二次形状分割
	 *
	 * @param bitmapList
	 */

	public List<Bitmap> shape_second_Division(List<Bitmap> bitmapList, IdentyColor identyColor, int colornum) {

		for (int j = 0; j < bitmapList.size(); j++) {

			shape_first_Division(bitmapList.get(j), false, identyColor, colornum);

			Log.e("****** j========", String.valueOf(j));
		}

		Log.e("******mResultList ", String.valueOf(mResultList.size()));

		return mResultList;

	}


	private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
		Matrix m = new Matrix();

		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);


		try {
			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

			return bm1;
		} catch (OutOfMemoryError ex) {
		}
		return null;
	}


	/**
	 * 确定形状个数,shapenum,0--三角形,1--圆形,2--矩形
	 *
	 * @param bp
	 * @return
	 */

	private void shapeIdentification(Bitmap bp, IdentyColor identyColor, int colornum) {

		/**
		 * 清除原始数据
		 */
		identyColor.getListl().clear();
		identyColor.getListr().clear();

		ArrayList<Coordinates> listl = identyColor.getListl();
		ArrayList<Coordinates> listr = identyColor.getListr();

		int up_index = 0;
		int down_index = 0;
		int num_index = 0;
		shapeResult = null;
		int width = bp.getWidth();
		int height = bp.getHeight();
		int[] pixels = new int[width * height];
		bp.getPixels(pixels, 0, width, 0, 0, width, height);
		// 得到上面第五排像素坐标
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				if (identyColor.isIdentyColor(r, g, b, colornum)) {
					if (up_index == 5) {
						listl.add(new Coordinates(x, y));
					}
					num_index++;
				}
			}
			if (num_index > 2) {
				up_index++;
				if (up_index > 5)
					break;
			}
		}
		num_index = 0;
		// 得到下面第五行坐标
		for (int y = height - 1; y > 0; y--) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				if (identyColor.isIdentyColor(r, g, b, colornum)) {// 红色
					if (down_index == 5) {
						listr.add(new Coordinates(x, y));
					}
					num_index++;
				}
			}
			if (num_index > 2) {
				down_index++;
				if (down_index > 5)
					break;
			}
		}
		shape(listl, listr);
	}


	// 形状识别
	private void shape(ArrayList<Coordinates> listl, ArrayList<Coordinates> listr) {
		int indexl = listl.size();
		int indexr = listr.size();
		if (indexl > 0 && indexr > 0) {// 判断颜色识别成功
			int differNum_up = indexl;
			int differNum_down = indexr;
			Log.e("上面像素值：", differNum_up + "下面像素值：" + differNum_down);

			//三角形
			if ((differNum_up - differNum_down > 55) || differNum_up - differNum_down < -55) {

				triaNum++;
			} else if (differNum_up < minNum) {
				circNum++;

			} else {
				rectNum++;
			}

		} else
			Toast.makeText(mContext, "颜色识别失败，请改正算法。。。", Toast.LENGTH_SHORT).show();
	}
}
