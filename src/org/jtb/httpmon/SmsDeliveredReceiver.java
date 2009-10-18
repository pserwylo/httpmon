package org.jtb.httpmon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsDeliveredReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.d(getClass().getSimpleName(), "SMS delivered");
			break;
		case Activity.RESULT_CANCELED:
			Log.d(getClass().getSimpleName(), "SMS not delivered");
			break;
		}
	}
}
