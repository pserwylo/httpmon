package org.jtb.httpmon.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class HeaderContainsCondition extends ContainsCondition {
	private String header;

	public HeaderContainsCondition(ConditionType conditionType) {
		super(conditionType);
	}

	@Override
	public void init(JSONObject jo) {
		super.init(jo);
		try {
			this.header = jo.getString("header");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		try {
			jo.put("header", header);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Header: ");
		sb.append(header);
		sb.append(" contains");
		switch (getPatternType()) {
		case TYPE_SUBSTRING:
			sb.append(" substring: ");
			break;
		case TYPE_WILDCARD:
			sb.append(" wildcard: ");
			break;
		case TYPE_REGEX:
			sb.append(" regex: ");
			break;
		}
		sb.append(getPattern());
		
		return sb.toString();
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	@Override
	public String getTargetString(Response r) {
		List<String> fields = r.getHeaderFields().get(header.toLowerCase());
		if (fields == null) {
			return "";
		}
		return fields.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeaderContainsCondition other = (HeaderContainsCondition) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}
}
