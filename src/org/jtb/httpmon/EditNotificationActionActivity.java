package org.jtb.httpmon;

import org.jtb.httpmon.model.NotificationAction;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditNotificationActionActivity extends EditActionActivity {
	private EditText mIntervalEdit;

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
	}

	protected void setViews() {
		mIntervalEdit.setText(Long
				.toString(((NotificationAction) mAction)
						.getIntervalMinutes()));
	}

	@Override
	protected void initViews() {
		mIntervalEdit = (EditText) findViewById(R.id.interval_edit);
	}

	protected boolean validateAction() {
		if (((NotificationAction) mAction).getIntervalMinutes() == 0) {
			Toast
					.makeText(
							this,
							"Please provide a non-zero interval.",
							Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}