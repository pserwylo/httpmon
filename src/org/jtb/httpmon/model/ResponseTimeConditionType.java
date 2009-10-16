package org.jtb.httpmon.model;

import org.jtb.httpmon.EditResponseTimeConditionActivity;

public class ResponseTimeConditionType extends ConditionType {

	@Override
	public Condition newCondition() {
		return new ResponseTimeCondition(this);
	}

	@Override
	public String toString() {
		return "Response Time";
	}

	@Override
	public Class getActivityClass() {
		return EditResponseTimeConditionActivity.class;
	}
}
