package com.easystream.core.stream.cv.videoimageshot.exception;

/**
 * 无法检索到流
 * @author yujian
 *
 */
public class StreamNotFoundException extends RuntimeException {

	public StreamNotFoundException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1L;

}
