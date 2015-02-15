package com.gtnexus.html5.util;

public class Error {

	private int pageId;
	private String errorType;
	private String errorMessage;
	private int lastConvertedLine;
	private String path;
	
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getLastConvertedLine() {
		return lastConvertedLine;
	}
	public void setLastConvertedLine(int lastConvertedLine) {
		this.lastConvertedLine = lastConvertedLine;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	
	
}
