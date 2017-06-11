package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConditionAdapter extends ArrayAdapter {
	private LayoutInflater inflater;
	private ArrayList<Condition> mConditions;

	ConditionAdapter(Activity context, ArrayList<Condition> conditions) {
		super(context, R.layout.condition_row, conditions);
		this.inflater = context.getLayoutInflater();
		this.mConditions = conditions;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Condition condition = mConditions.get(position);
		View view = inflater.inflate(R.layout.condition_row, null);

		TextView stringText = (TextView) view.findViewById(R.id.string_text);
		stringText.setText(condition.toString());

		return view;
	}
}
