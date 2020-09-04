package com.easystream.spring.service;

import com.alibaba.fastjson.JSONObject;
import com.easystream.core.EasystreamConfiguration;
import com.easystream.core.stream.CacheUtil;
import com.easystream.core.stream.CameraPojo;
import com.easystream.core.stream.CameraThread;
import com.easystream.core.stream.IpUtil;
import com.easystream.core.stream.cv.videoimageshot.exception.StreamRuntimeException;
import com.easystream.core.utils.Common;
import com.easystream.core.utils.ResponseHelper;
import com.easystream.spring.reflection.EasysteamService;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName EasystreamServiceImpl
 * @Description: TODO
 * @Author Soft001
 * @Date 2020/9/3
 **/
//@EasystreamServiceScan(name = "easystreamService")
public class EasystreamServiceImpl implements EasysteamService {

    @Autowired
    private EasystreamConfiguration configuration;

    /**
     * @param pojo
     * @return Map<String,String>
     * @Title: openCamera
     * @Description: 开启视频流
     **/
    public String openCamera(CameraPojo pojo) {

        // 返回结果
        //Map<String, String> map = new HashMap<String, String>();
        CameraPojo cameraPojo = new CameraPojo();
        try {
            // 获取当前时间
            String openTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
            // 是否存在的标志；0：不存在；1：存在
            int sign = 0;
            // 直播流
            if ("".equals(pojo.getStartTime())) {
                Set<String> keys = CacheUtil.STREAMMAP.keySet();
                for (String key : keys) {
                    if (key.equals(pojo.getToken())) {// 存在直播流
                        cameraPojo = CacheUtil.STREAMMAP.get(key);
                        sign = 1;
                        break;
                    }
                }
                if (sign == 1) {// 存在
                    cameraPojo.setCount(cameraPojo.getCount() + 1);
                    cameraPojo.setOpenTime(openTime);

                    //logger.info("打开：" + cameraPojo.getRtsp());
                    //System.out.println("重复打开---------：" + cameraPojo.getUrl());
                } else {
                    cameraPojo = openStream(pojo.getIp(), pojo.getUsername(), pojo.getPassword(), pojo.getChannel(),
                            pojo.getStream(), openTime, pojo.getToken(), pojo.getRtsp(), pojo.getType(), pojo.getPort(), configuration);

                    //logger.info("打开：" + cameraPojo.getRtsp());
                    //System.out.println("打开：" + cameraPojo.getRtsp());
                }

            } else {
                // 历史流
                Set<String> keys = CacheUtil.HISTORYjobMap.keySet();
                for (String key : keys) {
                    if (key.equals(pojo.getToken())) {// 存在历史流
                        CacheUtil.HISTORYjobMap.get(key).setInterrupted();
                        CacheUtil.HISTORYjobMap.remove(key);
                        break;
                    }
                }

                cameraPojo = openPlayBackStream(pojo.getIp(), pojo.getUsername(), pojo.getPassword(), pojo.getChannel(),
                        pojo.getStream(), pojo.getStartTime(), pojo.getEndTime(), pojo.getToken(), openTime, pojo.getType(), configuration);

                //logger.info("打开：" + cameraPojo.getRtsp());
                System.out.println("打开：" + cameraPojo.getRtsp());
            }
        } catch (Exception e) {
            throw new StreamRuntimeException(e);
        }
        return JSONObject.toJSONString(cameraPojo);
    }

