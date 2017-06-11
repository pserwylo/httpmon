package org.jtb.httpmon.model;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Condition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ConditionType conditionType = null;
	
	public Condition(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public abstract void init(JSONObject jo);
	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("type", conditionType.getClass().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}
	public abstract String toString();
	public abstract boolean isValid(Response response);

	public ConditionType getConditionType() {
		return conditionType;
	}
}
