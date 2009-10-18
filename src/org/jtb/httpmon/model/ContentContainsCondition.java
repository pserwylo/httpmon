package org.jtb.httpmon.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentContainsCondition extends Condition {
	public static final int TYPE_SUBSTRING = 0;
	public static final int TYPE_WILDCARD = 1;
	public static final int TYPE_REGEX = 2;
	
	private String pattern;
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	private int patternType;
	
	public int getPatternType() {
		return patternType;
	}

	public void setPatternType(int patternType) {
		this.patternType = patternType;
	}

	public ContentContainsCondition(ConditionType conditionType) {
		super(conditionType);
	}

	@Override
	public void init(JSONObject jo) {
		try {
			this.pattern = jo.getString("pattern");
			this.patternType = jo.getInt("patternType");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = super.toJSONObject();
		try {
			jo.put("pattern", pattern);
			jo.put("patternType", patternType);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Content contains ");
		switch (patternType) {
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
		sb.append(pattern);
		
		return sb.toString();
	}

	@Override
	public boolean isValid(Response response) {
		switch (patternType) {
		case TYPE_SUBSTRING:
			return isMatchesSubstring(response.getContent());
		case TYPE_WILDCARD:
			return isMatchesWildcard(response.getContent());
		case TYPE_REGEX:
			return isMatchesRegex(response.getContent());
		}
		
		throw new AssertionError("unknown pattern type: " + patternType);
	}

	private boolean isMatchesSubstring(String content) {
		return isMatchesRegex(content);
	}
	
	private boolean isMatchesWildcard(String content) {
		String regex = pattern.replace("*", ".*");
		Pattern p = Pattern.compile(regex, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		
		return m.find();
	}
	
	private boolean isMatchesRegex(String content) {
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		return m.find();
	}
}
