package com.exam.carrush.tools;

import android.content.Context;
import android.graphics.Bitmap;

import com.exam.carrush.tools.color.colorbean.IdentyColor;
import com.exam.carrush.tools.color.colorbean.StringColor;
import com.exam.carrush.tools.color.fragment.Coordinates;

import java.util.ArrayList;

/**
 * Created by epai on 17/3/3.
 */

public class Trafficlight_Recognizer {

	private Context mContext;

	private int red_Rmax = 255;
	private int red_Rmin = 160;
	private int red_Gmax = 60;
	private int red_Gmin = 0;
	private int red_Bmax = 100;
	private int red_Bmin = 0;

	private int Green_Rmax = 255;
	private int Green_Rmin = 100;
	private int Green_Gmax = 255;
	private int Green_Gmin = 100;
	private int Green_Bmax = 80;
	private int Green_Bmin = 0;


	private int left_Coordinates = 0;
	private int right_Coordinates = 0;

	private IdentyColor mIdentyColor;

	public Trafficlight_Recognizer(Context context) {

		mContext = context;
		mIdentyColor = new IdentyColor(mContext);
	}

	/**
	 * 过滤图像
	 *
	 * @param bip
	 * @return
	 */

	public Bitmap convertToBlack(Bitmap bip) {// 像素处理背景变为黑色，红绿黄不变
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

				if ((r <= red_Rmax && r >= red_Rmin) && (g <= red_Gmax && g >= red_Gmin) && (b <= red_Bmax && b >= red_Bmin)) {// 红色
					pl[offset + x] = pixel;
				} else if ((r <= Green_Rmax && r >= Green_Rmin) && (g <= Green_Gmax && g >= Green_Gmin) && (b <= Green_Bmax && b >=
					Green_Bmin)) {// 绿色
					pl[offset + x] = pixel;
				} else {
					pl[offset + x] = 0xff000000;// 黑色
				}
			}
		}
		Bitmap result = Bitmap.createBitmap(width, height,
			Bitmap.Config.ARGB_8888);
		result.setPixels(pl, 0, width, 0, 0, width, height);
		return result;

	}

	public IdentyColor getmIdentyColor() {
		return mIdentyColor;
	}

	public int getLeft_Coordinates() {
		return left_Coordinates;
	}

	public void setLeft_Coordinates(int left_Coordinates) {
		this.left_Coordinates = left_Coordinates;
	}

	public int getRight_Coordinates() {
		return right_Coordinates;
	}

	public void setRight_Coordinates(int right_Coordinates) {
		this.right_Coordinates = right_Coordinates;
	}

	/**
	 * 行形状分割
	 *
	 * @param
	 */
	public Bitmap shape_first_Division(Bitmap bp) {


		/**
		 * 将图像分割,
		 */
		Bitmap cutbitmap = Bitmap.createBitmap(bp, 100, 0, bp.getWidth() - 200, bp.getHeight());

		ArrayList<Coordinates> list = getListOf_Coordinates(cutbitmap);
		Bitmap bitmap = null;

		if (list.size() > 50) {
			bitmap = Bitmap.createBitmap(cutbitmap, list.get(50).getX(), 0,
				list.get(list.size() - 50).getX() - list.get(50).getX(), bp.getHeight());
		}

		return bitmap;
	}


	/**
	 * @param bp ,Bitmap图像,
	 * @return
	 */
	private ArrayList<Coordinates> getListOf_Coordinates(Bitmap bp) {

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
				if ((r <= red_Rmax && r >= red_Rmin) && (g <= red_Gmax && g >= red_Gmin) && (b <= red_Bmax && b >= red_Bmin)) {//红色
					list.add(new Coordinates(x, y));

				} else if ((r <= Green_Rmax && r >= Green_Rmin) && (g <= Green_Gmax && g >= Green_Gmin) && (b <= Green_Bmax && b >=
					Green_Bmin)) {//绿色
					list.add(new Coordinates(x, y));
				}
			}
		}
		return list;
	}


	/**
	 * @param bp 1--红色左转,2--红色右转,3--绿色左转,4--绿色右转
	 * @return
	 */

	public int shapeIdentfyTraffic(Bitmap bp) {
		if (bp == null) {
			return 0;
		}

		ArrayList<Coordinates> rlistcoorl = new ArrayList<Coordinates>();
		ArrayList<Coordinates> rlistcoorr = new ArrayList<Coordinates>();
		ArrayList<Coordinates> glistcoorl = new ArrayList<Coordinates>();
		ArrayList<Coordinates> glistcoorr = new ArrayList<Coordinates>();


		int critical_number = mIdentyColor.getmSharedPreferences().getInt(StringColor.Critical_Number, 3000);//取临界值,默认为3000


		int sort = 0;
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
				if ((r <= red_Rmax && r >= red_Rmin) && (g <= red_Gmax && g >= red_Gmin) && (b <= red_Bmax && b >= red_Bmin)) {//红色

					if (x < width / 2) {
						rlistcoorl.add(new Coordinates(x, y));  //取左半边的点
					} else {
						rlistcoorr.add(new Coordinates(x, y));//取右半边的点
					}

					sort = 1;

				} else if ((r <= Green_Rmax && r >= Green_Rmin) && (g <= Green_Gmax && g >= Green_Gmin) && (b <= Green_Bmax && b >=
					Green_Bmin)) {//绿色

					if (x < width / 2) {
						glistcoorl.add(new Coordinates(x, y));  //取左半边的点
					} else {
						glistcoorr.add(new Coordinates(x, y));//取右半边的点
					}

					sort = 2;
				}
			}
		}
		if (sort == 1) {   //红色

			if (rlistcoorl.size() > rlistcoorr.size()) {  //左边点比右边多,

				setLeft_Coordinates(rlistcoorl.size());
				setRight_Coordinates(rlistcoorr.size());

				return 1;   //红色左转
			} else {

				setLeft_Coordinates(rlistcoorl.size());
				setRight_Coordinates(rlistcoorr.size());

				return 2;  //红色右转
			}

		} else if (sort == 2) {//绿色


			if (glistcoorl.size() > glistcoorr.size()) {  //左边点比右边多,

				if (glistcoorl.size() - glistcoorr.size() < critical_number) {  //判断为掉头

					setLeft_Coordinates(glistcoorl.size());
					setRight_Coordinates(glistcoorr.size());

					return 5;
				} else {

					setLeft_Coordinates(glistcoorl.size());
					setRight_Coordinates(glistcoorr.size());

					return 3;   //绿色左转
				}
			} else {

				if (glistcoorr.size() - glistcoorl.size() < critical_number) {  //判断为掉头


					setLeft_Coordinates(glistcoorl.size());
					setRight_Coordinates(glistcoorr.size());
					return 5;
				} else {

					setLeft_Coordinates(glistcoorl.size());
					setRight_Coordinates(glistcoorr.size());

					return 4;  //绿色右转
				}
			}
		} else {
			return 6;
		}

	}

}
