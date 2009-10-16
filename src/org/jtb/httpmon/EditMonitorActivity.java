package org.jtb.httpmon;

import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.ConditionType;
import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class EditMonitorActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	private static final int EDIT_REQUEST_REQUEST = 0;
	private static final int NEW_CONDITION_REQUEST = 1;
	static final int EDIT_CONDITION_REQUEST = 2;

	private Monitor mMonitor;
	private EditText mNameEdit;
	private Button mEditRequestButton;
	private TextView mRequestText;
	private TextView mEmptyRequestText;
	private EditMonitorActivity mThis;
	private ListView mConditionList;
	private Button mAddConditionButton;
	private TextView mEmptyConditionsText;
	private Spinner mConditionsSpinner;
	private AlertDialog mConditionClickDialog;
	private Condition mEditCondition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_monitor);

		mMonitor = savedInstanceState != null ? (Monitor) savedInstanceState
				.get("org.jtb.httpmon.monitor") : null;
		if (mMonitor == null) {
			Bundle extras = getIntent().getExtras();
			mMonitor = extras != null ? (Monitor) extras
					.get("org.jtb.httpmon.monitor") : null;
		}

		if (mMonitor == null) {
			mMonitor = new Monitor();
		}

		mThis = this;
		mNameEdit = (EditText) findViewById(R.id.name_edit);
		mRequestText = (TextView) findViewById(R.id.request_text);
		mEmptyRequestText = (TextView) findViewById(R.id.empty_request_text);
		mEmptyConditionsText = (TextView) findViewById(R.id.empty_conditions_text);

		mConditionList = (ListView) findViewById(R.id.condition_list);
		mConditionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				mEditCondition = mMonitor.getConditions().get(position);
				Class c = mEditCondition.getConditionType().getActivityClass();
				if (c == null) {
					return;
				}
				Intent intent = new Intent(mThis, mEditCondition
						.getConditionType().getActivityClass());
				intent.putExtra("org.jtb.httpmon.condition", mEditCondition);
				startActivityForResult(intent, EDIT_CONDITION_REQUEST);
			}
		});
		mConditionList
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						mEditCondition = mMonitor.getConditions().get(position);
						AlertDialog.Builder builder = new ConditionClickDialog.Builder(
								mThis, mMonitor, position);
						mConditionClickDialog = builder.create();
						mConditionClickDialog.show();
						return true;
					}
				});

		mAddConditionButton = (Button) findViewById(R.id.add_condition_button);
		mAddConditionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ConditionType ct = (ConditionType) mConditionsSpinner
						.getSelectedItem();
				Class c = ct.getActivityClass();
				if (c == null) {
					Condition condition = ct.newCondition();
					mMonitor.getConditions().add(condition);
					setConditionsView();
				} else {
					Intent intent = new Intent(mThis, ct.getActivityClass());
					intent.putExtra("org.jtb.httpmon.conditionType", ct);
					startActivityForResult(intent, NEW_CONDITION_REQUEST);
				}
			}
		});

		mEditRequestButton = (Button) findViewById(R.id.edit_request_button);
		mEditRequestButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mThis, EditRequestActivity.class);
				intent.putExtra("org.jtb.httpmon.request", mMonitor
						.getRequest());
				startActivityForResult(intent, EDIT_REQUEST_REQUEST);
			}
		});

		mConditionsSpinner = (Spinner) findViewById(R.id.conditions_spinner);
		ArrayAdapter<ConditionType> aa = new ArrayAdapter<ConditionType>(this,
				android.R.layout.simple_spinner_item, ConditionType.TYPES);
		aa
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mConditionsSpinner.setAdapter(aa);

		setNameView();
		setRequestView();
		setConditionsView();
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
			save();
			return true;
		case CANCEL_MENU:
			cancel();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void cancel() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	private void save() {
		setMonitor();
		if (validateMonitor()) {
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.monitor", mMonitor);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	private boolean validateMonitor() {
		if (mMonitor.getName() == null || mMonitor.getName().length() == 0) {
			Toast.makeText(this, "Please give your monitor a name first.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (mMonitor.getRequest() == null) {
			Toast.makeText(this, "Please define a requet for your monitor.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (mMonitor.getConditions().size() == 0) {
			Toast.makeText(this,
					"Please define one or more conditions for your monitor.",
					Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	private void setMonitor() {
		mMonitor.setName(mNameEdit.getText().toString());
	}

	private void setNameView() {
		mNameEdit.setText(mMonitor.getName());
	}

	void setConditionsView() {
		if (mMonitor.getConditions().size() == 0) {
			mConditionList.setVisibility(View.GONE);
			mEmptyConditionsText.setVisibility(View.VISIBLE);
		} else {
			mConditionList.setVisibility(View.VISIBLE);
			mEmptyConditionsText.setVisibility(View.GONE);
			ConditionAdapter ca = new ConditionAdapter(this, mMonitor
					.getConditions());
			mConditionList.setAdapter(ca);
		}
	}

	private void setRequestView() {
		if (mMonitor.getRequest() == null) {
			mRequestText.setVisibility(View.GONE);
			mEmptyRequestText.setVisibility(View.VISIBLE);
		} else {
			mRequestText.setVisibility(View.VISIBLE);
			mRequestText.setText(mMonitor.getRequest().toString());
			mEmptyRequestText.setVisibility(View.GONE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_REQUEST_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Request request = (Request) data
						.getSerializableExtra("org.jtb.httpmon.request");
				mMonitor.setRequest(request);
				setRequestView();
			}
			break;
		case NEW_CONDITION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Condition condition = (Condition) data
						.getSerializableExtra("org.jtb.httpmon.condition");
				mMonitor.getConditions().add(condition);
				setConditionsView();
			}
			break;
		case EDIT_CONDITION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Condition condition = (Condition) data
						.getSerializableExtra("org.jtb.httpmon.condition");
				mMonitor.getConditions().remove(mEditCondition);
				mMonitor.getConditions().add(condition);
				setConditionsView();
			}
			break;
		}
	}
}