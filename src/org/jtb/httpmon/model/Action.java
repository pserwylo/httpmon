package org.jtb.httpmon.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Action implements Serializable {
	private static final long serialVersionUID = 1L;

	private ActionType actionType;
	
	public Action(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("type", actionType.getClass().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}
	
	public abstract void init(JSONObject jo);	
	public abstract void perform(Monitor monitor);

	public ActionType getActionType() {
		return actionType;
	}
}
