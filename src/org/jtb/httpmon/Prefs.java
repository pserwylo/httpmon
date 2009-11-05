package org.jtb.httpmon;

import java.security.KeyRep.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Prefs {
	private Context context = null;

	public Prefs(Context context) {
		this.context = context;
	}

	private String getString(String key, String def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String s = prefs.getString(key, def);
		return s;
	}

	private int getInt(String key, int def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		int i = Integer.parseInt(prefs.getString(key, Integer.toString(def)));
		return i;
	}

	private float getFloat(String key, float def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		float f = Float.parseFloat(prefs.getString(key, Float.toString(def)));
		return f;
	}

	private long getLong(String key, long def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		long l = Long.parseLong(prefs.getString(key, Long.toString(def)));
		return l;
	}

	private void setString(String key, String val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putString(key, val);
		e.commit();
	}

	private void setBoolean(String key, boolean val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putBoolean(key, val);
		e.commit();
	}

	private void setInt(String key, int val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putString(key, Integer.toString(val));
		e.commit();
	}

	private void setLong(String key, long val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putString(key, Long.toString(val));
		e.commit();
	}

	private boolean getBoolean(String key, boolean def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean b = prefs.getBoolean(key, def);
		return b;
	}

	public int getConnectionTimeout() {
		return getInt("connectionTimeout", 30);
	}

	public int getReadTimeout() {
		return getInt("readTimeout", 30);
	}
	
	public String getUserAgent() {
		return getString("userAgent", "");
	}
	
	public void addMonitor(Monitor monitor) {
		ArrayList<Monitor> monitors = getMonitors();
		monitors.add(monitor);
		setMonitors(monitors);
	}

	public void removeMonitor(Monitor monitor) {
		ArrayList<Monitor> monitors = getMonitors();
		monitors.remove(monitor);
		setMonitors(monitors);
	}

	public void setMonitor(Monitor monitor) {
		ArrayList<Monitor> monitors = getMonitors();
		monitors.remove(monitor);
		monitors.add(monitor);
		setMonitors(monitors);
	}

	public void setMonitors(ArrayList<Monitor> monitors) {
		JSONArray ja = Monitor.toJSONArray(monitors);
		String arrayString = ja.toString();
		setString("monitors", arrayString);
	}

	public ArrayList<Monitor> getMonitors() {
		String arrayString = getString("monitors", "[]");
		JSONArray ja;
		try {
			ja = new JSONArray(arrayString);
		} catch (JSONException e) {
			throw new AssertionError("could not parse monitor array string");
		}
		ArrayList<Monitor> monitors = Monitor.toMonitorList(ja);
		return monitors;
	}

	public Monitor getMonitor(String name) {
		ArrayList<Monitor> monitors = getMonitors();
		for (int i = 0; i < monitors.size(); i++) {
			if (monitors.get(i).getName().equals(name)) {
				return monitors.get(i);
			}
		}
		return null;
	}
	
	public boolean isBootStart() {
		return getBoolean("bootStart", false);
	}
}
