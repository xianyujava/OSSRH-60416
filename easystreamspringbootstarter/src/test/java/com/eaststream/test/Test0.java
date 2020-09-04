package com.eaststream.test;

import com.eaststream.test.client.EasystremClient;
import com.easystream.boot.annotation.EasystreamScan;
import com.easystream.core.EasystreamConfiguration;
import com.easystream.core.stream.CameraPojo;
import com.easystream.spring.reflection.EasysteamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles("test0")
@SpringBootTest(classes = Test0.class)
@EasystreamScan(basePackages = "com.eaststream.test.client")
//@EasystreamScan()
@EnableAutoConfiguration
public class Test0 {

    @Autowired
    private EasystremClient client;

    @Autowired
    private EasystreamConfiguration config0;

    @Autowired
    private EasysteamService easystreamService;

    @Test
    public void testConfiguration() {
        assertEquals("192.168.200.21", config0.getPush_host());

    }

    @Test
    public void testClient0() {
        CameraPojo pojo=new CameraPojo();
        pojo.setUsername("admin");
        pojo.setPort("8000");
        pojo.setPassword("xtzc_202");
        pojo.setIp("172.16.1.41");
        pojo.setChannel("2");
        pojo.setType("0");
        pojo.setToken("H");
        String result =client.openCamera(pojo);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testeasystreamService() {
        CameraPojo pojo=new CameraPojo();
        pojo.setUsername("admin");
        pojo.setPort("8000");
        pojo.setPassword("xtzc_202");
        pojo.setIp("172.16.1.41");
        pojo.setChannel("2");
        pojo.setType("0");
        pojo.setToken("H");
        String result =easystreamService.openCamera(pojo);
        System.out.println(result);
        assertNotNull(result);
    }

}
