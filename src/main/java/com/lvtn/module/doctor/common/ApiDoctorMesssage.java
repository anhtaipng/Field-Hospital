package com.lvtn.module.doctor.common;

import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.platform.common.ApiMessage;
import lombok.Setter;

@Setter
public class ApiDoctorMesssage extends ApiMessage {

    public static ApiMessage
            STATISTIC_TYPE_NOT_FOUND = new ApiDoctorMesssage(3102001, "Không tìm thấy kiểu chỉ số tương ứng");


    public static ApiMessage
            STATISTIC_RESULT__NOT_FOUND = new ApiDoctorMesssage(3103001, "Không tìm thấy kết quả chỉ số tương ứng");

    public ApiDoctorMesssage(int code, String message) {
        super(code, message);
    }
}
