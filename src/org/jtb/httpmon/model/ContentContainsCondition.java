package org.jtb.httpmon.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentContainsCondition extends ContainsCondition {
	public ContentContainsCondition(ConditionType conditionType) {
		super(conditionType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Content contains ");
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

	@Override
	public String getTargetString(Response r) {
		return r.getContent();
	}

}
