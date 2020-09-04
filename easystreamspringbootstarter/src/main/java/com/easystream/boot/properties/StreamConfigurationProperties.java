package com.easystream.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName StreamConfigurationProperties
 * @Description: TODO
 * @Author soft001
 * @Date 2020/8/30
 **/
//@Component
@ConfigurationProperties(prefix = "easystream", ignoreUnknownFields = true)
public class StreamConfigurationProperties {

    private String beanId;

    private String keepalive;// 保活时长（分钟）

    private String push_host;// 推送地址

    private String host_extra;// 额外地址

    private String push_port;// 推送端口

    private String main_code;// 主码流最大码率

    private String sub_code;// 主码流最大码率

    private String rtsptype;//1.nvr,2.合成服务

    private String access_port;

    public String getAccess_port() {
        return access_port;
    }

    public void setAccess_port(String access_port) {
        this.access_port = access_port;
    }

    public String getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(String keepalive) {
        this.keepalive = keepalive;
    }

    public String getPush_host() {
        return push_host;
    }

    public void setPush_host(String push_host) {
        this.push_host = push_host;
    }

    public String getHost_extra() {
        return host_extra;
    }

    public void setHost_extra(String host_extra) {
        this.host_extra = host_extra;
    }

    public String getPush_port() {
        return push_port;
    }

    public void setPush_port(String push_port) {
        this.push_port = push_port;
    }

    public String getMain_code() {
        return main_code;
    }

    public void setMain_code(String main_code) {
        this.main_code = main_code;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getRtsptype() {
        return rtsptype;
    }

    public void setRtsptype(String rtsptype) {
        this.rtsptype = rtsptype;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
