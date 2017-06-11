package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

public class MonitorScheduler {
	private static final int NOTIFY_RUNNING = 100;

	private Context mContext = null;
	private Prefs mPrefs = null;

	public MonitorScheduler(Context context) {
		this.mContext = context;
		mPrefs = new Prefs(mContext);
	}

	public void start(Monitor monitor) {
		AlarmManager mgr = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(monitor.getName(), null, mContext,
				MonitorReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mgr.cancel(pi);
		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock
				.elapsedRealtime(), monitor.getRequest().getInterval() * 1000,
				pi);
		monitor.setState(Monitor.STATE_STARTED);
		mPrefs.setMonitor(monitor);
	}

	public void stop(Monitor monitor) {
		AlarmManager mgr = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(monitor.getName(), null, mContext,
				MonitorReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mgr.cancel(pi);
		monitor.setState(Monitor.STATE_STOPPED);
		mPrefs.setMonitor(monitor);
	}

	public void stopAll(ArrayList<Monitor> monitors) {
		for (int i = 0; i < monitors.size(); i++) {
			stop(monitors.get(i));
		}
	}

	public void startAll(ArrayList<Monitor> monitors) {
		for (int i = 0; i < monitors.size(); i++) {
			start(monitors.get(i));
		}
	}

	public void restartAll(ArrayList<Monitor> monitors) {
		for (int i = 0; i < monitors.size(); i++) {
			if (monitors.get(i).getState() != Monitor.STATE_STOPPED) {
				start(monitors.get(i));
			}
		}
	}

	public void addBackgroundNotification(ArrayList<Monitor> monitors) {
		int runCount = 0;
		for (int i = 0; monitors != null && i < monitors.size(); i++) {
			if (monitors.get(i).getState() != Monitor.STATE_STOPPED) {
				runCount++;
			}
		}

		if (runCount > 0) {
			NotificationManager nm = (NotificationManager) mContext
					.getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = R.drawable.status;
			CharSequence tickerText = runCount + " monitor(s) running";

			NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
					.setSmallIcon(icon)
					.setTicker(tickerText)
					.setContentTitle(tickerText)
					.setContentTitle("Click to manage")
					.setWhen(System.currentTimeMillis())
					.setOngoing(true);

			Intent notificationIntent = new Intent(mContext,
					ManageMonitorsActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
					notificationIntent, 0);

			Notification notification = builder
					.setContentIntent(contentIntent)
					.build();

			notification.flags |= Notification.FLAG_NO_CLEAR;

			nm.notify(NOTIFY_RUNNING, notification);
		}
	}

	public void removeBackgroundNotification() {
		NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFY_RUNNING);
	}
}
