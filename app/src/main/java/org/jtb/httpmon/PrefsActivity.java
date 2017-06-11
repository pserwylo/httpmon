package org.jtb.httpmon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {
	private PrefsActivity mThis;

	private EditTextPreference mTimeoutPreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.prefs);

		mThis = this;
		setResult(Activity.RESULT_CANCELED);

		mTimeoutPreference = (EditTextPreference) findPreference("timeout");
	}
}
