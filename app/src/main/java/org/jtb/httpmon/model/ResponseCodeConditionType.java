package org.jtb.httpmon.model;

import org.jtb.httpmon.EditResponseCodeConditionActivity;

public class ResponseCodeConditionType extends ConditionType {

	@Override
	public Condition newCondition() {
		return new ResponseCodeCondition(this);
	}

	@Override
	public String toString() {
		return "Response Code";
	}

	@Override
	public Class getActivityClass() {
		return EditResponseCodeConditionActivity.class;
	}
}
