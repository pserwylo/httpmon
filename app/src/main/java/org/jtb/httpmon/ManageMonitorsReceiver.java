package org.jtb.httpmon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ManageMonitorsReceiver extends BroadcastReceiver {
	private ManageMonitorsActivity activity;

	public ManageMonitorsReceiver(ManageMonitorsActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("ManageMonitors.update")) {
			activity.update();
		}
	}
}
