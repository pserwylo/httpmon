package org.jtb.httpmon.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ContainsCondition extends Condition {
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

	public ContainsCondition(ConditionType conditionType) {
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

	public abstract String getTargetString(Response r);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + patternType;
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
		ContainsCondition other = (ContainsCondition) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (patternType != other.patternType)
			return false;
		return true;
	}

	@Override
	public boolean isValid(Response response) {
		switch (patternType) {
		case TYPE_SUBSTRING:
			return isMatchesSubstring(getTargetString(response));
		case TYPE_WILDCARD:
			return isMatchesWildcard(getTargetString(response));
		case TYPE_REGEX:
			return isMatchesRegex(getTargetString(response));
		}
		
		throw new AssertionError("unknown pattern type: " + patternType);
	}

	private boolean isMatchesSubstring(String s) {
		if (s == null) {
			return false;
		}
		String c = s.toLowerCase();
		String p = pattern.toLowerCase();
		return c.contains(p);
	}
	
	private boolean isMatchesWildcard(String s) {
		String regex = pattern.replace("*", ".*");
		Pattern p = Pattern.compile(regex, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		
		return m.find();
	}
	
	private boolean isMatchesRegex(String s) {
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		return m.find();
	}
}
