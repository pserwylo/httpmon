package org.jtb.httpmon.model;

import org.jtb.httpmon.EditNotificationActionActivity;

public class NotificationActionType extends ActionType {

	@Override
	public Class getActivityClass() {
		return EditNotificationActionActivity.class;
	}

	@Override
	public Action newAction() {
		return new NotificationAction(this);
	}

	@Override
	public String toString() {
		return "Send Notification";
	}

}
