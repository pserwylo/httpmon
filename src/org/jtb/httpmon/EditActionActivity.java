package org.jtb.httpmon;

import org.jtb.httpmon.model.Action;
import org.jtb.httpmon.model.ActionType;
import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.ConditionType;
import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public abstract class EditActionActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	protected Action mAction;

	protected abstract int getLayout();

	protected abstract void initViews();

	protected abstract boolean validateAction();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout());

		mAction = savedInstanceState != null ? (Action) savedInstanceState
				.get("org.jtb.httpmon.action")
				: null;
		if (mAction == null) {
			Bundle extras = getIntent().getExtras();
			mAction = extras != null ? (Action) extras
					.get("org.jtb.httpmon.action") : null;
		}
		if (mAction == null) {
			ActionType at = savedInstanceState != null ? (ActionType) savedInstanceState
					.get("org.jtb.httpmon.conditionType")
					: null;
			if (at == null) {
				Bundle extras = getIntent().getExtras();
				at = extras != null ? (ActionType) extras
						.get("org.jtb.httpmon.actionType") : null;
			}

			mAction = at.newAction();
		}

		initViews();
		setViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.save_menu).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, CANCEL_MENU, 1, R.string.cancel_menu).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return result;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private boolean save() {
		setAction();
		if (validateAction()) {
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.action", mAction);
			setResult(Activity.RESULT_OK, intent);
			return true;
		}
		return false;
	}

	private void cancel() {
		setResult(Activity.RESULT_CANCELED);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (save()) {
				return super.onKeyDown(keyCode, event);
			} else {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_MENU:
			if (save()) {
				finish();
			}
			return true;
		case CANCEL_MENU:
			cancel();
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected abstract void setAction();

	protected abstract void setViews();

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		}
	}
}