package com.easystream.core.utils;
import com.easystream.core.stream.cv.videoimageshot.exception.StreamRuntimeException;

/**
 * @ClassName URLUtils
 * @Description: URl工具類
 * @Author soft001
 * @Date 2020/8/31
 **/
public final class URLUtils {

    private URLUtils() {
    }

    public static boolean hasProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static void checkBaseURL(String baseUrl) {
        if (!hasProtocol(baseUrl)) {
            throw new StreamRuntimeException("base url has no protocol: " + baseUrl);
        }
    }

    public static String getValidBaseURL(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            int i = baseUrl.length() - 1;
            do {
                char ch = baseUrl.charAt(i);
                if (ch != '/') {
                    break;
                }
                i--;
            } while (i > 0);
            return baseUrl.substring(0, i + 1);
        }
        return baseUrl;
    }


    public static String getValidURL(String baseURL, String uri) {
        if (!URLUtils.hasProtocol(uri)) {
            if (StringUtils.isNotEmpty(baseURL)) {
                if (baseURL.endsWith("/")) {
                    baseURL = getValidBaseURL(baseURL);
                }
                if (!uri.startsWith("/")) {
                    uri = "/" + uri;
                }
                return baseURL + uri;
            }
            else {
                return  "http://" + uri;
            }
        }
        return uri;
    }

}
