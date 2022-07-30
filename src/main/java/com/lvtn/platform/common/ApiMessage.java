package com.lvtn.platform.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Format: xxxyyyzzz
 * <p>
 * xxx - Module code:
 * Common = 1,
 * Account = 2,
 * <p>
 * yyy - Entity Code:
 * Example: user = 1,
 * <p>
 * zzz - Error Code:
 * Example: not_found = 1
 */
@AllArgsConstructor
@Getter
public class ApiMessage {

    public static ApiMessage
            SUCCESS = new ApiMessage(0, "success"),
            UNAUTHORIZED = new ApiMessage(401, "unauthorized"),
            FORBIDDEN = new ApiMessage(403, "forbidden"),
            NOT_FOUND = new ApiMessage(404, "not-found"),
            UNSUPPORTED_MEDIA_TYPE = new ApiMessage(415, "unsupported-media-type"),
            INVALID_PARAM = new ApiMessage(499, "invalid-params"),
            NOT_READABLE_PARAM = new ApiMessage(498, "not-readable-params"),
            MISSING_PARAM = new ApiMessage(497, "missing-params"),
            UPLOAD_SIZE_EXCEEDED = new ApiMessage(496, "upload-size-exceeded"),
            REQUEST_METHOD_NOT_SUPPORT = new ApiMessage(405, "request-method-not-supported"),
            DATA_INTEGRITY_VIOLATION = new ApiMessage(495, "data-integrity-violation"),
            INTERNAL_SERVER_ERROR = new ApiMessage(500, "internal-server-error");

    protected final int code;
    protected final String message;
}

