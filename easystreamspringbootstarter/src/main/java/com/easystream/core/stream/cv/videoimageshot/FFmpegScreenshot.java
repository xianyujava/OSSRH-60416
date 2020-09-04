package com.easystream.core.stream.cv.videoimageshot;

import com.easystream.core.stream.cv.videoimageshot.core.JavaImgConverter;
import com.easystream.core.stream.cv.videoimageshot.exception.CodecNotFoundExpception;
import com.easystream.core.stream.cv.videoimageshot.threaddata.CurrentThreadData;

import java.awt.image.BufferedImage;
import java.io.IOException;



/**
 * 线程安全的FFmpeg截图服务
 * @see <li>
 * <ol>推荐使用BufferedImageGrabber,BufferGrabber,BytesGrabber三个接口进行实现 </ol>
 * <ol></ol>
 * </li>
 * @author yujian
 *
 */
//@Deprecated
public class FFmpegScreenshot implements Screenshot {

	@Override
	public boolean shot(String url, String imgurl) throws IOException, CodecNotFoundExpception {
		String fomrat = null;
		int index = -1;
		if (imgurl != null && (index = imgurl.lastIndexOf(".")) > 0) {
			fomrat = imgurl.substring(index + 1, imgurl.length());
		}
		return shot(url, imgurl, fomrat);
	}

	@Override
	public boolean shot(String url, String imgurl, String format) throws IOException, CodecNotFoundExpception {
		return shot(url, imgurl, format, null, null);

	}

	@Override
	public boolean shot(String url, String imgurl, String format, Integer width, Integer height) throws IOException, CodecNotFoundExpception {
		if (format == null) {
			format = CurrentThreadData.DETAULT_FORMAT;
		}
		BufferedImage img = CurrentThreadData.grabber.get().setWidth(width).setHeight(height).grabBufferImage(url);
		if (img != null) {
			JavaImgConverter.saveImage(img, format, imgurl);
			return true;
		}
		return false;
	}

	@Override
	public String getImgBase64(String url) throws IOException, CodecNotFoundExpception {
		return getImgBase64(url, null);
	}

	@Override
	public String getImgBase64(String url, String format) throws IOException, CodecNotFoundExpception {
		return getImgBase64(url, format, null, null);
	}

	@Override
	public String shotAndGetBase64(String url, String imgurl) throws IOException, CodecNotFoundExpception {
		return shotAndGetBase64(url,imgurl,null);
	}

	@Override
	public String shotAndGetBase64(String url, String imgurl, String format) throws IOException, CodecNotFoundExpception {
		return shotAndGetBase64(url,imgurl,format,null,null);
	}

	@Override
	public String getImgBase64(String url, String format, Integer width, Integer height) throws IOException, CodecNotFoundExpception {
		if (format == null) {
			format =CurrentThreadData.DETAULT_FORMAT;
		}
		BufferedImage img = CurrentThreadData.grabber.get().setWidth(width).setHeight(height).grabBufferImage(url);
		if (img!= null) {
			String base64=JavaImgConverter.bufferedImage2Base64(img, format);
			return base64;
		}
		return null;
	}

	@Override
	public String shotAndGetBase64(String url, String imgurl, String format, Integer width, Integer height)
			throws IOException, CodecNotFoundExpception {
		if (format == null) {
			format = CurrentThreadData.DETAULT_FORMAT;
		}
		BufferedImage img =CurrentThreadData.grabber.get().setWidth(width).setHeight(height).grabBufferImage(url);
		if (img != null) {
			JavaImgConverter.saveImage(img, format, imgurl);
			return JavaImgConverter.bufferedImage2Base64(img, format);
		}
		return null;
	}


}
