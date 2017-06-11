package org.jtb.httpmon.model;

import org.jtb.httpmon.EditContentContainsConditionActivity;
import org.jtb.httpmon.EditResponseTimeConditionActivity;

public class ContentContainsConditionType extends ConditionType {

	@Override
	public Condition newCondition() {
		return new ContentContainsCondition(this);
	}

	@Override
	public String toString() {
		return "Content Contains";
	}

	@Override
	public Class getActivityClass() {
		return EditContentContainsConditionActivity.class;
	}
}
