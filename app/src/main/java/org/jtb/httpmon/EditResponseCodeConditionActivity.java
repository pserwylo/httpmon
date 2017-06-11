package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;

import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.ResponseCodeCondition;
import org.jtb.httpmon.model.ResponseTimeCondition;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditResponseCodeConditionActivity extends EditConditionActivity {
	private EditText mResponseCodeEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected int getLayout() {
		return R.layout.edit_response_code_condition;
	}

	protected void setCondition() {
		int rc = Integer.parseInt(mResponseCodeEdit.getText().toString());
		((ResponseCodeCondition)mCondition).setResponseCode(rc);
	}

	protected void setViews() {
		mResponseCodeEdit.setText(Integer.toString(((ResponseCodeCondition)mCondition).getResponseCode()));
	}

	@Override
	protected void initViews() {		
		mResponseCodeEdit = (EditText)findViewById(R.id.response_code_edit);
	}
	
	protected boolean validateCondition() {
		if (((ResponseCodeCondition)mCondition).getResponseCode() == -1) {
			Toast.makeText(this, "Please give your your condition a non-zero response code.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}	
}