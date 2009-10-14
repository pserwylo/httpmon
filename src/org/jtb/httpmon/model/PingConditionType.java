package org.jtb.httpmon.model;

import org.jtb.httpmon.EditPingConditionActivity;

public class PingConditionType extends ConditionType {

	@Override
	public Condition newCondition() {
		return new PingCondition(this);
	}

	@Override
	public String toString() {
		return "Ping";
	}

	@Override
	public Class getActivityClass() {
		return EditPingConditionActivity.class;
	}
}
