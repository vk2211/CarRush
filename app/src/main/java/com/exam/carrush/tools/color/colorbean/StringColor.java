package com.exam.carrush.tools.color.colorbean;

/**
 * Created by epai on 17/3/3.
 */

public class StringColor {

	public static String Red_Rmax = "Red_Rmax";
	public static String Red_Rmin = "Red_Rmin";
	public static String Red_Gmax = "Red_Gmax";
	public static String Red_Gmin = "Red_Gmin";
	public static String Red_Bmax = "Red_Bmax";
	public static String Red_Bmin = "Red_Bmin";


	public static String Green_Rmax = "Green_Rmax";
	public static String Green_Rmin = "Green_Rmin";
	public static String Green_Gmax = "Green_Gmax";
	public static String Green_Gmin = "Green_Gmin";
	public static String Green_Bmax = "Green_Bmax";
	public static String Green_Bmin = "Green_Bmin";


	public static String Blue_Rmax = "Blue_Rmax";
	public static String Blue_Rmin = "Blue_Rmin";
	public static String Blue_Gmax = "Blue_Gmax";
	public static String Blue_Gmin = "Blue_Gmin";
	public static String Blue_Bmax = "Blue_Bmax";
	public static String Blue_Bmin = "Blue_Bmin";


	public static String Yellow_Rmax = "Yellow_Rmax";
	public static String Yellow_Rmin = "Yellow_Rmin";
	public static String Yellow_Gmax = "Yellow_Gmax";
	public static String Yellow_Gmin = "Yellow_Gmin";
	public static String Yellow_Bmax = "Yellow_Bmax";
	public static String Yellow_Bmin = "Yellow_Bmin";


	public static String Product_Rmax = "Product_Rmax";
	public static String Product_Rmin = "Product_Rmin";
	public static String Product_Gmax = "Product_Gmax";
	public static String Product_Gmin = "Product_Gmin";
	public static String Product_Bmax = "Product_Bmax";
	public static String Product_Bmin = "Product_Bmin";


	public static String Cyan_Rmax = "Cyan_Rmax";
	public static String Cyan_Rmin = "Cyan_Rmin";
	public static String Cyan_Gmax = "Cyan_Gmax";
	public static String Cyan_Gmin = "Cyan_Gmin";
	public static String Cyan_Bmax = "Cyan_Bmax";
	public static String Cyan_Bmin = "Cyan_Bmin";


	public static String Black_Rmax = "Black_Rmax";
	public static String Black_Rmin = "Black_Rmin";
	public static String Black_Gmax = "Black_Gmax";
	public static String Black_Gmin = "Black_Gmin";
	public static String Black_Bmax = "Black_Bmax";
	public static String Black_Bmin = "Black_Bmin";


	public static String White_Rmax = "White_Rmax";
	public static String White_Rmin = "White_Rmin";
	public static String White_Gmax = "White_Gmax";
	public static String White_Gmin = "White_Gmin";
	public static String White_Bmax = "White_Bmax";
	public static String White_Bmin = "White_Bmin";


	public static String Background_Rmax = "Background_Rmax";
	public static String Background_Rmin = "Background_Rmin";
	public static String Background_Gmax = "Background_Gmax";
	public static String Background_Gmin = "Background_Gmin";
	public static String Background_Bmax = "Background_Bmax";
	public static String Background_Bmin = "Background_Bmin";


	public static String getRmax(int colornum) {


		switch (colornum) {

		case 0:
			return Red_Rmax;

		case 1:
			return Green_Rmax;

		case 2:

			return Blue_Rmax;

		case 3:

			return Yellow_Rmax;

		case 4:

			return Product_Rmax;

		case 5:

			return Cyan_Rmax;

		case 6:

			return Black_Rmax;

		case 7:

			return White_Rmax;
		case 8:

			return Background_Rmax;

		default:
			return null;
		}

	}


	public static String getRmin(int colornum) {

		switch (colornum) {

		case 0:
			return Red_Rmin;

		case 1:
			return Green_Rmin;


		case 2:

			return Blue_Rmin;

		case 3:

			return Yellow_Rmin;

		case 4:

			return Product_Rmin;

		case 5:

			return Cyan_Rmin;

		case 6:

			return Black_Rmin;

		case 7:

			return White_Rmin;
		case 8:

			return Background_Rmin;

		default:
			return null;
		}
	}


	public static String getGmax(int colornum) {

		switch (colornum) {

		case 0:
			return Red_Gmax;

		case 1:
			return Green_Gmax;


		case 2:

			return Blue_Gmax;

		case 3:

			return Yellow_Gmax;

		case 4:

			return Product_Gmax;

		case 5:

			return Cyan_Gmax;

		case 6:

			return Black_Gmax;

		case 7:

			return White_Gmax;
		case 8:

			return Background_Gmax;

		default:
			return null;
		}
	}

	public static String getGmin(int colornum) {

		switch (colornum) {

		case 0:
			return Red_Gmin;

		case 1:
			return Green_Gmin;


		case 2:

			return Blue_Gmin;

		case 3:

			return Yellow_Gmin;

		case 4:

			return Product_Gmin;

		case 5:

			return Cyan_Gmin;

		case 6:

			return Black_Gmin;

		case 7:

			return White_Gmin;
		case 8:

			return Background_Gmin;

		default:
			return null;
		}
	}

	public static String getBmax(int colornum) {

		switch (colornum) {

		case 0:

			return Red_Bmax;
		case 1:

			return Green_Bmax;
		case 2:

			return Blue_Bmax;
		case 3:

			return Yellow_Bmax;
		case 4:

			return Product_Bmax;
		case 5:

			return Cyan_Bmax;
		case 6:

			return Black_Bmax;
		case 7:

			return White_Bmax;
		case 8:

			return Background_Bmax;

		default:
			return null;
		}
	}

	public static String getBmin(int colornum) {

		switch (colornum) {

		case 0:

			return Red_Bmin;
		case 1:

			return Green_Bmin;
		case 2:

			return Blue_Bmin;
		case 3:

			return Yellow_Bmin;
		case 4:

			return Product_Bmin;
		case 5:

			return Cyan_Bmin;
		case 6:

			return Black_Bmin;
		case 7:

			return White_Bmin;
		case 8:

			return Background_Bmin;

		default:
			return null;
		}
	}


}
