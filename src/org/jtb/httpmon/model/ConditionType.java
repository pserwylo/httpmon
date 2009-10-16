package org.jtb.httpmon.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ConditionType implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final ConditionType[] TYPES = new ConditionType[] {
		new PingConditionType(),
		new ResponseTimeConditionType()
	};
	
	public abstract Condition newCondition();
	public abstract String toString();
	public abstract Class getActivityClass();

	public static Condition newCondition(JSONObject jo) {
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
			ConditionType ct;
			try {
				ct = (ConditionType) c.newInstance();
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
			Condition condition = ct.newCondition();
			condition.init(jo);
			return condition;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
