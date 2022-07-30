package com.lvtn.module.patient.common;

import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.platform.common.ApiMessage;
import lombok.Setter;

@Setter
public class ApiPatientMesssage extends ApiMessage {

    public static ApiMessage
            PATIENT_NOT_FOUND = new ApiPatientMesssage(3101001, "Không tìm thấy bệnh nhân"),
            PATIENT_LOCATION_NOT_FOUND = new ApiPatientMesssage(3101001, "Không tìm thấy vị trí hiện tại của bệnh nhân"),
            PATIENT_EXISTED = new ApiPatientMesssage(3101002, "Bệnh nhân đã tồn tại"),
            PATIENT_EXITED = new ApiPatientMesssage(3101003, "Bệnh nhân đã xuất viện");

    public ApiPatientMesssage(int code, String message) {
        super(code, message);
    }
}
