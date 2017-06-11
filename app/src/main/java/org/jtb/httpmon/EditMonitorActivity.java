package org.jtb.httpmon;

import org.jtb.httpmon.model.Action;
import org.jtb.httpmon.model.ActionType;
import org.jtb.httpmon.model.Condition;
import org.jtb.httpmon.model.ConditionType;
import org.jtb.httpmon.model.Monitor;
import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class EditMonitorActivity extends Activity {
	private static final int HELP_DIALOG = 0;

	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;
	private static final int HELP_MENU = 2;

	private static final int EDIT_REQUEST_REQUEST = 0;
	private static final int NEW_CONDITION_REQUEST = 1;
	private static final int NEW_ACTION_REQUEST = 2;
	static final int EDIT_CONDITION_REQUEST = 3;
	static final int EDIT_ACTION_REQUEST = 4;

	private Monitor mMonitor;
	private EditText mNameEdit;
	private Button mEditRequestButton;
	private TextView mRequestText;
	private TextView mEmptyRequestText;
	private EditMonitorActivity mThis;
	private ListView mConditionList;
	private ListView mActionList;
	private Button mAddConditionButton;
	private Button mAddActionButton;
	private TextView mEmptyConditionsText;
	private TextView mEmptyActionsText;
	private Spinner mConditionsSpinner;
	private Spinner mActionsSpinner;
	private Condition mEditCondition;
	private Action mEditAction;

	private AlertDialog mConditionClickDialog;
	private AlertDialog mActionClickDialog;
	private AlertDialog mHelpDialog;

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
		mEmptyActionsText = (TextView) findViewById(R.id.empty_actions_text);
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

		mActionList = (ListView) findViewById(R.id.action_list);
		mActionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				mEditAction = mMonitor.getActions().get(position);
				Class c = mEditAction.getActionType().getActivityClass();
				if (c == null) {
					return;
				}
				Intent intent = new Intent(mThis, mEditAction.getActionType()
						.getActivityClass());
				intent.putExtra("org.jtb.httpmon.action", mEditAction);
				startActivityForResult(intent, EDIT_ACTION_REQUEST);
			}
		});
		mActionList.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				mEditAction = mMonitor.getActions().get(position);
				AlertDialog.Builder builder = new ActionClickDialog.Builder(
						mThis, mMonitor, position);
				mActionClickDialog = builder.create();
				mActionClickDialog.show();
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

		mAddActionButton = (Button) findViewById(R.id.add_action_button);
		mAddActionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ActionType at = (ActionType) mActionsSpinner.getSelectedItem();
				Class c = at.getActivityClass();
				if (c == null) {
					Action action = at.newAction();
					mMonitor.getActions().add(action);
					setActionsView();
				} else {
					Intent intent = new Intent(mThis, at.getActivityClass());
					intent.putExtra("org.jtb.httpmon.actionType", at);
					startActivityForResult(intent, NEW_ACTION_REQUEST);
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
		ArrayAdapter<ConditionType> caa = new ArrayAdapter<ConditionType>(this,
				android.R.layout.simple_spinner_item, ConditionType.TYPES);
		caa
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mConditionsSpinner.setAdapter(caa);

		mActionsSpinner = (Spinner) findViewById(R.id.actions_spinner);
		ArrayAdapter<ActionType> aaa = new ArrayAdapter<ActionType>(this,
				android.R.layout.simple_spinner_item, ActionType.TYPES);
		aaa
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mActionsSpinner.setAdapter(aaa);

		setNameView();
		setRequestView();
		setConditionsView();
		setActionsView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.save_menu).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, CANCEL_MENU, 1, R.string.cancel_menu).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, HELP_MENU, 3, R.string.help_menu).setIcon(
				android.R.drawable.ic_menu_help);
		return result;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (save()) {
				return super.onKeyDown(keyCode, event);
			} else {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_MENU:
			if (save()) {
				finish();
			}
			return true;
		case CANCEL_MENU:
			cancel();
			finish();
			return true;
		case HELP_MENU:
			showDialog(HELP_DIALOG);
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
	}

	private boolean save() {
		setMonitor();
		if (validateMonitor()) {
			Intent intent = new Intent();
			intent.putExtra("org.jtb.httpmon.monitor", mMonitor);
			setResult(Activity.RESULT_OK, intent);
			return true;
		}
		return false;
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

		if (mMonitor.getActions().size() == 0) {
			Toast
					.makeText(
							this,
							"You did not add any actions. No action will be taken when this monitor is invalid.",
							Toast.LENGTH_LONG).show();
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
			mConditionList.getParent().requestLayout();
		}
	}

	void setActionsView() {
		if (mMonitor.getActions().size() == 0) {
			mActionList.setVisibility(View.GONE);
			mEmptyActionsText.setVisibility(View.VISIBLE);
		} else {
			mActionList.setVisibility(View.VISIBLE);
			mEmptyActionsText.setVisibility(View.GONE);
			ActionAdapter aa = new ActionAdapter(this, mMonitor.getActions());
			mActionList.setAdapter(aa);
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
		case NEW_ACTION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Action action = (Action) data
						.getSerializableExtra("org.jtb.httpmon.action");
				mMonitor.getActions().add(action);
				setActionsView();
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
		case EDIT_ACTION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Action action = (Action) data
						.getSerializableExtra("org.jtb.httpmon.action");
				mMonitor.getActions().remove(mEditAction);
				mMonitor.getActions().add(action);
				setActionsView();
			}
			break;
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case HELP_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Edit Monitor Help");
			builder.setMessage(R.string.edit_monitor_help);
			builder.setNeutralButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dismissDialog(HELP_DIALOG);
						}
					});
			mHelpDialog = builder.create();
			return mHelpDialog;

		}
		return null;
	}

}