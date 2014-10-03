package com.gtnexus.html5.exception;

public class HTML5ParserException extends RuntimeException {

	/**
	 * Exception class to generate parser specific exception
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	private String type;
	private String tagInfo;
	public HTML5ParserException(String type, String message) {
		super();
		this.type = type;
		this.message = message;
	}
	public HTML5ParserException(String type, String message, String tagInfo) {
		super();
		this.type = type;
		this.message = message;
		this.tagInfo = tagInfo;

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

	public String getType() {
		return type;

	}

}
