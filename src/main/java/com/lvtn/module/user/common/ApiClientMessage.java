package com.lvtn.module.user.common;

import com.lvtn.platform.common.ApiMessage;
import lombok.Getter;

@Getter
public class ApiClientMessage extends ApiMessage {

    // Account: yyy = 1
    public static ApiMessage
            ACCOUNT_NOT_FOUND = new ApiClientMessage(3001001, "Chứng minh nhân dân hoặc mật khẩu không chính xác"),
            ACCOUNT_EXISTED = new ApiClientMessage(3001002, "Chứng minh nhân dân đã được sử dụng"),
            WRONG_PASSWORD = new ApiClientMessage(3001002, "Mật khẩu cũ không chính xác"),
            WRONG_PHONE_NUMBER = new ApiClientMessage(3001007, "Số điện thoại không đúng với tài khoản này"),
            ACCOUNT_IS_INACTIVE = new ApiClientMessage(3001007, "account.status.disabled"),
            OTP_EXPIRED = new ApiClientMessage(3001007, "mã OTP đã hết hạn"),
            WRONG_OTP = new ApiClientMessage(3001007, "Sai mã số OTP"),
            PASSWORD_MISMATCH = new ApiClientMessage(3001008, "password.mismatch");
    // Activation code: yyy = 2
    public static ApiClientMessage
            ACTIVATION_CODE_NOT_FOUND = new ApiClientMessage(3002001, "activation.code.not_found"),
            ACTIVATION_CODE_USED = new ApiClientMessage(3002002, "activation.code.used"),
            ACTIVATION_CODE_EXPIRED = new ApiClientMessage(3002003, "activation.code.expired");

    public static ApiClientMessage
            CLIENT_CREATE_SUCCESS = new ApiClientMessage(3002001, "client.create_success"),
            CLIENT_UPDATE_SUCCESS = new ApiClientMessage(3002001, "client.update_success"),
            CLIENT_DELETE_SUCCESS = new ApiClientMessage(3002001, "client.delete_success"),
            CLIENT_NOT_FOUND = new ApiClientMessage(3002001, "client.not_found"),
            CLIENT_NOT_HAVE_DEFAULT_SITE = new ApiClientMessage(3002001, "client.not_have_default_site");

    public static ApiClientMessage
            PERSON_CREATE_SUCCESS = new ApiClientMessage(3002001, "person.create_success"),
            PERSON_UPDATE_SUCCESS = new ApiClientMessage(3002001, "person.update_success"),
            PERSON_DELETE_SUCCESS = new ApiClientMessage(3002001, "person.delete_success"),
            PERSON_NOT_FOUND = new ApiClientMessage(3002001, "person.not_found");
;

    public ApiClientMessage(int code, String message) {
        super(code, message);
    }
}
