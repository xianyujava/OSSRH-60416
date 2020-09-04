package com.easystream.core.stream.cv.videoimageshot;

import java.io.*;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avcodec.AVPicture;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVIOContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.swscale.SwsContext;
import org.bytedeco.javacpp.*;

import static org.bytedeco.ffmpeg.global.avcodec.*;
import static org.bytedeco.ffmpeg.global.avformat.*;
import static org.bytedeco.ffmpeg.global.avutil.*;
import static org.bytedeco.ffmpeg.global.swscale.SWS_BICUBIC;
import static org.bytedeco.ffmpeg.global.swscale.sws_getContext;
import static org.bytedeco.ffmpeg.global.swscale.sws_scale;

public abstract class FFmpegJpgScreenshot {

	// Load only once formats and codecs
	static {
		av_register_all();
		avformat_network_init();
	}

	/**
	 * 打开视频流或者视频文件，并解码视频帧为YUVJ420P数据
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "deprecation", "resource" })
	private int openVideo(String url,String out_file) throws IOException {
		AVFormatContext pFormatCtx = new AVFormatContext(null);
		int i, videoStream;
		AVCodecContext pCodecCtx = null;
		AVCodec pCodec = null;
		AVFrame pFrame = null;
		AVPacket packet = new AVPacket();
		int[] frameFinished = new int[1];
		AVDictionary optionsDict = null;

		AVFrame pFrameRGB = null;
		int numBytes;
		BytePointer buffer = null;
		SwsContext sws_ctx = null;

		// Open video file

		if (avformat_open_input(pFormatCtx, url, null, null) != 0) {
			return -1; // Couldn't open file
		}

		// Retrieve stream information
		if (avformat_find_stream_info(pFormatCtx, (PointerPointer<Pointer>) null) < 0) {
			return -1;// Couldn't find stream information
		}

		av_dump_format(pFormatCtx, 0, url, 0);// Dump information about file onto standard error

		// Find the first video stream
		videoStream = -1;
		for (i = 0; i < pFormatCtx.nb_streams(); i++) {
			if (pFormatCtx.streams(i).codec().codec_type() == AVMEDIA_TYPE_VIDEO) {
				videoStream = i;
				break;
			}
		}
		if (videoStream == -1) {
			return -1; // Didn't find a video stream
		}

		// Get a pointer to the codec context for the video stream
		pCodecCtx = pFormatCtx.streams(videoStream).codec();

		// Find the decoder for the video stream
		pCodec = avcodec_find_decoder(pCodecCtx.codec_id());
		if (pCodec == null) {
			System.err.println("Unsupported codec!");
			return -1; // Codec not found
		}
		// Open codec
		if (avcodec_open2(pCodecCtx, pCodec, optionsDict) < 0) {
			return -1; // Could not open codec
		}

		pFrame = av_frame_alloc();// Allocate video frame

		// Allocate an AVFrame structure
		pFrameRGB = av_frame_alloc();
		if (pFrameRGB == null) {
			return -1;
		}
		int width = pCodecCtx.width(), height = pCodecCtx.height();
		pFrameRGB.width(width);
		pFrameRGB.height(height);
		pFrameRGB.format(AV_PIX_FMT_YUVJ420P);
		// Determine required buffer size and allocate buffer
		numBytes = avpicture_get_size(AV_PIX_FMT_YUVJ420P, width, height);

		buffer = new BytePointer(av_malloc(numBytes));

		sws_ctx = sws_getContext(pCodecCtx.width(), pCodecCtx.height(), pCodecCtx.pix_fmt(), pCodecCtx.width(),
				pCodecCtx.height(), AV_PIX_FMT_YUVJ420P, SWS_BICUBIC, null, null, (DoublePointer) null);

		// Assign appropriate parts of buffer to image planes in pFrameRGB
		// Note that pFrameRGB is an AVFrame, but AVFrame is a superset
		// of AVPicture
		avpicture_fill(new AVPicture(pFrameRGB), buffer, AV_PIX_FMT_YUVJ420P, pCodecCtx.width(), pCodecCtx.height());

		// Read frames and save first five frames to disk
		
		int ret=-1;
		while (av_read_frame(pFormatCtx, packet) >= 0) {
			if (packet.stream_index() == videoStream) {// Is this a packet from the video stream?
				avcodec_decode_video2(pCodecCtx, pFrame, frameFinished, packet);// Decode video frame

				// Did we get a video frame?
				if (frameFinished != null) {
					// 转换图像格式，将解压出来的YUV420P的图像转换为YUVJ420P的图像
					sws_scale(sws_ctx, pFrame.data(), pFrame.linesize(), 0, pCodecCtx.height(), pFrameRGB.data(),
							pFrameRGB.linesize());
				}

				if (frameFinished[0] != 0 && !pFrame.isNull()) {
					// Convert the image from its native format to YUVJ420P
					sws_scale(sws_ctx, pFrame.data(), pFrame.linesize(), 0, pCodecCtx.height(), pFrameRGB.data(),pFrameRGB.linesize());
					if((ret=saveImg(pFrameRGB,out_file))>=0) {
						break;
					}
				}
			}

		}
		av_free_packet(packet);// Free the packet that was allocated by av_read_frame
		// Free the RGB image
		av_free(buffer);

		av_free(pFrameRGB);

		av_free(pFrame);// Free the YUV frame

		avcodec_close(pCodecCtx);// Close the codec

		avformat_close_input(pFormatCtx);// Close the video file

		return ret;
	}

	/**
	 * 把YUVJ420P数据编码保存成jpg图片
	 * 
	 * @param pFrame
	 *            -YUVJ420P数据
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int saveImg(AVFrame pFrame, String out_file) {
		AVPacket pkt = null;
		AVStream pAVStream = null;
		AVCodec codec = null;
		int ret = -1;

		int width = pFrame.width(), height = pFrame.height();
		// 分配AVFormatContext对象
		AVFormatContext pFormatCtx = avformat_alloc_context();
		// 设置输出文件格式
		pFormatCtx.oformat(av_guess_format("mjpeg", null, null));
		if (pFormatCtx.oformat() == null) {
			return -1;
		}
		try {
			// 创建并初始化一个和该url相关的AVIOContext
			AVIOContext pb = new AVIOContext();
			if (avio_open(pb, out_file, AVIO_FLAG_READ_WRITE) < 0) {// dont open file
				return -1;
			}
			pFormatCtx.pb(pb);
			// 构建一个新stream
			pAVStream = avformat_new_stream(pFormatCtx, codec);
			if (pAVStream == null) {
				return -1;
			}
			int codec_id = pFormatCtx.oformat().video_codec();
			// 设置该stream的信息
			// AVCodecContext pCodecCtx = pAVStream.codec();
			AVCodecContext pCodecCtx = pAVStream.codec();
			pCodecCtx.codec_id(codec_id);
			pCodecCtx.codec_type(AVMEDIA_TYPE_VIDEO);
			pCodecCtx.pix_fmt(AV_PIX_FMT_YUVJ420P);
			pCodecCtx.width(width);
			pCodecCtx.height(height);
			pCodecCtx.time_base().num(1);
			pCodecCtx.time_base().den(25);

			// Begin Output some information
			av_dump_format(pFormatCtx, 0, out_file, 1);
			// End Output some information

			// 查找解码器
			AVCodec pCodec = avcodec_find_encoder(codec_id);
			if (pCodec == null) {// codec not found
				return -1;
			}
			// 设置pCodecCtx的解码器为pCodec
			if (avcodec_open2(pCodecCtx, pCodec, (PointerPointer<Pointer>) null) < 0) {
				System.err.println("Could not open codec.");
				return -1;
			}

			// Write Header
			avformat_write_header(pFormatCtx, (PointerPointer<Pointer>) null);

			// 给AVPacket分配足够大的空间
			pkt = new AVPacket();
			if (av_new_packet(pkt, width * height * 3) < 0) {
				return -1;
			}
			int[] got_picture = { 0 };
			// encode
			if (avcodec_encode_video2(pCodecCtx, pkt, pFrame, got_picture) >= 0) {
				// flush
				if ((ret = av_write_frame(pFormatCtx, pkt)) >= 0) {
					// Write Trailer
					if (av_write_trailer(pFormatCtx) >= 0) {
						System.err.println("Encode Successful.");
					}
				}
			}
			return ret;
			// 结束时销毁
		} finally {
			if (pkt != null) {
				av_free_packet(pkt);
			}
			if (pAVStream != null) {
				avcodec_close(pAVStream.codec());
			}
			if (pFormatCtx != null) {
				avio_close(pFormatCtx.pb());
				avformat_free_context(pFormatCtx);
			}
		}
	}
	
}
