package org.jtb.httpmon;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LogActivity extends Activity {
	private static final int MENU_PLAY = 6;
	private static final int WINDOW_SIZE = 500;
	 
	static final int CAT_WHAT = 0;
	static final int ENDSCROLL_WHAT = 1;

	private LinearLayout mCatLayout;
	private ScrollView mCatScroll;
	private Menu mMenu;
	private MenuItem mPlayItem;
	private Format mFormat = Format.TIME;
	private Level mLevel = Level.I;
	private Level mLastLevel = mLevel;
	private Logcat mLogcat;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CAT_WHAT:
				String line = (String) msg.obj;
				cat(line);
				break;
			case ENDSCROLL_WHAT:
				mCatScroll.post(new Runnable() {
					public void run() {
						mCatScroll.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
				break;
			}
		}
	};

	private void cat(String s) {
		if (mCatLayout.getChildCount() > WINDOW_SIZE) {
			mCatLayout.removeViewAt(0);
		}

		TextView entryText = new TextView(this);
		entryText.setText(s);
		Level level = mFormat.getLevel(s);
		if (level == null) {
			level = mLastLevel;
		} else {
			mLastLevel = level;
		}
		entryText.setTextColor(level.getColor());
		entryText.setTextSize(12);
		entryText.setTypeface(Typeface.DEFAULT_BOLD);
		mCatLayout.addView(entryText);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);

		mCatScroll = (ScrollView) findViewById(R.id.cat_scroll);
		mCatLayout = (LinearLayout) findViewById(R.id.cat_layout);

		reset();
	}

	private void reset() {
		Toast.makeText(this, "Reading log ...", Toast.LENGTH_LONG).show();
		mLastLevel = Level.V;

		new Thread(new Runnable() {
			public void run() {
				if (mLogcat != null) {
					mLogcat.stop();
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				mLogcat = new Logcat(mHandler, mFormat, mLevel);
				mLogcat.start();
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		mMenu = menu;

		mPlayItem = menu.add(0, MENU_PLAY, 0, R.string.pause_menu);
		mPlayItem.setIcon(android.R.drawable.ic_media_pause);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mLogcat.isPlay()) {
			mPlayItem.setTitle(R.string.pause_menu);
			mPlayItem.setIcon(android.R.drawable.ic_media_pause);
		} else {
			mPlayItem.setTitle(R.string.play_menu);
			mPlayItem.setIcon(android.R.drawable.ic_media_play);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PLAY:
			mLogcat.setPlay(!mLogcat.isPlay());
			return true;
		}

		return false;
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder;

		switch (id) {
		}
		return null;
	}

}