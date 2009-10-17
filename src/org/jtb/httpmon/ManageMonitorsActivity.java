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
	private static final int NOTIFY_RUNNING = 100;

	private static final int NEW_MONITOR_MENU = 0;
	private static final int START_ALL_MENU = 1;
	private static final int STOP_ALL_MENU = 2;
	private static final int PREFS_MENU = 3;

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

		mMonitorList = (ListView) findViewById(R.id.monitor_list);
		mMonitorList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				mEditMonitor = mMonitors.get(position);
				stopMonitor(mEditMonitor);
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

		setAllStopped();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_MONITOR_MENU, 0, R.string.new_monitor_menu).setIcon(
				R.drawable.add);
		menu.add(0, START_ALL_MENU, 1, R.string.start_all_menu).setIcon(
				R.drawable.upload);
		menu.add(0, STOP_ALL_MENU, 2, R.string.stop_all_menu).setIcon(
				R.drawable.cancel);
		menu.add(0, PREFS_MENU, 3, R.string.preferences_menu).setIcon(
				R.drawable.preferences);
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
			stopAll();
			return true;
		case START_ALL_MENU:
			startAll();
			return true;
		case PREFS_MENU:
			Intent i = new Intent(this, PrefsActivity.class);
			startActivityForResult(i, PREFS_REQUEST);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void stopAll() {
		for (int i = 0; i < mMonitors.size(); i++) {
			stopMonitor(mMonitors.get(i));
		}
	}

	private void startAll() {
		for (int i = 0; i < mMonitors.size(); i++) {
			startMonitor(mMonitors.get(i));
		}
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
					stopMonitor(monitor);
					startMonitor(monitor);
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

	private void setAllStopped() {
		Prefs prefs = new Prefs(this);
		ArrayList<Monitor> monitors = prefs.getMonitors();
		for (int i = 0; i < monitors.size(); i++) {
			monitors.get(i).setState(Monitor.STATE_STOPPED);
		}
		prefs.setMonitors(monitors);
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
		addRunningNotification();
		Log.d(getClass().getSimpleName(), "paused");
	}

	private void removeRunningNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFY_RUNNING);
	}

	private void addRunningNotification() {
		int runCount = 0;
		for (int i = 0; mMonitors != null && i < mMonitors.size(); i++) {
			if (mMonitors.get(i).getState() != Monitor.STATE_STOPPED) {
				runCount++;
			}
		}

		if (runCount > 0) {
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = android.R.drawable.stat_notify_sync;
			CharSequence tickerText = runCount + " monitor(s) running";

			Notification notification = new Notification(icon, tickerText,
					System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;

			CharSequence contentTitle = tickerText;
			CharSequence contentText = "Click to manage";

			Intent notificationIntent = new Intent(this,
					ManageMonitorsActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);

			notification.setLatestEventInfo(this, contentTitle, contentText,
					contentIntent);
			nm.notify(NOTIFY_RUNNING, notification);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		registerReceiver(mReceiver, new IntentFilter("ManageMonitors.update"));
		removeRunningNotification();
		update();
		mUpdateTimer = new Timer();
		mUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				sendBroadcast(new Intent("ManageMonitors.update"));
			}
		}, 15 * 1000, 15 * 1000);
		Log.d(getClass().getSimpleName(), "resumed");
	}

	void stopMonitor(Monitor monitor) {
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(monitor.getName(), null, this,
				MonitorReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mgr.cancel(pi);
		monitor.setState(Monitor.STATE_STOPPED);
		Prefs prefs = new Prefs(this);
		prefs.setMonitor(monitor);
		update();
	}

	void startMonitor(Monitor monitor) {
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(monitor.getName(), null, this,
				MonitorReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mgr.cancel(pi);
		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock
				.elapsedRealtime(), monitor.getRequest().getInterval() * 1000,
				pi);
		monitor.setState(Monitor.STATE_STARTED);
		Prefs prefs = new Prefs(this);
		prefs.setMonitor(monitor);
		update();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		}
		return null;
	}
}