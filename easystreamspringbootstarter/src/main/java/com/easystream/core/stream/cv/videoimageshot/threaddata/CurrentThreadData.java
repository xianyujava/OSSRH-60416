package com.easystream.core.stream.cv.videoimageshot.threaddata;

import com.easystream.core.stream.cv.videoimageshot.grabber.ffmpeg.FFmpeg4VideoImageGrabber;

/**
 * 当前线程共享数据
 * @author yujian
 *
 */
//@Deprecated
public class CurrentThreadData {

	public final static String DETAULT_FORMAT = "jpg";

	public final static ThreadLocal<FFmpeg4VideoImageGrabber> grabber = new ThreadLocal<FFmpeg4VideoImageGrabber>() {
		@Override
		protected FFmpeg4VideoImageGrabber initialValue() {
			return new FFmpeg4VideoImageGrabber();
		}
	};
}
