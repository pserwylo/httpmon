package org.jtb.httpmon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jtb.httpmon.model.Action;
import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;
import org.jtb.httpmon.model.Response;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MonitorService extends IntentService {
	public MonitorService() {
		super("monitorService");
		trustEveryone();
	}

	private void trustEveryone() {
		try {
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {

						public boolean verify(String hostname,
								SSLSession session) {
							return true;
						}
					});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context
					.getSocketFactory());
		} catch (Exception e) { // should never happen
			e.printStackTrace();
		}
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
			if (response.getThrowable() != null) {
				Log.w(getClass().getSimpleName(), response.getThrowable());
			}
			int state = Monitor.STATE_VALID;
			for (int i = 0; i < monitor.getConditions().size(); i++) {
				if (!monitor.getConditions().get(i).isValid(response)) {
					state = Monitor.STATE_INVALID;
					break;
				}
			}

			monitor = prefs.getMonitor(monitor.getName());
			if (monitor != null && monitor.getState() != Monitor.STATE_STOPPED) {
				monitor.setState(state);
				monitor.setLastUpdatedTime();

				for (int i = 0; i < monitor.getActions().size(); i++) {
					Action action = monitor.getActions().get(i);
					if (monitor.getState() == Monitor.STATE_INVALID) {
						action.failure(this, monitor);
					} else if (monitor.getState() == Monitor.STATE_VALID) {
						action.success(this, monitor);
					}
				}
				prefs.setMonitor(monitor);
				sendBroadcast(new Intent("ManageMonitors.update"));
			}
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

		Prefs prefs = new Prefs(this);
		int timeout = prefs.getTimeout();
		String userAgent = prefs.getUserAgent();

		try {
			URL u = new URL(request.getUrl());
			CodeTimer timer = new CodeTimer();
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();

			if (userAgent != null && userAgent.length() != 0) {
				uc.setRequestProperty("User-Agent", userAgent);
			}
			uc.setReadTimeout(timeout * 1000);
			uc.setUseCaches(false);

			int responseCode = uc.getResponseCode();
			response.setResponseCode(responseCode);
			response.setAlive(true);

			StringBuilder body = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), "ISO-8859-1"), 8192);
			String line = null;

			while ((line = reader.readLine()) != null) {
				body.append(line);
				body.append('\n');
			}
			response.setResponseTime(timer.getElapsed());
			response.setContent(body.toString());

			response.setHeaderFields(uc.getHeaderFields());
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
