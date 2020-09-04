package com.easystream.core.stream.cv.videoimageshot.exception;

/**
 * @ClassName StreamRuntimeException
 * @Description: TODO
 * @Author soft001
 * @Date 2020/9/1
 **/
public class StreamRuntimeException extends RuntimeException {
    public StreamRuntimeException(String message) {
        super(message);
    }

    public StreamRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }


    public StreamRuntimeException(Throwable cause) {
        super(cause);
    }
}
