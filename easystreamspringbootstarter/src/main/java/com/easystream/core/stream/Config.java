//package com.easystream.core.stream;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///*
// * @ClassName Config
// * @Description: 读取配置文件的bean
// * @Author Soft001
// * @Date 2020/2/14
// **/
//@Component
//public class Config {
//    @Value("${keepalive}")
//    private String keepalive;// 保活时长（分钟）
//    @Value("${push_host}")
//    private String push_host;// 推送地址
//    @Value("${host_extra}")
//    private String host_extra;// 额外地址
//    @Value("${push_port}")
//    private String push_port;// 推送端口
//    @Value("${main_code}")
//    private String main_code;// 主码流最大码率
//    @Value("${sub_code}")
//    private String sub_code;// 主码流最大码率
//    @Value("${rtsptype}")
//    private String rtsptype="1";//1.nvr,2.合成服务
//
//    public String getRtsptype() {
//        return rtsptype;
//    }
//
//    public void setRtsptype(String rtsptype) {
//        this.rtsptype = rtsptype;
//    }
//
//    public String getHost_extra() {
//        return host_extra;
//    }
//
//    public void setHost_extra(String host_extra) {
//        this.host_extra = host_extra;
//    }
//
//    public String getKeepalive() {
//        return keepalive;
//    }
//
//    public void setKeepalive(String keepalive) {
//        this.keepalive = keepalive;
//    }
//
//    public String getPush_host() {
//        return push_host;
//    }
//
//    public void setPush_host(String push_host) {
//        this.push_host = push_host;
//    }
//
//    public String getPush_port() {
//        return push_port;
//    }
//
//    public void setPush_port(String push_port) {
//        this.push_port = push_port;
//    }
//
//    public String getMain_code() {
//        return main_code;
//    }
//
//    public void setMain_code(String main_code) {
//        this.main_code = main_code;
//    }
//
//    public String getSub_code() {
//        return sub_code;
//    }
//
//    public void setSub_code(String sub_code) {
//        this.sub_code = sub_code;
//    }
//
//    @Override
//    public String toString() {
//        return "Config [keepalive=" + keepalive + ", push_host=" + push_host + ", push_port=" + push_port + "]";
//    }
//}
