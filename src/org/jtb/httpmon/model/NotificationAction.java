package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.jtb.httpmon.ManageMonitorsActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class NotificationAction extends Action {
	private int intervalMinutes = 15;
	private long lastNotificationTime = -1;
	private int failureCount = 0;
	private int requiredFailureCount = 1;
	private boolean flashNotification = false;
	private boolean alertNotification = false;
	private boolean vibrateNotification = false;

	public NotificationAction(ActionType actionType) {
		super(actionType);
	}

	@Override
	public void init(JSONObject jo) {
		try {
			this.intervalMinutes = jo.getInt("intervalMinutes");
			this.lastNotificationTime = jo.getLong("lastNotificationTime");
			this.failureCount = jo.getInt("failureCount");
			this.requiredFailureCount = jo.getInt("requiredFailureCount");
			this.flashNotification = jo.getBoolean("flashNotification");
			this.alertNotification = jo.getBoolean("alertNotification");
			this.vibrateNotification = jo.getBoolean("vibrateNotification");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void failure(Context context, Monitor monitor) {
		failureCount++;

		if (failureCount < requiredFailureCount) {
			return;
		}
		long now = System.currentTimeMillis();
		if (lastNotificationTime + (intervalMinutes * 60 * 1000) > now) {
			return;
		}

		lastNotificationTime = now;

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = android.R.drawable.stat_notify_error;
		CharSequence tickerText = "Monitor failed: " + monitor.getName();

		Notification notification = new Notification(icon, tickerText, monitor
				.getLastUpdatedTime());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.number = failureCount;

		if (flashNotification) {
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.ledOffMS = 100;
			notification.ledOnMS = 250;
			notification.ledARGB = Color.parseColor("#DC143C");
		}
		
		if (alertNotification) {
			notification.flags |= Notification.DEFAULT_SOUND;
		}

		if (vibrateNotification) {
			notification.vibrate = new long[] { 250, 100, 250, 100, 250, 100, 250, 100, 250 };
		}
		
		CharSequence contentTitle = "Monitor failed: " + monitor.getName();
		CharSequence contentText = monitor.getRequest().toString();

		Intent notificationIntent = new Intent(context,
				ManageMonitorsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		nm.notify(monitor.getName().hashCode(), notification);
	}

	@Override
	public void success(Context context, Monitor monitor) {
		failureCount = 0;
	}

	public void setIntervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
	}

	public int getIntervalMinutes() {
		return intervalMinutes;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		try {
			jo.put("intervalMinutes", intervalMinutes);
			jo.put("lastNotificationTime", lastNotificationTime);
			jo.put("failureCount", failureCount);
			jo.put("requiredFailureCount", requiredFailureCount);
			jo.put("flashNotification", flashNotification);
			jo.put("alertNotification", alertNotification);
			jo.put("vibrateNotification", vibrateNotification);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	public String toString() {
		return "Notify every " + intervalMinutes + " minutes, after "
				+ requiredFailureCount + " failures";
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setRequiredFailureCount(int requiredFailureCount) {
		this.requiredFailureCount = requiredFailureCount;
	}

	public int getRequiredFailureCount() {
		return requiredFailureCount;
	}

	public void setFlashNotification(boolean flashNotification) {
		this.flashNotification = flashNotification;
	}

	public boolean isFlashNotification() {
		return flashNotification;
	}

	public void setAlertNotification(boolean alertNotification) {
		this.alertNotification = alertNotification;
	}

	public boolean isAlertNotification() {
		return alertNotification;
	}

	public void setVibrateNotification(boolean vibrateNotification) {
		this.vibrateNotification = vibrateNotification;
	}

	public boolean isVibrateNotification() {
		return vibrateNotification;
	}
}
