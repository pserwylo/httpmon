package org.jtb.httpmon.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request implements Serializable {
	private String name;
	private String url;
	private int interval;
	//TODO: http parameters
	
	public Request() {
		// nothing
	}
	
	public Request(JSONObject jo) {
		try {
			this.name = jo.getString("name");
			this.url = jo.getString("url");
			this.interval = jo.getInt("interval");
		} catch (JSONException e) {
			throw new RuntimeException("error converting from JSON object", e) ;
		}
	}
	
	public JSONObject toJSONObject() {
		try {
			JSONObject jo = new JSONObject();
			jo.put("name", name);
			jo.put("url", url);
			jo.put("interval", interval);
			return jo;
		} catch (JSONException e) {
			throw new RuntimeException("error converting to JSON object", e);
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setInterval(String i) {
		this.interval = Integer.parseInt(i);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static ArrayList<Request> toRequestList(JSONArray ja) {
		try {
			ArrayList<Request> requests = new ArrayList<Request>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject requestJo = ja.getJSONObject(i);
				Request request = new Request(requestJo);
				requests.add(request);
			}
			return requests;
		} catch (JSONException e) {
			throw new RuntimeException("error converting from JSON array", e);
		}
	}
	
	public static JSONArray toJSONArray(ArrayList<Request> requests) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < requests.size(); i++) {
			JSONObject jo  = requests.get(i).toJSONObject();
			ja.put(jo);
		}
		
		return ja;
	}
	
	@Override
	public String toString() {
		return name;
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
		Request other = (Request) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
