package org.jtb.httpmon;

import java.util.List;

import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.Monitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ConditionClickDialog extends AlertDialog {
	public static class Builder extends AlertDialog.Builder {
		private Monitor mMonitor;
		private int mPosition;
		private EditMonitorActivity mActivity;

		public Builder(EditMonitorActivity activity, Monitor monitor,
				int position) {
			super(activity);

			this.mActivity = activity;
			this.mMonitor = monitor;
			this.mPosition = position;

			String[] items = new String[2];
			items[0] = "Edit";
			items[1] = "Remove";
			
			setItems(items,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Condition condition = mMonitor.getConditions().get(mPosition);
							AlertDialog ad = (AlertDialog) dialog;
							switch (which) {
							case 0:
								Intent i = new Intent(ad.getContext(),
										condition.getConditionType().getActivityClass());
								i.putExtra("org.jtb.httpmon.condition", condition);
								mActivity
										.startActivityForResult(
												i,
												EditMonitorActivity.EDIT_CONDITION_REQUEST);
								break;
							case 1:
								mMonitor.getConditions().remove(condition);
								mActivity.setConditionsView();
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

	public ConditionClickDialog(Context context, List<Monitor> monitors) {
		super(context);
	}
}
