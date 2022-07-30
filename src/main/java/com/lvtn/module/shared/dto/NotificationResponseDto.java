package com.lvtn.module.shared.dto;


import com.lvtn.platform.common.PageResponse;
import lombok.Data;
import org.aspectj.weaver.ast.Not;

import java.util.List;

@Data
public class NotificationResponseDto {
    private PageResponse<NotificationDto> notificationDtoList;
    private long notRead;
}
