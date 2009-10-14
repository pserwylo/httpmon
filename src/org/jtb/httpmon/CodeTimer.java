package org.jtb.httpmon;

public class CodeTimer {
	private long start;
	
	public CodeTimer() {
		start = System.currentTimeMillis();
	}
	
	public long getElapsed() {
		return System.currentTimeMillis() - start;
	}
}
