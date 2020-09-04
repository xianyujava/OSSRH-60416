package com.easystream.core.stream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CacheUtil
 * @Description: TODO
 * @Author Soft001
 * @Date 2020/2/13
 **/
public class CacheUtil {
    /*
	 * 保存已经开始推的直播流
	 */
    public static Map<String, CameraPojo> STREAMMAP = new ConcurrentHashMap<String, CameraPojo>();

    /*
     * 保存服务启动时间
     */
    public static long STARTTIME;

    /*
    // 存放直播流任务线程
     */
    public static Map<String, CameraThread.MyRunnable> jobMap = new ConcurrentHashMap<String, CameraThread.MyRunnable>();

    /*
	 * 保存已经开始推的历史流
	 */
    public static Map<String, CameraPojo> HISTORYMAP = new ConcurrentHashMap<String, CameraPojo>();

    /*
  // 存放历史流任务线程
   */
    public static Map<String, CameraThread.PlayBackRunnable> HISTORYjobMap = new ConcurrentHashMap<String, CameraThread.PlayBackRunnable>();
}
