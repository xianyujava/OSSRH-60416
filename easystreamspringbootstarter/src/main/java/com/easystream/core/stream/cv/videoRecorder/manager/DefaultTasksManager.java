package com.easystream.core.stream.cv.videoRecorder.manager;//package com.sljt.stream.cv.videoRecorder.manager;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Queue;
//import java.util.Stack;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import com.sljt.entity.RegionVideo;
//import com.sljt.stream.cv.videoRecorder.entity.RecordTask;
//import com.sljt.stream.cv.videoRecorder.recorder.JavaCVRecord;
//import com.sljt.stream.cv.videoRecorder.recorder.Recorder;
//import com.sljt.stream.cv.videoimageshot.exception.CodecNotFoundExpception;
//import com.sljt.util.CommUtil;
//import com.sljt.util.SaticScheduleTask;
//
///**
// * 默认任务管理（内置对象池管理）
// *
// * @author yujian
// */
//public class DefaultTasksManager implements TasksManager {
//    public static final int START_STATUS = 1;
//    public static final int PAUSE_STATUS = -1;
//    public static final int STOP_STATUS = 2;
//    //public static SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHmmss");
//
//    protected static volatile int task_id_index = 0;// id累加
//
//    protected volatile int maxSize = -1;// 最大任务限制，如果小于0则无限大
//
//    private String record_dir;//文件存储目录
//
//    private String play_url = "";//播放地址
//
//    public String getRecordDir() {
//        return record_dir;
//    }
//
//    public String getPlay_url() {
//        return play_url;
//    }
//
//    @Override
//    public TasksManager setPlayUrl(String play_url) {
//        this.play_url = play_url;
//        return this;
//    }
//
//    @Override
//    public TasksManager setRecordDir(String recordDir) {
//        this.record_dir = recordDir;
//        return this;
//    }
//
//    // 对象池操作
//    // 当前任务池大小
//    protected volatile int pool_size = 0;
//    // 当前任务池中数量
//    protected volatile int work_size = 0;
//    // 当前空闲任务数量
//    protected volatile int idle_size = 0;
//    /**
//     * 工作任务池
//     */
//    protected Queue<RecordTask> workpool = null;
//    /**
//     * 空闲任务池
//     */
//    protected Stack<RecordTask> idlepool = null;
//
//    /**
//     * 初始化
//     *
//     * @param maxSize -最大工作任务大小
//     * @throws Exception
//     */
//    public DefaultTasksManager(int maxSize) throws Exception {
//        super();
////		if (maxSize < 1) {
////			throw new Exception("maxSize不能空不能小于1");
////		}
//        this.maxSize = maxSize;
//        this.workpool = new ConcurrentLinkedQueue<>();
//        this.idlepool = new Stack<>();
//    }
//
//    @Override
//    public synchronized RecordTask createRcorder(String src, String out, String id) throws CodecNotFoundExpception, IOException, InterruptedException, CloneNotSupportedException {
//        RecordTask task = null;
//        Recorder recorder = null;
//        //int id = getId();
//        //System.out.println("创建时，当前池数量："+pool_size+",空闲数量："+idle_size+",工作数量："+work_size);
//        // 限制任务线程数量，先看有无空闲，再创建新的
//        //String playurl = (play_url == null ? out : play_url + out);
//        //out = (record_dir == null ? out : record_dir + out);
//        String rq = SaticScheduleTask.formatRq.format(new Date());
//        String dir = record_dir + rq + File.separator + id;
//        File file = new File(dir);
//        if (!file.exists()) {
//            // 创建文件夹，上级目录不存在时自动创建，使用file.mkdir()方法时上级目录不存在会抛异常
//            file.mkdirs();
//            CommUtil.executeLinuxCmd("chmod 777 " + dir);
//        }
//        out = dir + File.separator + CommUtil.formatSHM.format(new Date()) + out;
//        if (idle_size > 0) {// 如果有空闲任务，先从空闲任务池中获取
//            idle_size--;//空闲池数量减少
//            task = idlepool.pop();
//            task.setId(id);
//            task.setOut(out);
//            task.setSrc(src);
//            task.setPlayurl(dir);
//            saveTaskAndinitRecorder(task);
//            return task;
//        } else if (maxSize != -1 && pool_size < maxSize) {// 池中总数量未超出,则新建,若超出，不创建
//            recorder = new JavaCVRecord(src, out);
//            task = new RecordTask(id, src, out, recorder);
//            task.setPlayurl(dir);
//            saveTaskAndinitRecorder(task);
//            pool_size++;// 池中活动数量增加
//            return task;
//        } else if (maxSize == -1) {//不受限制
//            recorder = new JavaCVRecord(src, out);
//            task = new RecordTask(id, src, out, recorder);
//            task.setPlayurl(dir);
//            saveTaskAndinitRecorder(task);
//
//            return task;
//        }
//        //池中数量已满，且空闲池以空，返回null
//        // 超出限制数量，返回空
//        return null;
//    }
//
//
//    @Override
//    public boolean start(RecordTask task, RegionVideo regionVideo) {
//        if (task != null) {
//            Recorder recorder = task.getRecorder();
//            task.setstarttime(now());// 设置开始时间
//            task.setStatus(START_STATUS);// 状态设为开始
//            recorder.start();
//            new Thread() {
//                @Override
//                public void run() {
//                    new WorkerThreadTimer(CommUtil.FLVLENGTH, regionVideo, task).start();
//                }
//            }.start();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean pause(RecordTask task) {
//        if (task != null) {
//            task.setEndtime(now());
//            task.setStatus(PAUSE_STATUS);// 状态设为暂停
//            task.getRecorder().pause();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean carryon(RecordTask task) {
//        if (task != null) {
//            task.getRecorder().carryon();
//            task.setStatus(START_STATUS);// 状态设为开始
//        }
//        return false;
//    }
//
//    @Override
//    public boolean stop(RecordTask task) {
//        if (task != null) {
//            task.getRecorder().stop();// 停止录制
//            return recycleTask(task);
//        }
//        return false;
//    }
//
//
//    public boolean resRecord(RecordTask task, String out) throws IOException {
//        if (task != null) {
//            //stop(task);
//            //out = (record_dir == null ? out : record_dir + out);
//            // String dir=record_dir+task.getId();
//            out = task.getPlayurl() + File.separator + CommUtil.formatSHM.format(new Date()) + out;
//            task.getRecorder().resRecord(out);//
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 回收对象
//     *
//     * @return
//     */
//    private boolean recycleTask(RecordTask task) {
//        if (task != null && pool_size > 0 && work_size > 0) {
//            task.setEndtime(now());
//            task.setStatus(STOP_STATUS);
//            // 工作池中有没有
//            if (!workpool.contains(task)) {
//                return false;
//            }
//            // 从工作池中删除，存入空闲池
//            if (idlepool.add(task)) {
//                idle_size++;
//                if (workpool.remove(task)) {
//                    work_size--;
////					System.out.println("归还后，当前池数量："+pool_size+",空闲数量："+idle_size+",工作数量："+work_size);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public List<RecordTask> list() {
//        List<RecordTask> list = new ArrayList<>();
//        Iterator<RecordTask> itor = workpool.iterator();
//        for (; itor.hasNext(); ) {
//            list.add(itor.next());
//        }
//        return list;
//    }
//
//    @Override
//    public RecordTask getRcorderTask(String id) {
//        Iterator<RecordTask> itor = workpool.iterator();
//        for (; itor.hasNext(); ) {
//            RecordTask task = itor.next();
//            if (task.getId().equals(id)) {
//                return task;
//            }
//        }
//        return null;
//    }
//
//    /*
//     * 保存任务并初始化录制器
//     * @param task
//     * @throws CloneNotSupportedException
//     * @throws IOException
//     */
//    private void saveTaskAndinitRecorder(RecordTask task) throws CodecNotFoundExpception, CloneNotSupportedException, IOException {
//        Recorder recorder = task.getRecorder();
//        recorder.from(task.getSrc()).to(task.getOut());//重新设置视频源和输出
//        workpool.add(task);
//        //由于使用的是池中的引用，所以使用克隆用于保存副本
//
//        work_size++;//工作池数量增加
//    }
//
//    /*
//     * 获取当前时间
//     *
//     * @return
//     */
//    private Date now() {
//        return new Date();
//    }
//
//    /*
//     * 获取自增id
//     * @return
//     */
//    private int getId() {
//        return ++task_id_index;
//    }
//
//    /**
//     * 线程保活和销毁定时器
//     *
//     * @author eguid
//     */
//    class WorkerThreadTimer {
//        private Timer timer = null;
//        private int period = 60 * 1000;
//        private RecordTask task;
//        private RegionVideo regionVideo;
//
//        public WorkerThreadTimer(int period, RegionVideo regionVideo, RecordTask task) {
//            this.period = period;
//            this.regionVideo = regionVideo;
//            this.task = task;
//            timer = new Timer("录像线程池保活定时器", false);
//        }
//
//        public int getPeriod() {
//            return period;
//        }
//
//        public void setPeriod(int period) {
//            this.period = period;
//        }
//
//        public void start() {
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
////					for(RecordTask rt:idlepool) {
////						Recorder r=rt.getRecorder();
////						if(r!=null&&r.alive()) {
////							//如果线程已经停止，回收该线程到空闲池
////							int status=r.status();
////							if(status==2) {
////								recycleTask(rt);
////							}
////						}
////					}
//                    if (regionVideo.getIsrecord().equals("1")) {
//                        int index = CommUtil.getWeek(new Date());
//                        //if ()
//                        String stime = regionVideo.getsTime()[index].replaceAll(":","");
//                        String etime = regionVideo.geteTime()[index].replaceAll(":","");
//                        Recorder recorder = task.getRecorder();
//                        if (stime != null && etime != null && recorder != null && recorder.alive()) {
//                            try {
//                                String now = CommUtil.formatSHM.format(new Date());
//                                Date nowTime = CommUtil.formatSHM.parse(now);
//                                Date startTime = CommUtil.formatSHM.parse(stime);
//                                Date endTime = CommUtil.formatSHM.parse(etime);
//                                if (CommUtil.isEffectiveDate(nowTime, startTime, endTime)) {
//                                    int status = recorder.status();
//                                    //如果停止状态，直接唤醒录像计划
//                                    if (status == 2) {
//                                        resRecord(task, CommUtil.VIDEOFORMAT);
//                                    } else {
//                                        //运行状态
//                                        //stop(task);
//                                        resRecord(task, CommUtil.VIDEOFORMAT);
//                                    }
//
//                                } else {
//                                    stop(task);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                stop(task);
//                            }
//                        }
//                    } else {
//                        stop(task);
//                    }
//
//                }
//            }, 1000, period);
//        }
//    }
//
//    @Override
//    public boolean exist(String src, String out) {
////		System.err.println("检查本地服务是否在工作的录像任务："+src+","+out);
//        int index = -1;
//        for (RecordTask rt : workpool) {
////			System.err.println("循环中："+rt);
//            if (src.equals(rt.getSrc()) && rt.getOut().contains(out)) {
//                index++;
//                break;
//            }
//        }
////		System.err.println("检查本地服务是否在工作的录像任务结果："+index);
//        return index >= 0;
//    }
//
//}
