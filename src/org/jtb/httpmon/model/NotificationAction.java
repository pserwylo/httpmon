package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationAction extends Action {
	private int intervalMinutes;
	private long lastNotificationTime = -1;
	
	public NotificationAction(ActionType actionType) {
		super(actionType);
	}

	@Override
	public void init(JSONObject jo) {
		try {
			this.intervalMinutes = jo.getInt("intervalMinutes");
			this.lastNotificationTime = jo.getLong("lastNotificationTime");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void perform(Monitor monitor) {
		long now = System.currentTimeMillis();
		if (lastNotificationTime + intervalMinutes*60*1000 > now) {
			return;
		}
		
		// TODO: send notification
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
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}
	
	public String toString() {
		return "Notify every " + intervalMinutes + " minutes";
	}
}
