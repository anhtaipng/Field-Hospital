package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.PatientLocationDto;
import com.lvtn.module.shared.dto.RoomDto;
import com.lvtn.module.shared.dto.SickbedDto;
import com.lvtn.module.shared.dto.SickbedListRequestDto;
import com.lvtn.module.shared.model.Sickbed;
import com.lvtn.platform.common.PageResponse;

import java.util.List;

public interface SickbedService {
    int addSickbed(SickbedDto sickbedDto);
    int updateSickbed(SickbedDto sickbedDto);
    void deleteSickbed(SickbedDto sickbedDto);
    void enableSickbed(SickbedDto sickbedDto);
    void disableSickbed(SickbedDto sickbedDto);
    PageResponse<PatientLocationDto> getSickbedAvailableWithRoomType(SickbedListRequestDto sickbedListRequestDto);
    List<SickbedDto> getSickbedList(RoomDto roomDto);
}
