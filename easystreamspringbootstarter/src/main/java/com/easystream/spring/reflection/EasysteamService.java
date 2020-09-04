package com.easystream.spring.reflection;/**
 * Created by Soft001 on 2020/9/3.
 */

import com.easystream.core.stream.CameraPojo;

import java.util.Map;

/**
 * @ClassName MethodInterface
 * @Description: TODO
 * @Author Soft001
 * @Date 2020/9/3
 * @Version V1.0
 **/
public interface EasysteamService {
    String openCamera(CameraPojo pojo);

    String closeCamera(String tokens);

    String closeHistoryCamera(String tokens);

    Map<String, CameraPojo> getCameras();

    String keepAlive(String tokens);

    byte[] capture(String url);

    String keepPlayBackAlive(String tokens);
}
