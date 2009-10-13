package org.jtb.httpmon;

import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditMonitorActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	private Monitor mMonitor;
	private EditText mNameEdit;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_monitor);

        mMonitor = savedInstanceState != null ? (Monitor) savedInstanceState
				.get("org.jtb.httpmon.monitor") : null;
		if (mMonitor == null) {
			Bundle extras = getIntent().getExtras();
			mMonitor = extras != null ? (Monitor) extras
					.get("org.jtb.httpmon.monitor") : null;
		}		
		if (mMonitor == null) {
			mMonitor = new Monitor();
		}
		
		mNameEdit = (EditText)findViewById(R.id.name_edit);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.save_menu).setIcon(
				R.drawable.save);
		menu.add(0, CANCEL_MENU, 1, R.string.cancel_menu).setIcon(
				R.drawable.cancel);
		return result;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_MENU:
			update();
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.monitor", mMonitor);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		case CANCEL_MENU:
			setResult(Activity.RESULT_CANCELED);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}	
	
	private void update() {
		mMonitor.setName(mNameEdit.getText().toString());
	}
}