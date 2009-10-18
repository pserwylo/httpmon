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

public class EditContentContainsConditionActivity extends EditConditionActivity {

	private EditText mPatternEdit;
	private RadioGroup mTypeGroup;
	private RadioButton mSubstringRadio;
	private RadioButton mWildcardRadio;
	private RadioButton mRegexRadio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayout() {
		return R.layout.edit_content_contains_condition;
	}

	protected void setCondition() {
		String pattern = mPatternEdit.getText().toString();
		((ContentContainsCondition) mCondition).setPattern(pattern);
		if (mSubstringRadio.isChecked()) {
			((ContentContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_SUBSTRING);
		} else if (mWildcardRadio.isChecked()) {
			((ContentContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_WILDCARD);
		} else if (mRegexRadio.isChecked()) {
			((ContentContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_REGEX);
		}
	}

	protected void setViews() {
		mPatternEdit.setText(((ContentContainsCondition) mCondition)
				.getPattern());
		switch (((ContentContainsCondition) mCondition).getPatternType()) {
		case ContentContainsCondition.TYPE_SUBSTRING:
			mSubstringRadio.setChecked(true);
			break;
		case ContentContainsCondition.TYPE_WILDCARD:
			mWildcardRadio.setChecked(true);
			break;
		case ContentContainsCondition.TYPE_REGEX:
			mRegexRadio.setChecked(true);
			break;
		}
	}

	@Override
	protected void initViews() {
		mPatternEdit = (EditText) findViewById(R.id.pattern_edit);
		mTypeGroup = (RadioGroup) findViewById(R.id.type_group);
		mSubstringRadio = (RadioButton) findViewById(R.id.substring_option);
		mWildcardRadio = (RadioButton) findViewById(R.id.wildcard_option);
		mRegexRadio = (RadioButton) findViewById(R.id.regex_option);
	}

	protected boolean validateCondition() {
		if (((ContentContainsCondition) mCondition).getPattern() == null
				|| ((ContentContainsCondition) mCondition).getPattern()
						.length() == 0) {
			Toast.makeText(this, "Enter a non-empty pattern.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}