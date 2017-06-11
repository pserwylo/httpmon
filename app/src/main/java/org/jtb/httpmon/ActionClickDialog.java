package org.jtb.httpmon;

import java.util.List;

import org.jtb.httpmon.model.Action;
import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.Monitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ActionClickDialog extends AlertDialog {
	public static class Builder extends AlertDialog.Builder {
		private Monitor mMonitor;
		private int mPosition;
		private EditMonitorActivity mActivity;
		private Action mAction;

		public Builder(EditMonitorActivity activity, Monitor monitor,
				int position) {
			super(activity);

			this.mActivity = activity;
			this.mMonitor = monitor;
			this.mPosition = position;
			this.mAction = mMonitor.getActions().get(mPosition);

			String[] items;
			if (mAction.getActionType().getActivityClass() == null) {
				items = new String[1];
				items[0] = "Remove";
			} else {
				items = new String[2];
				items[0] = "Remove";
				items[1] = "Edit";
			}

			setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog ad = (AlertDialog) dialog;
					switch (which) {
					case 0:
						mMonitor.getActions().remove(mAction);
						mActivity.setActionsView();
						break;
					case 1:
						Class c = mAction.getActionType()
								.getActivityClass();
						if (c == null) {
							return;
						}
						Intent i = new Intent(ad.getContext(), c);
						i.putExtra("org.jtb.httpmon.action", mAction);
						mActivity.startActivityForResult(i,
								EditMonitorActivity.EDIT_ACTION_REQUEST);
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

	public ActionClickDialog(Context context, List<Monitor> monitors) {
		super(context);
	}
}
