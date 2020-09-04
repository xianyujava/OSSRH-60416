package com.easystream.core.stream.cv.videoimageshot.grabber;


import org.bytedeco.ffmpeg.avutil.AVFrame;

/**
 * 通用抓取器接口
 * @author eguid
 *
 */
public interface Grabber {

	/**
	 * 保存RGB像素帧
	 * @param pFrameRGB -默认rgb像素
	 * @param width
	 * @param height
	 * @return
	 */
	byte[] saveFrame(AVFrame pFrameRGB, int width, int height);
}
