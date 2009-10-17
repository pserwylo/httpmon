package org.jtb.httpmon.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ActionType implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final ActionType[] TYPES = new ActionType[] {
		new NotificationActionType()
	};
	
	public abstract Action newAction();
	public abstract String toString();
	public abstract Class getActivityClass();

	public static Action newAction(JSONObject jo) {
		try {
			String type = jo.getString("type");
			Class c;
			try {
				c = Class.forName(type);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException(cnfe);
			}
			if (c == null) {
				throw new AssertionError("unknown type: " + type);
			}
			ActionType at;
			try {
				at = (ActionType) c.newInstance();
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
			Action action = at.newAction();
			action.init(jo);
			return action;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
