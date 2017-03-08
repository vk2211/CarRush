/******************************************************************************
 * @file RaceMap.java
 * @brief
 * @author yaochuan (vk2211@163.com)
 * @module com.bkrcl.game_test
 * @date 2017年2月27日
 * @version 0.1
 * @history v0.1, 2017年2月27日, by yaochuan (vk2211@163.com)
 * 
 * 
 *          Copyright (C) ChinaUnicom 2017.
 *
 ******************************************************************************/

package com.exam.carrush.control;

import android.graphics.Point;
import android.util.Log;


public class CarModel {
	private static final String TAG = "Car";
	private static final int H = 0x10; // Horizontal
	private static final int V = 0x01; // Vertical
	private static final int P = 0x22; // Park
	private static final int D = 0x00; // Disable

	private static final int LE = 1; // Left Edge
	private static final int RE = 11;// Right Edge
	private static final int TE = 2; // Top Edge
	private static final int BE = 8; // Bottom Edge

	public static final int L = 1; // Left
	public static final int T = 2; // Top
	public static final int R = 3; // Right
	public static final int B = 4; // Bottom

	private int[][] mMap = {
		//               A     B     C     D     E     F     G     H     I     J     K
		/* 0 */{ 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 },
		/* 1 */{ 0x00, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x00 },
		/* 2 */{ 0x00, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x00 },
		/* 3 */{ 0x00, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x00 },
		/* 4 */{ 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00 },
		/* 5 */{ 0x00, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x00 },
		/* 6 */{ 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00 },
		/* 7 */{ 0x00, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x11, 0x10, 0x00 },
		/* 8 */{ 0x00, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x00 },
		/* 9 */{ 0x00, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x22, 0x00, 0x00 },
		/* X */{ 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 },
	};
	private Point mCurrentPos;
	private int mCurrentHeading;
	private CarMovementListener mCarMovementListener;

	public interface CarMovementListener {
		void onPrepare();

		void onTurn(int direction, int angle);

		void onGoToNextCross();

		void onGoBack();

		void onReach(int x, int y);

		void onEnd();
	}

	public void prepare() {
		mCarMovementListener.onPrepare();
	}

	public CarModel(int startX, int startY, int heading, CarMovementListener listener) {
		mCurrentPos = new Point(startX, startY);
		if (heading == 0) {
			if (startX <= TE) {
				mCurrentHeading = B;
			}
			if (startX >= BE) {
				mCurrentHeading = T;
			}
			if (startY <= LE) {
				mCurrentHeading = R;
			}
			if (startY >= RE) {
				mCurrentHeading = L;
			}
		} else {
			mCurrentHeading = heading;
		}
		mCarMovementListener = listener;
	}

	public void runTo(Point p) {
		run(mCurrentPos.x, mCurrentPos.y, p.x, p.y);
		mCarMovementListener.onReach(p.x, p.y);
	}

	public void runTo(int x, int y, int step) {
//		run(mCurrentPos.x, mCurrentPos.y, x, y);
		runRoad(mCurrentPos.x, mCurrentPos.y, x, y, step);
	}

	public void turnTo(int to) {
		turn(to);
	}

	private Point findNearestMainRoadPoint(int x, int y) {
		if (x <= TE) {
			return new Point(3, y);
		}
		if (x >= BE) {
			return new Point(7, y);
		}
		if (y <= LE) {
			return new Point(x, 2);
		}
		if (y >= RE) {
			return new Point(x, 10);
		}
		return new Point(x, y);
	}

	private void turn(int to) {
		if (mCurrentHeading == to) {
			return;
		}
		if (mCurrentHeading == T) {
			switch (to) {
			case L:
				mCarMovementListener.onTurn(L, 90);
				break;
			case R:
				mCarMovementListener.onTurn(R, 90);
				break;
			case T:
				break;
			case B:
				mCarMovementListener.onTurn(L, 180);
				break;
			}
		} else if (mCurrentHeading == R) {
			switch (to) {
			case L:
				mCarMovementListener.onTurn(L, 180);
				break;
			case R:
				break;
			case T:
				mCarMovementListener.onTurn(L, 90);
				break;
			case B:
				mCarMovementListener.onTurn(R, 90);
				break;
			}
		} else if (mCurrentHeading == L) {
			switch (to) {
			case L:
				break;
			case R:
				mCarMovementListener.onTurn(L, 180);
				break;
			case T:
				mCarMovementListener.onTurn(R, 90);
				break;
			case B:
				mCarMovementListener.onTurn(L, 90);
				break;
			}
		} else if (mCurrentHeading == B) {
			switch (to) {
			case L:
				mCarMovementListener.onTurn(R, 90);
				break;
			case R:
				mCarMovementListener.onTurn(L, 90);
				break;
			case T:
				mCarMovementListener.onTurn(L, 180);
				break;
			case B:
				break;
			}
		}
		mCurrentHeading = to;
	}

	private void runRoad(int fromMainX, int fromMainY, int toMainX, int toMainY, int step) {
		Log.d("runRoad", "#####################");
		if (fromMainX == toMainX && fromMainY == toMainY) {
			return;
		}
		int i;
		int j;
		if (fromMainX == toMainX) {
			i = fromMainX;
			if (fromMainY < toMainY) {
				turn(R);
				for (j = fromMainY + 1; j <= toMainY; j += step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			} else {
				turn(L);
				for (j = fromMainY - 1; j >= toMainY; j -= step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			}
		} else if (fromMainY == toMainY) {
			j = toMainY;
			if (fromMainX < toMainX) {
				turn(B);
				for (i = fromMainX + 1; i <= toMainX; i += step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			} else {
				turn(T);
				for (i = fromMainX - 1; i >= toMainX; i -= step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			}
		} else {
			i = fromMainX;
			if (fromMainY < toMainY) {
				turn(R);
				for (j = fromMainY + 1; j <= toMainY; j += step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			} else {
				turn(L);
				for (j = fromMainY - 1; j >= toMainY; j -= step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			}
			j = toMainY;
			if (fromMainX < toMainX) {
				turn(B);
				for (i = fromMainX + 1; i <= toMainX; i += step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			} else {
				turn(T);
				for (i = fromMainX - 1; i >= toMainX; i -= step) {
//					printf("(%d, %d)\n", i, j);
					Log.d(TAG, "runRoad: (" + i + "," + j + "), step=" + step + ",Heading: " + mCurrentHeading);
					mCurrentPos.x = i;
					mCurrentPos.y = j;
					mCarMovementListener.onGoToNextCross();
				}
			}
		}
		mCurrentPos.x = toMainX;
		mCurrentPos.y = toMainY;
	}

	public void back() {
		if (mCurrentHeading == L) {
			mCurrentPos.y++;
		} else if (mCurrentHeading == T) {
			mCurrentPos.x++;
		} else if (mCurrentHeading == R) {
			mCurrentPos.y--;
		} else if (mCurrentHeading == B) {
			mCurrentPos.x--;
		}
		mCarMovementListener.onGoBack();
	}

	private void run(int fromx, int fromy, int tox, int toy) {
		Point sp = findNearestMainRoadPoint(fromx, fromy);
		Point ep = findNearestMainRoadPoint(tox, toy);
		Log.e("1111111111", "111111111111");
		runRoad(fromx, fromy, sp.x, sp.y, 1);
		Log.e("2221111111", "111111111111");
		runRoad(sp.x, sp.y, ep.x, ep.y, 2);
		runRoad(ep.x, ep.y, tox, toy, 1);
	}

}
