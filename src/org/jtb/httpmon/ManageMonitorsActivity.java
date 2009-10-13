package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ManageMonitorsActivity extends Activity {
	private static final int NEW_MONITOR_MENU = 0;

	private static final int NEW_MONITOR_REQUEST = 0;

	private ListView mMonitorList;
	private ArrayList<Monitor> mMonitors;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_monitors);
		
		mMonitorList = (ListView)findViewById(R.id.monitor_list);
		
		update();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_MONITOR_MENU, 0, R.string.new_monitor_menu).setIcon(
				R.drawable.add);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case NEW_MONITOR_MENU:
			Intent intent = new Intent(this, EditMonitorActivity.class);
			startActivityForResult(intent, NEW_MONITOR_REQUEST);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case NEW_MONITOR_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Monitor monitor = (Monitor)data.getSerializableExtra("org.jtb.httpmon.monitor");
				Prefs prefs = new Prefs(this);
				prefs.addMonitor(monitor);
				update();
			}
			break;
		}
	}
	
	private void update() {
		Prefs prefs = new Prefs(this);
		mMonitors = prefs.getMonitors();
		MonitorAdapter ma = new MonitorAdapter(this, mMonitors);
		mMonitorList.setAdapter(ma);
	}
	
}