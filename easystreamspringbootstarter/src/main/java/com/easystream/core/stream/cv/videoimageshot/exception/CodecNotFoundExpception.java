package com.easystream.core.stream.cv.videoimageshot.exception;

/**
 * 编解码器异常
 *
 * @author yujian
 */
public class CodecNotFoundExpception extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public CodecNotFoundExpception(String message) {
        //super(message);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
