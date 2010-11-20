package org.jtb.httpmon;

import android.os.Bundle;

public class EditContentContainsConditionActivity extends EditContainsConditionActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayout() {
		return R.layout.edit_content_contains_condition;
	}
}