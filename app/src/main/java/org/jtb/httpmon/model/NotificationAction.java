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
import android.support.v4.app.NotificationCompat;

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

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setContentTitle("Monitor failed: " + monitor.getName())
				.setContentText(monitor.getRequest().toString())
				.setSmallIcon(android.R.drawable.stat_notify_error)
				.setTicker("Monitor failed: " + monitor.getName())
				.setWhen(monitor.getLastUpdatedTime())
				.setNumber(failureCount)
				.setAutoCancel(true);

		if (flashNotification) {
			builder.setLights(Color.parseColor("#DC143C"), 250, 100);
		}
		
		if (alertNotification) {
			builder.setDefaults(Notification.DEFAULT_SOUND);
		}

		if (vibrateNotification) {
			builder.setVibrate(new long[] { 250, 100, 250, 100, 250, 100, 250, 100, 250 });
		}
		
		Intent notificationIntent = new Intent(context,
				ManageMonitorsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		builder.setContentIntent(contentIntent);

		nm.notify(monitor.getName().hashCode(), builder.build());
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
