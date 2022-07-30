package com.lvtn.platform.exception;

import com.lvtn.platform.common.ApiMessage;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int code;
    private final String message;

    public ApiException(ApiMessage apiMessage) {
        super(apiMessage.getMessage());
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }


    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ApiException(int code, String message, boolean needTranslate) {
        super(message);
        this.code = code;
        if (needTranslate) {
            this.message = message;
        } else {
            this.message = message;
        }
    }

    public ApiException(int code, String message, boolean needTranslate, Object[] translatorObjects) {
        super(message);
        this.code = code;
        if (needTranslate) {
            this.message = message;
        } else {
            this.message = message;
        }
    }

}
