package com.easystream.core.stream.cv.videoRecorder.entity;

/**
 * @ClassName RegionVideo
 * @Description: TODO
 * @Author Soft001
 * @Date 2020/3/9
 **/
public class RegionVideo {
    private String region = "";
    private String number = "";
    private String ip = "";
    private String port = "554";
    private String name = "";
    private String password = "";
    private String channelnum = "";
    private String type = "";
    private String online = "0";//0:不在线，1：在线
    private String km = "";
    private String recordplan = "[[],[],[],[],[],[],[],[]]";//录像计划
    private String isrecord = "0";//0:不开启录像，1：开始录像计划
    private String[] sTime = new String[7];//"00:08:00";//开始录像时间
    private String[] eTime = new String[7];//"23:59:00";//结束录像时间
    public RegionVideo(){
        for (int i=0;i<7;i++){
            sTime[i]="00:08:00";
            eTime[i]="23:59:00";
        }
    }
    public String getRecordplan() {
        return recordplan;
    }

    public void setRecordplan(String recordplan) {
        this.recordplan = recordplan;
    }

    public String getIsrecord() {
        return isrecord;
    }

    public void setIsrecord(String isrecord) {
        this.isrecord = isrecord;
    }

    public String[] getsTime() {
        return sTime;
    }

    public void setsTime(String[] sTime) {
        this.sTime = sTime;
    }

    public String[] geteTime() {
        return eTime;
    }

    public void seteTime(String[] eTime) {
        this.eTime = eTime;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public int getIntNumber() {
        return Integer.parseInt(number);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannelnum() {
        return channelnum;
    }

    public void setChannelnum(String channelnum) {
        this.channelnum = channelnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
