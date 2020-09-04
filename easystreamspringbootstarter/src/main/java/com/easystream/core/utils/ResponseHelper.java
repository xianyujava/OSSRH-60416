package com.easystream.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {

    public ResponseHelper() {
    }

    public static <T> ResponseModel<T> notFound(String message) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setCode(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseModel<T> internalServerError(String message) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseModel<T> notAllowedError(String message) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        response.setCode(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseModel<T> validationFailure(String message) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseModel<T> buildResponseModel(T data) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.OK.value());
        response.setCode(HttpStatus.OK.getReasonPhrase());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setData(data);
        return response;
    }

    public static <T> ResponseModel<T> buildERRORResponseModel(T data) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        response.setCode(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setData(data);
        return response;
    }

    public static Map<String, String> buildResponseResult(String code, String message) {
        Map<String, String> response = new HashMap();
        response.put("code", code);
        try {
            response.put("message", URLEncoder.encode(message, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

}
