package com.gtnexus.html5.exception;

public class HTML5ParserException extends Exception {

	/**
	 * Exception class to generate parser specific exception
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	private String tagInfo;
	private String stackTrace;

	public HTML5ParserException(String message, String tagInfo,
			String stackTrace) {
		super();
		this.message = message;
		this.tagInfo = tagInfo;
		this.stackTrace = stackTrace;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}

	public String getParentStackTrace() {
		return stackTrace;
	}

	public void setParentStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
