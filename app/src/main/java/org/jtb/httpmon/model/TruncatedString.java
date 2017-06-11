package org.jtb.httpmon.model;

public class TruncatedString {
	private String truncated = null;
	
	public TruncatedString(String s, int size) {
		if (s.length() > size) {
			s = s.substring(0, size-3);
			s += "...";
		}
		truncated = s;
	}
	
	@Override
	public String toString() {
		return truncated;
	}
}
