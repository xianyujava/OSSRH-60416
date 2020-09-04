package com.easystream.core.stream.cv.videoimageshot.exception;

/**
 * 文件或流无法打开
 * @author yujian
 *
 */
public class FileNotOpenException extends RuntimeException{
	
	public FileNotOpenException(String message) {
		super(message);
	}
	
	private static final long serialVersionUID = 1L;
}
