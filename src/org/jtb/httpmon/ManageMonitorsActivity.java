package org.jtb.httpmon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ManageMonitorsActivity extends Activity {
	private static final int HELP_DIALOG = 0;
	
	private static final int NEW_MONITOR_MENU = 0;
	private static final int START_ALL_MENU = 1;
	private static final int STOP_ALL_MENU = 2;
	private static final int PREFS_MENU = 3;
	private static final int HELP_MENU = 4;

	static final int NEW_MONITOR_REQUEST = 0;
	static final int EDIT_MONITOR_REQUEST = 1;
	static final int PREFS_REQUEST = 2;

	private ListView mMonitorList;
	private ArrayList<Monitor> mMonitors;
	private ManageMonitorsActivity mThis;
	private TextView mEmptyListText;
	private ManageMonitorsReceiver mReceiver;
	private Monitor mEditMonitor;
	private Timer mUpdateTimer;
	private MonitorScheduler mScheduler;
	
	private AlertDialog mHelpDialog;
	
	public Monitor getEditMonitor() {
		return mEditMonitor;
	}

	public void setEditMonitor(Monitor mEditMonitor) {
		this.mEditMonitor = mEditMonitor;
	}

	private AlertDialog mMonitorClickDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_monitors);

		mThis = this;
		mReceiver = new ManageMonitorsReceiver(this);
		mScheduler = new MonitorScheduler(this);
		
		mMonitorList = (ListView) findViewById(R.id.monitor_list);
		mMonitorList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				mEditMonitor = mMonitors.get(position);
				mScheduler.stop(mEditMonitor);
				Intent intent = new Intent(mThis, EditMonitorActivity.class);
				intent.putExtra("org.jtb.httpmon.monitor", mEditMonitor);
				startActivityForResult(intent, EDIT_MONITOR_REQUEST);
			}
		});
		mMonitorList.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				AlertDialog.Builder builder = new MonitorClickDialog.Builder(
						mThis, mMonitors, position);
				mMonitorClickDialog = builder.create();
				mMonitorClickDialog.show();
				return true;
			}
		});

		mEmptyListText = (TextView) findViewById(R.id.empty_list_text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_MONITOR_MENU, 0, R.string.new_monitor_menu).setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(0, START_ALL_MENU, 1, R.string.start_all_menu).setIcon(
				android.R.drawable.ic_menu_upload);
		menu.add(0, STOP_ALL_MENU, 2, R.string.stop_all_menu).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, PREFS_MENU, 3, R.string.preferences_menu).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(0, HELP_MENU, 3, R.string.help_menu).setIcon(
				android.R.drawable.ic_menu_help);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case NEW_MONITOR_MENU:
			Intent intent = new Intent(this, EditMonitorActivity.class);
			intent.putExtra("org.jtb.httpmon.new", true);
			startActivityForResult(intent, NEW_MONITOR_REQUEST);
			return true;
		case STOP_ALL_MENU:
			mScheduler.stopAll(mMonitors);
			update();
			return true;
		case START_ALL_MENU:
			mScheduler.startAll(mMonitors);
			return true;
		case PREFS_MENU:
			Intent i = new Intent(this, PrefsActivity.class);
			startActivityForResult(i, PREFS_REQUEST);
			return true;
		case HELP_MENU:
			showDialog(HELP_DIALOG);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PREFS_REQUEST:
			break;
		case NEW_MONITOR_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Monitor monitor = (Monitor) data
						.getSerializableExtra("org.jtb.httpmon.monitor");
				Prefs prefs = new Prefs(this);
				prefs.addMonitor(monitor);
				update();
				Toast.makeText(this, "New monitor saved.", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast
						.makeText(this, "New monitor canceled.",
								Toast.LENGTH_LONG).show();
			}
			break;
		case EDIT_MONITOR_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Monitor monitor = (Monitor) data
						.getSerializableExtra("org.jtb.httpmon.monitor");
				if (monitor.getState() != Monitor.STATE_STOPPED) {
					mScheduler.stop(monitor);
					mScheduler.start(monitor);
				}
				Prefs prefs = new Prefs(this);
				prefs.removeMonitor(mEditMonitor);
				prefs.addMonitor(monitor);
				update();
				Toast.makeText(this, "Monitor saved.", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(this, "Edit monitor canceled.",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	void update() {
		Prefs prefs = new Prefs(this);
		mMonitors = prefs.getMonitors();

		if (mMonitors.size() == 0) {
			mMonitorList.setVisibility(View.GONE);
			mEmptyListText.setVisibility(View.VISIBLE);
		} else {
			Collections.sort(mMonitors);
			mMonitorList.setVisibility(View.VISIBLE);
			mEmptyListText.setVisibility(View.GONE);
			MonitorAdapter ma = new MonitorAdapter(this, mMonitors);
			mMonitorList.setAdapter(ma);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		mUpdateTimer.cancel();
		mScheduler.addRunningNotification(mMonitors);
		
		Log.d(getClass().getSimpleName(), "paused");
	}
	
	@Override
	public void onResume() {
		super.onResume();

		registerReceiver(mReceiver, new IntentFilter("ManageMonitors.update"));
		mScheduler.removeRunningNotification();
		update();
		mScheduler.restartAll(mMonitors);
		mUpdateTimer = new Timer();
		mUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				sendBroadcast(new Intent("ManageMonitors.update"));
			}
		}, 15 * 1000, 15 * 1000);
		
		Log.d(getClass().getSimpleName(), "resumed");
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case HELP_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Manage Monitors Help");
			builder
					.setMessage(R.string.manage_monitors_help);
			builder.setNeutralButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dismissDialog(HELP_DIALOG);
						}
					});
			mHelpDialog = builder.create();
			return mHelpDialog;
		
		}
		return null;
	}
}