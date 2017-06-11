package org.jtb.httpmon;

import org.jtb.httpmon.model.NotificationAction;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EditNotificationActionActivity extends EditActionActivity {
	private EditText mIntervalEdit;
	private EditText mFailureEdit;
	private CheckBox mFlashCheck;
	private CheckBox mAlertCheck;
	private CheckBox mVibrateCheck;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayout() {
		return R.layout.edit_notification_action;
	}

	protected void setAction() {
		int ni = Integer.parseInt(mIntervalEdit.getText()
				.toString());
		((NotificationAction) mAction).setIntervalMinutes(ni);
		int fc = Integer.parseInt(mFailureEdit.getText()
				.toString());
		((NotificationAction) mAction).setRequiredFailureCount(fc);
		boolean fn = mFlashCheck.isChecked();
		((NotificationAction) mAction).setFlashNotification(fn);
		boolean an = mAlertCheck.isChecked();
		((NotificationAction) mAction).setAlertNotification(an);
		boolean vn = mVibrateCheck.isChecked();
		((NotificationAction) mAction).setVibrateNotification(vn);
	}

	protected void setViews() {
		mIntervalEdit.setText(Long
				.toString(((NotificationAction) mAction)
						.getIntervalMinutes()));
		mFailureEdit.setText(Long
				.toString(((NotificationAction) mAction)
						.getRequiredFailureCount()));
		mFlashCheck.setChecked(((NotificationAction) mAction).isFlashNotification());
		mAlertCheck.setChecked(((NotificationAction) mAction).isAlertNotification());
		mVibrateCheck.setChecked(((NotificationAction) mAction).isVibrateNotification());
	}

	@Override
	protected void initViews() {
		mIntervalEdit = (EditText) findViewById(R.id.interval_edit);
		mFailureEdit = (EditText) findViewById(R.id.failure_edit);
		mFlashCheck = (CheckBox) findViewById(R.id.flash_check);
		mAlertCheck = (CheckBox) findViewById(R.id.alert_check);
		mVibrateCheck = (CheckBox) findViewById(R.id.vibrate_check);
	}

	protected boolean validateAction() {
		if (((NotificationAction) mAction).getIntervalMinutes() == 0) {
			Toast
					.makeText(
							this,
							"Provide a non-zero interval.",
							Toast.LENGTH_LONG).show();
			return false;
		}
		if (((NotificationAction) mAction).getRequiredFailureCount() == 0) {
			Toast
					.makeText(
							this,
							"Enter non-zero number of failures.",
							Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}