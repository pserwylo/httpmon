package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Prefs prefs = new Prefs(context);
		if (!prefs.isBootStart()) {
			return;
		}
		MonitorScheduler scheduler = new MonitorScheduler(context);
		ArrayList<Monitor> monitors = prefs.getMonitors();
		scheduler.restartAll(monitors);
		if (prefs.isBackgroundNotification()) {
			scheduler.addBackgroundNotification(monitors);
		}
	}

}
