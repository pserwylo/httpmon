package org.jtb.httpmon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Monitor implements Serializable, Comparable<Monitor> {
	private static final long serialVersionUID = 1L;

	public static final int STATE_STOPPED = 0;
	public static final int STATE_STARTED = 1;
	public static final int STATE_VALID = 2;
	public static final int STATE_INVALID = 3;
	public static final int STATE_RUNNING = 4;

	private String name;
	private Request request;
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private int state = STATE_STOPPED;
	private long creationTime = -1;
	private long lastUpdatedTime = -1;

	public Monitor() {
		creationTime = System.currentTimeMillis();
	}

	public Monitor(JSONObject jo) {
		try {
			this.name = jo.getString("name");
			this.state = jo.getInt("state");
			this.creationTime = jo.getLong("creationTime");
			if (jo.has("lastUpdatedTime")) {
				this.lastUpdatedTime = jo.getLong("lastUpdatedTime");
			}
			this.setRequest(new Request(jo.getJSONObject("request")));

			JSONArray conditionArray = jo.getJSONArray("conditions");
			if (conditionArray != null) {
				for (int i = 0; i < conditionArray.length(); i++) {
					JSONObject conditionObject = conditionArray
							.getJSONObject(i);
					Condition condition = ConditionType
							.newCondition(conditionObject);
					conditions.add(condition);
				}
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monitor other = (Monitor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public JSONObject toJSONObject() {
		try {
			JSONObject jo = new JSONObject();
			jo.put("name", name);
			jo.put("state", state);
			jo.put("creationTime", creationTime);
			jo.put("lastUpdatedTime", lastUpdatedTime);
			jo.put("request", request.toJSONObject());
			JSONArray conditionArray = new JSONArray();
			for (int i = 0; i < conditions.size(); i++) {
				JSONObject conditionObject = conditions.get(i).toJSONObject();
				conditionArray.put(conditionObject);
			}
			jo.put("conditions", conditionArray);
			return jo;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public static ArrayList<Monitor> toMonitorList(JSONArray ja) {
		try {
			ArrayList<Monitor> monitors = new ArrayList<Monitor>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject monitorJo = ja.getJSONObject(i);
				Monitor monitor = new Monitor(monitorJo);
				monitors.add(monitor);
			}
			return monitors;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public static JSONArray toJSONArray(ArrayList<Monitor> monitors) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < monitors.size(); i++) {
			JSONObject jo = monitors.get(i).toJSONObject();
			ja.put(jo);
		}

		return ja;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public int compareTo(Monitor other) {
		if (other.getCreationTime() > creationTime) {
			return -1;
		} else if (other.getCreationTime() < creationTime) {
			return 1;
		}
		return 0;
	}

	public void setLastUpdatedTime() {
		this.lastUpdatedTime = System.currentTimeMillis();
	}

	public long getLastUpdatedTime() {
		return lastUpdatedTime;
	}
}
