package com.easystream.core.stream.cv.videoimageshot.grabber.ffmpeg;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.easystream.core.stream.cv.videoimageshot.core.JavaImgConverter;
import com.easystream.core.stream.cv.videoimageshot.exception.CodecNotFoundExpception;
import com.easystream.core.stream.cv.videoimageshot.grabber.Base64Grabber;
import com.easystream.core.stream.cv.videoimageshot.grabber.BufferGrabber;
import com.easystream.core.stream.cv.videoimageshot.grabber.BufferedImageGrabber;
import com.easystream.core.stream.cv.videoimageshot.grabber.BytesGrabber;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.javacpp.BytePointer;

import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_BGR24;


/**
 * Updated the code according to the new ffmpeg api to better support the screenshot function
 * @author eguid
 */
public class FFmpeg4VideoImageGrabber extends GrabberTemplate4 implements Base64Grabber,BufferedImageGrabber,BufferGrabber,BytesGrabber {

	public final static String DETAULT_FORMAT = "jpg";

	public FFmpeg4VideoImageGrabber setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public FFmpeg4VideoImageGrabber setHeight(Integer height) {
		this.height = height;
		return this;
	}
	@Override
	public byte[] saveFrame(AVFrame frameRGB, int width, int height) {
		BytePointer data = frameRGB.data(0);
		int size = width * height * 3;
		//复制虚拟机外内存数据到java虚拟机中，因为这个方法之后会清理内存
		byte[] bytes=new byte[size];
		data.position(0).limit(size).get(bytes,0,size);
		return bytes;
	}
	
	/*
	 * 验证并初始化
	 * @param url
	 * @param fmt
	 * @return
	 */
	private boolean validateAndInit(String url,Integer fmt) {
		if (url == null) {
			throw new IllegalArgumentException("Didn't open video file");
		}
		if(fmt == null) {
			this.fmt=AV_PIX_FMT_BGR24;
		}
		return true;
	}
	
	@Override
	public byte[] grabBytes() throws IOException , CodecNotFoundExpception {
		return grabBytes(this.url);
	}

	@Override
	public byte[] grabBytes(String url) throws IOException, CodecNotFoundExpception {
		return grabBytes(url,null);
	}

	@Override
	public byte[] grabBytes(String url, Integer fmt) throws IOException, CodecNotFoundExpception {
		byte[] buf=null;
		if(validateAndInit(url,fmt)) {
			buf = grabVideoFrame(url,this.fmt);
		}
		return buf;
	}

	@Override
	public byte[][] grabBytes(String url, int sum, int interval) throws IOException, CodecNotFoundExpception {
		return grabBytes(url,null,sum,interval);
	}
	
	@Override
	public byte[][] grabBytes(String url, Integer fmt, int sum, int interval) throws IOException, CodecNotFoundExpception {
		byte[][] bufs=null;
		if(validateAndInit(url,fmt)) {
			bufs= grabVideoFrame(url, this.fmt, sum, interval);
		}
		return bufs;
	}
	
	@Override
	public ByteBuffer grabBuffer() throws IOException, CodecNotFoundExpception {
		return grabBuffer(this.url);
	}

	@Override
	public ByteBuffer grabBuffer(String url) throws IOException, CodecNotFoundExpception {
		return grabBuffer(url,null);
	}

	@Override
	public ByteBuffer grabBuffer(String url, Integer fmt) throws IOException, CodecNotFoundExpception {
		byte[] bytes=grabBytes(url, fmt);
		ByteBuffer buf=ByteBuffer.wrap(bytes);
		return buf;
	}

	@Override
	public ByteBuffer[] grabBuffers(String url, int sum, int interval) throws IOException, CodecNotFoundExpception {
		return grabBuffers(url,null,sum,interval);
	}

