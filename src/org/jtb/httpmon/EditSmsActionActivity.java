package org.jtb.httpmon;

import org.jtb.httpmon.model.NotificationAction;
import org.jtb.httpmon.model.SmsAction;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EditSmsActionActivity extends EditActionActivity {
	private EditText mPhoneEdit;
	private EditText mIntervalEdit;
	private EditText mFailureEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayout() {
		return R.layout.edit_sms_action;
	}

	protected void setAction() {
		int ni = Integer.parseInt(mIntervalEdit.getText().toString());
		((SmsAction) mAction).setIntervalMinutes(ni);
		int fc = Integer.parseInt(mFailureEdit.getText().toString());
		((SmsAction) mAction).setRequiredFailureCount(fc);
		String pn = mPhoneEdit.getText().toString();
		((SmsAction) mAction).setPhoneNumber(pn);
	}

	protected void setViews() {
		mIntervalEdit.setText(Long.toString(((SmsAction) mAction)
				.getIntervalMinutes()));
		mFailureEdit.setText(Long.toString(((SmsAction) mAction)
				.getRequiredFailureCount()));
		mPhoneEdit.setText(((SmsAction) mAction).getPhoneNumber());
	}

	@Override
	protected void initViews() {
		mIntervalEdit = (EditText) findViewById(R.id.interval_edit);
		mFailureEdit = (EditText) findViewById(R.id.failure_edit);
		mPhoneEdit = (EditText) findViewById(R.id.phone_edit);
	}

	protected boolean validateAction() {
		if (((SmsAction) mAction).getPhoneNumber() == null
				|| ((SmsAction) mAction).getPhoneNumber().length() == 0) {
			Toast.makeText(this, "Enter a phone number.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (((SmsAction) mAction).getIntervalMinutes() == 0) {
			Toast.makeText(this, "Provide a non-zero interval.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (((SmsAction) mAction).getRequiredFailureCount() == 0) {
			Toast.makeText(this, "Enter non-zero number of failures.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}