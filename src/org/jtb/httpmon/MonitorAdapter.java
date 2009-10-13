package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		Monitor monitor = mMonitors.get(position);
		View view = inflater.inflate(R.layout.monitor_row, null);

		TextView nameText = (TextView) view.findViewById(R.id.name_text);
		nameText.setText(monitor.getName());

		return view;
	}
}