	@Override
	public ByteBuffer[] grabBuffers(String url, Integer fmt, int sum, int interval) throws IOException, CodecNotFoundExpception {
		if(sum>0) {
			byte[][] bytes=grabBytes(url,fmt, sum, interval);
			if(bytes!=null) {
				ByteBuffer[] bufs=new ByteBuffer[sum];
				for(int i=0;i<bytes.length;i++) {
					bufs[i]=ByteBuffer.wrap(bytes[i]);
				}
				return bufs;
			}
		}
		return null;
	}
	
	@Override
	public BufferedImage grabBufferImage() throws IOException, CodecNotFoundExpception {
		return grabBufferImage(this.url,null);
	}

	@Override
	public BufferedImage grabBufferImage(String url) throws IOException, CodecNotFoundExpception {
		return grabBufferImage(url,null);
	}

	@Override
	public BufferedImage grabBufferImage(String url, Integer fmt) throws IOException, CodecNotFoundExpception {
		BufferedImage image=null;
		byte[] buf=grabBytes(url,fmt);
		image= JavaImgConverter.BGR2BufferedImage(buf,this.width,this.height);
		return image;
	}
	
	@Override
	public BufferedImage[] grabBufferImages(String url, int sum, int interval) throws IOException, CodecNotFoundExpception {
		return grabBufferImages(url,null,sum,interval);
	}

	@Override
	public BufferedImage[] grabBufferImages(String url, Integer fmt, int sum, int interval) throws IOException, CodecNotFoundExpception {
		BufferedImage[] images=null;
		if(sum>0) {
			byte[][] bytes=grabBytes(url,fmt, sum, interval);
			if(bytes!=null) {
				images=new BufferedImage[sum];
				for(int i=0;i<bytes.length;i++) {
					images[i]= JavaImgConverter.BGR2BufferedImage(bytes[i],this.width,this.height);
				}
				return images;
			}
		}
		return null;
	}

	
	@Override
	public String getBase64Image(String url) throws IOException, CodecNotFoundExpception {
		return getBase64Image(url, null);
	}

	@Override
	public String getBase64Image(String url, String format) throws IOException, CodecNotFoundExpception {
		return getBase64Image(url, format,this.width,this.height);
	}

	@Override
	public String getBase64Image(String url, String format, Integer width, Integer height) throws IOException, CodecNotFoundExpception {
		if (format == null) {
			format =DETAULT_FORMAT;
		}
		BufferedImage img =grabBufferImage(url);
		if (img!= null) {
			String base64=JavaImgConverter.bufferedImage2Base64(img, format);
			return base64;
		}
		return null;
	}

	@Override
	public String shotAndGetBase64Image(String url, String imgurl) throws IOException, CodecNotFoundExpception {
		return shotAndGetBase64Image(url, imgurl, null);
	}

	@Override
	public String shotAndGetBase64Image(String url, String imgurl, String format) throws IOException, CodecNotFoundExpception {
		return shotAndGetBase64Image(url, imgurl, format,null,null);
	}

	@Override
	public String shotAndGetBase64Image(String url, String imgurl, String format, Integer width, Integer height)
			throws IOException, CodecNotFoundExpception {
		if (format == null) {
			format = DETAULT_FORMAT;
		}
		BufferedImage img =grabBufferImage(url);
		if (img != null) {
			JavaImgConverter.saveImage(img, format, imgurl);
			return JavaImgConverter.bufferedImage2Base64(img, format);
		}
		return null;
	}
	
	private String url;//视频地址
	private Integer fmt;//图像数据结构
	
	public FFmpeg4VideoImageGrabber() {}
	
	public FFmpeg4VideoImageGrabber(String url) {
		this.url=url;
	}
	
	public FFmpeg4VideoImageGrabber(String url, Integer fmt) {
		super();
		this.url = url;
		this.fmt = fmt;
	}
	
	public FFmpeg4VideoImageGrabber(String url, Integer fmt,Integer width,Integer height) {
		super(width,height);
		this.url = url;
		this.fmt = fmt;
		this.width=width;
		this.height=height;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public int getFmt() {
		return fmt;
	}

	public void setFmt(int fmt) {
		this.fmt = fmt;
	}
	
}
