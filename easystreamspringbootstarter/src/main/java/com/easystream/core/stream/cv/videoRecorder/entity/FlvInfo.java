package com.easystream.core.stream.cv.videoRecorder.entity;

/**
 * @ClassName FlvInfo
 * @Description: FLV播放流
 * @Author Soft001
 * @Date 2020/4/27
 **/
public class FlvInfo implements Comparable<FlvInfo> {
    private int duration ;
    private String url = "";
    private int sort;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(FlvInfo o) {
        return this.sort - o.sort;
    }
}
