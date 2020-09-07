# OSSRH-60416


<p align="center">
<a href="https://gitee.com/null_166_0518/easystream.git">
    EASYSTREAM FOR SPRINGBOOT 
</a>
</p>


<p align="center">

<a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
    <img src="https://img.shields.io/badge/JDK-1.8+-yellow" alt="JDK">
</a>

<a href="https://opensource.org/licenses/mit-license.php">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License">
</a>

</p>

<h1 align="center">easystream - 简单的rtsp转rtmp,hlv,hls播放流框架</h1>


项目介绍：
-------------------------------------

easystream是一个对海康大华rtsp二次封装转换为web直播流的框架。<br>
利用nginx实现rtmp、hlv、hls格式播放视频。windows下编译过的nginx请项目中下载，Linux下需动态编译需要欢迎索取。


easystream配置加到application.yml
-----
```xml
easystream:
  #对应推送的nginx服务地址
  push_host: 192.168.200.2
  #直播流保活时间(分钟)
  keepalive: 1
  #额外推送地址
  host_extra: 127.0.0.1
  #nginx推送端口
  push_port: 1935
  #主码流最大码率
  main_code: 5120
  #子码流最大码率
  sub_code: 1024
  #1nvr,2,合成服务
  rtsptype: 1
  #nginx视频流访问端口
  access_port: 8085
```

极速开始
-------------------------------------
以下例子基于Spring Boot

### 第一步：添加Maven依赖

直接添加以下maven依赖即可

```xml
<dependency>
	<groupId>com.github.xianyujava</groupId>
	<artifactId>easystream-spring-boot-starter</artifactId>
	<version>1.1.1</version>
</dependency>
```


### 第二步：扫描接口

在Spring Boot的配置类或者启动类上加上`@EasystreamScan`注解

```java
@SpringBootApplication
@Configuration
@EasystreamScan
public class MyApplication {
  public static void main(String[] args) {
      SpringApplication.run(MyApplication.class, args);
   }
}
```

### 第三步：调用接口

OK，我们可以愉快地调用接口了

```java
// 注入接口实例
@Autowired
private EasysteamService easysteamService;
...

```

## 开启视频流

```java
/**
 * CameraPojo
 *  private String username = "";// 摄像头账号
    private String password = "";// 摄像头密码
    private String ip = "";// 摄像头ip
    private String port = "554";
    private String channel = "";// 摄像头通道
    private String stream = "1";// 摄像头码流 1主码流，2子码流
    private String rtsp = "";// 返回的rtsp地址
    private String rtmp = "";// 返回的rtmp地址
    private String url = "";// 返回的hlv播放地址
    private String startTime = "";// 回放开始时间,回放时候才要赋值
    private String endTime = "";// 回放结束时间,回放时候才要赋值
    private String openTime = "";// 打开时间
    private int count = 0;// 默认使用人数
    private String token = "";//直播和回放必须指定一个唯一标志
    private String hls = "";//返回hls播放
    private String type="";//默认空为合成视频，0:nvr，1:ipc
 */
String result = easysteamService.openCamera(CameraPojo pojo);
```


## 关闭直播视频流

```java
/**
 * tokens为打开视频流的时的token,多个用逗号分隔；例如1,3
 */
String result = easysteamService.closeCamera(String tokens);
```

## 关闭回放视频流

```java
/**
 * tokens为打开视频流的时的token,多个用逗号分隔；例如1,3
 */
String result = easysteamService.closeHistoryCamera(String tokens);
```


## 视频流保活,定时调用此方法否则超时会停止转码流

```java
/**
 * tokens为打开视频流的时的token,多个用逗号分隔；例如1,3
 */
String result = easysteamService.keepAlive(String tokens);
```


## 视频回放流保活,定时调用此方法否则超时会停止转码流

```java
/**
 * tokens为打开视频流的时的token,多个用逗号分隔；例如1,3
 */
String result = easysteamService.keepPlayBackAlive(String tokens);
```


### 联系作者:<br>

亲，进群前记得先star一下哦~

项目协议
--------------------------
The MIT License (MIT)

Copyright (c) 2020 jian yu



