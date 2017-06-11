package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;

import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.ResponseTimeCondition;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditResponseTimeConditionActivity extends EditConditionActivity {
	private EditText mResponseTimeEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected int getLayout() {
		return R.layout.edit_response_time_condition;
	}

	protected void setCondition() {
		int rt = Integer.parseInt(mResponseTimeEdit.getText().toString());
		((ResponseTimeCondition)mCondition).setResponseTimeMilliseconds(rt);
	}

	protected void setViews() {
		mResponseTimeEdit.setText(Long.toString(((ResponseTimeCondition)mCondition).getResponseTimeMilliseconds()));
	}

	@Override
	protected void initViews() {		
		mResponseTimeEdit = (EditText)findViewById(R.id.response_time_edit);
	}
	
	protected boolean validateCondition() {
		if (((ResponseTimeCondition)mCondition).getResponseTimeMilliseconds() == 0) {
			Toast.makeText(this, "Please give your your condition a non-zero response time.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}	
}