package org.jtb.httpmon.model;

import org.jtb.httpmon.EditNotificationActionActivity;
import org.jtb.httpmon.EditSmsActionActivity;

public class SmsActionType extends ActionType {

	@Override
	public Class getActivityClass() {
		return EditSmsActionActivity.class;
	}

	@Override
	public Action newAction() {
		return new SmsAction(this);
	}

	@Override
	public String toString() {
		return "Send Text Message";
	}

}
