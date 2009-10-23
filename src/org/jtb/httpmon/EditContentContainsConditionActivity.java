package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;

import org.jtb.httpmon.model.ContentContainsCondition;
import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.ResponseTimeCondition;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditContentContainsConditionActivity extends EditContainsConditionActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayout() {
		return R.layout.edit_content_contains_condition;
	}
}