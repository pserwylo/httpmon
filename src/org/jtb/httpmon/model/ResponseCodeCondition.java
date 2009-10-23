package org.jtb.httpmon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseCodeCondition extends Condition {
	public ResponseCodeCondition(ConditionType conditionType) {
		super(conditionType);
	}

	private int responseCode = -1;

	@Override
	public void init(JSONObject jo) {
		try {
			this.responseCode = jo.getInt("responseCode");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		try {
			jo.put("responseCode", responseCode);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + responseCode;
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
		ResponseCodeCondition other = (ResponseCodeCondition) obj;
		if (responseCode != other.responseCode)
			return false;
		return true;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public String toString() {
		return "HTTP response code is: " + responseCode;
	}

	@Override
	public boolean isValid(Response response) {
		if (response.getResponseCode() == responseCode) {
			return true;
		}
		return false;
	}	
}
