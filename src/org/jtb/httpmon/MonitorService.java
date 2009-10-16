package org.jtb.httpmon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;
import org.jtb.httpmon.model.Response;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MonitorService extends IntentService {
	public MonitorService() {
		super("monitorService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String name = null;
		try {
			name = intent.getAction();
			Log.d(getClass().getSimpleName(), "received intent for: " + name);
			if (name == null) {
				Log.w(getClass().getSimpleName(), "name was null, returning");
				return;
			}

			Prefs prefs = new Prefs(this);
			Monitor monitor = prefs.getMonitor(name);
			if (monitor == null) {
				Log.w(getClass().getSimpleName(), "monitor was null for name: "
						+ name + ", returning");
				return;
			}
			monitor.setState(Monitor.STATE_RUNNING);
			prefs.setMonitor(monitor);
			sendBroadcast(new Intent("ManageMonitors.update"));

			Response response = getResponse(monitor.getRequest());
			int state = Monitor.STATE_VALID;
			for (int i = 0; i < monitor.getConditions().size(); i++) {
				if (!monitor.getConditions().get(i).isValid(response)) {
					state = Monitor.STATE_INVALID;
					break;
				}
			}

			monitor = prefs.getMonitor(monitor.getName());
			if (monitor != null) {
				monitor.setState(state);
				monitor.setLastUpdatedTime();
				prefs.setMonitor(monitor);
			}
			sendBroadcast(new Intent("ManageMonitors.update"));
		} finally {
			if (name != null) {
				Intent wlIntent = new Intent("release", null, this,
						WakeLockReceiver.class);
				wlIntent.putExtra("org.jtb.httpmon.monitor.name", name);
				sendBroadcast(wlIntent);
			}
		}
	}

	private Response getResponse(Request request) {
		Response response = new Response();
		BufferedReader reader = null;

		try {
			URL u = new URL(request.getUrl());
			CodeTimer timer = new CodeTimer();
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			uc.setReadTimeout(30 * 1000); // 30 seconds

			response.setAlive(true);
			int responseCode = uc.getResponseCode();
			response.setResponseCode(responseCode);
			if (uc.getResponseCode() != 200) {
				// TODO: android log
				return null;
			}

			StringBuilder body = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), "ISO-8859-1"), 8192);
			String line = null;

			while ((line = reader.readLine()) != null) {
				body.append(line);
				body.append('\n');
			}
			response.setResponseTime(timer.getElapsed());
			response.setBody(body.toString());
		} catch (Throwable t) {
			response.setThrowable(t);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ioe) {
				// TODO: android log
			}
		}

		return response;
	}
}
