package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PingCondition extends Condition {
	public PingCondition(ConditionType conditionType) {
		super(conditionType);
	}

	@Override
	public void init(JSONObject jo) {
		// nothing
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		return jo;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Can be pinged";
	}

	@Override
	public boolean isValid(Response response) {
		if (response.isAlive()) {
			return true;
		}
		return false;
	}	
}
