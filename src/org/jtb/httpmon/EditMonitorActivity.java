package org.jtb.httpmon;

import java.util.ArrayList;

import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditMonitorActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	private static final int NEW_REQUEST_REQUEST = 0;
	private static final int EDIT_REQUEST_REQUEST = 1;

	private Monitor mMonitor;
	private EditText mNameEdit;
	private Button mNewRequestButton;
	private Button mEditRequestButton;
	private EditMonitorActivity mThis;
	private Spinner mRequestSpinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_monitor);

		Boolean newMonitor = savedInstanceState != null ? (Boolean) savedInstanceState
				.get("org.jtb.httpmon.new")
				: null;
		if (newMonitor == null) {
			Bundle extras = getIntent().getExtras();
			newMonitor = extras != null ? (Boolean) extras
					.get("org.jtb.httpmon.new") : null;
		}

		if (newMonitor == null) {
			throw new AssertionError("new flag was not set");
		}

		if (newMonitor) {
			mMonitor = new Monitor();
		} else {
			mMonitor = savedInstanceState != null ? (Monitor) savedInstanceState
					.get("org.jtb.httpmon.monitor")
					: null;
			if (mMonitor == null) {
				Bundle extras = getIntent().getExtras();
				mMonitor = extras != null ? (Monitor) extras
						.get("org.jtb.httpmon.monitor") : null;
			}
			
			if (mMonitor == null) {
				throw new AssertionError("monitor was not set");
			}
		}
		

		mThis = this;
		mNameEdit = (EditText) findViewById(R.id.name_edit);
		if (!newMonitor) {
			mNameEdit.setEnabled(false);
		}
		
		mNewRequestButton = (Button) findViewById(R.id.new_request_button);
		mNewRequestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mThis, EditRequestActivity.class);
				intent.putExtra("org.jtb.httpmon.new", true);
				startActivityForResult(intent, NEW_REQUEST_REQUEST);
			}
		});
		mEditRequestButton = (Button) findViewById(R.id.edit_request_button);
		mEditRequestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mThis, EditRequestActivity.class);
				Request request = new Prefs(mThis).getRequest((String) mRequestSpinner.getSelectedItem());
				intent.putExtra("org.jtb.httpmon.new", false);
				intent.putExtra("org.jtb.httpmon.request", request);
				startActivityForResult(intent, EDIT_REQUEST_REQUEST);
			}
		});

		mRequestSpinner = (Spinner) findViewById(R.id.request_spinner);
		updateSpinner();
		setViews();
	}

	private void updateSpinner() {
		ArrayList<String> requestNames = new Prefs(this).getRequestNames();
		ArrayAdapter<String> raa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, requestNames);
		raa
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mRequestSpinner.setAdapter(raa);
	}

	private void selectRequest(Request request) {
		selectRequest(request.getName());
	}
	
	private void selectRequest(String requestName) {
		ArrayList<String> requestNames = new Prefs(this).getRequestNames();
		int i = requestNames.indexOf(requestName);
		mRequestSpinner.setSelection(i);
		mRequestSpinner.setSelected(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.save_menu).setIcon(R.drawable.save);
		menu.add(0, CANCEL_MENU, 1, R.string.cancel_menu).setIcon(
				R.drawable.cancel);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_MENU:
			setMonitor();
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.monitor", mMonitor);
			setResult(Activity.RESULT_OK, intent);
			finish();
			return true;
		case CANCEL_MENU:
			setResult(Activity.RESULT_CANCELED);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setMonitor() {
		mMonitor.setName(mNameEdit.getText().toString());
		mMonitor.setRequestName((String) mRequestSpinner.getSelectedItem());
	}
	
	private void setViews() {
		mNameEdit.setText(mMonitor.getName());
		selectRequest(mMonitor.getRequestName());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case NEW_REQUEST_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Request request = (Request) data
						.getSerializableExtra("org.jtb.httpmon.request");
				Prefs prefs = new Prefs(this);
				prefs.addRequest(request);
				updateSpinner();
				selectRequest(request);
			}
			break;
		case EDIT_REQUEST_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Request request = (Request) data
						.getSerializableExtra("org.jtb.httpmon.request");
				Prefs prefs = new Prefs(this);
				prefs.setRequest(request);
			}
			break;
		}
	}

}