package org.jtb.httpmon.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private String url;
	private int interval;
	//TODO: http parameters
	
	public Request() {
		// nothing
	}
	
	public Request(JSONObject jo) {
		try {
			this.url = jo.getString("url");
			this.interval = jo.getInt("interval");
		} catch (JSONException e) {
			throw new RuntimeException("error converting from JSON object", e) ;
		}
	}
	
	public JSONObject toJSONObject() {
		try {
			JSONObject jo = new JSONObject();
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

	@Override
	public String toString() {
		return url;
	}
}
