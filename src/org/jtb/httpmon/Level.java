package org.jtb.httpmon;

import android.content.Context;
import android.graphics.Color;

public enum Level {
	V(0, Color.parseColor("#121212")), D(1, Color.parseColor("#00006C")), I(2,
			Color.parseColor("#20831B")), W(3, Color.parseColor("#FD7916")), E(
			4, Color.parseColor("#FD0010")), F(5, Color.parseColor("#ff0066"));

	private static Level[] byOrder = new Level[6];

	static {
		byOrder[0] = V;
		byOrder[1] = D;
		byOrder[2] = I;
		byOrder[3] = W;
		byOrder[4] = E;
		byOrder[5] = F;
	}

	private int mColor;
	private int mValue;

	private Level(int value, int color) {
		mValue = value;
		mColor = color;
	}

	public int getColor() {
		return mColor;
	}

	public int getValue() {
		return mValue;
	}

	public static Level getByOrder(int value) {
		return byOrder[value];
	}
}
