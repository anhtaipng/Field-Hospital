package com.lvtn.module.patient.controller;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.mapper.StatsMapper;
import com.lvtn.module.shared.repository.PatientRepository;
import com.lvtn.module.shared.service.StatsService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.common.Response;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
@Slf4j
public class StatsPatientController {
    @Autowired
    StatsService statsService;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    StatsMapper statsMapper;

    @GetMapping("/stats/get_all")
    public Response<List<StatsResponseDto>> getown(Principal user){
        return Response.success(statsService.getStats(user.getName()).stream().map(statsMapper::mapStatisticResultToStatsResponseDto).collect(Collectors.toList()));
    }

    @GetMapping("/stats/get_first_all")
    public Response<List<AllStatsResponseDto>> getFirstAll(Principal user){
        return Response.success(statsMapper.mapStatisticResultToAllStatsResponseDto(statsService.getFirstStatsByType(user.getName())));
    }

    @PostMapping("/stats/get")
    public Response<StatisticResultResponse> getStatsByType(@Valid @RequestBody StatisticResultRequest statisticResultRequest, Principal user){
        try{
            return Response.success(statsService.getStatsByType(statisticResultRequest,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/stats/add")
    public Response<StatsResponseDto> createStats(@Valid @RequestBody StatsCreateRequest patientCreateStatsRequest, Principal user){
        try{
            return Response.success(statsService.createStats(patientCreateStatsRequest,user.getName()));
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping("/stats/delete")
    public Response<Void> deleteStats(@RequestBody @Valid StatsResponseDto statsResponseDto){
        try{
            statsService.deleteStats(statsResponseDto);
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }
}
