package org.jtb.httpmon.model;

import org.jtb.httpmon.EditContentContainsConditionActivity;
import org.jtb.httpmon.EditHeaderContainsConditionActivity;
import org.jtb.httpmon.EditResponseTimeConditionActivity;

public class HeaderContainsConditionType extends ConditionType {

	@Override
	public Condition newCondition() {
		return new HeaderContainsCondition(this);
	}

	@Override
	public String toString() {
		return "Header Contains";
	}

	@Override
	public Class getActivityClass() {
		return EditHeaderContainsConditionActivity.class;
	}
}
