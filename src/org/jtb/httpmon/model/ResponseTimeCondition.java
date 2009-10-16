package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseTimeCondition extends Condition {
	public ResponseTimeCondition(ConditionType conditionType) {
		super(conditionType);
	}

	private long responseTimeMilliseconds = 1000;

	public void setResponseTimeMilliseconds(long responseTimeMilliseconds) {
		this.responseTimeMilliseconds = responseTimeMilliseconds;
	}

	public long getResponseTimeMilliseconds() {
		return responseTimeMilliseconds;
	}

	@Override
	public void init(JSONObject jo) {
		try {
			this.responseTimeMilliseconds = jo.getLong("responseTimeMilliseconds");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		try {
			jo.put("responseTimeMilliseconds", responseTimeMilliseconds);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (int) (responseTimeMilliseconds ^ (responseTimeMilliseconds >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseTimeCondition other = (ResponseTimeCondition) obj;
		if (responseTimeMilliseconds != other.responseTimeMilliseconds)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Resonse time is less than " + responseTimeMilliseconds + "ms";
	}

	@Override
	public boolean isValid(Response response) {
		if (response.getResponseCode() != -1 && response.getResponseTime() < responseTimeMilliseconds) {
			return true;
		}
		return false;
	}	
}
