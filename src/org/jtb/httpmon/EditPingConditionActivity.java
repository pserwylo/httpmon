package org.jtb.httpmon;

import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.ConditionType;
import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditPingConditionActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	private PingCondition mCondition;
	private EditText mResponseTimeEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_ping_condition);

		mCondition = savedInstanceState != null ? (PingCondition) savedInstanceState
				.get("org.jtb.httpmon.condition") : null;
		if (mCondition == null) {
			Bundle extras = getIntent().getExtras();
			mCondition = extras != null ? (PingCondition) extras
					.get("org.jtb.httpmon.condition") : null;
		}
		if (mCondition == null) {
			ConditionType ct = savedInstanceState != null ? (ConditionType) savedInstanceState
					.get("org.jtb.httpmon.conditionType") : null;
			if (ct == null) {
				Bundle extras = getIntent().getExtras();
				ct = extras != null ? (ConditionType) extras
						.get("org.jtb.httpmon.conditionType") : null;
			}

			mCondition = (PingCondition)ct.newCondition();
		}

		mResponseTimeEdit = (EditText) findViewById(R.id.response_time_edit);

		setViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.save_menu).setIcon(R.drawable.save);
		menu.add(0, CANCEL_MENU, 1, R.string.cancel_menu).setIcon(
				R.drawable.cancel);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_MENU:
			setCondition();
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.condition", mCondition);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		case CANCEL_MENU:
			setResult(Activity.RESULT_CANCELED);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setCondition() {
		int rt = Integer.parseInt(mResponseTimeEdit.getText().toString());
		mCondition.setResponseTimeMilliseconds(rt);
	}

	private void setViews() {
		mResponseTimeEdit.setText(Long.toString(mCondition.getResponseTimeMilliseconds()));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		}
	}

}