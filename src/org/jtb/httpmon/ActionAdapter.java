package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Action;
import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.Monitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActionAdapter extends ArrayAdapter {
	private LayoutInflater inflater;
	private ArrayList<Action> mActions;

	ActionAdapter(Activity context, ArrayList<Action> actions) {
		super(context, R.layout.action_row, actions);
		this.inflater = context.getLayoutInflater();
		this.mActions = actions;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Action action = mActions.get(position);
		View view = inflater.inflate(R.layout.action_row, null);

		TextView stringText = (TextView) view.findViewById(R.id.string_text);
		stringText.setText(action.toString());

		return view;
	}
}
