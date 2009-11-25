package org.jtb.httpmon;

import java.util.Collections;
import java.util.HashMap;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class SmsSentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.d(getClass().getSimpleName(), "SMS sent");
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Log.d(getClass().getSimpleName(), "SMS generic failure");
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Log.d(getClass().getSimpleName(), "SMS no service");
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Log.d(getClass().getSimpleName(), "SMS null PDU");
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Log.d(getClass().getSimpleName(), "SMS radio off");
			break;
		}
	}
}
