package org.jtb.httpmon;

import java.util.List;

import org.jtb.httpmon.model.Monitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MonitorClickDialog extends AlertDialog {
	public static class Builder extends AlertDialog.Builder {
		private List<Monitor> mMonitors;
		private int mPosition;
		private ManageMonitorsActivity mActivity;
		private MonitorScheduler mScheduler;

		public Builder(ManageMonitorsActivity activity, List<Monitor> monitors,
				int position) {
			super(activity);

			this.mActivity = activity;
			this.mMonitors = monitors;
			this.mPosition = position;
			this.mScheduler = new MonitorScheduler(mActivity);
			
			String[] items = new String[3];
			if (mMonitors.get(mPosition).getState() == Monitor.STATE_STOPPED) {
				items[0] = "Start";
			} else {
				items[0] = "Stop";
			}
			items[1] = "Edit";
			items[2] = "Remove";

			setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Monitor monitor = mMonitors.get(mPosition);
					AlertDialog ad = (AlertDialog) dialog;
					switch (which) {
					case 0:
						if (monitor.getState() == Monitor.STATE_STOPPED) {
							mScheduler.start(monitor);
						} else {
							mScheduler.stop(monitor);
							mActivity.update();
						}
						break;
					case 1:
						mScheduler.stop(monitor);
						mActivity.setEditMonitor(monitor);
						Intent i = new Intent(ad.getContext(),
								EditMonitorActivity.class);
						i.putExtra("org.jtb.httpmon.monitor", monitor);
						mActivity.startActivityForResult(i,
								ManageMonitorsActivity.EDIT_MONITOR_REQUEST);
						break;
					case 2:
						mScheduler.stop(monitor);
						Prefs prefs = new Prefs(ad.getContext());
						prefs.removeMonitor(monitor);
						mActivity.update();
						break;
					}
				}
			});
			setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
	}

	public MonitorClickDialog(Context context, List<Monitor> monitors) {
		super(context);
	}
}
