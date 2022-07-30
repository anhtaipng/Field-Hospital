package com.lvtn.module.shared.common;

import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.platform.common.ApiMessage;
import lombok.Setter;

@Setter
public class ApiSharedMesssage extends ApiMessage {

    public static ApiMessage
            PATIENT_STATUS_NOT_FOUND = new ApiSharedMesssage(1234, "Trạng thái bệnh nhân không tồn tại"),
            STATISTIC_TYPE_NOT_FOUND = new ApiSharedMesssage(123451, "Không tìm thấy kiểu chỉ số tương ứng"),
            STATISTIC_TYPE_EXIST = new ApiSharedMesssage(12331, "Kiểu chỉ số đã tồn tại!");

    public static ApiMessage
            TEST_TYPE_NOT_FOUND = new ApiSharedMesssage(3102001, "Không tìm thấy kiểu xét nghiệm tương ứng"),
            TEST_TYPE_EXIST = new ApiSharedMesssage(3102002, "Kiểu xét nghiệm đã tồn tại!");

    public static ApiMessage
            STATISTIC_RESULT__NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy kết quả chỉ số tương ứng");

    public static ApiMessage
            DOCTOR_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy bác sĩ tương ứng"),
            DOCTOR_HAD_GROUP = new ApiSharedMesssage(3103001, "Bác sĩ đã có nhóm bệnh nhân tương ứng, vui lòng kiểm tra lại"),
            NURSE_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy y tá tương ứng"),
            NURSE_HAD_GROUP = new ApiSharedMesssage(3103001, "Y tá đã có nhóm bệnh nhân tương ứng, vui lòng kiểm tra lại"),
            PATIENT_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy bệnh nhân tương ứng");

    public static ApiMessage
            DELETE_SUCCESSFUL = new ApiSharedMesssage(3103001, "Xóa thành công"),
            TRANSFER_SUCCESSFUL = new ApiSharedMesssage(3103001, "Chuyển thành công");

    public static ApiMessage
            LOCATION_NOT_AVAILABLE = new ApiSharedMesssage(3103001, "Giường bệnh đã được sử dụng"),
            LOCATION_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy giường bệnh tương ứng");

    public static ApiMessage
            SICKBED_EXISTED = new ApiSharedMesssage(3103001, "Gường bệnh đã tồn tại"),
            SICKBED_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy giường bệnh tương ứng"),
            SICKBED_EMPTY = new ApiSharedMesssage(3103001, "Giường bệnh đang trống"),
            SICKBED_USED = new ApiSharedMesssage(3103001, "Giường bệnh đã được sử dụng"),
            SICKBED_DISABLE = new ApiSharedMesssage(3103001, "Giường bệnh hiện không khả dụng"),
            SICKBED_REQUESTED = new ApiSharedMesssage(3103001, "Giường bệnh đã được yêu cầu cho bệnh nhân khác");

    public static ApiMessage
            ROOM_EXISTED = new ApiSharedMesssage(3103001, "Phòng bệnh đã tồn tại"),
            ROOM_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy phòng bệnh tương ứng");

    public static ApiMessage
            FLOOR_EXISTED = new ApiSharedMesssage(3103001, "Tầng nhà đã tồn tại"),
            FLOOR_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy tầng nhà tương ứng");

    public static ApiMessage
            BUILDING_EXISTED = new ApiSharedMesssage(3103001, "Tòa nhà đã tồn tại"),
            BUILDING_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy tòa nhà tương ứng"),
            HOSPITAL_EXISTED = new ApiSharedMesssage(3103001, "Bệnh viện đã tồn tại"),
            HOSPITAL_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy bệnh viện tương ứng");

    public static ApiMessage
            ROOM_TYPE_EXISTED = new ApiSharedMesssage(3103001, "Loại phòng đã tồn tại"),
            ROOM_TYPE_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy loại phòng tương ứng");

    public static ApiMessage
            MEDICAL_DEVICE_TYPE_EXISTED = new ApiSharedMesssage(3103001, "Loại thiết bị đã tồn tại"),
            MEDICAL_DEVICE_TYPE_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy loại thiết bị tương ứng");

    public static ApiMessage
            MEDICAL_DEVICE_EXISTED = new ApiSharedMesssage(3103001, "Thiết bị đã tồn tại"),
            MEDICAL_DEVICE_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy thiết bị tương ứng");

    public static ApiMessage
            DEPENDENT_EXISTED = new ApiSharedMesssage(3103001, "Người thân đã tồn tại"),
            DEPENDENT_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy người thân");

    public static ApiMessage
            PRESCRIPTION_NOT_FOUND = new ApiSharedMesssage(3103001, "Chưa có đơn thuốc nào");

    public static ApiMessage
            EXAMINATION_NOT_FOUND = new ApiSharedMesssage(3103001, "Chưa có lần khám bệnh nào");

    public static ApiMessage
            ALLERGY_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy tiền sử dị ứng tương ứng");

    public static ApiMessage
            SICKNESS_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy tiền sử bệnh tương ứng");

    public static ApiMessage
            SURGERY_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy tiền sử phẫu thuật tương ứng");

    public static ApiMessage
            VACCINE_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy lịch sử tiêm vaccine tương ứng"),
            WRONG_ENUM_VALUE = new ApiSharedMesssage(3103001, "Sai giá trị của kiểu enum");

    public static ApiMessage
            TEST_RESULT_NOT_FOUND = new ApiSharedMesssage(3103001, "Không tìm thấy kết quả xét nghiệm tương ứng");

    public static ApiMessage
            PRESCRIPTION_DETAIL_EXISTED = new ApiSharedMesssage(34411,"Loại thuốc đã tồn tại");

    public static ApiMessage
            USERNAME_NOT_FOUND_IN_TOKEN = new ApiSharedMesssage(34411,"Không tìm thấy username từ token"),
            WRONG_PASSWORD = new ApiSharedMesssage(55115,"Tài khoản hoặc mật khẩu không hợp lệ"),
            TOKEN_EXPIRED = new ApiSharedMesssage(77777,"The Token has expired");

    public static ApiMessage
            PATIENT_NOT_BELONG = new ApiSharedMesssage(34411,"Bệnh nhân không thuộc sự quản lý của bạn"),
            MEDICINE_NOT_ENOUGH = new ApiSharedMesssage(34411,"Số thuốc còn lại không đủ"),
            GROUP_TYPE_NOT_HAVE = new ApiSharedMesssage(34411,"Bạn không phụ trách bệnh nhân ở nhóm mới này"),
            IMPORT_PATIENT_NOT_FOUND = new ApiSharedMesssage(34411,"Không tìm thấy bệnh nhân được thông báo chuyển viện tương ứng"),
            STATUS_CHANGED = new ApiSharedMesssage(34411,"Bệnh nhân đã không còn ở bệnh viện");


    public ApiSharedMesssage(int code, String message) {
        super(code, message);
    }
}
