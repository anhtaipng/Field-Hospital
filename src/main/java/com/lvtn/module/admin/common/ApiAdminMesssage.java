package com.lvtn.module.admin.common;

import com.lvtn.platform.common.ApiMessage;
import lombok.Setter;

@Setter
public class ApiAdminMesssage extends ApiMessage {

    public static ApiMessage
            MEDICINE_EXISTED = new ApiAdminMesssage(1123123, "Thuốc đã tồn tại"),
            MEDICINE_NOT_FOUND = new ApiAdminMesssage(3103001, "Không tìm thấy thuốc tương ứng");

    public static ApiMessage
            SYMPTOM_EXISTED = new ApiAdminMesssage(1123123, "Triệu chứng tồn tại"),
            SYMPTOM_NOT_FOUND = new ApiAdminMesssage(3103001, "Không tìm thấy triệu chứng tương ứng");

    public static ApiMessage
            MEDICINE_BATCH_EXISTED = new ApiAdminMesssage(1123123, "Lô thuốc đã tồn tại"),
            MEDICINE_BATCH_NOT_FOUND = new ApiAdminMesssage(3103001, "Không tìm thấy lô thuốc tương ứng");

    public static ApiMessage
            BVDC_GROUP_NOT_FOUND = new ApiAdminMesssage(3103001, "Không tìm thấy nhóm bệnh nhân tương ứng"),
            BVDC_GROUP_HAS_PATIENT = new ApiAdminMesssage(3103001, "Nhóm bệnh nhân vẫn còn bệnh nhân");

    public ApiAdminMesssage(int code, String message) {
        super(code, message);
    }
}
