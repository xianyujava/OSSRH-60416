package com.easystream.core.stream;

import com.easystream.core.EasystreamConfiguration;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName CameraThread
 * @Description: 视频流线程类
 * @Author yujian
 * @Date 2020/2/13
 **/
public class CameraThread {
    // 创建线程池
    public static ExecutorService es = Executors.newCachedThreadPool();

    /**
     * 直播线程
     */
    public static class MyRunnable implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(MyRunnable.class);
        private EasystreamConfiguration configuration;
        private CameraPojo cameraPojo;
        private Thread nowThread;
        private CameraPush push;

        public byte[] getFream(String DIR) throws FrameGrabber.Exception {
            return push.grabber.getBytes(DIR);
        }

        public MyRunnable(CameraPojo cameraPojo,EasystreamConfiguration configuration) {
            this.cameraPojo = cameraPojo;
            this.configuration=configuration;
        }

        // 中断线程
        public void setInterrupted() {
            nowThread.interrupt();
        }

        @Override
        public void run() {
            // 直播流
            try {
                // 获取当前线程存入缓存
                nowThread = Thread.currentThread();
                CacheUtil.STREAMMAP.put(cameraPojo.getToken(), cameraPojo);
                // 执行转流推流任务
                push = new CameraPush(cameraPojo,configuration).from();
               // push.setApplicationContext(configuration);
                if (push != null) {
                    push.to().go(nowThread);
                }
                //push.grabber.grabImage();
                // 清除缓存
                CacheUtil.STREAMMAP.remove(cameraPojo.getToken());
                CacheUtil.jobMap.remove(cameraPojo.getToken());
                logger.info(cameraPojo.getRtsp() + " 推流结束...");
            } catch (Exception e) {
                System.err.println(
                        "当前线程：" + Thread.currentThread().getName() + " 当前任务：" + cameraPojo.getRtsp() + "停止...");
                CacheUtil.STREAMMAP.remove(cameraPojo.getToken());
                CacheUtil.jobMap.remove(cameraPojo.getToken());
                e.printStackTrace();
            }
        }
    }

    /**
     * 回放线程
     */
    public static class PlayBackRunnable implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(PlayBackRunnable.class);
        private CameraPojo cameraPojo;
        private Thread nowThread;
        private EasystreamConfiguration configuration;
        public PlayBackRunnable(CameraPojo cameraPojo,EasystreamConfiguration configuration) {
            this.cameraPojo = cameraPojo;
            this.configuration=configuration;
        }

        // 中断线程
        public void setInterrupted() {
            nowThread.interrupt();
        }

        @Override
        public void run() {
            // 回放流
            try {
                // 获取当前线程
                nowThread = Thread.currentThread();
                CacheUtil.HISTORYMAP.put(cameraPojo.getToken(), cameraPojo);
                // 执行转流推流任务
                CameraPush push = new CameraPush(cameraPojo,this.configuration).from();
                //push.setApplicationContext(configuration);
                if (push != null) {
                    push.to().go(nowThread);
                }
                // 清除缓存
                CacheUtil.HISTORYMAP.remove(cameraPojo.getToken());
                CacheUtil.HISTORYjobMap.remove(cameraPojo.getToken());
                logger.info(cameraPojo.getRtsp() + " 历史流结束...");
            } catch (Exception e) {
                System.err.println(
                        "当前线程：" + Thread.currentThread().getName() + " 当前任务：" + cameraPojo.getRtsp() + "回放停止...");
                CacheUtil.HISTORYMAP.remove(cameraPojo.getToken());
                CacheUtil.HISTORYjobMap.remove(cameraPojo.getToken());
                e.printStackTrace();
            }
        }
    }

}
