package com.easystream.core.stream;

import com.easystream.core.EasystreamConfiguration;
import com.easystream.core.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName TimerUtil
 * @Description: 定时查看直播流和历史流是否超时，则删除集合
 * @Author Soft001
 * @Date 2020/2/13
 **/
//@Component
public class TimerUtil {
    //@Autowired
    private EasystreamConfiguration config;// 配置文件bean

    public TimerUtil(EasystreamConfiguration config) {
        this.config = config;
    }

    public static Timer timer;

    // @Override
    public void run() {
        // 超过5分钟，结束推流
        timer = new Timer("timeTimer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //logger.info("******   执行定时任务       BEGIN   ******");
                //System.out.println("******   执行直播流定时任务       BEGIN   ******");
                // 管理缓存
                if (null != CacheUtil.STREAMMAP && 0 != CacheUtil.STREAMMAP.size()) {
                    Set<String> keys = CacheUtil.STREAMMAP.keySet();
                    for (String key : keys) {
                        try {
                            // 最后打开时间
                            long openTime = Common.format
                                    .parse(CacheUtil.STREAMMAP.get(key).getOpenTime()).getTime();
                            // 当前系统时间
                            long newTime = new Date().getTime();
                            // 如果通道使用人数为0，则关闭推流
                            if (CacheUtil.STREAMMAP.get(key).getCount() == 0) {
                                // 结束线程
                                CacheUtil.jobMap.get(key).setInterrupted();
                                System.out.println("[定时任务：]  结束： " + CacheUtil.STREAMMAP.get(key).getRtsp() + "  推流任务！");
                                // 清除缓存
                                CacheUtil.STREAMMAP.remove(key);
                                CacheUtil.jobMap.remove(key);

                            } else if ((newTime - openTime) / 1000 / 60 > Integer.valueOf(config.getKeepalive())) {
                                CacheUtil.jobMap.get(key).setInterrupted();
                                //logger.debug("[定时任务：]  结束： " + CacheUtil.STREAMMAP.get(key).getRtsp() + "  推流任务！");
                                System.out.println(Common.fromDateH() + "[超时预览：]  结束： " + CacheUtil.STREAMMAP.get(key).getRtsp() + "  推流任务！");
                                CacheUtil.jobMap.remove(key);
                                CacheUtil.STREAMMAP.remove(key);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // logger.info("******   执行定时任务       END     ******");
                // System.out.println("******   执行历史流定时任务       END     ******");
                if (null != CacheUtil.HISTORYMAP && 0 != CacheUtil.HISTORYMAP.size()) {
                    Set<String> keys = CacheUtil.HISTORYMAP.keySet();
                    for (String key : keys) {
                        try {
                            // 最后打开时间
                            long openTime = Common.format
                                    .parse(CacheUtil.HISTORYMAP.get(key).getOpenTime()).getTime();
                            // 当前系统时间
                            long newTime = new Date().getTime();
                            // 如果通道使用人数为0，则关闭推流
                            if (CacheUtil.HISTORYMAP.get(key).getCount() == 0) {
                                // 结束线程
                                CacheUtil.HISTORYjobMap.get(key).setInterrupted();
                                System.out.println("[回放定时任务：]  结束： " + CacheUtil.HISTORYMAP.get(key).getRtsp() + "  推流任务！");
                                // 清除缓存
                                CacheUtil.HISTORYMAP.remove(key);
                                CacheUtil.HISTORYjobMap.remove(key);

                            } else if ((newTime - openTime) / 1000 / 60 > Integer.valueOf(config.getKeepalive())) {
                                CacheUtil.HISTORYjobMap.get(key).setInterrupted();
                                //logger.debug("[定时任务：]  结束： " + CacheUtil.STREAMMAP.get(key).getRtsp() + "  推流任务！");
                                System.out.println(Common.fromDateH() + "[回放超时预览：]  结束： " + CacheUtil.HISTORYMAP.get(key).getRtsp() + "  推流任务！");
                                CacheUtil.HISTORYjobMap.remove(key);
                                CacheUtil.HISTORYMAP.remove(key);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 1, 1000 * 10);
    }
}
