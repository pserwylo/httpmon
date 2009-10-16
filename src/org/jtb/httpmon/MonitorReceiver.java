package org.jtb.httpmon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MonitorReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(getClass().getSimpleName(), "received intent for: "
				+ intent.getAction());
		Intent wlIntent = new Intent("acquire", null, context,
				WakeLockReceiver.class);
		wlIntent.putExtra("org.jtb.httpmon.monitor.name", intent.getAction());
		context.sendBroadcast(wlIntent);
		Intent svcIntent = new Intent(intent.getAction(), null, context,
				MonitorService.class);
		context.startService(svcIntent);
	}
}
