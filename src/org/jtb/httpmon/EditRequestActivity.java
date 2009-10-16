package org.jtb.httpmon;

import java.net.MalformedURLException;
import java.net.URL;

import org.jtb.httpmon.model.Request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditRequestActivity extends Activity {
	private static final int SAVE_MENU = 0;
	private static final int CANCEL_MENU = 1;

	private Request mRequest;
	private EditText mUrlEdit;
	private EditText mIntervalEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_request);

		mRequest = savedInstanceState != null ? (Request) savedInstanceState
				.get("org.jtb.httpmon.request") : null;
		if (mRequest == null) {
			Bundle extras = getIntent().getExtras();
			mRequest = extras != null ? (Request) extras
					.get("org.jtb.httpmon.request") : null;
		}
		if (mRequest == null) {
			mRequest = new Request();
		}

		mUrlEdit = (EditText) findViewById(R.id.url_edit);
		mIntervalEdit = (EditText) findViewById(R.id.interval_edit);

		setViews();
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
			setRequest();
			if (validateRequest()) {
				Intent intent = new Intent();
				intent.putExtra("org.jtb.httpmon.request", mRequest);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			return true;
		case CANCEL_MENU:
			setResult(Activity.RESULT_CANCELED);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean validateRequest() {
		try { 
			new URL(mRequest.getUrl());
		} catch (MalformedURLException mue) {
			Toast.makeText(this, "The URL you entered is not valid.",
					Toast.LENGTH_LONG).show();
			return false;			
		}
		if (mRequest.getUrl().equalsIgnoreCase("http://")) {
			Toast.makeText(this, "The URL you entered is not valid.",
					Toast.LENGTH_LONG).show();
			return false;						
		}
		if (mRequest.getInterval() == 0) {
			Toast.makeText(this, "Please give your your request a non-zero interval.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private void setRequest() {
		mRequest.setUrl(mUrlEdit.getText().toString());
		mRequest.setInterval(mIntervalEdit.getText().toString());
	}

	private void setViews() {
		mUrlEdit.setText(mRequest.getUrl());
		mIntervalEdit.setText(Integer.toString(mRequest.getInterval()));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		}
	}

}