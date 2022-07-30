package com.lvtn.platform.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Function;

public class RequestHelper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Gson GSON = new Gson();
    private static final Gson GSON_RESPONSE = new GsonBuilder().setPrettyPrinting().create();

    static {
        /*
         * this resolve problem: The destination property matches multiple source property hierarchies:
         *
         * 	com.can.service.sale.entity.Product.getBrandId()
         * 	com.can.service.sale.entity.Product.getBrand()/com.can.service.sale.entity.Brand.getId()
         */
        MODEL_MAPPER.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        /*
         * Unrecognized field: Example: id in UpdateDto and no id in CreateDto
         */
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        /*
         * Map LocalDateTime
         */
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static String convertToJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String prettyJson(Object obj) {
        return GSON_RESPONSE.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <S, T> List<T> createDtoList(List<S> entityList, Function<S, T> mapFunction) {
        List<T> dtoList = new ArrayList<>();
        for (S entity : entityList) {
            T dto = mapFunction.apply(entity);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        return request.getHeader(Constant.AUTH_HEADER);
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getResponse();
    }

    public static TimeZone getTimeZone() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return TimeZone.getTimeZone(Constant.DEFAULT_ZONE_ID);
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        var zoneId = request.getHeader(Constant.ZONE_ID_HEADER);

        if (StringUtils.isBlank(zoneId)) {
            return TimeZone.getTimeZone(Constant.DEFAULT_ZONE_ID);
        }

        var timeZone = TimeZone.getTimeZone(zoneId);
        if (!Objects.equals(timeZone.getID(), zoneId)) {
            return TimeZone.getTimeZone(Constant.DEFAULT_ZONE_ID);
        }
        return timeZone;
    }

    public static String getTimeZoneId() {
        return getTimeZone().getID();
    }

    public static String getHeader(String header) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getHeader(header);
    }

    public static String getRequestId() {
        return getHeader(Constant.REQUEST_ID_HEADER);
    }

    public static String getClientIP() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        var clientIp = request.getHeader(Constant.CLIENT_ID_HEADER);
        if (StringUtils.isNotBlank(clientIp)) {
            return clientIp;
        }

        // Set from nginx,...
        clientIp = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(clientIp)) {
            return clientIp;
        }

        // Web server via a proxy server or maybe your application is behind a load balancer.
        // Should access the X-Forwarded-For http header in such a case to get the user's IP address.
        clientIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(clientIp) || clientIp.equalsIgnoreCase("127.0.0.1")) {
            clientIp = request.getRemoteAddr();
        }

        // Get localhost ip if remoteAddr like 0:0:0:0:0:0:0:1
        if (clientIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                return null;
            }
        }
        return clientIp;
    }

    public static ZoneId getZoneId() {
        return getTimeZone().toZoneId();
    }

    public static String getZoneIdString() {
        return getHeader(Constant.ZONE_ID_HEADER);
    }

    public static String getUserAgent() {
        return getHeader(Constant.USER_AGENT_HEADER);
    }

    public static ZonedDateTime nowAtZonedDateTime() {
        return ZonedDateTime.now(RequestHelper.getTimeZone().toZoneId());
    }

}
