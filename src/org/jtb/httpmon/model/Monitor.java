package org.jtb.httpmon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Monitor implements Serializable {
	private String name;
	private String requestName;
	
	public Monitor() {
		// nothing
	}
	
	public Monitor(JSONObject jo) {
		try {
			this.name = jo.getString("name");
			this.setRequestName(jo.getString("requestName"));
		} catch (JSONException e) {
			throw new RuntimeException("error converting from JSON object", e) ;
		}
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
			jo.put("requestName", requestName);
			return jo;
		} catch (JSONException e) {
			throw new RuntimeException("error converting to JSON object", e);
		}
	}
	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getRequestName() {
		return requestName;
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
			throw new RuntimeException("error converting from JSON array", e);
		}
	}
	
	public static JSONArray toJSONArray(ArrayList<Monitor> monitors) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < monitors.size(); i++) {
			JSONObject jo  = monitors.get(i).toJSONObject();
			ja.put(jo);
		}
		
		return ja;
	}
}
