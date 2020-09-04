package com.easystream.core.utils;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Common {
    /**
     * 日期转换yyyy-MM-dd HH:mm:ss
     */
    public static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    /**
     * 日期转换yyyy-MM-dd
     */
    public static SimpleDateFormat formatRq = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * addyujian 存放用户登陆session
     */
    public static Map<String, Object> sessionMap = new HashMap();

    /**
     * 判断变量是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s || "".equals(s) || "".equals(s.trim())
                || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 使用率计算
     *
     * @return
     */
    public static String fromUsage(long free, long total) {
        Double d = new BigDecimal(free * 100 / total).setScale(1,
                BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(d);
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd hh:mm:ss
     *
     * @return String
     */
    public static String fromDateH() {
        //DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd
     *
     * @return String
     */
    public static String fromDateY() {
        DateFormat format1 = new SimpleDateFormat("yyyy");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式： hh:mm:ss
     *
     * @return String
     */
    public static String fromDate() {
        DateFormat format1 = new SimpleDateFormat("HH:mm");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd
     *
     * @return String
     */
    public static String fromDateYMD() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(new Date());
    }

    /**
     * 返回当前时间 格式： hh:mm:ss
     *
     * @return String
     */
    public static String fromDatehms() {
        DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        return format1.format(new Date());
    }

    /**
     * 用来去掉List中空值和相同项的。
     *
     * @param list
     * @return
     */
    public static List<String> removeSameItem(List<String> list) {
        List<String> difList = new ArrayList<String>();
        for (String t : list) {
            if (t != null && !difList.contains(t)) {
                difList.add(t);
            }
        }
        return difList;
    }

    /**
     * 传入原图名称，，获得一个以时间格式的新名称
     *
     * @param fileName 原图名称
     * @return
     */
    public static String generateFileName(String fileName) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        return formatDate + random + extension;
    }

    /**
     * 取得html网页内容 UTF8编码
     *
     * @param urlStr 网络地址
     * @return String
     */
    public static String getInputHtmlUTF8(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url
                    .openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200) {
                // 通过输入流获取网络图片
                InputStream inputStream = httpsURLConnection.getInputStream();
                String data = readHtml(inputStream, "UTF-8");
                inputStream.close();
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;

    }

    /**
     * 取得html网页内容 GBK编码
     *
     * @param urlStr 网络地址
     * @return String
     */
    public static String getInputHtmlGBK(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url
                    .openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200) {
                // 通过输入流获取网络图片
                InputStream inputStream = httpsURLConnection.getInputStream();
                String data = readHtml(inputStream, "GBK");
                inputStream.close();
                return data;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return null;

    }

    /**
     * @param inputStream
     * @param uncode      编码 GBK 或 UTF-8
     * @return
     * @throws Exception
     */
    public static String readHtml(InputStream inputStream, String uncode)
            throws Exception {
        InputStreamReader input = new InputStreamReader(inputStream, uncode);
        BufferedReader bufReader = new BufferedReader(input);
        String line = "";
        StringBuilder contentBuf = new StringBuilder();
        while ((line = bufReader.readLine()) != null) {
            contentBuf.append(line);
        }
        return contentBuf.toString();
    }

    /**
     * @return 返回资源的二进制数据 @
     */
    public static byte[] readInputStream(InputStream inputStream) {

        // 定义一个输出流向内存输出数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 定义一个缓冲区
        byte[] buffer = new byte[1024];
        // 读取数据长度
        int len = 0;
        // 当取得完数据后会返回一个-1
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                // 把缓冲区的数据 写到输出流里面
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // 得到数据后返回
        return byteArrayOutputStream.toByteArray();

    }


    /**
     * 校验ip输入是否正确
     *
     * @param ipStr
     * @return
     */
    public static boolean checkIp(String ipStr) {
        try {
            String[] ips = ipStr.split(";");
            for (String ipstr : ips) {
                String string = ipstr.replace(".", "-");
                String[] ip = string.split("-");
                for (int i = 0; i < 3; i++) {
                    if (!ip[i].equals(ip[i + 4])) {
                        return false;
                    }
                }
                if (Integer.parseInt(ip[3]) > Integer.parseInt(ip[7])) {
                    return false;
                }
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCarInfo(String CardCode)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String year = CardCode.substring(6).substring(0, 4);// 得到年份
        String yue = CardCode.substring(10).substring(0, 2);// 得到月份
        String day = CardCode.substring(12).substring(0, 2);//得到日
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
        } else {
            sex = "男";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        //String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("csrq", year + "-" + yue + "-" + day);
        map.put("sex", sex);
        map.put("age", age);
        return map;
    }

    /**
     * 15位身份证的验证
     *
     * @param
     * @throws Exception
     */
    public static Map<String, Object> getCarInfo15W(String card)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String uyear = "19" + card.substring(6, 8);// 年份
        String uyue = card.substring(8, 10);// 月份
        // String uday=card.substring(10, 12);//日
        String usex = card.substring(14, 15);// 用户的性别
        String sex;
        if (Integer.parseInt(usex) % 2 == 0) {
            sex = "女";
        } else {
            sex = "男";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        // String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
        }
        map.put("sex", sex);
        map.put("age", age);
        return map;
    }

    /**
     * 执行Linux命令
     *
     * @param cmd
     * @return
     * @throws Exception
     */
    public static String executeLinuxCmd(String cmd) throws IOException, InterruptedException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("linux") >= 0) {
            System.out.println("got cmd job : " + cmd);
            Runtime run = Runtime.getRuntime();
            //try {
            Process process = run.exec(cmd);
            InputStream in = process.getInputStream();
            //BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            // System.out.println("[check] now size \n"+bs.readLine());
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            System.out.println("job result [" + out.toString() + "]");
            in.close();
            process.waitFor();
            process.destroy();
            return out.toString();
            //} catch (Exception e) {
            //   e.printStackTrace();
            //    System.out.println("--------------------------");
            // }
            // return null;
        }
        return "";
    }

    public static List<Map<String, Object>> toTreeMap(List<Map<String, Object>> list, String fields[]) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map m = null;
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                m = new TreeMap<String, Object>();
                for (int i = 0; i < fields.length; i++) {
                    m.put(i < 10 ? "0" + i : new Integer(i).toString(), map.get(fields[i]));
                    //  m.put(new Integer(i).toString(), map.get(fields[i])) 排序有bug，超过10就有问题，不过上面的方法也只能到19，不过应该够用了 。
                }
                result.add(m);
            }
        }
        return result;
    }

    public static String UUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.replaceAll("-", "");// s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }

    public static String FileType(String fileName) {
        String fileType = "";
        if (fileName != null) {
            fileType = fileName.substring(fileName.lastIndexOf("."));
        }
        return fileType;
    }

    public static int FileSize(int size) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0");
        int fileSize = Integer.valueOf(df.format(size / 1024.0));
        return fileSize;
    }

    /**
     * 判断字符串最好一个字符是否是指定的
     *
     * @param str
     * @param last
     * @return
     */
    public static boolean IsLastStr(String str, String last) {
        try {
            if (str.lastIndexOf(last) == str.length()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
