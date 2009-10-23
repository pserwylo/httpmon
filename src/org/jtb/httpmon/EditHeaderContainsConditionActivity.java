package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;

import org.jtb.httpmon.model.ContentContainsCondition;
import org.jtb.httpmon.model.HeaderContainsCondition;
import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.ResponseTimeCondition;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditHeaderContainsConditionActivity extends EditContainsConditionActivity {
	private EditText mHeaderEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void setCondition() {
		super.setCondition();
		
		String header = mHeaderEdit.getText().toString();
		((HeaderContainsCondition) mCondition).setHeader(header);
	}

	protected void setViews() {
		super.setViews();
		mHeaderEdit.setText(((HeaderContainsCondition) mCondition)
				.getHeader());
	}

	@Override
	protected void initViews() {
		super.initViews();
		mHeaderEdit = (EditText) findViewById(R.id.header_edit);
	}

	protected boolean validateCondition() {
		if (!super.validateCondition()) {
			return false;
		}
		if (((HeaderContainsCondition) mCondition).getHeader() == null
				|| ((HeaderContainsCondition) mCondition).getHeader()
						.length() == 0) {
			Toast.makeText(this, "Enter a non-empty header field.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	protected int getLayout() {
		return R.layout.edit_header_contains_condition;
	}
}