    /**
     * @param ip
     * @param username
     * @param password
     * @param channel
     * @param stream
     * @param openTime
     * @return CameraPojo
     * @Title: openStream
     * @Description: 推流器
     **/
    private CameraPojo openStream(String ip, String username, String password, String channel, String stream,
                                  String openTime, String ch, String rtspPath, String type, int portnum, EasystreamConfiguration configuration) {
        CameraPojo cameraPojo = new CameraPojo();
        // 生成token
        //String token = UUID.randomUUID().toString();
        String token = ch;
        String rtsp = "";
        String rtmp = "";
        //String IP = IpUtil.IpConvert(ip);
        String url = "";
        String hls = "";
        // 直播流
        rtsp = rtspPath;
        //为场地区域监控视频流请求
        if (type.equals("0")) {//nvr
            rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":554/Streaming/Channels/" + channel.trim() + "0" + stream;
        } else if (type.equals("1")) {//ipc
            //rtsp://admin:abc12345@172.17.32.245:554/h264/ch1/main/av_stream
            rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":554/h264/ch" + channel.trim() + "/main/av_stream";
        } else {
            rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":554/cam/realmonitor?channel=" + channel.trim() + "&subtype=0";
        }
        cameraPojo.setIp(ip);
        cameraPojo.setPort(String.valueOf(portnum));
        //rtmp直播
        rtmp = "rtmp://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getPush_port() + "/region/" + token;
        //flv直播，
        url = "http://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getAccess_port() + "/flv?port=" + configuration.getPush_port() + "&app=region&stream=" + token;

        //hls点播
        hls = "http://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getAccess_port() + "/hls/" + token + ".m3u8";

        cameraPojo.setUsername(username);
        cameraPojo.setPassword(password);
        //cameraPojo.setIp(IP);
        cameraPojo.setChannel(channel);
        cameraPojo.setStream(stream);
        cameraPojo.setRtsp(rtsp);
        cameraPojo.setRtmp(rtmp);
        cameraPojo.setUrl(url);
        cameraPojo.setOpenTime(openTime);
        cameraPojo.setCount(1);
        cameraPojo.setToken(token);
        cameraPojo.setHls(hls);

        // 执行任务
        CameraThread.MyRunnable job = new CameraThread.MyRunnable(cameraPojo, configuration);
        CameraThread.es.execute(job);
        CacheUtil.jobMap.put(token, job);

        return cameraPojo;
    }

    private CameraPojo openPlayBackStream(String ip, String username, String password, String channel, String stream,
                                          String starttime, String endtime, String ch, String openTime, String type, EasystreamConfiguration configuration) {
        CameraPojo cameraPojo = new CameraPojo();
        // 生成token
        //String token = UUID.randomUUID().toString();
        String token = ch;
        String rtsp = "";
        String rtmp = "";
        //String IP = IpUtil.IpConvert(ip);
        String url = "";
        String hls = "";
        // String[] car = token.split("_");
        if (type.equals("0")) {//nvr
            //海康rtsp流格式
            rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":554/Streaming/tracks/" + channel
                    + "0" + stream + "?starttime=" + starttime.substring(0, 8) + "t" + starttime.substring(8) + "z&endtime="
                    + endtime.substring(0, 8) + "t" + endtime.substring(8) + "z";
        } else if (type.equals("2")) {
            //大华rtsp流格式
            //rtsp://[username]:[password]@[ip]:[port]/cam/playback?channel=1&subtype=0&starttime=2017_01_10_01_00_00&endtime=2017_01_10_02_00_00
            rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":554/cam/playback/?channel=" + channel + "&subtype=" + stream + "&starttime=" +
                    starttime.substring(0, 4) + "_" + starttime.substring(8, 10) + "_" + starttime.substring(10, 12) + "_" + starttime.substring(12, 14) + "_" + starttime.substring(14, 16)
                    + "_" + starttime.substring(16, 18) + "&endtime=" + endtime.substring(0, 4) + "_" + endtime.substring(8, 10) + "_" + endtime.substring(10, 12) + "_" +
                    endtime.substring(12, 14) + "_" + endtime.substring(14, 16) + "_" + endtime.substring(16, 18);
        }
        //接入nvrip
        cameraPojo.setIp(IpUtil.IpConvert(ip));

        cameraPojo.setStartTime(starttime);
        cameraPojo.setEndTime(endtime);

//        rtmp = "rtmp://" + IpUtil.IpConvert(config.getPush_host()) + ":" + config.getPush_port() + "/history/"
//                + token;
//        if (config.getHost_extra().equals("127.0.0.1")) {
//            url = rtmp;
//        } else {
//            url = "rtmp://" + IpUtil.IpConvert(config.getHost_extra()) + ":" + config.getPush_port() + "/history/"
//                    + token;
//        }
        //1.flv直播，2.hls点播，3.rtmp直播
        rtmp = "rtmp://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getPush_port() + "/history/" + token;
        url = "http://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getAccess_port() + "/flv?port=" + configuration.getPush_port() + "&app=history&stream=" + token;
        hls = "http://" + IpUtil.IpConvert(configuration.getPush_host()) + ":" + configuration.getAccess_port() + "/hls/" + token + ".m3u8";
        cameraPojo.setUsername(username);
        cameraPojo.setPassword(password);
        cameraPojo.setChannel(channel);
        cameraPojo.setStream(stream);
        cameraPojo.setRtsp(rtsp);
        cameraPojo.setRtmp(rtmp);
        cameraPojo.setUrl(url);
        cameraPojo.setOpenTime(openTime);
        cameraPojo.setCount(1);
        cameraPojo.setToken(token);
        cameraPojo.setHls(hls);

        // 执行任务
        CameraThread.PlayBackRunnable job = new CameraThread.PlayBackRunnable(cameraPojo, configuration);
        CameraThread.es.execute(job);
        CacheUtil.HISTORYjobMap.put(token, job);
        return cameraPojo;
    }

