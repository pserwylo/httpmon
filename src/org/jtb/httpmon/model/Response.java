package org.jtb.httpmon.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
	private long responseTime = -1;
	private String content = null;
	private int responseCode = -1;
	private Throwable throwable = null;
	private boolean alive = false;
	private Map<String,List<String>> headerFields;
	
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setHeaderFields(Map<String,List<String>> headerFields) {
		this.headerFields = headerFields;
	}

	public Map<String,List<String>> getHeaderFields() {
		return headerFields;
	}
}
