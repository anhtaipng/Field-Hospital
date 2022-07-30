package com.lvtn.module.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticType {
    @Id
    private String statisticType;
    @Column
    private Float minValue;
    @Column
    private Float lowValue;
    @Column
    private Float highValue;
    @Column
    private Float maxValue;
    @Column
    private String unit;
//    Cách xếp loại nguy cơ cao, thấp hoặc trung bình:
//
//          (cao) < minValue < (trung bình) < lowValue < (thấp) < highValue < (trung bình) < maxValue < (cao)
//
//    Ví dụ: xét nghiệm kết quả ra giá trị x
//    - x < minValue -> nguy cơ cao
//    - minValue < x < lowValue -> nguy cơ trung bình
//    - lowValue < x < highValue -> nguy cơ thấp
//    - highValue < x < maxValue -> nguy cơ trung bình
//    - maxValue < x -> nguy cơ cao
//
//    Với các trường hợp == một trong các giá trị minValue, lowValue, highValue, maxValue
//    Ta sẽ lấy xếp loại nằm cạnh nhỏ hơn (có lợi cho bệnh nhân hơn)
//    Vậy ta có công thức tổng quát:
//    - x == minValue -> nguy cơ trung bình
//    - x == lowValue -> nguy cơ thấp
//    - x == highValue -> nguy cơ thấp
//    - x == maxValue -> nguy cơ trung bình
//
//
//    Các ví dụ trực quan (hiểu thêm chứ m chỉ cần làm logic như các công thức ở trên là được)
//    Nhịp thở:  (cao)  20  (trung bình)  40  (thấp)  60  (trung bình)  80  (cao)
//    Nếu nhịp thở = 20 == minValue -> nguy cơ trung bình
//    Nếu nhịp thở = 40 == lowValue -> nguy cơ thấp
//    Nếu nhịp thở = 60 == highValue -> nguy cơ thấp
//    Nếu nhịp thở = 80 == maxValue -> nguy cơ trung bình
//
//    Có thay đổi một chút so với lúc thảo luận: Có thể sẽ có 1 giá trị null trong 4 biến (tuỳ trường hợp chỉ số mà có hoặc không)
//    Ví dụ:
//    SpO2 (%):  (cao)  94  (trung bình)  98  (thấp)  100  (trung bình)  NULL  (cao)
//    Nếu SpO2 = 100% == highValue -> nguy cơ thấp
//
//    Đường huyết:  (cao)  NULL  (trung bình)  0  (thấp)  20  (trung bình)  40  (cao)
//    Nếu đường huyết = 0 == lowValue -> nguy cơ thấp
//

}
