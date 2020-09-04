package com.easystream.core.stream;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    public static String IpConvert(String domainName) {
        String ip = domainName;
        try {
            ip = InetAddress.getByName(domainName).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
        return ip;
    }

    public static boolean ping(String ipAddress) {
        boolean status = false;
        try {
            int timeOut = 3000;  //超时应该在3钞以上
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
        } catch (Exception e) {
            return false;
        }
        return status;
    }
}
