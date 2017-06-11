package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MonitorAdapter extends ArrayAdapter {
	private LayoutInflater inflater;
	private ArrayList<Monitor> mMonitors;

	MonitorAdapter(Activity context, ArrayList<Monitor> monitors) {
		super(context, R.layout.monitor_row, monitors);
		this.inflater = context.getLayoutInflater();
		this.mMonitors = monitors;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.monitor_row, null);			
		}
		
		Monitor monitor = mMonitors.get(position);

		ImageView statusImg = (ImageView) view.findViewById(R.id.status_img);
		int state = monitor.getState();
		int id;
		if (state == Monitor.STATE_INVALID) {
			id = R.drawable.invalid;
		} else if (state == Monitor.STATE_VALID) {
			id = R.drawable.valid;
		} else if (state == Monitor.STATE_RUNNING || state == Monitor.STATE_STARTED) {
			id = R.drawable.running;
		} else {
			id = R.drawable.stopped;
		}
		statusImg.setImageResource(id);

		TextView nameText = (TextView) view.findViewById(R.id.name_text);
		nameText.setText(monitor.getName());

		TextView requestText = (TextView) view.findViewById(R.id.request_text);
		requestText.setText(monitor.getRequest().toString());

		TextView lastText = (TextView) view.findViewById(R.id.last_text);
		if (state == Monitor.STATE_RUNNING) {
			lastText.setText("Updating ...");
		} else {
			lastText.setText(getFuzzyTime(monitor.getLastUpdatedTime()));
		}

		return view;
	}

	private String getFuzzyTime(long updated) {
		if (updated == -1) {
			return "Never updated";
		}
		long now = System.currentTimeMillis();
		long diff = now - updated;

		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffDays > 0) {
			return "Updated ~" + diffDays + " days ago";
		}
		long diffHours = diff / (60 * 60 * 1000);
		if (diffHours > 0) {
			return "Updated ~" + diffHours + " hours ago";
		}
		long diffMins = diff / (60 * 1000);
		if (diffMins > 0) {
			return "Updated ~" + diffMins + " minutes ago";
		}
		long diffSecs = diff / 1000;
		if (diffSecs > 0) {
			return "Updated ~" + diffSecs + " seconds ago";
		}
		return "Just updated";
	}
}
