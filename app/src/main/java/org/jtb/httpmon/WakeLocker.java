package org.jtb.httpmon;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class WakeLocker {
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

	public static synchronized void acquire(Context context, String name) {
		String tag = getLockTag(name);
		WakeLock wakeLock = getLock(context, tag);
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
			Log.d("httpmon", "wake lock acquired for tag: " + tag);
		}
	}

	public static synchronized void release(String name) {
		String tag = getLockTag(name);
		WakeLock wakeLock = wakeLocks.get(tag);
		if (wakeLock == null) {
			Log
					.w("httpmon",
							"release attempted, but wake lock was null for tag: "
									+ tag);
		} else {
			if (wakeLock.isHeld()) {
				wakeLock.release();
				wakeLocks.remove(tag);
				Log.d("httpmon", "wake lock released for tag: " + tag);
			} else {
				Log.w("httpmon",
						"release attempted, but wake lock was not held for tag: "
								+ tag);
			}
		}
	}

	private static String getLockTag(String name) {
		String tag = "org.jtb.httpmon.lock." + name;
		return tag;
	}
}
