package com.lvtn.platform.common;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@NoArgsConstructor
public class Response<T> {
    boolean success;
    int code;
    String message;
    T payload;

    public Response(boolean success, ApiMessage apiMessage, T payload) {
        this.success = success;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
        this.payload = payload;
    }

    public Response(boolean success, int code, String message, T payload) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public static <T> Response<T> error(ApiMessage apiMessage) {
        return new Response<>(false, apiMessage, null);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(false, code, message, null);
    }

    public static <T> Response<T> success() {
        return new Response<>(true, ApiMessage.SUCCESS, null);
    }

    public static <T> Response<T> success(T payload) {
        return new Response<>(true, ApiMessage.SUCCESS, payload);
    }

    public static <T> Response<T> success(ApiMessage apiMessage) {
        return new Response<>(true, apiMessage, null);
    }

    public static Response<ApiMessage> ofApiMessage(ApiMessage apiMessage) {
        return new Response<>(true, ApiMessage.SUCCESS, new ApiMessage(apiMessage.code, apiMessage.message));
    }

    public static <T> Response<T> success(T payload, ApiMessage apiMessage) {
        return new Response<>(true, apiMessage, payload);
    }

    public static void servletResponse(HttpServletResponse response, ApiMessage apiMessage) throws IOException {
        servletResponse(response, apiMessage, mapHttpStatus(apiMessage.getCode()), false);
    }

    public static void servletResponse(HttpServletResponse response, ApiMessage apiMessage, HttpStatus httpStatus, boolean close) throws IOException {
        String responseString = RequestHelper.convertToJson(Response.error(apiMessage));
        servletResponse(response, responseString, httpStatus, close);
    }

    public static void servletResponse(HttpServletResponse response, int code, String message) throws IOException {
        servletResponse(response, code, message, mapHttpStatus(code), false);
    }

    public static void servletResponse(HttpServletResponse response, int code, String message, HttpStatus httpStatus, boolean close) throws IOException {
        String responseString = RequestHelper.convertToJson(Response.error(code, message));
        servletResponse(response, responseString, httpStatus, close);
    }

    public static void servletResponse(HttpServletResponse response, String responseString, HttpStatus httpStatus, boolean close) throws IOException {
        response.setContentType(Constant.API_CONTENT_TYPE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding(Constant.DEFAULT_ENCODING);
        var writer = response.getWriter();
        writer.write(responseString);
        if (close) {
            writer.close();
        }
    }

    private static HttpStatus mapHttpStatus(int code) {
        try {
            return HttpStatus.valueOf(code);
        } catch (Exception ex) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}