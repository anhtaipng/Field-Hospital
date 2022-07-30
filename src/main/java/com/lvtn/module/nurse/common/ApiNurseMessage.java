package com.lvtn.module.nurse.common;

import com.lvtn.module.doctor.common.ApiDoctorMesssage;
import com.lvtn.platform.common.ApiMessage;
import lombok.Setter;

@Setter
public class ApiNurseMessage extends ApiMessage {

    public static ApiMessage
            PATIENT_ID_NOT_FOUND = new ApiNurseMessage(8001001, "Không tìm thấy ID bệnh nhân tương ứng");

    public static ApiMessage
            STATISTIC_TYPE_NOT_FOUND = new ApiNurseMessage(8002001, "Không tìm thấy kiểu chỉ số tương ứng");


    public static ApiMessage
            STATISTIC_RESULT__NOT_FOUND = new ApiNurseMessage(8003001, "Không tìm thấy kết quả chỉ số tương ứng");


    public ApiNurseMessage(int code, String message) {
        super(code, message);
    }
}
