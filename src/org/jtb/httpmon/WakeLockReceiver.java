package org.jtb.httpmon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class WakeLockReceiver extends BroadcastReceiver {
	private static final Map<String, WakeLock> wakeLocks = new HashMap<String, PowerManager.WakeLock>();

	private static PowerManager.WakeLock getLock(Context context, String tag) {
		WakeLock wl = wakeLocks.get(tag);
		if (wl == null) {
			PowerManager mgr = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);

			wl = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
			wl.setReferenceCounted(true);
			wakeLocks.put(tag, wl);
		}
		return wl;
	}

	private static synchronized void acquire(Context context, String tag) {
		WakeLock wakeLock = getLock(context, tag);
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
			Log.d(WakeLockReceiver.class.getSimpleName(),
					"wake lock acquired for tag: " + tag);
		}
	}

	private static synchronized void release(String tag) {
		WakeLock wakeLock = wakeLocks.get(tag);
		if (wakeLock == null) {
			Log
					.w(WakeLockReceiver.class.getSimpleName(),
							"release attempted, but wake lock was null for tag: "
									+ tag);
		} else {
			if (wakeLock.isHeld()) {
				wakeLock.release();
				wakeLocks.remove(tag);
				Log.d(WakeLockReceiver.class.getSimpleName(),
						"wake lock released for tag: " + tag);
			} else {
				Log.w(WakeLockReceiver.class.getSimpleName(),
						"release attempted, but wake lock was not held for tag: "
								+ tag);
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("acquire")) {
			acquire(context, getLockTag(intent));
		} else if (intent.getAction().equals("release")) {
			release(getLockTag(intent));
		}
	}

	private static String getLockTag(Intent i) {
		String name = i.getStringExtra("org.jtb.httpmon.monitor.name");
		if (name == null) {
			throw new AssertionError("name was null");
		}
		return "org.jtb.httpmon.wakelock." + name;
	}
}
