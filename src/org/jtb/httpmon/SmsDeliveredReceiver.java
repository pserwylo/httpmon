package org.jtb.httpmon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsDeliveredReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.d("httpmon", "SMS delivered");
			break;
		case Activity.RESULT_CANCELED:
			Log.d("httpmon", "SMS not delivered");
			break;
		}
	}
}
