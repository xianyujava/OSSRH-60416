package com.easystream.core.stream.cv.videoimageshot;

import com.easystream.core.stream.cv.videoimageshot.exception.CodecNotFoundExpception;
import com.easystream.core.stream.cv.videoimageshot.grabber.BytesGrabber;
import com.easystream.core.stream.cv.videoimageshot.threaddata.CurrentThreadData;
import org.bytedeco.ffmpeg.avutil.AVFrame;

import java.io.IOException;


/**
 * 线程安全的FFmpeg截图服务
 *
 * @author eguid
 * @see <li>
 * <ol>推荐使用BufferedImageGrabber,BufferGrabber,BytesGrabber三个接口进行实现 </ol>
 * <ol></ol>
 * </li>
 */
//@Deprecated
public class FFmpegByteshot implements BytesGrabber {

    @Override
    public byte[] grabBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] grabBytes(String url) throws IOException, CodecNotFoundExpception {

        return CurrentThreadData.grabber.get().grabBytes(url);

    }

    @Override
    public byte[] grabBytes(String url, Integer fmt) throws IOException {
        return new byte[0];
    }

    @Override
    public byte[][] grabBytes(String url, int sum, int interval) throws IOException {
        return new byte[0][];
    }

    @Override
    public byte[][] grabBytes(String url, Integer fmt, int sum, int interval) throws IOException {
        return new byte[0][];
    }

    @Override
    public byte[] saveFrame(AVFrame pFrameRGB, int width, int height) {
        return new byte[0];
    }
}
