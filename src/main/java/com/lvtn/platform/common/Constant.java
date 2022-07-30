package com.lvtn.platform.common;

import org.springframework.data.domain.Sort;

public class Constant {

    public static final String API_CONTENT_TYPE = "application/json";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";
    public static final String SYSTEM_HEADER = "X-System";
    public static final String COMPANY_HEADER = "X-companyId";
    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String CLIENT_ID_HEADER = "X-Client-IP";
    public static final String ZONE_ID_HEADER = "X-Zone-Id";
    public static final String SERVICE_HEADER = "X-Service-Token";
    public static final String DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    public static final String DATA_INTEGRITY_VIOLATE = "data_integrity_violate.";

    public static int DEFAULT_PAGE_SIZE = 20;
    public static String ASCENDING_SORT = "ASC";
    public static String DESCENDING_SORT = "DESC";
    public static String COLUMN_DEFAULT_SORT = "id";
    public static Sort DEFAULT_SORT = Sort.by("id").ascending();
    public static String DISPLAY_POSITION_SORT = "displayPosition:ASC";
    public static String UTC_TIMEZONE = "UTC";
    public static String DEFAULT_ENCODING = "UTF-8";

    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String XLSX_CONTENT_DELIMITER = ",";
    public static Long SITE_CAPABILITY_OFFICE = -1L;

}
