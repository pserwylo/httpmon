package org.jtb.httpmon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MonitorReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("httpmon", "received intent for: "
				+ intent.getAction());
		WakeLocker.acquire(context, intent.getAction());
		Intent svcIntent = new Intent(intent.getAction(), null, context,
				MonitorService.class);
		context.startService(svcIntent);
	}
}
