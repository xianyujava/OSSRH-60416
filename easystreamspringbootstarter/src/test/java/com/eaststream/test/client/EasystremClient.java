package com.eaststream.test.client;

import com.easystream.core.annotation.RequestType;
import com.easystream.core.stream.CameraPojo;

/**
 * @ClassName EasystremClient
 * @Description: TODO
 * @Author soft001
 * @Date 2020/9/1
 **/
public interface EasystremClient {
     @RequestType("openCamera")
     String openCamera(CameraPojo pojo);
}
