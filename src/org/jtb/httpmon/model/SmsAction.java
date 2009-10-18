package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class SmsAction extends Action {
	private int intervalMinutes = 15;
	private long lastNotificationTime = -1;
	private int failureCount = 0;
	private int requiredFailureCount = 1;
	private String phoneNumber;

	public SmsAction(ActionType actionType) {
		super(actionType);
	}

	@Override
	public void init(JSONObject jo) {
		try {
			this.intervalMinutes = jo.getInt("intervalMinutes");
			this.lastNotificationTime = jo.getLong("lastNotificationTime");
			this.failureCount = jo.getInt("failureCount");
			this.requiredFailureCount = jo.getInt("requiredFailureCount");
			this.phoneNumber = jo.getString("phoneNumber");
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

		StringBuilder msg = new StringBuilder();
		msg.append("Monitor failed: ");
		msg.append(monitor.getName());
		msg.append('\n');
		msg.append(monitor.getRequest().toString());

		sendSms(context, phoneNumber, msg.toString());
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
			jo.put("phoneNumber", phoneNumber);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	public String toString() {
		return "Send text message to: " + phoneNumber + ", every "
				+ intervalMinutes + " minutes, after " + requiredFailureCount
				+ " failures";
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private void sendSms(final Context context, String destNumber,
			String message) {

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				new Intent("sms_sent"), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent("sms_delivered"), 0);

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(destNumber, null, message, sentPI, deliveredPI);
	}
}
