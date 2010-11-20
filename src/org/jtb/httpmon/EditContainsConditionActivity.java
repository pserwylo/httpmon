package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jtb.httpmon.model.ContainsCondition;
import org.jtb.httpmon.model.ContentContainsCondition;
import org.jtb.httpmon.model.PingCondition;
import org.jtb.httpmon.model.ResponseTimeCondition;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public abstract class EditContainsConditionActivity extends
		EditConditionActivity {

	private EditText mPatternEdit;
	private RadioGroup mTypeGroup;
	private RadioButton mSubstringRadio;
	private RadioButton mWildcardRadio;
	private RadioButton mRegexRadio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void setCondition() {
		String pattern = mPatternEdit.getText().toString();
		((ContainsCondition) mCondition).setPattern(pattern);
		if (mSubstringRadio.isChecked()) {
			((ContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_SUBSTRING);
		} else if (mWildcardRadio.isChecked()) {
			((ContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_WILDCARD);
		} else if (mRegexRadio.isChecked()) {
			((ContainsCondition) mCondition)
					.setPatternType(ContentContainsCondition.TYPE_REGEX);
		}
	}

	protected void setViews() {
		mPatternEdit.setText(((ContainsCondition) mCondition).getPattern());
		switch (((ContainsCondition) mCondition).getPatternType()) {
		case ContainsCondition.TYPE_SUBSTRING:
			mSubstringRadio.setChecked(true);
			break;
		case ContainsCondition.TYPE_WILDCARD:
			mWildcardRadio.setChecked(true);
			break;
		case ContainsCondition.TYPE_REGEX:
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
		String pattern = ((ContainsCondition) mCondition).getPattern();
		if (pattern == null || pattern.length() == 0) {
			Toast.makeText(this, "Enter a non-empty pattern.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (((ContainsCondition) mCondition).getPatternType() == ContainsCondition.TYPE_REGEX) {
			try {
				Pattern p = Pattern.compile(pattern);
			} catch (PatternSyntaxException e) {
				Toast.makeText(this, "Invalid regular expression.",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}
}