    /**
     * @param tokens
     * @return void
     * @Title: closeCamera
     * @Description:关闭视频流
     **/
    public String closeCamera(String tokens) {
        Map map = new HashMap();
        try {
            if (null != tokens && "" != tokens) {
                String[] tokenArr = tokens.split(",");
                for (String token : tokenArr) {
                    if (CacheUtil.jobMap.containsKey(token) && CacheUtil.STREAMMAP.containsKey(token)) {
                        if (0 < CacheUtil.STREAMMAP.get(token).getCount()) {
                            // 人数-1
                            CacheUtil.STREAMMAP.get(token).setCount(CacheUtil.STREAMMAP.get(token).getCount() - 1);
                            // CacheUtil.jobMap.remove(token);
                        }
                    }
                }
            }
            map.put("code", "1");
        } catch (Exception e) {
            throw new StreamRuntimeException(e);
        }
        return JSONObject.toJSONString(ResponseHelper.buildResponseModel(map));
    }

    /**
     * @param tokens
     * @return void
     * @Title: closeCamera
     * @Description:关闭回放流
     **/
    public String closeHistoryCamera(String tokens) {
        Map map = new HashMap();
        try {
            if (null != tokens && "" != tokens) {
                String[] tokenArr = tokens.split(",");
                for (String token : tokenArr) {
                    if (CacheUtil.HISTORYjobMap.containsKey(token)) {
                        CacheUtil.HISTORYjobMap.get(token).setInterrupted();
                        //logger.debug("[定时任务：]  结束： " + CacheUtil.STREAMMAP.get(key).getRtsp() + "  推流任务！");

                        CacheUtil.HISTORYjobMap.remove(token);
                    }
                }
            }
            map.put("code", "1");
        } catch (Exception e) {
            throw new StreamRuntimeException(e);
        }
        return JSONObject.toJSONString(ResponseHelper.buildResponseModel(map));
    }

    /**
     * @return Map<String, CameraPojo>
     * @Title: getCameras
     * @Description:获取直播视频流
     **/
    public Map<String, CameraPojo> getCameras() {
        return CacheUtil.STREAMMAP;
    }

    /**
     * @param tokens
     * @return void
     * @Title: keepAlive
     * @Description:视频流保活
     **/
    public String keepAlive(String tokens) {
        try {
            // 校验参数
            if (null != tokens && "" != tokens) {
                String[] tokenArr = tokens.split(",");
                for (String token : tokenArr) {
                    // 直播流token
                    if (null != CacheUtil.STREAMMAP.get(token)) {
                        //System.out.println("++++++++++++++++++++++++"+token);
                        CameraPojo cameraPojo = CacheUtil.STREAMMAP.get(token);
                        // 更新当前系统时间
                        cameraPojo.setOpenTime(Common.fromDateH());
                        return JSONObject.toJSONString(cameraPojo);
                    }
                }
            }
        } catch (Exception e) {
            throw new StreamRuntimeException(e);
        }
        return "";
    }


    /**
     * @MethodName:
     * @Description: 抓怕照片
     * @Param:
     * @Return:
     * @Author: Soft001
     * @Date: 2020/4/2
     **/
    public byte[] capture(String url) {
        byte[] res = new byte[0];
        try {
            res = CacheUtil.jobMap.get(url).getFream("c:");
        } catch (FrameGrabber.Exception e) {
            throw new StreamRuntimeException(e);
        }
        return res;
    }

    /**
     * @param tokens
     * @return void
     * @Title: keepAlive
     * @Description:视频回放流保活
     **/
    public String keepPlayBackAlive(String tokens) {
        try {
            // 校验参数
            if (null != tokens && "" != tokens) {
                String[] tokenArr = tokens.split(",");
                for (String token : tokenArr) {
                    // 直播流token
                    if (null != CacheUtil.HISTORYMAP.get(token)) {
                        //System.out.println("++++++++++++++++++++++++"+token);
                        CameraPojo cameraPojo = CacheUtil.HISTORYMAP.get(token);
                        // 更新当前系统时间
                        cameraPojo.setOpenTime(Common.fromDateH());
                        return JSONObject.toJSONString(cameraPojo);
                    }
                }
            }
        } catch (Exception e) {
            throw new StreamRuntimeException(e);
        }
        return "";
    }

}
