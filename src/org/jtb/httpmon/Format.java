package org.jtb.httpmon;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public enum Format {
	BRIEF("brief", Pattern.compile("^([VDIWEF]+)/")),
	PROCESS("process", Pattern.compile("^([VDIWEF]+)\\(")),
	TAG("tag", Pattern.compile("^([VDIWEF]+)/")),
	THREAD("thread", Pattern.compile("^([VDIWEF]+)\\(")),
	TIME("time", Pattern.compile("([VDIWEF]+)/")),
	LONG("long", Pattern.compile("([VDIWEF]+)/")),
	RAW("raw", null);
	
	private static Format[] byOrder = new Format[7];

	static {
		byOrder[0] = BRIEF;
		byOrder[1] = PROCESS;
		byOrder[2] = TAG;
		byOrder[3] = THREAD;
		byOrder[4] = TIME;
		byOrder[5] = LONG;
		byOrder[6] = RAW;
	}
	
	private static final HashMap<String,Format> VALUE_MAP = new HashMap<String,Format>();
	
	static {
		VALUE_MAP.put(BRIEF.mValue, BRIEF); 
		VALUE_MAP.put(PROCESS.mValue, PROCESS); 
		VALUE_MAP.put(TAG.mValue, TAG); 
		VALUE_MAP.put(THREAD.mValue, THREAD); 
		VALUE_MAP.put(TIME.mValue, TIME); 
		VALUE_MAP.put(RAW.mValue, RAW); 
		VALUE_MAP.put(LONG.mValue, LONG); 
	}
		
	private String mValue;
	private Pattern mLevelPattern;
	
	private Format(String value, Pattern levelPattern) {
		mValue = value;
		mLevelPattern = levelPattern;
	}
		
	public static final Format byValue(String value) {
		return VALUE_MAP.get(value);
	}
	
	public Level getLevel(String line) {
		if (mLevelPattern == null) {
			return null;
		}
		Matcher m = mLevelPattern.matcher(line);
		if (m.find()) {
			return Level.valueOf(m.group(1));
		}
		return null;
	}
	
	public static Format getByOrder(int order) {
		return byOrder[order];
	}
	
	public String getValue() {
		return mValue;
	}
}
